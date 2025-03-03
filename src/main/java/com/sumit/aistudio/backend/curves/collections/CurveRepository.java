package com.sumit.aistudio.backend.curves.collections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CurveRepository extends JpaRepository<Curve, UUID> {

    @Query("SELECT p FROM Curve p WHERE p.type = :type")
    Optional<Curve> findByType(@Param("type") String type);
}