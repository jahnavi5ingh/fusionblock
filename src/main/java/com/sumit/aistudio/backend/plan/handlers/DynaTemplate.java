package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@IsNodeHandler
@Component
public  class DynaTemplate extends Handler{
    @Override
    public void handleNode(Node node) {
        String template = node.getData().getStringProperty("template");
        Map<String,Object> contextData = new HashMap<>();
        node.getData().getProperties().forEach((k,v)->{
           contextData.put(k,v);
        });
       String text =  context.getVelocityHelper().generateContentStringTemplate(template,contextData).toString();
        node.getOutput().getProperties().put("output",text);
    }
}