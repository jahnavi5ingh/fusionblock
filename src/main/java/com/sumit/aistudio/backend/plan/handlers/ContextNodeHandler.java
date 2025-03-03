package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class ContextNodeHandler extends Handler {
    @Override
    public void handleNode(Node node) {
        String cname = node.getData().getStringProperty("cname");
        String contexText = node.getData().getStringProperty("contexText");
        try {
            String text= context.getPromptLinker().compileContext(contexText, context.getPromptLinker());
            System.out.println("Prompt generated: "+text);
            context.getPromptLinker().setContext(cname, text);
            node.getOutput().getProperties().put("output",text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
