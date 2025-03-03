package com.sumit.aistudio.backend;


import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;

@Service
public class VelocityService {

    @Autowired
    private VelocityEngine velocityEngine;

    public String generateContent(String templateName, Map<String, Object> model) {
        VelocityContext context = new VelocityContext(model);
        StringWriter writer = new StringWriter();
        velocityEngine.mergeTemplate(templateName, "UTF-8", context, writer);
        return writer.toString();
    }
}
