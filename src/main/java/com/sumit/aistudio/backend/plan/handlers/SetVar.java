package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class SetVar extends Handler{
    @Override
    public void handleNode(Node node) {
        try {
            String varName = node.getData().getStringProperty("name");
            String varValue = node.getData().getStringProperty("value");
            node.getExecutionContext().getVars().put(varName, varValue);
            node.getOutput().getProperties().put("output", varValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
