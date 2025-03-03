package com.sumit.aistudio.backend.plan.handlers.save;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sumit.aistudio.backend.collections.Collections;
import com.sumit.aistudio.backend.collections.CollectionsService;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.CollectionObj;
import com.sumit.aistudio.backend.plan.handlers.Handler;
import com.sumit.aistudio.backend.plan.handlers.fusion360.done.Fusion360Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@IsNodeHandler
@Component
public class SavePoints extends Fusion360Handler {
    @Autowired
    CollectionsService collectionsService;
    ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    @Override
    public void handleNode(Node node) {
        String name = node.getData().getStringProperty("name");
        Object object = node.getData().getProperty("points");
        List<Point> points = (List<Point>) object;
        translateFromCartesian(points);
        addUIDUIIFMissing(points);
        String data = prettyJson(object);
        Collections collection = new Collections();
        collection.setType(name);
        collection.setData(data);
        collectionsService.getEntityByType(name).ifPresentOrElse(
                collection1 -> {
                    collection.setId(collection1.getId());
                    collectionsService.updateEntity(collection);
                },
                () -> collectionsService.updateEntity(collection));
         try {
             System.out.println(object==null?"null":object.toString());
                node.getOutput().getProperties().put("output",object);
            } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
