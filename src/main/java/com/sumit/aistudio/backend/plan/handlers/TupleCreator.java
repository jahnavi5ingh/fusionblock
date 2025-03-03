package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Edge;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@IsNodeHandler
@Component
public class TupleCreator extends Handler {
    @Override
    public void handleNode(Node node) {
        String name = "collections";
        List<List<Object>> result = new ArrayList<>();
        try {
            List<Object> values = (List<Object>)node.getExecutionContext().getPlanExecutor().getIncommingValues(name,node);
            //create list of collectiions assumign obvjects are lists
            List<List<Object>> collections = new ArrayList<>();
            for(Object value: values){
                if(value instanceof List){
                    collections.add((List<Object>)value);
                }else{
                    collections.add(new ArrayList<Object>(){{add(value);}});
                }
            }
            result = createTuples(collections);
            //set to output
            node.getOutput().getProperties().put("output",result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
