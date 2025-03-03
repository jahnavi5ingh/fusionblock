package com.sumit.aistudio.backend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class VelocityController {

    @Autowired
    private VelocityService velocityService;

    @GetMapping("/velocity")
    public String getVelocityTemplate(@RequestParam String name) {
        Map<String, Object> model = new HashMap<>();
        model.put("name", name);
        return velocityService.generateContent("templates/example.vm", model);
    }
}
