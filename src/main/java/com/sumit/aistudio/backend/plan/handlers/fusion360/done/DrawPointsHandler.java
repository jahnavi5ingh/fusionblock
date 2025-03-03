package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.fusion360.GeometryUtils;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@IsNodeHandler
@Component
public class DrawPointsHandler extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        List<Point> points = (List<Point>) node.getData().getProperty("collection");
        try {
            System.out.println(points);
            ///iterate over points and call fusion client draw point
            float[][] pointFloats = GeometryUtils.convertPointsToArray(points);
            fusion360Client.drawPoints(pointFloats);

            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
