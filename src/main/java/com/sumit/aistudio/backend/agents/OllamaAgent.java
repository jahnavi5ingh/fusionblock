package com.sumit.aistudio.backend.agents;

import io.github.ollama4j.models.OllamaResult;
import io.github.ollama4j.utils.Options;
import org.springframework.stereotype.Service;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.utils.OptionsBuilder;

@Service
public class OllamaAgent extends AbstractAgent{



    String host = "http://localhost:11434/";

    OllamaAPI ollamaAPI = new OllamaAPI(host);
    {
        new Thread(()-> {
            ollamaAPI.setRequestTimeoutSeconds(60);
            try {
                ollamaAPI.generate(mapModel("_"), "", false, new OptionsBuilder().setTemperature(.1f).build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }


    protected  OllamaResult generate(String model, String prompt, boolean b, Options build){
        try {
            return ollamaAPI.generate(model ,prompt, false, new OptionsBuilder().build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getName() {
        return "ollama_agent";
    }
}
