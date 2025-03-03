package com.sumit.aistudio.backend.plan.handlers.fusion360.points;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumit.aistudio.backend.collections.Collections;
import com.sumit.aistudio.backend.collections.CollectionsService;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;


@IsNodeHandler
@Component
public class ConvertToSpline extends Handler {

    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handleNode(Node node) {
        Object object = node.getData().getProperty("points");
        List<Point> points = null;
        if(! (object instanceof String)){
            try {
                points = objectMapper.readValue(object.toString(), new TypeReference<List<Point>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else{
            points = (List<Point>) object;
        }
        //iterate over each point and change mode to spline
        for(Point point:points){
            point.setMode("spline");
        }
        try {
            node.getOutput().getProperties().put("output",points);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
