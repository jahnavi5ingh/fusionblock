package com.sumit.aistudio.backend.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumit.aistudio.backend.plan.ExecutionContext;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Node {
    private String id;
    private String type;
    private Position position;
    private NodeData data = new NodeData();
    private Measured measured;
    private boolean selected;
    private boolean dragging;
    private double width;
    private double height;
    private boolean resizing;




    ExecutionContext context = new ExecutionContext();

    List<Node> children = new ArrayList<>();

    public void addChild(Node node) {
        children.add(node);
    }
    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    private NodeData output = new NodeData();


    public NodeData getOutput() {
        return output;
    }

    public void setOutput(NodeData output) {
        this.output = output;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public NodeData getData() {
        return data;
    }

    public void setData(NodeData data) {
        this.data = data;
    }

    public Measured getMeasured() {
        return measured;
    }

    public void setMeasured(Measured measured) {
        this.measured = measured;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isResizing() {
        return resizing;
    }

    public void setResizing(boolean resizing) {
        this.resizing = resizing;
    }

    public ExecutionContext getExecutionContext() {
        return context;
    }
    public void setExecutionContext(ExecutionContext context) {
        this.context = context;
    }

}
