package com.sumit.aistudio.backend.ptl;

public class ParsedTemplate {
    private String promptID;
    private String template;
    private String model;
    private String clean;
    private String tags;

    public ParsedTemplate(String promptID, String template, String model, String clean, String tags) {
        this.promptID = promptID;
        this.template = template;
        this.model = model;
        this.clean = clean;
        this.tags = tags;
    }

    public String getPromptID() {
        return promptID;
    }

    public String getTemplate() {
        return template;
    }

    public String getModel() {
        return model;
    }

    public String getClean() {
        return clean;
    }

    public String getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "PromptID: " + promptID +
                ", Template: " + template +
                ", Model: " + model +
                ", Clean: " + clean +
                ", Tags: " + tags;
    }
}
