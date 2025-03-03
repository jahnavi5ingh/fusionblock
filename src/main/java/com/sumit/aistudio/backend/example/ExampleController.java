package com.sumit.aistudio.backend.example;


import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/records")
public class ExampleController {

    private final List<Record> records = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    // Get all records
    @GetMapping
    public List<Record> getAllRecords() {
        return records;
    }

    // Add a new record
    @PostMapping
    public Record addRecord(@RequestBody Record newRecord) {
        newRecord.setId(idCounter.incrementAndGet());
        records.add(newRecord);
        return newRecord;
    }

    // Update an existing record
    @PutMapping("/{id}")
    public Record updateRecord(@PathVariable long id, @RequestBody Record updatedRecord) {
        Optional<Record> existingRecord = records.stream()
                .filter(record -> record.getId() == id)
                .findFirst();

        if (existingRecord.isPresent()) {
            Record record = existingRecord.get();
            record.setName(updatedRecord.getName());
            record.setDescription(updatedRecord.getDescription());
            record.setChoice(updatedRecord.getChoice());
            record.setConfig(updatedRecord.getConfig());
            return record;
        } else {
            throw new RuntimeException("Record not found with id: " + id);
        }
    }

    // Delete a record
    @DeleteMapping("/{id}")
    public void deleteRecord(@PathVariable long id) {
        records.removeIf(record -> record.getId() == id);
    }
}
