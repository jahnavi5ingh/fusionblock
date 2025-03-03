package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@IsNodeHandler
@Component
public class DrawLinesPair extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name = node.getData().getStringProperty("name");
        List<Point> points = getPointsFromInput(node.getData().getProperty("points"));

        List<Point> points2 = getPointsFromInput( node.getData().getProperty("points2"));
        try {
            System.out.println(points);
            fusion360Client.drawLinesPair(convertPointsToFloatArray(points),convertPointsToFloatArray(points2),  name);

            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
