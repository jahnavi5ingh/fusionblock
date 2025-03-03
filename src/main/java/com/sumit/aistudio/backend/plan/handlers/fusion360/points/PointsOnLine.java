package com.sumit.aistudio.backend.plan.handlers.fusion360.points;

import com.sumit.aistudio.backend.fusion360.GeometryUtils;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@IsNodeHandler
@Component
public class PointsOnLine extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        int count = node.getData().getIntProperty("count");
        // get point1 and point2 from getData and the use that to getpointonline from GeometryUtils.
        Point point1 = (Point)node.getData().getProperty("point1");
        Point point2 = (Point)node.getData().getProperty("point2");
        List<Point> values = new ArrayList<>();
        double [] [] pts = GeometryUtils.getPointsOnLine(point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ(), count);
        for (int i = 0; i < pts.length; i++) {
            values.add(new Point((float)pts[i][0], (float)pts[i][1], (float)pts[i][2]));
        }
        node.getOutput().getProperties().put("output", values);

    }
}
