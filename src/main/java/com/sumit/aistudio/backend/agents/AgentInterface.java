package com.sumit.aistudio.backend.agents;

import com.sumit.aistudio.backend.prompt.Prompt;
import com.sumit.aistudio.backend.ptl.PromptLinker;
import com.sumit.aistudio.backend.ptl.nodes.ContextNode;
import com.sumit.aistudio.backend.ptl.nodes.GenInfoNode;
import com.sumit.aistudio.backend.ptl.nodes.PromptNode;

public interface AgentInterface {
    String getName();
    public String callOllama(Prompt promptVal, GenInfoNode genInfoNode, ContextNode contextNode, PromptNode promptNode, PromptLinker linkerInfo)throws Exception ;
    public String callOllamaPromptStr(String prompt, String model)throws Exception ;

    }
