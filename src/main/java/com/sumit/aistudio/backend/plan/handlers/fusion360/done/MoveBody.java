package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class MoveBody extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        Point point = (Point) node.getData().getProperty("point");
        float [] pt = super.pointToFloatArray(point);
        try {
            fusion360Client.moveBody(node.getData().getStringProperty("name"),pt);
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
