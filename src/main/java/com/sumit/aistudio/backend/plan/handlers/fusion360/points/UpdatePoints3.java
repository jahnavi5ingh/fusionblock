package com.sumit.aistudio.backend.plan.handlers.fusion360.points;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@IsNodeHandler
@Component
public class UpdatePoints3 extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        List<Point> points = (List<Point>) node.getData().getProperties().get("points");
        List<Point> points1 = (List<Point>) node.getData().getProperties().get("points1");
        List<Point> points2 = (List<Point>) node.getData().getProperties().get("points2");
        List<Point> points3 = (List<Point>) node.getData().getProperties().get("points3");
        String xexpr = (String) node.getData().getProperties().get("xexpr");
        String yexpr = (String) node.getData().getProperties().get("yexpr");
        String zexpr = (String) node.getData().getProperties().get("zexpr");

        List<Point>newPoints = new ArrayList<>();
        //switch on field if x, y or z
        Expression xe = new ExpressionBuilder(xexpr)
                .variables("x", "y", "z","x1","x2","x3","y1","y2","y3","z1","z2","z3","i")
                .build();
        Expression ye = new ExpressionBuilder(yexpr)
                .variables("x", "y", "z","x1","x2","x3","y1","y2","y3","z1","z2","z3","i")
                .build();
        Expression ze = new ExpressionBuilder(zexpr)
                .variables("x", "y", "z","x1","x2","x3","y1","y2","y3","z1","z2","z3","i")
                .build();
        int i = 0;
        //iterate points and create new point and cahnge x,y or z based on field
        for(Point point: points){
            Point newPoint = point;
            xe.setVariable("x", point.getX());
            xe.setVariable("y", point.getY());
            xe.setVariable("z", point.getZ());
            xe.setVariable("x1", getXMod(points1,i));
            xe.setVariable("x2", getXMod(points2,i));
            xe.setVariable("x3", getXMod(points3,i));
            xe.setVariable("y1", getYMod(points1,i));
            xe.setVariable("y2", getYMod(points2,i));
            xe.setVariable("y3", getYMod(points3,i));
            xe.setVariable("z1", getZMod(points1,i));
            xe.setVariable("z2", getZMod(points2,i));
            xe.setVariable("z3", getZMod(points3,i));

            xe.setVariable("i", i);
            newPoint.setX((float)xe.evaluate());
            ye.setVariable("x", point.getX());
            ye.setVariable("y", point.getY());
            ye.setVariable("z", point.getZ());
            ye.setVariable("x1", getXMod(points1,i));
            ye.setVariable("x2", getXMod(points2,i));
            ye.setVariable("x3", getXMod(points3,i));
            ye.setVariable("y1", getYMod(points1,i));
            ye.setVariable("y2", getYMod(points2,i));
            ye.setVariable("y3", getYMod(points3,i));
            ye.setVariable("z1", getZMod(points1,i));
            ye.setVariable("z2", getZMod(points2,i));
            ye.setVariable("z3", getZMod(points3,i));
            ye.setVariable("i", i);
            newPoint.setY((float)ye.evaluate());
            ze.setVariable("x", point.getX());
            ze.setVariable("y", point.getY());
            ze.setVariable("z", point.getZ());
            ze.setVariable("x1", getXMod(points1,i));
            ze.setVariable("x2", getXMod(points2,i));
            ze.setVariable("x3", getXMod(points3,i));
            ze.setVariable("y1", getYMod(points1,i));
            ze.setVariable("y2", getYMod(points2,i));
            ze.setVariable("y3", getYMod(points3,i));
            ze.setVariable("z1", getZMod(points1,i));
            ze.setVariable("z2", getZMod(points2,i));
            ze.setVariable("z3", getZMod(points3,i));


            ze.setVariable("i", i);
            newPoint.setZ((float)ze.evaluate());
            newPoints.add(newPoint);
            i++;
        }


        node.getOutput().getProperties().put("output", newPoints);

    }
    //function to get x coordinate from points collection at index i
    //it will first check if collection is not null and has size greater than or equal to i
    //then it will get the point at index i and return its x coordinate
    //or else return 0
    public float getX(List<Point> points, int i){
        if(points!=null && points.size()>i){
            return points.get(i).getX();
        }
        return 0;
    }
    //function to get y coordinate from points collection at index i
    //it will first check if collection is not null and has size greater than or equal to i
    //then it will get the point at index i and return its y coordinate
    //or else return 0
    public float getY(List<Point> points, int i){
        if(points!=null && points.size()>i){
            return points.get(i).getY();
        }
        return 0;
    }
    //function to get z coordinate from points collection at index i
    //it will first check if collection is not null and has size greater than or equal to i
    //then it will get the point at index i and return its z coordinate
    //or else return 0
    public float getZ(List<Point> points, int i){
        if(points!=null && points.size()>i){
            return points.get(i).getZ();
        }
        return 0;
    }
    //function to get z coordinate from points collection at index i
    //it will first check if collection is not null
    //then it will get the point at index i mod size of collection and return its z coordinate
    //or else return 0
    public float getZMod(List<Point> points, int i){
        if(points!=null){
            return points.get(i%points.size()).getZ();
        }
        return 0;
    }
    //function to get y coordinate from points collection at index i
    //it will first check if collection is not null
    //then it will get the point at index i mod size of collection and return its y coordinate
    //or else return 0
    public float getYMod(List<Point> points, int i){
        if(points!=null){
            return points.get(i%points.size()).getY();
        }
        return 0;
    }
    //function to get x coordinate from points collection at index i
    //it will first check if collection is not null
    //then it will get the point at index i mod size of collection and return its x coordinate
    //or else return 0
    public float getXMod(List<Point> points, int i){
        if(points!=null){
            return points.get(i%points.size()).getX();
        }
        return 0;
    }

}
