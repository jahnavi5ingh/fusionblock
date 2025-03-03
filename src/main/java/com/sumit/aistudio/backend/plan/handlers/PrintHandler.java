package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.prompttemplates.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;


@IsNodeHandler
@Component
public class PrintHandler extends Handler {
    @Override
    public void handleNode(Node node) {
        Object object = node.getData().getProperty("texttoprint");
         try {
             System.out.println(object==null?"null":object.toString());
                node.getOutput().getProperties().put("output",object);
            } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
