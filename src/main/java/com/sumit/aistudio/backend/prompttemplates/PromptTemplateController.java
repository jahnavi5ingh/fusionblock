package com.sumit.aistudio.backend.prompttemplates;


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
@RequestMapping("/api/prompttemplates")
public class PromptTemplateController {

    @Autowired
    private PromptTemplateService promptService;

    @PostMapping
    public ResponseEntity<PromptTemplate> createPrompt(@RequestBody PromptTemplate prompt) {
        PromptTemplate savedPrompt = promptService.savePrompt(prompt);
        return ResponseEntity.ok(savedPrompt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromptTemplate> updatePrompt(@PathVariable UUID id, @RequestBody PromptTemplate promptDetails) {
        PromptTemplate updatedPrompt = promptService.updatePrompt(id, promptDetails);
        return ResponseEntity.ok(updatedPrompt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrompt(@PathVariable UUID id) {
        promptService.deletePrompt(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromptTemplate> getPromptById(@PathVariable UUID id) {
        Optional<PromptTemplate> prompt = promptService.getPromptById(id);
        return prompt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping
    public List<PromptTemplate> getAllRecords(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return promptService.getAllPrompts(pageable).get().collect(Collectors.toList());
    }
    @GetMapping("/count")
    public long getCount() {
              return promptService.getTotalCount();
    }
}
