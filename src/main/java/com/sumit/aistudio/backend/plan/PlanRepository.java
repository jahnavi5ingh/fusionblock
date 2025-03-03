package com.sumit.aistudio.backend.plan;



import com.sumit.aistudio.backend.dynaComponents.DynaComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    @Query("SELECT p FROM Plan p WHERE p.name = :name")
    Optional<Plan> findByName(@Param("name") String name);
}
