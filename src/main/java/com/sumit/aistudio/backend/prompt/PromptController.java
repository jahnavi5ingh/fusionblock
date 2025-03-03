package com.sumit.aistudio.backend.prompt;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prompts")
public class PromptController {

    @Autowired
    private PromptService promptService;

    @PostMapping
    public ResponseEntity<Prompt> createPrompt(@RequestBody Prompt prompt) {
        Prompt savedPrompt = promptService.savePrompt(prompt);
        return ResponseEntity.ok(savedPrompt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prompt> updatePrompt(@PathVariable UUID id, @RequestBody Prompt promptDetails) {
        Prompt updatedPrompt = promptService.updatePrompt(id, promptDetails);
        return ResponseEntity.ok(updatedPrompt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrompt(@PathVariable UUID id) {
        promptService.deletePrompt(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prompt> getPromptById(@PathVariable UUID id) {
        Optional<Prompt> prompt = promptService.getPromptById(id);
        return prompt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping
    public List<Prompt> getAllRecords(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "100000") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return promptService.getAllPrompts(pageable).get().collect(Collectors.toList());
    }
    @GetMapping("/count")
    public long getCount() {
              return promptService.getTotalCount();
    }
}
