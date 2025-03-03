package com.sumit.aistudio.backend.plan;

import com.sumit.aistudio.backend.graph.Edge;
import com.sumit.aistudio.backend.graph.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionContext {
    Map<String,Object> vars = new HashMap<>();
    PlanExecutor planExecutor;
    Map<String, List<Edge>> edges;
    Map<String, Node> nodes;

    public Map<String, Object> getVars() {
        return vars;
    }

    public void setVars(Map<String, Object> vars) {
        this.vars = vars;
    }

    public PlanExecutor getPlanExecutor() {
        return planExecutor;
    }

    public void setPlanExecutor(PlanExecutor planExecutor) {
        this.planExecutor = planExecutor;
    }

    public Map<String, List<Edge>> getEdges() {
        return edges;
    }

    public void setEdges(Map<String, List<Edge>> edges) {
        this.edges = edges;
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, Node> nodes) {
        this.nodes = nodes;
    }
}
