package com.sumit.aistudio.backend.plan.handlers.fusion360.paths;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class CreateTangentPlane extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String path_name = (String) node.getData().getProperty("path_name");
        float distance = node.getData().getFloatProperty("distance");
        int dirx = node.getData().getIntProperty("dirx");
        int diry = node.getData().getIntProperty("diry");
        String name = (String) node.getData().getProperty("name");
        int n = node.getData().getIntProperty("count");
        try {
            String result = fusion360Client.create_tangentplane_along_path(path_name, name, distance, n, dirx, diry);
            NPointPathResponse response  = getResponse(result,NPointPathResponse.class);
            node.getOutput().getProperties().put("output", response.getPoints());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
