package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class AddText extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {

        try {
            fusion360Client.addText(node.getData().getStringProperty("text"),
                    node.getData().getStringProperty("planeName"),
                    pointToFloatArray((Point)node.getData().getProperty("point1")),
                    pointToFloatArray((Point)node.getData().getProperty("point2")),
                    node.getData().getFloatProperty("fontSize"));
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
