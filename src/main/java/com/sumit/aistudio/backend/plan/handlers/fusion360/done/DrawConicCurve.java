package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class DrawConicCurve extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name=  node.getData().getStringProperty("name");
        Point startPoint = (Point) node.getData().getProperty("startPoint");
        Point endPoint = (Point) node.getData().getProperty("endPoint");
        Point apexPoint = (Point) node.getData().getProperty("apexPoint");
        float rhoValue= node.getData().getFloatProperty("rhoValue");
        try {
            fusion360Client.drawConicCurve(pointToFloatArray(startPoint),pointToFloatArray(endPoint),pointToFloatArray(apexPoint),rhoValue,name);
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
