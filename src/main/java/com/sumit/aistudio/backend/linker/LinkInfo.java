package com.sumit.aistudio.backend.linker;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "linkinfo")
public class LinkInfo {

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 255)
    private String id;

    @Column(name = "output", columnDefinition = "TEXT")
    private String output;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
