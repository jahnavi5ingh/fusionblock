package com.sumit.aistudio.backend.plan.handlers.fusion360.paths;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class CreatePipe extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String path_name = (String) node.getData().getProperty("path_name");
        float section_size = node.getData().getFloatProperty("section_size");
        String section_type = (String) node.getData().getProperty("section_type");
        try {
            fusion360Client.createPipe(path_name, section_size, section_type);
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
