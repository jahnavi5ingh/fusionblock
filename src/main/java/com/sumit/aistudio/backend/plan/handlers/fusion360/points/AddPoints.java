package com.sumit.aistudio.backend.plan.handlers.fusion360.points;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

import java.util.List;

@IsNodeHandler
@Component
public class AddPoints extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        List<Point> points = (List<Point>) node.getData().getProperties().get("points");
        String name = "collections";
        try {
            List<Object> values = (List<Object>)node.getExecutionContext().getPlanExecutor().getIncommingValues(name,node);

            for(Object value: values){
                if(value instanceof List){
                    List<Point> points2 = (List<Point>) value;
                    for(Point point: points2){
                        points.add(point);
                    }
                }
            }
            //set to output
            node.getOutput().getProperties().put("output",points);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        node.getOutput().getProperties().put("output", points);

    }
}
