package com.sumit.aistudio.backend.models;

import java.util.ArrayList;
import java.util.List;

public class Path {
    String mode;
    List<Point> points = new ArrayList<>();
    //add getter setter
    public String getMode() {
        return mode;
    }
    //setmode
    public void setMode(String mode) {
        this.mode = mode;
    }
    //getpoints
    public List<Point> getPoints() {
        return points;
    }
    //setpoints
    public void setPoints(List<Point> points) {
        this.points = points;
    }
    //addpoint
    public void addPoint(Point point) {
        points.add(point);
    }
}
