package com.sumit.aistudio.backend.ptl;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

public class VelocityHelper {
    private VelocityEngine velocityEngine;

    public VelocityHelper() {
        velocityEngine = velocityEngine();
    }

    public String generateContent(String templateName, Map<String, Object> model) {
        VelocityContext context = new VelocityContext(model);
        StringWriter writer = new StringWriter();
        velocityEngine.mergeTemplate(templateName, "UTF-8", context, writer);
        return writer.toString();
    }
    public  StringBuffer generateContentStringTemplate( String in,Map<String, Object> contextMap) {
        StringReader s = new StringReader(in);
        StringWriter out = new StringWriter();
        VelocityEngine ve = new VelocityEngine();
        try {
            VelocityContext context = new VelocityContext();
            if(contextMap!=null){
                for (String key : contextMap.keySet()) {
                    Object val = contextMap.get(key);
                    context.put(key, val);
                }
            }
            ve.evaluate(context, out, "OntheflyTemplate", s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.getBuffer();
    }

    public VelocityEngine velocityEngine() {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        VelocityEngine velocityEngine = new VelocityEngine(props);
        return velocityEngine;
    }
}
