package com.sumit.aistudio.backend.ptl.nodes;

public class PromptNode extends Node {

    public PromptNode(String info) {
        super(info);
    }

    @Override
    public String toString() {
        return "PromptNode{" +
                "info='" + info + '\'' +
                ", children=" + children +
                '}';
    }
}