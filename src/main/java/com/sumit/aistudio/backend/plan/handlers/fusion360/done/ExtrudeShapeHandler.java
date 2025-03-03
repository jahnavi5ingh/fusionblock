package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class ExtrudeShapeHandler extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name = node.getData().getStringProperty("name");
        float offset = node.getData().getFloatProperty("offset");
        float taperAngle = node.getData().getFloatProperty("taperAngle");
        String bodyName = node.getData().getStringProperty("bodyName");
        try {
            fusion360Client.extrudeShape(name, offset, taperAngle,bodyName);

            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
