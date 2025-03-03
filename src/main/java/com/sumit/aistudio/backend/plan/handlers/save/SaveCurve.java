package com.sumit.aistudio.backend.plan.handlers.save;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sumit.aistudio.backend.collections.Collections;
import com.sumit.aistudio.backend.curves.collections.Curve;
import com.sumit.aistudio.backend.curves.collections.CurveService;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.Handler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@IsNodeHandler
@Component
public class SaveCurve extends Fusion360Handler {
    @Autowired
    CurveService service;
    ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    @Override
    public void handleNode(Node node) {
        String name = node.getData().getStringProperty("name");
        Object object = node.getData().getProperty("curve");
        List<Point> points = (List<Point>) object;
        translateFromCartesian(points);
        addUIDUIIFMissing(points);

        String data = prettyJson(object);
        Curve collection = new Curve();
        collection.setType(name);
        collection.setData(data);
        service.getEntityByType(name).ifPresentOrElse(
                collection1 -> {
                    collection.setId(collection1.getId());
                    service.updateEntity(collection);
                },
                () -> service.updateEntity(collection));
         try {
             System.out.println(object==null?"null":object.toString());
                node.getOutput().getProperties().put("output",object);
            } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
