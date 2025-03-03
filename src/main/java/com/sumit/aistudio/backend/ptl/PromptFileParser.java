package com.sumit.aistudio.backend.ptl;

import com.sumit.aistudio.backend.ptl.nodes.*;
import com.sumit.aistudio.backend.utils.Reader;
import com.sumit.aistudio.backend.utils.StringReader;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;


public class PromptFileParser {



    public GenInfoNode parseFileFinal(String filePath, PromptLinker linkerInfo) throws IOException {
        String root = StringUtils.substringAfterLast(filePath,"/");
        if(root==null ||root.length()<1 ) root = StringUtils.substringAfterLast(filePath,"\\");

        GenInfoNode rootNode = new GenInfoNode("//#START:"+root+":file(\"none\",false,[\"file\"]);"); // Root node for the entire file
        Stack<GenInfoNode> stack = new Stack<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        StringBuilder currentText = new StringBuilder();
        GenInfoNode currentNode = rootNode;
        while ((line = reader.readLine()) != null) {
            String testLine = line.trim();
           if (testLine.startsWith("//#G{")) {
                if(currentText.length()>0) {
                    currentNode.addChild(new TextNode(currentText.toString()));
                }
                currentText = new StringBuilder();
                currentText.append(line+"\n");
                String id = StringUtils.substringAfter(testLine,"//#G{").trim();
                String genInfoNode = linkerInfo.link(id);
                if(genInfoNode!=null){;
                  currentText.append(genInfoNode+"\n");
                }
                currentNode.addChild(new TextNode(currentText.toString()));
                currentText = new StringBuilder();
            }else if (testLine.startsWith("//#G}")) {
                currentNode.addChild(new TextNode(line));
                currentText = new StringBuilder();
            }else{
                 currentText.append(line+"\n");
            }
        }
        reader.close();
        if(currentText .length()>0)
            currentNode.addChild(new TextNode(currentText.toString()));
        return rootNode;
    }
    public GenInfoNode parseFile(String filePath, PromptLinker linkerInfo) throws IOException {
        String root = StringUtils.substringAfterLast(filePath,"/");
        Reader reader = new com.sumit.aistudio.backend.utils.FileReader(filePath);
        return parseFile(root,reader,linkerInfo);
    }

    public GenInfoNode parseData(String filePath,String data,PromptLinker linkerInfo) throws IOException {

        Reader reader = new StringReader(filePath,data);
        return parseFile(filePath,reader,linkerInfo);
    }
        private GenInfoNode parseFile(String root,Reader reader, PromptLinker linkerInfo) throws IOException {

        GenInfoNode rootNode = new GenInfoNode("//#START:"+root+":file(\"none\",false,[\"file\"]);"); // Root node for the entire file
        Stack<GenInfoNode> stack = new Stack<>();
          String line;

        StringBuilder currentText = new StringBuilder();
        GenInfoNode currentNode = rootNode;
        ContextNode contextNode = null;
        DependencyNode dependencyNode = null;
        while ((line = reader.readLine()) != null) {
            String testLine = line.trim();
            if (testLine.startsWith("//#START")) {
                if(currentText.length()>0){
                    currentNode.addChild(new TextNode(currentText.toString()));
                    currentText=new StringBuilder();
                }
                stack.push(currentNode);
                currentNode = new GenInfoNode(line);
            }else if (testLine.startsWith("//#D{")) {
                if(currentText.length()>0) {
                    currentNode.addChild(new TextNode(currentText.toString()));
                    currentText = new StringBuilder();
                }
                dependencyNode =  new DependencyNode("");
                dependencyNode.setContextId(StringUtils.substringAfter(testLine,"//#C{").trim());
            }else if (testLine.startsWith("//#D}")) {
                dependencyNode.setInfo(currentText.toString());
                currentText = new StringBuilder();
                currentNode.addChild(dependencyNode);
                dependencyNode = null;
            }else if (testLine.startsWith("//#G{")) {
                if(currentText.length()>0) {
                    currentNode.addChild(new TextNode(currentText.toString()));
                }
                currentText = new StringBuilder();
                currentText.append(line+"\n");
                String id = StringUtils.substringAfter(testLine,"//#G{").trim();
                String genInfoNode = linkerInfo.link(id);
                if(genInfoNode!=null){;
                    currentText.append(genInfoNode+"\n");
                }
                currentNode.addChild(new TextNode(currentText.toString()));
                currentText = new StringBuilder();
            }else if (testLine.startsWith("//#G}")) {
                currentNode.addChild(new TextNode(line));
                currentText = new StringBuilder();
            }else if (testLine.startsWith("//#C{")) {
                if(currentText.length()>0) {
                    currentNode.addChild(new TextNode(currentText.toString()));
                    currentText = new StringBuilder();
                }
                contextNode =  new ContextNode("");
                contextNode.setContextId(StringUtils.substringAfter(testLine,"//#C{").trim());
            }else if (testLine.startsWith("//#C}")) {
                contextNode.setInfo(currentText.toString());
                currentText = new StringBuilder();
                currentNode.addChild(contextNode);
                contextNode = null;
            }else if (testLine.startsWith("//#{")) {
                if(currentText.length()>0) {
                    currentNode.addChild(new TextNode(currentText.toString()));
                    currentText = new StringBuilder();
                }
            }else if (testLine.startsWith("//#}")) {
                PromptNode promptNode =  new PromptNode(currentText.toString());
                currentText = new StringBuilder();
                currentNode.addChild(promptNode);
            }else if (testLine.startsWith("//#END")) {
                if(currentText.length()>0) {
                    currentNode.addChild(new TextNode(currentText.toString()));
                    currentText = new StringBuilder();
                }
                GenInfoNode temp = currentNode;
                currentNode = stack.pop();
                currentNode.addChild(temp);
            }else{
                currentText.append(line+"\n");
            }
        }

        reader.close();
        if(currentText .length()>0)
            currentNode.addChild(new TextNode(currentText.toString()));
        return rootNode;
    }
}
