package com.sumit.aistudio.backend.plan.handlers.fusion360.Surface;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class SurfaceSweep extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name = (String) node.getData().getProperty("name");
        String profile = (String) node.getData().getProperty("profile");
        String pathName = (String) node.getData().getStringProperty("pathName");
        float twistAngle = (float) node.getData().getFloatProperty("twistAngle");
        int orientation = (int) node.getData().getIntProperty("orientation",1);
        try {
            fusion360Client.surfaceSweep(name, profile, pathName, twistAngle,orientation);
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
