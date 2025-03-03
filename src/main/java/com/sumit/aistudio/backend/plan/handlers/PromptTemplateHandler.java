package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.ptl.PromptFileParser;
import com.sumit.aistudio.backend.ptl.PromtCompilerGenerator;
import com.sumit.aistudio.backend.ptl.nodes.GenInfoNode;
import com.sumit.aistudio.backend.ptl.nodes.GeneratedNode;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class PromptTemplateHandler extends Handler {
    @Override
    public void handleNode(Node node) {
        String pname = node.getData().getStringProperty("pname");
        String promptStr = node.getData().getStringProperty("promptText");
        try {
            PromptFileParser tester = new PromptFileParser();
            PromtCompilerGenerator gen = new PromtCompilerGenerator();
            String base = "C:/projects/aibackend/src/main/resources/ptl/";

                GenInfoNode genInfoNode = tester.parseData(pname,promptStr, context.getPromptLinker());
                System.out.println(genInfoNode);
                GeneratedNode rootGen = gen.generate(genInfoNode, context.getPromptLinker(), context.getPromptService(), context.getAgent());
                System.out.println(rootGen.getInfo());
                context.getLinkInfoService().saveLinkInfo(context.getPromptLinker());
                 node.getOutput().getProperties().put("output",rootGen.getInfo());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
