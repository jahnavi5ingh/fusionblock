package com.sumit.aistudio.backend.plan;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @Autowired
    PlanExecutor planExecutor ;

    @GetMapping("/executeName")
    public ResponseEntity<String> executePlanByName(@RequestParam String name) {
        planExecutor.executeGraph(planService.getPlanByName(name).get().getData());
        return ResponseEntity.ok("Done");
    }
    @PostMapping("/execute")
    public ResponseEntity<String> executePlan(@RequestBody Plan plan) {
        planExecutor.executeGraph(plan.getData());
        return ResponseEntity.ok("Done");
    }

    @PostMapping
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        Plan savedPlan = planService.savePlan(plan);
        return ResponseEntity.ok(savedPlan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable UUID id, @RequestBody Plan planDetails) {
        Plan updatedPlan = planService.updatePlan(id, planDetails);
        return ResponseEntity.ok(updatedPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable UUID id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable UUID id) {
        Optional<Plan> plan = planService.getPlanById(id);
        return plan.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Plan> getAllPlans(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "100000" ) int size) {
            Pageable pageable = PageRequest.of(page, size);
        List<Plan> plans = planService.getAllPlans(pageable).get().collect(Collectors.toList());
        return plans;
    }
    @GetMapping("/count")
    public long getCount() {
        return planService.getTotalCount();
    }

    @PostMapping("/createComponent")
    public ResponseEntity<String> createComponent(@RequestBody PlanComponent component) {
        planExecutor.createComponent(component);
        return ResponseEntity.ok("Done");
    }
}
