package com.sumit.aistudio.backend.collections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CollectionsRepository extends JpaRepository<Collections, UUID> {

    @Query("SELECT p FROM Collections p WHERE p.type = :type")
    Optional<Collections> findByType(@Param("type") String type);
}