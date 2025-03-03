package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class SelectPlane extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
          try {
            fusion360Client.selectPlane(
                    node.getData().getStringProperty("name")
                    );
            node.getExecutionContext().getVars().put("selected_plane", node.getData().getStringProperty("name"));
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
