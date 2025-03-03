package com.sumit.aistudio.backend.agents;

import io.github.ollama4j.models.OllamaResult;
import io.github.ollama4j.utils.Options;
import org.springframework.stereotype.Service;

@Service
public class OpenAIAgent extends AbstractAgent{
    @Override
    protected OllamaResult generate(String model, String prompt, boolean b, Options build) {
        return null;
    }

    @Override
    public String getName() {
        return "openai_agent";
    }
}
