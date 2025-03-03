package com.sumit.aistudio.backend.dynaComponents;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynaComponentData {
    List<FieldInfo> fieldInfo = new ArrayList<>();
    Map<String, String> initialValues = Maps.newHashMap();
    List<FieldInfo> outputFieldInfo = new ArrayList<>();

    public List<FieldInfo> getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(List<FieldInfo> fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public Map<String, String> getInitialValues() {
        return initialValues;
    }

    public void setInitialValues(Map<String, String> initialValues) {
        this.initialValues = initialValues;
    }

    public List<FieldInfo> getOutputFieldInfo() {
        return outputFieldInfo;
    }

    public void setOutputFieldInfo(List<FieldInfo> outputFieldInfo) {
        this.outputFieldInfo = outputFieldInfo;
    }
    public static FieldInfo getDefaultOutputFieldInfo(){
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setFieldName("output");
        fieldInfo.setFieldLabel("Output");
        fieldInfo.setFieldType("string");
        fieldInfo.setHeader("0");
        return fieldInfo;
    }
    public static FieldInfo getDefaultNameInputFieldInfo(){
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setFieldName("name");
        fieldInfo.setFieldLabel("name");
        fieldInfo.setFieldType("string");
        fieldInfo.setHeader("0");
        return fieldInfo;
    }
}
