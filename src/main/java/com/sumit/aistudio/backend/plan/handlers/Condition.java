package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@IsNodeHandler
@Component
public class Condition extends Handler{
    @Override
    public void handleNode(Node node) {
        System.out.println("Condition: " + node.getData());
        boolean condition = (boolean) node.getData().getBooleanProperty("condition");
        node.getOutput().getProperties().put("output", condition);
        if(condition){
            Set<String> done = new HashSet<>();
            for (Node child : node.getChildren()) {
                if (!done.contains(child.getId())) {
                    executeChildNode(node, child);
                    done.add(child.getId());
                    System.out.println("Executing child: " + child.getData());
                }
            }
        }
    }
}
