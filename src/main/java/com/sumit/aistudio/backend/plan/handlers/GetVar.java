package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class GetVar extends Handler{
    @Override
    public void handleNode(Node node) {
        try {
            String varName = node.getData().getStringProperty("name");
            Object val = node.getExecutionContext().getVars().get(varName);
            node.getOutput().getProperties().put("output", val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
