package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
@IsNodeHandler
@Component
public class SaveToFileHandler extends Handler {
    @Override
    public void handleNode(Node node) {
        String fname = node.getData().getStringProperty("fname");
        String fileText = node.getData().getStringProperty("fileText");
        try {
            String text= context.getPromptLinker().compileContext(fileText, context.getPromptLinker());
            FileUtils.write(new File(fname),text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
