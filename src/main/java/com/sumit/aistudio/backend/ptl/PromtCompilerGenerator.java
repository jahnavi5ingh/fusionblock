package com.sumit.aistudio.backend.ptl;

import com.sumit.aistudio.backend.agents.OllamaAgent;
import com.sumit.aistudio.backend.prompt.Prompt;
import com.sumit.aistudio.backend.prompt.PromptService;
import com.sumit.aistudio.backend.ptl.nodes.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class PromtCompilerGenerator {


    public Map<String, GenInfoNode> generateMapOfGenInfoNode(GenInfoNode parentNode){
        Map<String,GenInfoNode> generatedNodeMap = new HashMap<>();
        generatedNodeMap.put(parentNode.getParsedTemplate().getPromptID(),parentNode);
        _generateMapOfGenInfoNode(parentNode,generatedNodeMap);
        return generatedNodeMap;
    }

    private void _generateMapOfGenInfoNode(GenInfoNode parentNode, Map<String, GenInfoNode> generatedNodeMap) {
        for (Node currentNode : parentNode.getChildren()) {
            if (currentNode instanceof GenInfoNode) {
                generatedNodeMap.put(((GenInfoNode) currentNode).getParsedTemplate().getPromptID(), ((GenInfoNode) currentNode));
                _generateMapOfGenInfoNode((GenInfoNode) currentNode, generatedNodeMap);
            }
        }
    }

    public GeneratedNode generate(GenInfoNode parentNode, PromptLinker linkerInfo, PromptService promptService, OllamaAgent agent){
        GeneratedNode generatedNode = new GeneratedNode("");
        StringBuilder genText = new StringBuilder();
        ContextNode contextNode = null;
        DependencyNode dependencyNode   = null;
        boolean mode = true;//inline;
        String offlineText = "";
        for(Node currentNode: parentNode.getChildren()){
            if(currentNode instanceof GenInfoNode) {
                GeneratedNode child = generate((GenInfoNode)currentNode, linkerInfo,promptService,agent);
                genText.append(child.getInfo()+"\n");
            }else if(currentNode instanceof TextNode) {
                genText .append(currentNode.getInfo()+"\n");
            }else if(currentNode instanceof  ContextNode) {
                contextNode = (ContextNode) currentNode;
            }else if(currentNode instanceof  DependencyNode) {
                dependencyNode = (DependencyNode) currentNode;
            } else if(currentNode instanceof  PromptNode){
                PromptOutput pout = generatePromptOutput((GenInfoNode)parentNode,contextNode,(PromptNode)currentNode,dependencyNode,linkerInfo,promptService,agent);
                if(pout.isInline()) {
                    genText.append(pout.getText() + "\n");
                    mode = true;
                }else{
                    offlineText = (offlineText + pout.getText() +"\n");
                    mode = false;
                }
            }
        }
        if(mode) {
            generatedNode.setInfo(genText.toString());
        }else{
            String offlineTextGen = StringUtils.replace(offlineText,"/*inlinegen*/",genText.toString());
            generatedNode.setInfo(offlineTextGen);
        }
        linkerInfo.addPromptOutput(parentNode.getParsedTemplate().getPromptID(),generatedNode.getInfo());
        return generatedNode;
    }


    private PromptOutput generatePromptOutput(GenInfoNode genInfoNode, ContextNode contextNode, PromptNode promptNode, DependencyNode dependencyNode, PromptLinker linkerInfo, PromptService promptService, OllamaAgent agent) {
        PromptOutput outp = new PromptOutput();

        String ctxStr =  (contextNode == null ? "" :linkerInfo.compileContext(contextNode.getInfo(),linkerInfo));

        Optional<Prompt> p = promptService.getPromptByType(genInfoNode.getParsedTemplate().getTemplate());
        if(p.isPresent()){
            outp.setInline(p.get().isInline());
            try {
                outp.setText(agent.callOllama(p.get(),genInfoNode,contextNode,promptNode,linkerInfo));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            if (genInfoNode.getInfo().contains("javaclass")) {
                outp.setInline(false);
                outp.setText(" /*inlinegen*/ " + "\n" + ctxStr + "\n" + genInfoNode.getInfo() + "\n" + promptNode.getInfo() + " \n");

            } else {
                outp.setText(ctxStr + "\n" + genInfoNode.getInfo() + "\n" + promptNode.getInfo());
            }
        }
         return outp;
    }


}
