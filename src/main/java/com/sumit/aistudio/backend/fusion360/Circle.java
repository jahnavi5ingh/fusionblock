package com.sumit.aistudio.backend.fusion360;

import org.locationtech.jts.geom.Point;

public class Circle {
    private final double radius;

    private final Point center;
    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public Point getCenter() {
        return center;
    }
}
