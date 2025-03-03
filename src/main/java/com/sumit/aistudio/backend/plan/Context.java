package com.sumit.aistudio.backend.plan;

import com.sumit.aistudio.backend.agents.OllamaAgent;
import com.sumit.aistudio.backend.fusion360.Fusion360Client;
import com.sumit.aistudio.backend.linker.LinkInfoService;
import com.sumit.aistudio.backend.prompt.PromptService;
import com.sumit.aistudio.backend.prompttemplates.PromptTemplateService;
import com.sumit.aistudio.backend.ptl.PromptLinker;
import com.sumit.aistudio.backend.ptl.VelocityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Context {
    @Autowired
    PromptLinker linkerInfo;

    @Autowired
    PromptService promptService;

    @Autowired
    PromptTemplateService promptTemplateService;

    @Autowired
    OllamaAgent agent;

    @Autowired
    LinkInfoService linkInfoService;

    VelocityHelper velocityHelper =  new VelocityHelper();

    public VelocityHelper getVelocityHelper() {
        return velocityHelper;
    }

    public LinkInfoService getLinkInfoService() {
        return linkInfoService;
    }


    public PromptService getPromptService() {
        return promptService;
    }

    public OllamaAgent getAgent() {
        return agent;
    }

    public PromptTemplateService getPromptTemplateService() {
        return promptTemplateService;
    }
    public PromptLinker getPromptLinker() {
        return linkerInfo;
    }

    public void setLinkerInfo(PromptLinker linkerInfo) {
        this.linkerInfo = linkerInfo;
    }


}
