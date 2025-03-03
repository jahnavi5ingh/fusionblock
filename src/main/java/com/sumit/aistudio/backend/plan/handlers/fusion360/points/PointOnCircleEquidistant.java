package com.sumit.aistudio.backend.plan.handlers.fusion360.points;

import com.sumit.aistudio.backend.fusion360.GeometryUtils;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.IntRange;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@IsNodeHandler
@Component
public class PointOnCircleEquidistant extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        int count = node.getData().getIntProperty("count");
        float x = node.getData().getFloatProperty("x");
        float y = node.getData().getFloatProperty("y");
        float z = node.getData().getFloatProperty("z");
        float radius = node.getData().getFloatProperty("r");
        List<Point> values = new ArrayList<>();
        double [] [] pts = GeometryUtils.generateEquallySpacedPointsOnCircle(x,y,radius,count);
        for (int i = 0; i < pts.length; i++) {
            values.add(new Point((float) pts[i][0], (float) pts[i][1], z));
        }

        node.getOutput().getProperties().put("output", values);

    }
}
