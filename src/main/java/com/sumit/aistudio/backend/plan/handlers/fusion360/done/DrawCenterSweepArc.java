package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class DrawCenterSweepArc extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name=  node.getData().getStringProperty("name");
        Point startPoint = (Point) node.getData().getProperty("startPoint");
        Point center_point = (Point) node.getData().getProperty("centerPoint");
        float angle= node.getData().getFloatProperty("sweepAngle");
        try {
            fusion360Client.createSweepArc(pointToFloatArray(startPoint),pointToFloatArray(center_point),angle,name);
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
