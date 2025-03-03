package com.sumit.aistudio.backend.ptl.nodes;

public class ContextNode extends Node {
    public ContextNode(String info) {
        super(info);
    }
    String contextId;

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    @Override
    public String toString() {
        return "ContextNode{" +
                "info='" + info + '\'' +
                ", children=" + children +
                '}';
    }
}