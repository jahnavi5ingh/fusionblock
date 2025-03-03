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
public class IndexedPoint extends Fusion360Handler {

    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handleNode(Node node) {

        int index = node.getData().getIntProperty("index");
        List<Point> points = super.getPointsFromInput(node.getData().getProperty("points"));
        try {
            node.getOutput().getProperties().put("output",points.get(index));
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
}
