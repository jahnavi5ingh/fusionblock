package com.sumit.aistudio.backend.ptl.nodes;

import com.sumit.aistudio.backend.ptl.ParsedTemplate;
import com.sumit.aistudio.backend.ptl.PromptTemplateCmdParser;

import java.util.ArrayList;
import java.util.List;

public class GenInfoNode extends Node {



    ParsedTemplate parsedTemplate;

    public GenInfoNode(String info) {
        super(info);
        parseInfo(info);
    }

    public void setInfo(String info) {
        this.info = info;
        parseInfo(info);
    }

    private void parseInfo(String info) {
        parsedTemplate = PromptTemplateCmdParser.parseTemplate(info);
    }

    public ParsedTemplate getParsedTemplate() {
        return parsedTemplate;
    }


    List<PromptNode> promptNodes = new ArrayList<>();

    public List<PromptNode> getPromptNodes() {

        return promptNodes;
    }

    public void setPromptNodes(List<PromptNode> promptNodes) {
        this.promptNodes = promptNodes;
    }

    public void addPromptNode(PromptNode promptNode){
        promptNodes.add(promptNode);
    }


    public static GenInfoNode getRoot(String rootName){
        GenInfoNode rootNode = new GenInfoNode("//#START:"+rootName+":file(\"none\",false,[\"file\"]);"); // Root node for the entire file
        return rootNode;
    }

    @Override
    public String toString() {
        return "GenInfoNode{" +
                "info='" + info + '\'' +
                ", children=" + children +
                '}';
    }
}