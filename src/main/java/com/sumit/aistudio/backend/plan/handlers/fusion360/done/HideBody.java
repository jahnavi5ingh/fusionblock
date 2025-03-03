package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class HideBody extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {

        try {
            fusion360Client.hideBody(node.getData().getStringProperty("name"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
