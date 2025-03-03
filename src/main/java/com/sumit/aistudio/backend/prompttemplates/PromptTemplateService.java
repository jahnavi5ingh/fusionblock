package com.sumit.aistudio.backend.prompttemplates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PromptTemplateService {

    @Autowired
    private PromptTemplateRepository promptRepository;

    public PromptTemplate savePrompt(PromptTemplate prompt) {
        if (prompt.getId() == null) {
            prompt.setCreatedDate(LocalDateTime.now());
            prompt.setUpdatedDate(LocalDateTime.now());
            return promptRepository.save(prompt);
        }else {
            prompt.setUpdatedDate(LocalDateTime.now());
            return updatePrompt(prompt.getId(),prompt);
        }

    }

    public PromptTemplate updatePrompt(UUID id, PromptTemplate promptDetails) {
        Optional<PromptTemplate> promptOptional = promptRepository.findById(id);

        if (promptOptional.isPresent()) {
            PromptTemplate prompt = promptOptional.get();
            prompt.setPromptType(promptDetails.getPromptType());
            prompt.setClean(promptDetails.isClean());
            prompt.setOutputType(promptDetails.getOutputType());
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

    public Optional<PromptTemplate> getPromptById(UUID id) {
        return promptRepository.findById(id);
    }

    public Optional<PromptTemplate> getPromptByType(String type) {
        return promptRepository.findByType(type);
    }

    //add pagination support
    public Page<PromptTemplate> getAllPrompts(Pageable pageable) {
        return promptRepository.findAll(pageable);
    }

    public long getTotalCount() {
        return promptRepository.count();
    }
}
