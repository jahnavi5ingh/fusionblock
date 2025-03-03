// StructureGenerator.java
package com.sumit.aistudio.backend.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class StructureGenerator {

    public static class FieldStructure {
        private String fieldName;
        private String fieldType;
        private String fieldLabel;
        private String validation;
        private boolean readonly;
        private List<String> choices;

        // Getters and Setters
        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public String getFieldLabel() {
            return fieldLabel;
        }

        public void setFieldLabel(String fieldLabel) {
            this.fieldLabel = fieldLabel;
        }

        public String getValidation() {
            return validation;
        }

        public void setValidation(String validation) {
            this.validation = validation;
        }

        public boolean isReadonly() {
            return readonly;
        }

        public void setReadonly(boolean readonly) {
            this.readonly = readonly;
        }

        public List<String> getChoices() {
            return choices;
        }

        public void setChoices(List<String> choices) {
            this.choices = choices;
        }

        @Override
        public String toString() {
            return "{ " +
                    "fieldName: '" + fieldName + '\'' +
                    ", fieldType: '" + fieldType + '\'' +
                    ", fieldLabel: '" + fieldLabel + '\'' +
                    (validation != null ? ", validation: '" + validation + '\'' : "") +
                    (readonly ? ", readonly: " + readonly : "") +
                    (choices != null ? ", choices: " + choices : "") +
                    " },";
        }
    }

    public static List<FieldStructure> generateStructure(String className) {
        List<FieldStructure> structure = new ArrayList<>();

        try {
            Class<?> clazz = Class.forName(className);
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                FieldStructure fieldStructure = new FieldStructure();
                fieldStructure.setFieldName(field.getName());
                fieldStructure.setFieldLabel(capitalize(field.getName()));
                fieldStructure.setFieldType(getFieldType(field.getType()));

                // Example of setting readonly and validation based on field names
                if (field.getName().equalsIgnoreCase("createdDate") || field.getName().equalsIgnoreCase("updatedDate")) {
                    fieldStructure.setReadonly(true);
                }

                if (field.getType().equals(String.class) && field.getName().equalsIgnoreCase("name")) {
                    fieldStructure.setValidation("required");
                }

                // Example of setting choices for a "choice" type field
                if (field.getName().equalsIgnoreCase("status")) {
                    fieldStructure.setFieldType("choice");
                    List<String> choices = new ArrayList<>();
                    choices.add("Active");
                    choices.add("Inactive");
                    fieldStructure.setChoices(choices);
                }

                structure.add(fieldStructure);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return structure;
    }

    private static String getFieldType(Class<?> type) {
        if (type.equals(String.class)) {
            return "string";
        } else if (type.equals(int.class) || type.equals(Integer.class)) {
            return "int";
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return "boolean";
        } else if (type.equals(java.util.Date.class) || type.equals(java.time.LocalDateTime.class)) {
            return "date";
        } else {
            return "string";
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static void main(String[] args) {
        // Example usage
        List<FieldStructure> structure = generateStructure("com.sumit.aistudio.backend.prompt.Prompt");
        structure.forEach(System.out::println);
    }
}
