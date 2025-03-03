package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Path;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@IsNodeHandler
@Component
public class DrawLine extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name = node.getData().getStringProperty("name");
        Point p1 = (Point) node.getData().getProperty("p1");
        Point p2 = (Point) node.getData().getProperty("p2");
        Object planeVar = node.getExecutionContext().getVars().get("selected_plane");
        planeVar = planeVar == null ? "xy" : planeVar;
        String selected_plane = planeVar.toString();
        if(selected_plane.equals("xz")) {
            //for each path iterate its points and sway y and z
            p1.setY(p1.getY()*-1);
            p2.setY(p2.getY()*-1);
    }

        try {

            fusion360Client.drawLine(pointToFloatArray(p1), pointToFloatArray(p2), name);

            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
