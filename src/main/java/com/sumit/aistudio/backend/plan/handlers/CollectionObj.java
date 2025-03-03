package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.collections.Collections;
import com.sumit.aistudio.backend.collections.CollectionsService;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@IsNodeHandler
@Component
public class CollectionObj  extends Handler{
    @Autowired
    CollectionsService service;

    @Override
    public void handleNode(Node node) {
        try {
            String type = node.getData().getStringProperty("CollectionType");
            Optional<Collections> entity = service.getEntityByType(type);
            if(entity.isPresent()){
                node.getOutput().getProperties().put("output", entity.get().getData());
            }else{
                //set output to null
                node.getOutput().getProperties().put("output", null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
