package com.sumit.aistudio.backend.plan.handlers.parameters;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.Handler;
import org.springframework.stereotype.Component;


@IsNodeHandler
@Component
public class OutputParam extends Handler {
    @Override
    public void handleNode(Node node) {
       String name = node.getData().getStringProperty("outputName");
       Object val = node.getData().getProperty("inputValue");
         System.out.println("InputParam: " + name + " = " + val);
         //set to output
         node.getOutput().getProperties().put("output", val);
    }
}
