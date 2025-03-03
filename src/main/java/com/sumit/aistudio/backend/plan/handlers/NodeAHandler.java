package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class NodeAHandler extends Handler{
    @Override
    public void handleNode(Node node) {
        System.out.println("NodeAHandler: "+node.getData());
        node.getOutput().getProperties().put("a",node.getData().getStringProperty("a")+"_a");
        node.getOutput().getProperties().put("b",node.getData().getStringProperty("b")+"_b");
    }
}
