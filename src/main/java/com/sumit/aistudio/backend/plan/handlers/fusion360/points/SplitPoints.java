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
public class SplitPoints extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        List<Point> points = (List<Point>) node.getData().getProperties().get("points");
        String expr = (String) node.getData().getProperties().get("expression");
        List<Point> newPoints = new ArrayList<>();
        //switch on field if x, y or z
        Expression e = new ExpressionBuilder(expr)
                .variables("x", "y", "z","i")
                .build();
        int i = 0;
        for (Point point : points) {
            Point newPoint = point;
            newPoint.setIndex(i);
            e.setVariable("x", point.getX());
            e.setVariable("y", point.getY());
            e.setVariable("z", point.getZ());
            e.setVariable("i", i);

            if(((int)e.evaluate())==0) newPoints.add(newPoint);
            i++;
        }

        node.getOutput().getProperties().put("output", newPoints);

    }
}
