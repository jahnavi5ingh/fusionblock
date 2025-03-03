package com.sumit.aistudio.backend.plan.handlers.fusion360.points;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.Handler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@IsNodeHandler
@Component
public class FilterPointsByTags extends Fusion360Handler {

    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handleNode(Node node) {
        List<Point> points = super.getPointsFromInput(node.getData().getProperty("points"));
        List<Point> temp = new ArrayList<>();
        temp.addAll(points);
        //add tag "null" to points that does not have tags or tags is empty
        for(Point point:temp){
            if(point.getTags() == null || point.getTags().isEmpty()){
                point.getTags().add("null");
            }
        }
        points = temp;
        String[] tags = node.getData().getStringProperty("tags").split(",");

        String filterType = node.getData().getStringProperty("filterType");
        //iterate over each point and change mode to spline
        List<Point> output = new ArrayList<>();
        for(Point point:points){
            if(filterType.equals("include")){
                boolean add = false;

                for (String tag : tags) {
                    if(point.getTags().contains(tag)){
                        add = true;
                        break;
                    }
                }
                if(add){
                    output.add(point);
                }

            }else{
                boolean add = true;
                for (String tag : tags) {
                    if(point.getTags().contains(tag)){
                        add = false;
                        break;
                    }
                }
                if(add){
                    output.add(point);
                }
            }
        }
        try {
            node.getOutput().getProperties().put("output",output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
