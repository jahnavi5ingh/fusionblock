package com.sumit.aistudio.backend.plan.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.Context;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public  class Handler {
    protected Context context;
    protected String output = "output";
    protected String done = "done";
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Handler withInitContext(Context context){
        this.context= context;
        return this;
    }

    public void handleNode(Node node){
        System.out.println("Children: "+node.getChildren());
        System.out.println("Type: "+node.getType());
        System.out.println("Data: "+node.getData());
        for(Node child: node.getChildren()){
            System.out.println("Executing child: "+child.getData());
        }
    }

    public boolean isEmpty(String str){
        return str == null || str.trim().isEmpty();
    }
    protected static void updateOutputs(Node node) {
        node.getExecutionContext().getPlanExecutor().updateOutputs(node,
                node.getExecutionContext().getEdges(),
                node.getExecutionContext().getNodes());
    }
    protected static void executeChildNode(Node node, Node child) {
        node.getExecutionContext().getPlanExecutor().executeNode(child,
                node.getExecutionContext().getEdges(),
                node.getExecutionContext().getNodes());
    }
    public static List<List<Object>> createTuples(List<List<Object>> collections) {
        List<List<Object>> tuples = new ArrayList<>();

        // Find the maximum size among all inner lists
        int maxSize = collections.stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        // Iterate from 0 to maxSize - 1 to form each tuple
        for (int i = 0; i < maxSize; i++) {
            List<Object> tuple = new ArrayList<>();

            // Iterate through each collection
            for (List<Object> collection : collections) {
                if (i < collection.size()) {
                    tuple.add(collection.get(i));
                } else {
                    tuple.add(null);
                }
            }
            tuples.add(tuple);
        }
        return tuples;
    }

    public  String prettyJson(Object object){
        String jsonOutput = gson.toJson(object);
        return jsonOutput;
    }
}
