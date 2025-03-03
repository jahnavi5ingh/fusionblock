package com.sumit.aistudio.backend.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParsingService {

    public GraphData parseJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, GraphData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
