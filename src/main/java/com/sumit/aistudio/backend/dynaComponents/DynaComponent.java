package com.sumit.aistudio.backend.dynaComponents;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class DynaComponent {

    @Id
    @GeneratedValue
    private UUID id;
    private String compType;


    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    private String grp = "default";

    private String parentType = "None";


    private String pureImpure = "pure";


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

    public String getCompType() {
        return compType;
    }

    public void setCompType(String compType) {
        this.compType = compType;
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

    public String getGrp() {
        return grp;
    }

    public void setGrp(String grp) {
        this.grp = grp;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getPureImpure() {
        return pureImpure;
    }

    public void setPureImpure(String pureImpure) {
        this.pureImpure = pureImpure;
    }
}
