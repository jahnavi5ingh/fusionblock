package com.sumit.aistudio.backend.prompttemplates;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class PromptTemplate {

    @Id
    @GeneratedValue
    private UUID id;
    private String promptType;
    private boolean clean;
    private String outputType;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String data;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPromptType() {
        return promptType;
    }

    public void setPromptType(String promptType) {
        this.promptType = promptType;
    }



    public boolean isClean() {
        return clean;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }



    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
