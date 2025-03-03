package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;


@IsNodeHandler
@Component
public class RedirectNode extends Handler {
    @Override
    public void handleNode(Node node) {
       //to do nothing
        System.out.println("RedirectNode ");
        //take indefaultPort value and set to output
        Object prop = node.getData().getProperty("indefaultPort");
        node.getOutput().getProperties().put("output", prop);
    }
}
