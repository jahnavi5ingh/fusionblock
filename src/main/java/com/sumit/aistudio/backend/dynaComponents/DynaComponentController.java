package com.sumit.aistudio.backend.dynaComponents;


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
@RequestMapping("/api/dynaComponents")
public class DynaComponentController {

    @Autowired
    private DynaComponentService promptService;

    @PostMapping
    public ResponseEntity<DynaComponent> createPrompt(@RequestBody DynaComponent prompt) {
        DynaComponent savedPrompt = promptService.updateEntity(prompt);
        return ResponseEntity.ok(savedPrompt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DynaComponent> updatePrompt(@PathVariable UUID id, @RequestBody DynaComponent promptDetails) {
        DynaComponent updatedPrompt = promptService.updateEntity(id, promptDetails);
        return ResponseEntity.ok(updatedPrompt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrompt(@PathVariable UUID id) {
        promptService.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DynaComponent> getPromptById(@PathVariable UUID id) {
        Optional<DynaComponent> prompt = promptService.getEntityById(id);
        return prompt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping
    public List<DynaComponent> getAllRecords(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return promptService.getAllEntities(pageable).get().collect(Collectors.toList());
    }
    @GetMapping("/count")
    public long getCount() {
              return promptService.getTotalCount();
    }
}
