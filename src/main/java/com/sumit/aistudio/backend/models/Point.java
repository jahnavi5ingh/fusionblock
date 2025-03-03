package com.sumit.aistudio.backend.models;

import java.util.ArrayList;
import java.util.List;

public class Point {
float x;
float y;
float z;
String uid;
String mode;

float xloc;
float yloc;

List<String> tags = new ArrayList<>();


    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public float getXloc() {
        return xloc;
    }

    public void setXloc(float xloc) {
        this.xloc = xloc;
    }

    public float getYloc() {
        return yloc;
    }

    public void setYloc(float yloc) {
        this.yloc = yloc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    int index = 0;


    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Point(float x, float y, float z, int index) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.index = index;
    }
    public Point() {
    }
    //add a clone construction to create from another point
    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
        this.index = point.index;
        this.uid = point.uid;
        this.mode = point.mode;
        this.xloc = point.xloc;
        this.yloc = point.yloc;

    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
