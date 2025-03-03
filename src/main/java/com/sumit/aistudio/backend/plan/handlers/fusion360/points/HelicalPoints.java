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
public class HelicalPoints extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        int count = node.getData().getIntProperty("count");
        float x = node.getData().getFloatProperty("x");
        float y = node.getData().getFloatProperty("y");
        float z = node.getData().getFloatProperty("z");
        float radius = node.getData().getFloatProperty("r");
        float zheight = node.getData().getFloatProperty("height");
        int turns = node.getData().getIntProperty("turns");
        List<Point> values = new ArrayList<>();
        double [] [] pts = GeometryUtils.generateHelicalCurvePoints( x, y, z, radius, zheight, turns,count);
        for (int i = 0; i < pts.length; i++) {
            values.add(new Point((float)pts[i][0], (float)pts[i][1], (float)pts[i][2]));
        }
        values.add(new Point(values.get(0).getX(),values.get(0).getY(), zheight));

        node.getOutput().getProperties().put("output", values);

    }
}
