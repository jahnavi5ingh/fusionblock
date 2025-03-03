package com.sumit.aistudio.backend.plan.handlers;

import com.google.common.collect.Maps;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.*;

@IsNodeHandler
@Component
public class SubPlan extends Handler{
    @Override
    public void handleNode(Node node) {
        System.out.println("NodeAHandler: "+node.getData());
        List<Map> inputFields = (List<Map>) node.getData().getProperties().get("fieldInfo");
        Map<String , Object> inputMap = Maps.newHashMap();
        for(Map inputField: inputFields){
            System.out.println("For each for item: "+inputField);
            inputMap.put((String)inputField.get("fieldName"), node.getData().getProperties().get(inputField.get("fieldName")));
        }
        String subPlanName = (String) node.getData().getProperties().get("type");
        Map<String,Object> output = node.getExecutionContext().getPlanExecutor().executePlan(subPlanName, inputMap);
        List<Map> outputFeilds=  (List<Map>) node.getData().getProperties().get("outputFieldInfo");
        for(Map outputField: outputFeilds){
            node.getOutput().getProperties().put((String)outputField.get("fieldName"), output.get((String)outputField.get("fieldName")));
        }

    }



}
