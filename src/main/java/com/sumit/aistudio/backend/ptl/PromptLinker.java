package com.sumit.aistudio.backend.ptl;

import com.sumit.aistudio.backend.agents.AgentInterface;
import com.sumit.aistudio.backend.agents.OllamaAgent;
import com.sumit.aistudio.backend.linker.LinkInfo;
import com.sumit.aistudio.backend.linker.LinkInfoService;
import com.sumit.aistudio.backend.prompt.Prompt;
import com.sumit.aistudio.backend.prompt.PromptService;
import com.sumit.aistudio.backend.ptl.nodes.GenInfoNode;
import org.aspectj.weaver.loadtime.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PromptLinker {
    @Autowired
    LinkInfoService linkInfoService;
    //map of string to string called contexts
    Map<String,String> promptsOutputs = new HashMap<>();
    Map<String,String> contextOutputs = new HashMap<>();
    Map<String, GenInfoNode> genInfoNodeMap = new HashMap<>();

    @Autowired
    PromtCompilerGenerator generator = new PromtCompilerGenerator();

    @Autowired
    PromptService promptService;

    @Autowired
    OllamaAgent agent;

    VelocityHelper velocityHelper =  new VelocityHelper();


    public void setPromptService(PromptService promptService) {
        this.promptService = promptService;
    }

    public void setAgent(OllamaAgent agent) {
        this.agent = agent;
    }

    public void setGenerator(PromtCompilerGenerator generator) {
        this.generator = generator;
    }

    public void addGenInfoNode(String promptId, GenInfoNode genInfoNode){
        genInfoNodeMap.put(promptId,genInfoNode);
    }
    public GenInfoNode getGenInfoNode(String promptId){
        return genInfoNodeMap.get(promptId);
    }

    public void setGenInfoNodeMap(Map<String, GenInfoNode> genInfoNodeMap) {
        this.genInfoNodeMap = genInfoNodeMap;
    }

    public Map<String, GenInfoNode> getGenInfoNodeMap() {
        return genInfoNodeMap;
    }

    public void addPromptOutput(String promptId, String promptInfo){
        promptsOutputs.put(promptId,promptInfo);
    }
    public String link(String promptId){
        if(promptsOutputs.containsKey(promptId)){
            return promptsOutputs.get(promptId);
        }else{
            System.out.println("looking: "+promptId);
            Optional<LinkInfo> optional = linkInfoService.findById(promptId);
            if(optional.isPresent()) {
                System.out.println("found: "+promptId );
                promptsOutputs.put(promptId,optional.get().getOutput());
                return optional.get().getOutput();
            }else{
                //generate from pompt
                GenInfoNode parent = GenInfoNode.getRoot(promptId+"_parent");
                parent.addChild(genInfoNodeMap.get(promptId));
                return generator.generate(parent,this,promptService,agent).getInfo();
            }

        }
    }

    public String std(String type){
        Optional<Prompt> prompt = promptService.getPromptByType(type);
        if(prompt.isPresent()) {
            String promptStr = compileContext(prompt.get().getData(),this);
            return promptStr;
        }
        return "";
    }


    public Map<String, String> getPromptsOutputs() {
        return promptsOutputs;
    }

    public String generatePrompt(String model, String promptStr) throws Exception {
        return generatePrompt(model,promptStr,agent);
    }

        public String generatePrompt(String model, String promptStr, AgentInterface agentIn) throws Exception {
          promptStr = compileContext(promptStr,this);
        return agentIn.callOllamaPromptStr(promptStr,model);
    }

    public String compileContext(String contextStr, PromptLinker linkerInfo) {
        Map<String,Object> contextMap = new HashMap<>();
        contextMap.put("linker",linkerInfo);
        return velocityHelper.generateContentStringTemplate(contextStr,contextMap).toString();
    }

    public Map<String, String> getContextOutputs() {
        return contextOutputs;
    }
    public String getContext(String contextId){
        return contextOutputs.get(contextId);
    }
    public void setContext(String contextId,String context){
        contextOutputs.put(contextId,context);
    }
}
