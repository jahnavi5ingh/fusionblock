package com.sumit.aistudio.backend.prompttemplates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PromptTemplateRepository extends JpaRepository<PromptTemplate, UUID> {

    @Query("SELECT p FROM PromptTemplate p WHERE p.promptType = :type")
    Optional<PromptTemplate> findByType(@Param("type") String type);
}