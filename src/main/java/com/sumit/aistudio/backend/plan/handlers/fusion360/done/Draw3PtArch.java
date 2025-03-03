package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class Draw3PtArch extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name=  node.getData().getStringProperty("name");
        Point startPoint = (Point) node.getData().getProperty("startPoint");
        Point endPoint = (Point) node.getData().getProperty("endPoint");
        Point pointOnArc = (Point) node.getData().getProperty("pointOnArc");

        try {
            fusion360Client.draw3PtArc(pointToFloatArray(startPoint),pointToFloatArray(pointOnArc),pointToFloatArray(endPoint),name);
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
