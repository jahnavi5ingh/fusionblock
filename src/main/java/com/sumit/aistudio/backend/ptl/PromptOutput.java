package com.sumit.aistudio.backend.ptl;

public class PromptOutput {
    boolean inline = true;
    String text ;

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
