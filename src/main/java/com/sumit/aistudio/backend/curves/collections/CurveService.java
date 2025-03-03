package com.sumit.aistudio.backend.curves.collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CurveService {

    @Autowired
    private CurveRepository repository;

    public Curve updateEntity(Curve entity) {
        if (entity.getId() == null) {
            entity.setCreatedDate(LocalDateTime.now());
            entity.setUpdatedDate(LocalDateTime.now());
            Optional<Curve> ent = repository.findByType(entity.getType());
            if(ent.isPresent()){
                Curve en = ent.get();
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

    public Curve updateEntity(UUID id, Curve entityDetails) {
        Optional<Curve> promptOptional = repository.findById(id);

        if (promptOptional.isPresent()) {
            Curve prompt = promptOptional.get();
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

    public Optional<Curve> getEntityById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Curve> getEntityByType(String type) {
        return repository.findByType(type);
    }

    //add pagination support
    public Page<Curve> getAllEntities(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public long getTotalCount() {
        return repository.count();
    }


}
