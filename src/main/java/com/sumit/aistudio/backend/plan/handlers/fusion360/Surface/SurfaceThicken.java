package com.sumit.aistudio.backend.plan.handlers.fusion360.Surface;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class SurfaceThicken extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name = (String) node.getData().getProperty("name");
        String[] surfaceNames = (String[]) node.getData().getProperty("surfaceNames");
        float thickness = (float) node.getData().getFloatProperty("thickness");
        try {
            fusion360Client.surfaceThicken(name, surfaceNames, thickness);
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
