package com.sumit.aistudio.backend.dynaComponents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DynaCompRepository extends JpaRepository<DynaComponent, UUID> {

    @Query("SELECT p FROM DynaComponent p WHERE p.compType = :type")
    Optional<DynaComponent> findByType(@Param("type") String type);
}