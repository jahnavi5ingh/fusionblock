package com.sumit.aistudio.backend.operations;

import com.sumit.aistudio.backend.dynaComponents.DynaComponent;
import com.sumit.aistudio.backend.ptl.VelocityHelper;

import java.util.HashMap;
import java.util.Map;

public class GenerateDynaComponents {
    public static void main(String[] args) {
        System.out.println("Generating dynamic components");
        VelocityHelper velocityHelper = new VelocityHelper();
        String[] fields = {"name", "age", "address"};
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("inputArray", fields);
        String str =  velocityHelper.generateContent("templates/dynaComponents.vm", ctx);
        System.out.println(str);

        DynaComponent dc = new DynaComponent();
        dc.setCompType("Test");
        dc.setPureImpure("notpure");
        dc.setGrp("Points");
        dc.setData(str);
        dc.setParentType("None");
        System.out.println(dc);
    }
}
