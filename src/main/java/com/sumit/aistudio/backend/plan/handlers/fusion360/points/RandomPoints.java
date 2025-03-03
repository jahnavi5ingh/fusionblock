package com.sumit.aistudio.backend.plan.handlers.fusion360.points;

import com.sumit.aistudio.backend.fusion360.GeometryUtils;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.models.IntRange;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@IsNodeHandler
@Component
public class RandomPoints extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        int count = node.getData().getIntProperty("count");
        String xrange = node.getData().getStringProperty("xrange");

        String yrange = node.getData().getStringProperty("yrange");
        String zrange = node.getData().getStringProperty("zrange");
        IntRange xRange = new IntRange(xrange);
        IntRange yRange = new IntRange(yrange);
        IntRange zRange = new IntRange(zrange);

        List<Point> values = new ArrayList<>();
        GeometryUtils geometryUtils = new GeometryUtils();
        double[][] dblValues = geometryUtils.generateRandomPointsInBoundingBox(xRange.getMin(), xRange.getMax(), yRange.getMin(),
                yRange.getMax(), zRange.getMin(), zRange.getMax(), count);
        for (int i = 0; i < dblValues.length; i++) {
            values.add(new Point((float) dblValues[i][0], (float) dblValues[i][1], (float) dblValues[i][2]));
        }
        node.getOutput().getProperties().put("output", values);

    }
}
