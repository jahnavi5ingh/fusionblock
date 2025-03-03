package com.sumit.aistudio.backend.prompt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PromptService {

    @Autowired
    private PromptRepository promptRepository;

    public Prompt savePrompt(Prompt prompt) {
        if (prompt.getId() == null) {
            prompt.setCreatedDate(LocalDateTime.now());
            prompt.setUpdatedDate(LocalDateTime.now());
            return promptRepository.save(prompt);
        }else {
            prompt.setUpdatedDate(LocalDateTime.now());
            return updatePrompt(prompt.getId(),prompt);
        }

    }

    public Prompt updatePrompt(UUID id, Prompt promptDetails) {
        Optional<Prompt> promptOptional = promptRepository.findById(id);

        if (promptOptional.isPresent()) {
            Prompt prompt = promptOptional.get();
            prompt.setPromptType(promptDetails.getPromptType());
            prompt.setClean(promptDetails.isClean());
            prompt.setOutputType(promptDetails.getOutputType());
            prompt.setMeta(promptDetails.isMeta());
            prompt.setModel(promptDetails.getModel());
            prompt.setData(promptDetails.getData());
            prompt.setUpdatedDate(LocalDateTime.now());
            return promptRepository.save(prompt);
        } else {
            throw new RuntimeException("Prompt not found with id: " + id);
        }
    }

    public void deletePrompt(UUID id) {
        promptRepository.deleteById(id);
    }

    public Optional<Prompt> getPromptById(UUID id) {
        return promptRepository.findById(id);
    }

    public Optional<Prompt> getPromptByType(String type) {
        return promptRepository.findByType(type);
    }

    //add pagination support
    public Page<Prompt> getAllPrompts(Pageable pageable) {
        return promptRepository.findAll(pageable);
    }

    public long getTotalCount() {
        return promptRepository.count();
    }
}
