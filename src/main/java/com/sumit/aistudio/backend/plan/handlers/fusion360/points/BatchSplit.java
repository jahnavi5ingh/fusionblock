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
public class BatchSplit extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
       //get in props count, xrangeLow, high,yrange low,high ,zrange low, high where low and high values are in same string separated by : in xrange...
        List<Point> points = (List<Point>) node.getData().getProperties().get("points");
        int count = node.getData().getIntProperty("count");
        //split points into count number of points
        List<List<Point>> batches = new ArrayList<>();
        // craete batches of count number of points
        List<Point> batch = new ArrayList<>();
        int i = 0;
        for(Point point: points){
            batch.add(point);
            i++;
            if(i==count){
                batches.add(batch);
                batch = new ArrayList<>();
                i=0;
            }
        }
        if(batch.size()>0){
            batches.add(batch);
        }
        node.getOutput().getProperties().put("output", batches);

    }
}
