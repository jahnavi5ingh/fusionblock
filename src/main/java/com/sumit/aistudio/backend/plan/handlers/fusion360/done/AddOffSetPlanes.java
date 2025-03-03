package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class AddOffSetPlanes extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
          try {

              int count = node.getData().getIntProperty("countOfPlanes");
              String name = node.getData().getStringProperty("name");
              for(int i=0;i<count;i++) {
                  String planeName =  name+"_"+i;
                  fusion360Client.addOffsetPlane(node.getData().getFloatProperty("offset")*(i+1),
                          planeName,
                          node.getData().getStringProperty("basePlane")
                  );
              }
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
