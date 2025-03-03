package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class RevolveShape extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        Object axisVal = node.getData().getProperty("axis");
        String bodyName = node.getData().getStringProperty("bodyName");
        int axis = 0;
                if(axisVal instanceof String) {
                   switch ((String) ((String) axisVal).toLowerCase()) {
                       case "x":
                       case "0":
                           axis = 0;
                           break;
                       case "y":
                       case "1":
                           axis = 1;
                           break;
                       case "z":
                       case "2":
                           axis = 2;
                           break;
                   }
                } else if(axisVal instanceof Integer) {
                    axis = (int) axisVal;
                }
        try {
            fusion360Client.revolve(node.getData().getStringProperty("shape"),axis, node.getData().getFloatProperty("angle"),bodyName);

            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
