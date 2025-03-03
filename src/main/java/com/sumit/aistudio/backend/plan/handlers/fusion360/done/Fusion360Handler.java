package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumit.aistudio.backend.fusion360.Fusion360Client;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.handlers.Handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public abstract class Fusion360Handler extends Handler {

    public static final double EPSILONE = .0001;
    protected Map<String, Object> fusion360Context = new HashMap<>();
    protected  Fusion360Client fusion360Client = new Fusion360Client();
    ObjectMapper mapper = new ObjectMapper();
    Point cartesianCenter = new Point(600,500,0);
    @Override
    public void handleNode(Node node) {
        String texttoprint = node.getData().getStringProperty("texttoprint");
         try {
             System.out.println(texttoprint);
                node.getOutput().getProperties().put("output",texttoprint);
            } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    protected Fusion360Client getFusion360Client() {
        return fusion360Client;
    }

    public Map<String, Object> getFusion360Context() {
        return fusion360Context;
    }
    protected  float[] pointToFloatArray(Point point){
        return new float[]{point.getX(),point.getY(),point.getZ()};
    }

    protected Point floatarrayToPoint(float[] floatArray){
        return new Point(floatArray[0],floatArray[1],floatArray[2]);
    }
    protected float[][] convertPointsToFloatArray(List<Point> points) {
        float[][] pointsArray = new float[points.size()][3];
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            pointsArray[i] = pointToFloatArray(point);
        }
        return pointsArray;
    }
    protected String[] toStringArray(String input){
        return input.split(",");
    }
    protected float[] stringArrayToFloatArray(String[] input){
        float[] floatArray = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            floatArray[i] = Float.parseFloat(input[i]);
        }
        return floatArray;
    }
    //use object mapper and take a string and return list<Point>
    protected List<Point> stringToListPoint(String input){
        try {
            return mapper.readValue(input, new TypeReference<List<Point>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean isClosedPath(List<Point> points){
       //compare x,y,z values
        Point firstPoint = points.get(0);
        Point lastPoint = points.get(points.size()-1);
        // take abs of x ,y z difference nad if that is greater then .0001 then return false
        //make .oo1 as constant
        return Math.abs(firstPoint.getX()-lastPoint.getX())< EPSILONE && Math.abs(firstPoint.getY()-lastPoint.getY())< EPSILONE && Math.abs(firstPoint.getZ()-lastPoint.getZ())< EPSILONE;
    }
    protected void translateFromCartesian(List<Point> points){
        // Translate mouse coordinates to Cartesian coordinates
        for(Point point: points) {
            var mouseX = cartesianCenter.getX() + point.getX()*10;
            var mouseY = cartesianCenter.getY() - point.getY()*10;
            point.setXloc(mouseX);
            point.setYloc(mouseY);
        }
    }
    protected void scalePoints(List<Point> points, float scale){

        float scalex=scale;
        float scaley=scale;
        float scalez=scale;
        for(Point point: points){
            point.setX(scale(point.getX(),scalex));
            point.setY(scale(point.getY(),scaley));
            point.setZ(scale(point.getZ(),scalez));
        }
    }
    protected float scale(float f,float scale){
           float scaled = (float)(((f / scale) + .0000001) * 1000) / 1000;
           return scaled;
     }
    protected void addUIDUIIFMissing(List<Point>points){
        for(Point point: points){
            if(point.getUid()==null){
                point.setUid(UUID.randomUUID().toString());
            }
        }
    }
    protected <T> T getResponse(String s, Class<T> cls){
        try {
            return mapper.readValue(s,cls);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected List<Point> getPointsFromInput(Object crv){
        List<Point> points = null;
        if(crv instanceof String){
            points = (List<Point>) stringToListPoint((String) crv);
        }else{
            points = (List<Point>) crv;
        }
        return points;
    }
}
