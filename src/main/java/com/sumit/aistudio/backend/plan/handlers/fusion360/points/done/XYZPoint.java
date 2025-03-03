package com.sumit.aistudio.backend.plan.handlers.fusion360.points.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class XYZPoint extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        float x = node.getData().getFloatProperty("x");
        float y = node.getData().getFloatProperty("y");
        float z = node.getData().getFloatProperty("z");
        Point point = new Point(x, y, z);
        try {
        node.getOutput().getProperties().put("output", point);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
