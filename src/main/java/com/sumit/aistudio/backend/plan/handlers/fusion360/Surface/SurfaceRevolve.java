package com.sumit.aistudio.backend.plan.handlers.fusion360.Surface;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class SurfaceRevolve extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name = (String) node.getData().getProperty("name");
        String profile = (String) node.getData().getProperty("profile");
        String axis_name = (String) node.getData().getStringProperty("axis_name");
        float angle = (float) node.getData().getFloatProperty("angle");
        int profileItemIndex = (int) node.getData().getIntProperty("profileItemIndex");
        try {
            fusion360Client.surfaceRevolve(name, profile, axis_name, angle, profileItemIndex);
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
