package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class ForLoopHandler extends Handler{
    @Override
    public void handleNode(Node node) {
       int incr = (int) node.getData().getIntProperty("incrValue");
       int init = (int) node.getData().getIntProperty("initValue");
       int stop = (int) node.getData().getIntProperty("stopValue");
        for(int i = init; i<stop; i+=incr){
            System.out.println("For each for item: "+i);
            node.getOutput().getProperties().put("output", i);
            updateOutputs(node);
            for(Node child: node.getChildren()){
                executeChildNode(node, child);
                System.out.println("Executing child: "+child.getData());
            }
        }
    }
}
