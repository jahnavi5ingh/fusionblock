package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.Plan;
import com.sumit.aistudio.backend.plan.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@IsNodeHandler
@Component
public class GeomGenerator extends Handler{
    @Autowired
    PlanService planService;
    @Override
    public void handleNode(Node node) {
        try {
            String generator  = node.getData().getStringProperty("generator");
            Optional<Plan> plan = planService.getPlanByName(generator);
            if(plan.isPresent()){
                node.getExecutionContext().getPlanExecutor().executeGraph(plan.get().getData());
            }
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
