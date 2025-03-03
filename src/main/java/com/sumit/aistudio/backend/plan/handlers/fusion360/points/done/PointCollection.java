package com.sumit.aistudio.backend.plan.handlers.fusion360.points.done;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

import java.util.List;

@IsNodeHandler
@Component
public class PointCollection extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       String data =  node.getData().getStringProperty("collection");
        ObjectMapper mapper = new ObjectMapper();
        try {
           List<Point> values =  mapper.readValue(data, new TypeReference<List<Point>>() {  });
            node.getOutput().getProperties().put("output", values);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
