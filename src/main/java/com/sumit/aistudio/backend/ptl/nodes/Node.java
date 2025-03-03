package com.sumit.aistudio.backend.ptl.nodes;

import java.util.ArrayList;
import java.util.List;

public class Node {
     String info;
     List<Node> children = new ArrayList<>();


    public Node(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }


    public void addChild(Node child) {
        children.add(child);
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
