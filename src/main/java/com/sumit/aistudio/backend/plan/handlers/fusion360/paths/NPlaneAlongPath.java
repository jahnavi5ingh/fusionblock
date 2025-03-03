package com.sumit.aistudio.backend.plan.handlers.fusion360.paths;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class NPlaneAlongPath extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String path_name = (String) node.getData().getProperty("path_name");
        int n = node.getData().getIntProperty("count");
        String name = (String) node.getData().getProperty("name");
        try {
            fusion360Client.create_n_planes_along_path(path_name, name, n);
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
