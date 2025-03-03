package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@IsNodeHandler
@Component
public class ForEachMap extends Handler{
    @Override
    public void handleNode(Node node) {
        System.out.println("NodeAHandler: "+node.getData());
        Map<String,Object> coll = (Map<String,Object>) node.getData().getProperty("collection");
        for(String key: coll.keySet()){
            System.out.println("Executing: "+key);
            node.getOutput().getProperties().put("key", key);
            node.getOutput().getProperties().put("value", coll.get(key));
            updateOutputs(node);
            for(Node child: node.getChildren()){
                executeChildNode(node, child);
                System.out.println("Executing child: "+child.getData());
            }
        }

    }
}
