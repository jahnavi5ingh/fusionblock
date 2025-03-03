package com.sumit.aistudio.backend.agents;

import com.sumit.aistudio.backend.prompt.Prompt;
import com.sumit.aistudio.backend.ptl.PromptLinker;
import com.sumit.aistudio.backend.ptl.VelocityHelper;
import com.sumit.aistudio.backend.ptl.nodes.ContextNode;
import com.sumit.aistudio.backend.ptl.nodes.GenInfoNode;
import com.sumit.aistudio.backend.ptl.nodes.PromptNode;
import io.github.ollama4j.models.OllamaResult;
import io.github.ollama4j.utils.Options;
import io.github.ollama4j.utils.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAgent implements AgentInterface{
    VelocityHelper velocityHelper =  new VelocityHelper();
    protected  String mapModel(String model) {
        switch (model) {
            case "l":
                return "llama3.1";
            case "d":
                return "deepseek-coder-v2";
            case "c":
                return "codestral";
            case "s":
                return "sqlcoder";
            case "cl":
                return "codellama";
            case "_":
            case "-":
                return "llama3.1";
            default:
                return "llama3.1";
        }
    }
    public String callOllamaPromptStr(String prompt,String model)throws Exception {
        System.out.println("Ollama called");


        OllamaResult result =
                generate(mapModel(model), prompt,false,new OptionsBuilder().build());

        System.out.println(result.getResponse());
        return result.getResponse();
    }


    public String callOllama(Prompt promptVal, GenInfoNode genInfoNode, ContextNode contextNode, PromptNode promptNode, PromptLinker linkerInfo)throws Exception {
        System.out.println("Ollama called");

        Map<String, Object> ctx = new HashMap<>();
        ctx.put("context", contextNode==null?"":contextNode.getInfo());
        ctx.put("question", promptNode.getInfo());
        ctx.put("linker",linkerInfo);
        String prompt =  velocityHelper.generateContentStringTemplate(promptVal.getData(),ctx).toString();

        OllamaResult result =
                generate(mapModel(genInfoNode.getParsedTemplate().getModel()), prompt,false,new OptionsBuilder().build());

        System.out.println(result.getResponse());
        return result.getResponse();
    }

    protected abstract OllamaResult generate(String model, String prompt, boolean b, Options build);
}
