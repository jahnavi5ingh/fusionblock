package com.sumit.aistudio.backend.plan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sumit.aistudio.backend.NodeHandlerBeanPostProcessor;
import com.sumit.aistudio.backend.dynaComponents.DynaComponent;
import com.sumit.aistudio.backend.dynaComponents.DynaComponentData;
import com.sumit.aistudio.backend.dynaComponents.DynaComponentService;
import com.sumit.aistudio.backend.dynaComponents.FieldInfo;
import com.sumit.aistudio.backend.graph.*;
import com.sumit.aistudio.backend.plan.handlers.*;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.cycle.SzwarcfiterLauerSimpleCycles;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class PlanExecutor  {

   @Autowired
   NodeHandlerBeanPostProcessor nodeHandlerBeanPostProcessor;

   @Autowired
   DynaComponentService dynaComponentService;

   @Autowired
   PlanService planService;
    @Autowired
    Context initContext;
    ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    Handler defaultHandler = new Handler() {
        @Override
        public void handleNode(Node node) {
            System.out.println("Default handler for node: "+node.getId());
            super.handleNode(node);
        }
    };
    Map<String, Handler> nodeHandleMap = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("Init of nodes");
        nodeHandlerBeanPostProcessor.getNodeHandlerBeans().stream().forEach(handler -> {
            if (handler.getClass().getSimpleName().contains("Handler"))
            nodeHandleMap.put(StringUtils.substringBeforeLast(handler.getClass().getSimpleName(),"Handler"),handler.withInitContext(initContext));
            else
            nodeHandleMap.put(handler.getClass().getSimpleName(),handler.withInitContext(initContext));
        });
        System.out.println("Done init of nodes");

    }


    public void executeGraph(String jsonString){
        executeGraphWithIO(jsonString, new HashMap<>());
    }

    public Map<String,Object> executeGraphWithIO(String jsonString , Map<String,Object> inputs){
        GraphData root = getGraphData(jsonString);
        for(Node node: root.getNodes()){
            if(node.getType().equals("DynamicFormNode")){
                String nodeType = node.getData().getStringProperty("type");
                if(nodeType.equals("InputParam")){
                    String inputName = node.getData().getStringProperty("inputName");
                    if(inputs.containsKey(inputName)) {
                        node.getData().getProperties().put("inputValue", inputs.get(inputName));
                    }
                }
            }
        }
        DirectedMultigraph<Node, Edge> g = makeGraph(root);
        Map<String,Node> allNodes = new HashMap<>();
        Map<String,List<Edge>> allEdges = new HashMap<>();
        g.vertexSet().stream().forEach(node -> allNodes.put(node.getId(),node));
        root.getEdges().stream().forEach(edge -> {
            if(!allEdges.containsKey(edge.getSource())){
                allEdges.put(edge.getSource(),new ArrayList<>());
            }
            allEdges.get(edge.getSource()).add(edge);

            if(!allEdges.containsKey(edge.getTarget())){
                allEdges.put(edge.getTarget(),new ArrayList<>());
            }
            allEdges.get(edge.getTarget()).add(edge);
        });

        Map<String,Object> vars = new HashMap<>();
        for(Node node: allNodes.values()){
            node.getExecutionContext().setVars(vars);
            node.getExecutionContext().setPlanExecutor(this);
            node.getExecutionContext().setEdges(allEdges);
            node.getExecutionContext().setNodes(allNodes);
        }

        List<List<Node>> cycles = detectCycles(g);
        updateForCycles(cycles, g);
        List<Node> sortedList = topologicalSort(g);
        Map<String,Node> nodes = new HashMap<>();
        g.vertexSet().stream().forEach(node -> nodes.put(node.getId(),node));
        Map<String,List<Edge>> edges = new HashMap<>();
        root.getEdges().stream().forEach(edge -> {
            if(!edges.containsKey(edge.getSource())){
                edges.put(edge.getSource(),new ArrayList<>());
            }
            edges.get(edge.getSource()).add(edge);
        });


        for(Node node: sortedList){
            executeNode(node, allEdges, allNodes);
        }
        Map<String,Object> outputs = new HashMap<>();
        for(Node node: root.getNodes()){
            if(node.getType().equals("DynamicFormNode")){
                String nodeType = node.getData().getStringProperty("type");
                if(nodeType.equals("OutputParam")){
                    String inputName = node.getData().getStringProperty("outputName");
                    if(node.getOutput().getProperties().containsKey("output")) {
                        outputs.put(inputName,node.getOutput().getProperties().get("output"));
                    }
                }
            }
        }
        return outputs;
    }

    private static GraphData getGraphData(String jsonString) {
        ParsingService parsingService = new ParsingService();
        GraphData root = parsingService.parseJson(jsonString);
        return root;
    }

    public void executeNode(Node node, Map<String, List<Edge>> edges, Map<String, Node> nodes) {
        System.out.println("Executing node: "+ node.getId()+" of type: "+ node.getType());
        Handler handler = nodeHandleMap.get(node.getType());
        if(handler == null) {
            if(node.getType().equals("DynamicFormNode")){

                String nodeType = node.getData().getStringProperty("type");
                Optional<DynaComponent> comp = dynaComponentService.getEntityByTypeCache(nodeType);
                if(comp.isPresent()){
                    String parentNodeType = comp.get().getParentType();
                    if(parentNodeType!=null && !parentNodeType.isEmpty() && !parentNodeType.equalsIgnoreCase("None")){
                        nodeType = parentNodeType;
                    }
                }
                System.out.println("Dynamic form node type: "+nodeType);
                handler = nodeHandleMap.get(nodeType);
                if(handler==null) {
                    nodeType.endsWith("DynaTemplate");
                    handler = nodeHandleMap.get("DynaTemplate");
                }
            }
        }
        if(handler==null){
            handler = defaultHandler;
        }
        handler.handleNode(node);

        updateOutputs(node, edges, nodes);
    }

    public  void updateOutputs(Node node, Map<String, List<Edge>> edges, Map<String, Node> nodes) {
        if(edges.containsKey(node.getId())) {
            edges.get(node.getId()).stream().filter(edge -> edge.getSource().equals(node.getId())).forEach(edge -> {
                Node target = nodes.get(edge.getTarget());
                if(target!=null) {
                    String inputPort = edge.getSourceHandle().contains(".") ? StringUtils.substringAfterLast(edge.getSourceHandle(), ".") : edge.getSourceHandle();
                    String outputPort = edge.getTargetHandle().contains(".") ? StringUtils.substringAfterLast(edge.getTargetHandle(), ".") : edge.getTargetHandle();
                    System.out.println("Executing edge: " + edge.getId());
                    Object sourceVal = node.getOutput().getProperties().get(inputPort);
                    target.getData().getProperties().put(outputPort, sourceVal);
                }
            });

        }
    }
    public List<Object> getIncommingValues(String portName, Node node){
        List<Object> values = new ArrayList<>();
        Map<String, List<Edge>> edges = node.getExecutionContext().getEdges();
        Map<String, Node> nodes = node.getExecutionContext().getNodes();
        if(edges.containsKey(node.getId())) {
            edges.get(node.getId()).stream().filter(edge -> edge.getTarget().equals(node.getId())).forEach(edge -> {
                Node sourceNode = nodes.get(edge.getSource());
                if(sourceNode!=null) {
                    String inputPort = edge.getSourceHandle().contains(".") ? StringUtils.substringAfterLast(edge.getSourceHandle(), ".") : edge.getSourceHandle();
                    String outputPort = edge.getTargetHandle().contains(".") ? StringUtils.substringAfterLast(edge.getTargetHandle(), ".") : edge.getTargetHandle();
                    if(portName.equals(outputPort)) {
                        System.out.println("Executing edge: " + edge.getId());
                        Object sourceVal = sourceNode.getOutput().getProperties().get(inputPort);
                        values.add(sourceVal);
                    }
                }

            });
        }
        return values;
    }
    public List<List<Node>> detectCycles(DirectedMultigraph<Node, Edge> g) {
        System.out.println(g);
        SzwarcfiterLauerSimpleCycles<Node, Edge> cycleDetector = new SzwarcfiterLauerSimpleCycles<>(g);
        List<List<Node>> cycles = cycleDetector.findSimpleCycles();
        System.out.println(cycles);
        return cycles;
    }

    public void updateForCycles(List<List<Node>> cycles, DirectedMultigraph<Node, Edge> g) {
        Map<String, List<Node>> maxCylce = new HashMap<>();
        for(List<Node> cycle: cycles){
            if(!maxCylce.containsKey(cycle.get(0).getId())){
                maxCylce.put(cycle.get(0).getId(),cycle);
            }
            else{
                if(maxCylce.get(cycle.get(0).getId()).size()<cycle.size()){
                    maxCylce.put(cycle.get(0).getId(),cycle);
                }
            }
        }
        for (List<Node> cycle : maxCylce.values()) {
            Node parent = cycle.get(0);


            Node prev = parent;
            List<Node> toRemove = new ArrayList<>();
            for (int i = 1; i < cycle.size(); i++) {
                Node next = cycle.get(i);
                System.out.println("Removing edge between: "+prev.getId()+" and "+next.getId());
                g.removeEdge(prev,next);
                parent.addChild(next);
                toRemove.add(next);
                prev = next;
            }
            g.removeEdge(prev,parent);
            for (Node node : toRemove) {
                g.removeVertex(node);
            }

        }
    }


    public DirectedMultigraph<Node, Edge> makeGraph(GraphData root) {
        DirectedMultigraph<Node, Edge> g = new DirectedMultigraph<>(Edge.class);
        //map of nodes
        Map<String,Node> nodeMap = new HashMap<>();
        //create node map
        for (Node node : root.getNodes()) {
            nodeMap.put(node.getId(),node);
        }
        for (Node node : root.getNodes()) {
            g.addVertex(node);
        }
        Map<String,Edge> edgeMap = new HashMap<>();
        for (Edge edge : root.getEdges()) {
            Node source = nodeMap.get(edge.getSource());
            Node target = nodeMap.get(edge.getTarget());
            if(!edgeMap.containsKey(source.getId()+"_"+target.getId())){
                edgeMap.put(source.getId()+"_"+target.getId(),edge);
                g.addEdge(source, target, edge);
            }
        }
        return g;
    }

    public List<Node> topologicalSort(DirectedMultigraph<Node, Edge> g) {
        CycleDetector<Node, Edge> cycleDetector = new CycleDetector<>(g);
        if (cycleDetector.detectCycles()) {
            throw new IllegalStateException("Graph contains a cycle; topological sort not possible.");
        }

        List<Node> sortedNodes = new ArrayList<>();
        TopologicalOrderIterator<Node, Edge> iterator = new TopologicalOrderIterator<>(g);
        while (iterator.hasNext()) {
            sortedNodes.add(iterator.next());
        }
        return sortedNodes;
    }

    public void createComponent(PlanComponent component) {
        DynaComponent dynaComponent = new DynaComponent();
        dynaComponent.setCompType(component.getName());
        dynaComponent.setParentType("SubPlan");
        dynaComponent.setGrp("UserLib");
        dynaComponent.setPureImpure("not");
        //
        DynaComponentData data = new DynaComponentData();

        planService.getPlanByName(component.getName()).ifPresent(plan -> {
            int ip = 0;
            GraphData root = getGraphData(plan.getData());
            for(Node node: root.getNodes()){
                if(node.getType().equals("DynamicFormNode")){
                    String nodeType = node.getData().getStringProperty("type");
                    if(nodeType.equals("OutputParam")){
                        FieldInfo fieldInfo = new FieldInfo();
                        fieldInfo.setFieldName(node.getData().getStringProperty("outputName"));
                        fieldInfo.setFieldLabel(node.getData().getStringProperty("outputName"));
                        fieldInfo.setFieldType("string");
                        fieldInfo.setHeader(""+node.getData().getIntProperty("header"));
                        data.getOutputFieldInfo().add(fieldInfo);
                    } else if (nodeType.equals("InputParam")) {
                        FieldInfo fieldInfo = new FieldInfo();
                        fieldInfo.setFieldName(node.getData().getStringProperty("inputName"));
                        fieldInfo.setFieldLabel(node.getData().getStringProperty("inputName"));
                        fieldInfo.setFieldType("string");
                        fieldInfo.setHeader(String.valueOf(ip));
                        data.getFieldInfo().add(fieldInfo);
                        ip++;
                    }

                }
            }
        });







        data.getInitialValues().put("name","some name");
        try {
            dynaComponent.setData(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Optional<DynaComponent> compFromDB = dynaComponentService.getEntityByType(dynaComponent.getCompType());
        if(compFromDB.isPresent()){
            dynaComponent.setId(compFromDB.get().getId());
        }
        dynaComponentService.updateEntity(dynaComponent);

    }

    public Map<String, Object> executePlan(String subPlanName, Map<String, Object> inputMap) {
        Optional<Plan> plan = planService.getPlanByName(subPlanName);
        if(plan.isPresent()){
            return executeGraphWithIO(plan.get().getData(),inputMap);
        }
        return new HashMap<>();
    }
}
