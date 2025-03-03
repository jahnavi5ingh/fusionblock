package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.agents.AgentManager;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@IsNodeHandler
@Component
public class PromptAgentHandler extends Handler {
    @Autowired
    AgentManager agentManager;

    @Override
    public void handleNode(Node node) {
        String agentName = node.getData().getStringProperty("pname");
         try {
                node.getOutput().getProperties().put("output",agentManager.getAgent(agentName));
            } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
