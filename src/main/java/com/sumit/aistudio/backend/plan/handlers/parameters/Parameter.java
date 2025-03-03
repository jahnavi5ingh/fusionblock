package com.sumit.aistudio.backend.plan.handlers.parameters;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.Handler;
import org.springframework.stereotype.Component;


@IsNodeHandler
@Component
public class Parameter extends Handler {
    @Override
    public void handleNode(Node node) {
         try {
                node.getData().getProperties().forEach((k,v)->{
                    node.getOutput().getProperties().put(k,v);
                });
            } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
