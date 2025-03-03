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
public class UpdatePoints2 extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        List<Point> points = (List<Point>) node.getData().getProperties().get("points");
        String xexpr = (String) node.getData().getProperties().get("xexpr");
        String yexpr = (String) node.getData().getProperties().get("yexpr");
        String zexpr = (String) node.getData().getProperties().get("zexpr");
        float a = node.getData().getFloatProperty("a");
        float b = node.getData().getFloatProperty("b");
        float c = node.getData().getFloatProperty("c");
        float d = node.getData().getFloatProperty("d");
        List<Point>newPoints = new ArrayList<>();
        //switch on field if x, y or z
        Expression xe = new ExpressionBuilder(xexpr)
                .variables("x", "y", "z","a","b","c","d","i")
                .build();
        Expression ye = new ExpressionBuilder(yexpr)
                .variables("x", "y", "z","a","b","c","d","i")
                .build();
        Expression ze = new ExpressionBuilder(zexpr)
                .variables("x", "y", "z","a","b","c","d","i")
                .build();
        int i = 0;
        //iterate points and create new point and cahnge x,y or z based on field
        for(Point point: points){
            Point newPoint = point;
            xe.setVariable("x", point.getX());
            xe.setVariable("y", point.getY());
            xe.setVariable("z", point.getZ());
            xe.setVariable("a", a);
            xe.setVariable("b", b);
            xe.setVariable("c", c);
            xe.setVariable("d", d);
            xe.setVariable("i", i);
            newPoint.setX((float)xe.evaluate());
            ye.setVariable("x", point.getX());
            ye.setVariable("y", point.getY());
            ye.setVariable("z", point.getZ());
            ye.setVariable("a", a);
            ye.setVariable("b", b);
            ye.setVariable("c", c);
            ye.setVariable("d", d);
            ye.setVariable("i", i);
            newPoint.setY((float)ye.evaluate());
            ze.setVariable("x", point.getX());
            ze.setVariable("y", point.getY());
            ze.setVariable("z", point.getZ());
            ze.setVariable("a", a);
            ze.setVariable("b", b);
            ze.setVariable("c", c);
            ze.setVariable("d", d);
            ze.setVariable("i", i);
            newPoint.setZ((float)ze.evaluate());
            newPoints.add(newPoint);
            i++;
        }


        node.getOutput().getProperties().put("output", newPoints);

    }
}
