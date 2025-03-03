package com.sumit.aistudio.backend.plan.handlers.fusion360.points;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@IsNodeHandler
@Component
public class RandomizePoints extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        List<Point> points = (List<Point>) node.getData().getProperties().get("points");
        String what = (String) node.getData().getProperties().get("what");
        String[] props = what.split("\n");
        //for each prop call a function to randomize that prop
        for(String prop: props){
            String[] range = prop.split(":");
            String propName = range[0];
            String[] lowHigh = range[1].split(",");
            double low = Double.parseDouble(lowHigh[0]);
            double high = Double.parseDouble(lowHigh[1]);
            for(Point point: points){
                double value = Math.random() * (high - low) + low;
                //if prop is x,yz then set x,y,z
                if(propName.equals("x")){
                    point.setX(point.getX() + (float)value);
                }else if(propName.equals("y")){
                    point.setY(point.getY() + (float)value);
                }else if(propName.equals("z")){
                    point.setZ(point.getZ() + (float)value);
                }
            }
        }
        //clone points into newpoints
        List<Point> newPoints = new ArrayList<>();
        for(Point point: points){
            newPoints.add(new Point(point));
        }

        node.getOutput().getProperties().put("output", newPoints);

    }
}
