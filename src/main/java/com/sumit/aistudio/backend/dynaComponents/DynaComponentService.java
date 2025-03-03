package com.sumit.aistudio.backend.dynaComponents;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class DynaComponentService {

    @Autowired
    private DynaCompRepository repository;
    //create google guava cache for components
    private com.google.common.cache.Cache<String, DynaComponent> cache;
    {
        cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }

    public DynaComponent updateEntity(DynaComponent prompt) {
        if (prompt.getId() == null) {
            prompt.setCreatedDate(LocalDateTime.now());
            prompt.setUpdatedDate(LocalDateTime.now());
            return repository.save(prompt);
        }else {
            prompt.setUpdatedDate(LocalDateTime.now());
            return updateEntity(prompt.getId(),prompt);
        }

    }

    public DynaComponent updateEntity(UUID id, DynaComponent promptDetails) {
        Optional<DynaComponent> promptOptional = repository.findById(id);

        if (promptOptional.isPresent()) {
            DynaComponent prompt = promptOptional.get();
            prompt.setCompType(promptDetails.getCompType());
             prompt.setData(promptDetails.getData());
             prompt.setGrp(promptDetails.getGrp());
             prompt.setParentType(promptDetails.getParentType());
             prompt.setPureImpure(promptDetails.getPureImpure());
            prompt.setUpdatedDate(LocalDateTime.now());
            return repository.save(prompt);
        } else {
            throw new RuntimeException("Prompt not found with id: " + id);
        }
    }

    public void deleteEntity(UUID id) {
        repository.deleteById(id);
    }

    public Optional<DynaComponent> getEntityById(UUID id) {
        return repository.findById(id);
    }

    public Optional<DynaComponent> getEntityByType(String type) {
        return repository.findByType(type);
    }
    public Optional<DynaComponent> getEntityByTypeCache(String type) {
        try {
            return Optional.of(cache.get(type, () -> repository.findByType(type).get()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //add pagination support
    public Page<DynaComponent> getAllEntities(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public long getTotalCount() {
        return repository.count();
    }
}
