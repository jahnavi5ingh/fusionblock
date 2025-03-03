package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;


@IsNodeHandler
@Component
public class IntNameCreator extends Handler {
    @Override
    public void handleNode(Node node) {
        //take pattern and ivalue from param and using string format format pattern and set to output
        String pattern = (String) node.getData().getProperties().get("pattern");
        Integer ivalue = (Integer) node.getData().getIntProperty("ivalue");

        try {
            String object  = String.format(pattern, ivalue);
            node.getOutput().getProperties().put("output",object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
