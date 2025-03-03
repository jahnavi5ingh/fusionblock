package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class StringNodeHandler extends Handler {
    @Override
    public void handleNode(Node node) {
        String cname = node.getData().getStringProperty("cname");
        String contexText = node.getData().getStringProperty("contexText");
        try {

            node.getOutput().getProperties().put("output",contexText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
