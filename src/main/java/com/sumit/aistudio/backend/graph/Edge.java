package com.sumit.aistudio.backend.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Edge {
    private String source;
    private String sourceHandle;
    private String target;
    private String targetHandle;
    private String id;

    // Getters and Setters
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceHandle() {
        return sourceHandle;
    }

    public void setSourceHandle(String sourceHandle) {
        this.sourceHandle = sourceHandle;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetHandle() {
        return targetHandle;
    }

    public void setTargetHandle(String targetHandle) {
        this.targetHandle = targetHandle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
