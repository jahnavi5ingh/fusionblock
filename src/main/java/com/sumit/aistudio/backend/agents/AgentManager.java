package com.sumit.aistudio.backend.agents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentManager {
@Autowired
OllamaAgent agent;
@Autowired
OllamaAgent OpenAIAgent;

public AgentInterface getAgent(String agentName) {
    if (agentName.equals("ollama_agent")) {
        return agent;
    } else if (agentName.equals("openai_agent")) {
        return OpenAIAgent;
    }
    return null;
}
}
