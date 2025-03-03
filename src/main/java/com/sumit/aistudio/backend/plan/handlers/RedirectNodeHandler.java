package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;


@IsNodeHandler
@Component
public class RedirectNodeHandler extends Handler {
    @Override
    public void handleNode(Node node) {
        Object data = node.getData().getProperties().get("indefaultPort");
         try {
                node.getOutput().getProperties().put("output",data);
            } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
