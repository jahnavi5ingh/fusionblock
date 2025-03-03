package com.sumit.aistudio.backend.plan.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@IsNodeHandler
@Component
public class ToSet extends Handler{
    @Override
    public void handleNode(Node node) {
        String data =  node.getData().getStringProperty("data");
        ObjectMapper mapper = new ObjectMapper();
        try {
            Set values =  mapper.readValue(data, new TypeReference<Set>() {  });
            node.getOutput().getProperties().put("output", values);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
