package com.sumit.aistudio.backend.plan.handlers.fusion360.paths;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class PointAlongPath extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String path_name = (String) node.getData().getProperty("path_name");
        float distance = node.getData().getFloatProperty("distance");
        String name = (String) node.getData().getProperty("name");
        try {
            String response = fusion360Client.create_point_along_path(path_name, name, distance);
            PointPathResponse pointPathResponse = getResponse(response, PointPathResponse.class);
            Point p = pointPathResponse.getPoint();
            node.getOutput().getProperties().put("output", p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
