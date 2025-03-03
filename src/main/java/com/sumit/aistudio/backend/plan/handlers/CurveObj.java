package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.collections.Collections;
import com.sumit.aistudio.backend.collections.CollectionsService;
import com.sumit.aistudio.backend.curves.collections.Curve;
import com.sumit.aistudio.backend.curves.collections.CurveService;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@IsNodeHandler
@Component
public class CurveObj extends Handler{
    @Autowired
    CurveService service;

    @Override
    public void handleNode(Node node) {
        try {
            String type = node.getData().getStringProperty("CurveName");
            Optional<Curve> entity = service.getEntityByType(type);
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
