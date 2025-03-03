package com.sumit.aistudio.backend.collections;


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
@RequestMapping("/api/collections")
public class CollectionsController {

    @Autowired
    private CollectionsService entityService;

    @PostMapping
    public ResponseEntity<Collections> createPrompt(@RequestBody Collections entity) {
        Collections savedPrompt = entityService.updateEntity(entity);
        return ResponseEntity.ok(savedPrompt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Collections> updatePrompt(@PathVariable UUID id, @RequestBody Collections entityDetails) {
        Collections updatedPrompt = entityService.updateEntity(id, entityDetails);
        return ResponseEntity.ok(updatedPrompt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrompt(@PathVariable UUID id) {
        entityService.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collections> getPromptById(@PathVariable UUID id) {
        Optional<Collections> prompt = entityService.getEntityById(id);
        return prompt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getByName/{id}")
    public ResponseEntity<Collections> getByName(@PathVariable String id) {
        Optional<Collections> prompt = entityService.getEntityByType(id);
        return prompt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping
    public List<Collections> getAllRecords(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return entityService.getAllEntities(pageable).get().collect(Collectors.toList());
    }
    @GetMapping("/count")
    public long getCount() {
              return entityService.getTotalCount();
    }
}
