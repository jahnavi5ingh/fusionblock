package com.sumit.aistudio.backend.plan.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@IsNodeHandler
@Component
public class ToMap extends Handler{
    @Override
    public void handleNode(Node node) {
        String data =  node.getData().getStringProperty("data");
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map values =  mapper.readValue(data, new TypeReference<Map>() {  });
            node.getOutput().getProperties().put("output", values);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
