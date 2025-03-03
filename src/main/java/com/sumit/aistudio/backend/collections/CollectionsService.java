package com.sumit.aistudio.backend.collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CollectionsService {

    @Autowired
    private CollectionsRepository repository;

    public Collections updateEntity(Collections entity) {
        if (entity.getId() == null) {
            entity.setCreatedDate(LocalDateTime.now());
            entity.setUpdatedDate(LocalDateTime.now());
            Optional<Collections> ent = repository.findByType(entity.getType());
            if(ent.isPresent()){
                Collections en = ent.get();
               en.setData(entity.getData());
              return  repository.save(en);
            }else {
                return repository.save(entity);
            }
        }else {
            entity.setUpdatedDate(LocalDateTime.now());
            return updateEntity(entity.getId(),entity);
        }

    }

    public Collections updateEntity(UUID id, Collections entityDetails) {
        Optional<Collections> promptOptional = repository.findById(id);

        if (promptOptional.isPresent()) {
            Collections prompt = promptOptional.get();
            prompt.setType(entityDetails.getType());
             prompt.setData(entityDetails.getData());
            prompt.setUpdatedDate(LocalDateTime.now());
            return repository.save(prompt);
        } else {
            throw new RuntimeException("Prompt not found with id: " + id);
        }
    }

    public void deleteEntity(UUID id) {
        repository.deleteById(id);
    }

    public Optional<Collections> getEntityById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Collections> getEntityByType(String type) {
        return repository.findByType(type);
    }

    //add pagination support
    public Page<Collections> getAllEntities(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public long getTotalCount() {
        return repository.count();
    }


}
