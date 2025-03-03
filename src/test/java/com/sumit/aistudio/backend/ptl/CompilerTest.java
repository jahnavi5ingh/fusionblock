package com.sumit.aistudio.backend.ptl;

import com.sumit.aistudio.backend.agents.OllamaAgent;
import com.sumit.aistudio.backend.prompt.PromptService;
import com.sumit.aistudio.backend.ptl.nodes.GenInfoNode;
import com.sumit.aistudio.backend.ptl.nodes.GeneratedNode;

public class CompilerTest {
    public static void main(String[] args) throws Exception {
        PromptFileParser tester = new PromptFileParser();
        PromptLinker linkerInfo = new PromptLinker();

        PromptService promptService = null;
        OllamaAgent agent = null;
        String base = "C:/projects/aibackend/src/main/resources/";
        GenInfoNode node = tester.parseFile(base + "ptl/old/test4.ptl", linkerInfo);
        System.out.println(node);
        PromtCompilerGenerator gen = new PromtCompilerGenerator();
        GeneratedNode rootGen = gen.generate(node, linkerInfo,promptService,agent);

        GenInfoNode node2 = tester.parseFile(base + "/ptl/old/test3.ptl", linkerInfo);
        System.out.println(node2);
        GeneratedNode rootGen2 = gen.generate(node2, linkerInfo,promptService,agent);
        System.out.println(rootGen2.getInfo());

        String codeBase = "C:/projects/aibackend/src/main/java/";
        GenInfoNode node3 = tester.parseFileFinal(codeBase + "com/sumit/aistudio/backend/ptl/Node2.java", linkerInfo);
        System.out.println(node3);
        GeneratedNode rootGen3 = gen.generate(node3, linkerInfo,promptService,agent);
        System.out.println(rootGen3.getInfo());
    }
}
