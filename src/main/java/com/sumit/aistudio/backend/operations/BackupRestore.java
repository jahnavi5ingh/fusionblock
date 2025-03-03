package com.sumit.aistudio.backend.operations;

import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BackupRestore {

    private static final String BASE_URL = "http://localhost:8080/api/%s";

    private static final String CREATE_RECORD_ENDPOINT = BASE_URL + "create";
    private static final String LOCAL_PATH = "C:/projects/aibackend/src/main/resources/data/%s_version.json";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BackupRestore() {

    }

    public void backupData(String dataType) {
        int page = 0;
        int size = 100000;
        boolean hasMoreData = true;
        String localPath = String.format(LOCAL_PATH, dataType);

        List<Map<String, Object>> allData = new ArrayList<>();
        String endPoint = String.format(BASE_URL, dataType);
        while (hasMoreData) {
            String url = endPoint + "?page=" + page + "&size=" + size;
            List<Map<String, Object>> data = restTemplate.getForObject(url, List.class);

            if (data == null || data.isEmpty()) {
                hasMoreData = false;
            } else {
                allData.addAll(data);  // Append page data to the main list
                page++;
            }
        }

        try {
            objectMapper.writeValue(new File(localPath), allData);  // Save all data to file at once
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreData(String dataType) {
        try {
            String localPath = String.format(LOCAL_PATH, dataType);
            List<Map<String, Object>> data = objectMapper.readValue(new File(localPath), List.class);

            for (Map<String, Object> record : data) {
                restTemplate.postForObject(CREATE_RECORD_ENDPOINT, record, Void.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BackupRestore service = new BackupRestore();
        // Perform backup        System.out.println("Backing up data for prompts, prompttemplates, dynaComponents, and plans...");
        System.out.println("prompts");
        service.backupData("prompts");
        System.out.println("prompttemplates");
        service.backupData("prompttemplates");
        System.out.println("dynaComponents");
        service.backupData("dynaComponents");
        System.out.println("plans");
        service.backupData("plans");
        System.out.println("Data backup completed and saved to " + LOCAL_PATH);

    }
}
