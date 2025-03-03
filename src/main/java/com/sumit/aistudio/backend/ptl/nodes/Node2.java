package com.sumit.aistudio.backend.ptl.nodes;

import java.util.ArrayList;
import java.util.List;

public class Node2 {
    String info;
    List<Node2> children = new ArrayList<>();


    public Node2(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }


    public void addChild(Node2 child) {
        children.add(child);
    }




    //#G{42
//valid 42 context

//#START:42:java("f",true,["a"]);
//valid 42 prompt



    //#G}
    public List<Node2> getChildren() {
        return children;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}




