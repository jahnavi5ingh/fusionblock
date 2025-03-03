package com.sumit.aistudio.backend;

import com.sumit.aistudio.backend.linker.LinkInfo;
import com.sumit.aistudio.backend.linker.LinkInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/linkinfo")
public class LinkInfoController {

    @Autowired
    private LinkInfoService linkInfoService;

    @GetMapping
    public List<LinkInfo> getAllLinkInfos() {
        return linkInfoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkInfo> getLinkInfoById(@PathVariable("id") String id) {
        Optional<LinkInfo> linkInfo = linkInfoService.findById(id);
        return linkInfo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public LinkInfo createLinkInfo(@RequestBody LinkInfo linkInfo) {
        return linkInfoService.save(linkInfo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LinkInfo> updateLinkInfo(@PathVariable("id") String id, @RequestBody LinkInfo linkInfo) {
        Optional<LinkInfo> existingLinkInfo = linkInfoService.findById(id);
        if (existingLinkInfo.isPresent()) {
            linkInfo.setId(id);
            return ResponseEntity.ok(linkInfoService.save(linkInfo));
        } else {
            return ResponseEntity.ok(linkInfoService.save(linkInfo));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLinkInfo(@PathVariable("id") String id) {
        Optional<LinkInfo> existingLinkInfo = linkInfoService.findById(id);
        if (existingLinkInfo.isPresent()) {
            linkInfoService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
