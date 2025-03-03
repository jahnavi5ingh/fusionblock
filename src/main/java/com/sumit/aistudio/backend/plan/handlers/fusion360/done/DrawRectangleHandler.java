package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class DrawRectangleHandler extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name=  node.getData().getStringProperty("name");
        Point point1 = (Point) node.getData().getProperty("point1");
        Point point2 = (Point) node.getData().getProperty("point2");
        try {
            System.out.println(point1);
            fusion360Client.drawRectangle(pointToFloatArray(point1),pointToFloatArray(point2),name);

            node.getOutput().getProperties().put("output", name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
