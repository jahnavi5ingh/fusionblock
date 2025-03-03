package com.sumit.aistudio.backend.plan.handlers.fusion360.Surface;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class SurfaceLoft extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name = (String) node.getData().getProperty("name");
        String[] profiles =  node.getData().getStringProperty("profiles").split(",");

        try {
            fusion360Client.surfaceLoft(name, profiles);
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
