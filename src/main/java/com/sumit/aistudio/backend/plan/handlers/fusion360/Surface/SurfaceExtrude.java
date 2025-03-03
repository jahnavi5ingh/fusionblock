package com.sumit.aistudio.backend.plan.handlers.fusion360.Surface;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class SurfaceExtrude extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name = (String) node.getData().getProperty("name");
        String profile = (String) node.getData().getProperty("profile");
        float distance = (float) node.getData().getFloatProperty("distance");
        float taperAngle = (float) node.getData().getFloatProperty("taperAngle");
        int profileItemIndex = (int) node.getData().getIntProperty("profileItemIndex");
        try {
            fusion360Client.surfaceExtrude(name, profile, distance, taperAngle, profileItemIndex);
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
