package com.sumit.aistudio.backend.plan.handlers.fusion360.points;

import com.google.common.collect.Lists;
import com.sumit.aistudio.backend.fusion360.GeometryUtils;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@IsNodeHandler
@Component
public class ReversePoints extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        List<Point> points = (List<Point>) node.getData().getProperties().get("points");
        //reverse points into newpoints
        List<Point> newPoints = Lists.newArrayList();
        newPoints.addAll(points);
        Collections.reverse(newPoints);

        node.getOutput().getProperties().put("output", newPoints);

    }
}
