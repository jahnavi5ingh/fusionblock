package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.prompt.Prompt;
import com.sumit.aistudio.backend.prompttemplates.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@IsNodeHandler
@Component
public class ReadPromptTemplateHandler extends Handler {
    @Override
    public void handleNode(Node node) {
        String pname = node.getData().getStringProperty("name");
         try {
            Optional<PromptTemplate> prompt = context.getPromptTemplateService().getPromptByType(pname);
            if(prompt.isPresent()){
                String sysPromptText = prompt.get().getData();
                node.getOutput().getProperties().put("output",sysPromptText);
            }else {
                node.getOutput().getProperties().put("output","");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
