package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class SweepSketch extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {

        try {
            fusion360Client.sweepSketch(
                    node.getData().getStringProperty("sketch"),
                    node.getData().getStringProperty("path"),
                    node.getData().getStringProperty("name"),
                    node.getData().getFloatProperty("twistAngle"),
                    node.getData().getIntProperty("orientation",1));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
