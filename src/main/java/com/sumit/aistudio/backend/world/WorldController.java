package com.sumit.aistudio.backend.world;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/world")
public class WorldController {

    @PostMapping("/in")
    public  String in(@RequestBody  String msg){
        System.out.println("in - "+msg);
        return "in - "+msg;
    }
    @PostMapping("/out")
    public  String out(@RequestBody  String msg){
        System.out.println("out - "+msg);
        return "out - "+msg;
    }
}
