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
public class MirrorPoints extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        List<Point> points = (List<Point>) node.getData().getProperty("points");
        int axis = node.getData().getIntProperty("axis");

        //coinvert points to double array
        double[][] values = new double[points.size()][3];
        for (int i = 0; i < points.size(); i++) {
            values[i][0] = points.get(i).getX();
            values[i][1] = points.get(i).getY();
            values[i][2] = points.get(i).getZ();
        }
        //get the translation values
        double[][] valuesTranslated = GeometryUtils.mirror(values, axis);
        //convert back to points
        List<Point> pointsTranslated = new ArrayList<>();
        for (int i = 0; i < valuesTranslated.length; i++) {
            points.get(i).setX((float) valuesTranslated[i][0]);
            points.get(i).setY((float) valuesTranslated[i][1]);
            points.get(i).setZ((float) valuesTranslated[i][2]);
            pointsTranslated.add(new Point((float) valuesTranslated[i][0], (float) valuesTranslated[i][1], (float) valuesTranslated[i][2]));
        }
        node.getOutput().getProperties().put("output", pointsTranslated);

    }
}
