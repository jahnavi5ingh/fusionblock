package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@IsNodeHandler
@Component
public class PromptNode2Handler extends Handler {
    @Override
    public void handleNode(Node node) {
        String pname = node.getData().getStringProperty("pname");
        String model = node.getData().getStringProperty("modelName");
        String sysPrompt = node.getData().getStringProperty("sysPrompt");
        String contextVal = node.getData().getStringProperty("contextVal");
        String promptText = node.getData().getStringProperty("promptText");
        try {
            Optional<Prompt> prompt = context.getPromptService().getPromptByType(sysPrompt);
            if(prompt.isPresent()){
                String sysPromptText = prompt.get().getData();
                Map<String, Object> mapContext = new HashMap<>();
                mapContext.put("context", contextVal);
                mapContext.put("question", promptText);
                String promptStrCompiled  = context.getVelocityHelper().generateContentStringTemplate(sysPromptText,mapContext).toString();
                String text= context.getPromptLinker().generatePrompt(isEmpty(model)?prompt.get().getModel():model, promptStrCompiled);
                System.out.println("Prompt generated: "+text);
                context.getPromptLinker().addPromptOutput(pname,text);
                node.getOutput().getProperties().put("output",text);

            }else {
                String text = context.getPromptLinker().generatePrompt(model, promptText);
                System.out.println("Prompt generated: " + text);
                context.getPromptLinker().addPromptOutput(pname, text);
                node.getOutput().getProperties().put("output",text);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
