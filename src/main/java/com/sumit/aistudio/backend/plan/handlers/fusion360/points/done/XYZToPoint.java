package com.sumit.aistudio.backend.plan.handlers.fusion360.points.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class XYZToPoint extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String xyz = node.getData().getStringProperty("x_y_z");
        float [] xyzF =  stringArrayToFloatArray(toStringArray(xyz));

        try {
        node.getOutput().getProperties().put("output", floatarrayToPoint(xyzF));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
