package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@IsNodeHandler
@Component
public class ReadFileHandler extends Handler{
    @Override
    public void handleNode(Node node) {
        System.out.println("ReadFileHandler: "+node.getData());
       String file =  node.getData().getProperties().get("fname").toString();
        System.out.println("Reading file: "+file);
        String data  = getData(file);
        System.out.println("Data: "+data);
        node.getOutput().getProperties().put("output",data);


    }

    private static String getData(String file)  {
        try {
            return FileUtils.readFileToString(new File(file));
        } catch (IOException e) {
           e.printStackTrace();
        }
        return "";
    }
}
