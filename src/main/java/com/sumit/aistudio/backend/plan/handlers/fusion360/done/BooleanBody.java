package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class BooleanBody extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {

        try {
            fusion360Client.cutBody(node.getData().getStringProperty("body1"), node.getData().getStringProperty("body2"));
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
