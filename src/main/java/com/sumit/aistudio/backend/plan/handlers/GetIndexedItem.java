package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.List;


@IsNodeHandler
@Component
public class GetIndexedItem extends Handler {
    @Override
    public void handleNode(Node node) {
        List collection = (List) node.getData().getProperties().get("collection");
        Integer itemIndex = (Integer) node.getData().getProperties().get("itemIndex");

        try {

            node.getOutput().getProperties().put("output",collection.get(itemIndex));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
