package com.sumit.aistudio.backend.plan;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import java.time.LocalDateTime;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public Plan savePlan(Plan plan) {
        // Set the created date to now if it's a new entity
        if (plan.getId() == null) {
            plan.setId(UUID.randomUUID());
            plan.setCreatedDate(LocalDateTime.now());
            plan.setUpdatedDate(LocalDateTime.now());
            return planRepository.save(plan);
        }else {
            return updatePlan(plan.getId(),plan);
        }

    }

    public Plan updatePlan(UUID id, Plan planDetails) {
        Optional<Plan> planOptional = planRepository.findById(id);

        if (planOptional.isPresent()) {
            Plan plan = planOptional.get();
            plan.setData(planDetails.getData()==null?plan.getData():planDetails.getData());
            plan.setName(planDetails.getName());
            // Set the updated date to now
            plan.setUpdatedDate(LocalDateTime.now());
            return planRepository.save(plan);
        } else {
            planDetails.setCreatedDate(LocalDateTime.now());
            planDetails.setUpdatedDate(LocalDateTime.now());
            return planRepository.save(planDetails);
        }
    }

    public void deletePlan(UUID id) {
        planRepository.deleteById(id);
    }

    public Optional<Plan> getPlanById(UUID id) {
        return planRepository.findById(id);
    }

    public Page<Plan> getAllPlans(Pageable pageable) {
        return planRepository.findAll(pageable);
    }
    public Optional<Plan> getPlanByName(String name) {
        return planRepository.findByName(name);
    }
    public long getTotalCount() {
        return planRepository.count();
    }
}