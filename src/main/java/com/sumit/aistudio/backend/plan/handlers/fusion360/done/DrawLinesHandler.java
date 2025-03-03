package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@IsNodeHandler
@Component
public class DrawLinesHandler extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name = node.getData().getStringProperty("name");
        boolean close = node.getData().getBooleanProperty("close");
        List<Point> points = (List<Point>) node.getData().getProperty("points");
        try {
            System.out.println(points);
            fusion360Client.drawLines(convertPointsToFloatArray(points),  name,close);

            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
