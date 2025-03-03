package com.sumit.aistudio.backend.plan.handlers.fusion360.points.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class PointToXYZ extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        Point xyz = (Point)node.getData().getProperty("point");
       try {
        node.getOutput().getProperties().put("x", xyz.getX());
            node.getOutput().getProperties().put("y", xyz.getY());
            node.getOutput().getProperties().put("z", xyz.getZ());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
