package com.sumit.aistudio.backend.plan.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@IsNodeHandler
@Component
public class ForEach extends Handler{
    @Override
    public void handleNode(Node node) {
        System.out.println("NodeAHandler: "+node.getData());
        Collection coll = (Collection) node.getData().getProperty("collection");
        Iterator iter = coll.iterator();
        for(int i=0;i<coll.size();i++){
            Object obj = iter.next();
            System.out.println("For each for item: "+obj);
            node.getOutput().getProperties().put("iter", i);
            node.getOutput().getProperties().put("output", obj);
            updateOutputs(node);
            //todo sumit do it better adding skip for now
            Set<String> done = new HashSet<>();
            for(Node child: node.getChildren()){
                if(!done.contains(child.getId())){
                    executeChildNode(node, child);
                    done.add(child.getId());
                    System.out.println("Executing child: "+child.getData());

                }
            }
        }
    }



}
