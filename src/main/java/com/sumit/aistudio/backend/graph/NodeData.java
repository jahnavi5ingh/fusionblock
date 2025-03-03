package com.sumit.aistudio.backend.graph;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;



public class NodeData {
    Logger logger = LoggerFactory.getLogger(NodeData.class);
    Map<String,Object> properties = new HashMap<>();


    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
        if(properties.get("initialValues") != null){
            properties.putAll((Map<String, Object>) properties.get("initalValues"));
        }
    }

    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public String getStringProperty(String key) {
        return ""+properties.get(key);
    }

    public Integer getIntProperty(String key,int defaultVal) {
        return Integer.parseInt(""+(properties.get(key)==null?defaultVal:properties.get(key)));
    }
    public Integer getIntProperty(String key) {
        return Integer.parseInt(""+properties.get(key));
    }
    public Double getDoubleProperty(String key) {
        return Double.parseDouble(""+properties.get(key));
    }
    public Float getFloatProperty(String key) {
        Object val = (properties.get(key)==null|| properties.get(key).toString().isEmpty() )?0:properties.get(key);
        if(val instanceof Float){
            return ((Float) properties.get(key));
        }
        return Float.parseFloat(""+val);
    }

    public Boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(""+properties.get(key));
    }

    @JsonAnySetter
    public void ignored(String name, Object value) {
        logger.info("Adding: " +name + " : " + value);

              properties.put(name,value);
        if(name.equals("initialValues")){
            properties.putAll((Map<String, Object>) value);
        }
    }

    @Override
    public String toString() {
        return "NodeData{" +
                "properties=" + properties +
                '}';
    }
}
