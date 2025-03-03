package com.sumit.aistudio.backend.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Root {
    private GraphData data;  // Changed from String to GraphData
    private String id;
    private String name;

    // Getters and Setters
    public GraphData getData() {
        return data;
    }

    public void setData(GraphData data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
