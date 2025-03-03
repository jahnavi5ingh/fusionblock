package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;


@IsNodeHandler
@Component
public class IntNamesCreator extends Handler {
    @Override
    public void handleNode(Node node) {
        //take pattern and ivalue from param and using string format format pattern and set to output
        String pattern1 = (String) node.getData().getProperties().get("pattern1");
        String pattern2 = (String) node.getData().getProperties().get("pattern2");
        String pattern3 = (String) node.getData().getProperties().get("pattern3");
        Integer ivalue = (Integer) node.getData().getProperties().get("ivalue");

        try {
            String object1  = String.format(pattern1, ivalue);
            String object2  = String.format(pattern2, ivalue);
            String object3  = String.format(pattern3, ivalue);

            node.getOutput().getProperties().put("output1",object1);
            node.getOutput().getProperties().put("output2",object2);
            node.getOutput().getProperties().put("output3",object3);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
