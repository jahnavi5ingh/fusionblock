package com.sumit.aistudio.backend.ptl.nodes;


public class TextNode extends Node {


    public TextNode(String text) {
        super(text);
    }

    @Override
    public String toString() {
        return "TextNode{" +
                "info='" + info + '\'' +
                '}';
    }
}