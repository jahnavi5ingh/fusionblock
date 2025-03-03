package com.sumit.aistudio.backend.prompt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PromptRepository extends JpaRepository<Prompt, UUID> {

    @Query("SELECT p FROM Prompt p WHERE p.promptType = :type")
    Optional<Prompt> findByType(@Param("type") String type);
}