package com.sumit.aistudio.backend.plan.handlers.save;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sumit.aistudio.backend.dynaComponents.DynaComponent;
import com.sumit.aistudio.backend.dynaComponents.DynaComponentData;
import com.sumit.aistudio.backend.dynaComponents.DynaComponentService;
import com.sumit.aistudio.backend.dynaComponents.FieldInfo;
import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@IsNodeHandler
@Component
public class CreateDynaComponent extends Handler {
    @Autowired
    DynaComponentService service;

    @Override
    public void handleNode(Node node) {
        String name = node.getData().getStringProperty("compType");
        String parentType = node.getData().getStringProperty("parentType");
        String group = node.getData().getStringProperty("group");
        String isPure = node.getData().getStringProperty("pureImpure");
        String inputs = node.getData().getStringProperty("inputs");
        String outputs = node.getData().getStringProperty("outputs");

        DynaComponent component = new DynaComponent();
        DynaComponentData data = new DynaComponentData();
        component.setCompType(name);
        component.setParentType(parentType);
        component.setGrp(group);
        component.setPureImpure(isPure);
        //split input string by \n and then add field in data
        String[] inputArray = inputs.split("\n");

        String [] outputArray = outputs.split("\n");
        int i = 0;
        for(String input:inputArray){
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setHeader(""+i++);
            fieldInfo.setFieldName(input.split(":")[0]);
            fieldInfo.setFieldLabel(input);
            fieldInfo.setFieldType("string");
            data.getFieldInfo().add(fieldInfo);
        }
        i = 0;
        for(String s:inputArray){
            String []vals = s.split(":");
            if(vals.length>1){
                data.getInitialValues().put(vals[0],vals[1]);
          ;}
        }
        i=0;
        for(String output: outputArray) {
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setHeader("" + i++);
            fieldInfo.setFieldName(output);
            fieldInfo.setFieldLabel(output);
            fieldInfo.setFieldType("string");
            data.getOutputFieldInfo().add(fieldInfo);
        }
            component.setData( prettyJson(data));

        service.getEntityByType(name).ifPresentOrElse(
                collection1 -> {
                    component.setId(collection1.getId());
                    service.updateEntity(component);
                },
                () -> service.updateEntity(component));
        node.getOutput().getProperties().put(output,done);


    }
}
