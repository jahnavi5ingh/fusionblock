package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.jcomp.JCompiler;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@IsNodeHandler
@Component
public class DynaCodeHandler extends Handler {
    @Autowired
    JCompiler jCompiler;

    @Override
    public void handleNode(Node node) {
        String code = node.getData().getStringProperty("code");
         try {
              Map output  = jCompiler.compileHandler(code).handle(node.getData().getProperties());
              output.forEach((k,v)-> {
                  node.getOutput().getProperties().put(k.toString(), v);
              });
            } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
