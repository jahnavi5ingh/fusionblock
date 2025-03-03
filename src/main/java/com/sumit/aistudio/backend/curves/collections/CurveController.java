package com.sumit.aistudio.backend.curves.collections;


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
@RequestMapping("/api/curves")
public class CurveController {

    @Autowired
    private CurveService entityService;

    @PostMapping
    public ResponseEntity<Curve> create(@RequestBody Curve entity) {
        Curve savedPrompt = entityService.updateEntity(entity);
        return ResponseEntity.ok(savedPrompt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curve> update(@PathVariable UUID id, @RequestBody Curve entityDetails) {
        Curve updatedPrompt = entityService.updateEntity(id, entityDetails);
        return ResponseEntity.ok(updatedPrompt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        entityService.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curve> getById(@PathVariable UUID id) {
        Optional<Curve> prompt = entityService.getEntityById(id);
        return prompt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping
    public List<Curve> getAllRecords(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return entityService.getAllEntities(pageable).get().collect(Collectors.toList());
    }
    @GetMapping("/count")
    public long getCount() {
              return entityService.getTotalCount();
    }
}
