package com.sumit.aistudio.backend.linker;

import com.sumit.aistudio.backend.ptl.PromptLinker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class LinkInfoService {

    @Autowired
    private LinkInfoRepository linkInfoRepository;

    public List<LinkInfo> findAll() {
        return linkInfoRepository.findAll();
    }

    public Optional<LinkInfo> findById(String stringId) {
        return linkInfoRepository.findById(stringId);
    }

    public LinkInfo save(LinkInfo linkInfo) {
        return linkInfoRepository.save(linkInfo);
    }

    public void deleteById(String stringId) {
        linkInfoRepository.deleteById(stringId);
    }

    public void saveLinkInfo(PromptLinker linkerInfo) {
        for(Map.Entry<String,String>en:linkerInfo.getPromptsOutputs().entrySet()) {
            System.out.println(en.getKey() + " : " + en.getValue());
            LinkInfo linkInfo = new LinkInfo();
            linkInfo.setId(en.getKey());
            linkInfo.setOutput(en.getValue());
            linkInfoRepository.save(linkInfo);

        }
    }
}
