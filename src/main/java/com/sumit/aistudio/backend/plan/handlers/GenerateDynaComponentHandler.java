package com.sumit.aistudio.backend.plan.handlers;

import com.sumit.aistudio.backend.dynaComponents.DynaComponent;
import com.sumit.aistudio.backend.dynaComponents.DynaComponentService;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.ptl.VelocityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@IsNodeHandler
@Component
public class GenerateDynaComponentHandler extends Handler {
    VelocityHelper velocityHelper = new VelocityHelper();
    @Autowired
    DynaComponentService service;

    @Override
    public void handleNode(Node node) {
        String compType = node.getData().getStringProperty("compType");
        String grp = node.getData().getStringProperty("grp");
        String pureImpure = "notpure";
        String parentType = "None";
        String [] fieldsArray = node.getData().getStringProperty("fields").split("\n");
         try {
             Map<String, Object> ctx = new HashMap<>();
             ctx.put("inputArray", fieldsArray);
             String str =  velocityHelper.generateContent("templates/dynaComponents.vm", ctx);
             System.out.println(str);
             DynaComponent dc = new DynaComponent();
             dc.setCompType(compType);
             dc.setPureImpure(pureImpure);
             dc.setGrp(grp);
             dc.setData(str);
             dc.setParentType(parentType);
             service.updateEntity(dc);
             node.getOutput().getProperties().put("output",dc);
            } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
