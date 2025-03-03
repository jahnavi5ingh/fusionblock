package com.sumit.aistudio.backend.ptl.nodes;


public class DependencyNode extends Node {


    public DependencyNode(String text) {
        super(text);
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
        return "TextNode{" +
                "info='" + info + '\'' +
                '}';
    }
}