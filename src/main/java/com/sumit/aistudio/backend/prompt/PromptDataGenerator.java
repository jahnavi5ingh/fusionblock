package com.sumit.aistudio.backend.prompt;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class PromptDataGenerator {

    private static final String API_ENDPOINT = "http://localhost:8080/api/prompts"; // Replace with your actual endpoint URL

    public static class Prompt {
        private String promptType;
        private boolean clean = false;
        private String outputType = "text";
        private boolean isMeta = false;
        private String model = "_";
        private boolean inline = false;
        private String data;

        // Constructors, Getters, and Setters
        public Prompt(String promptType, String data) {
            this.promptType = promptType;
            this.data = data;
        }

        public String getPromptType() {
            return promptType;
        }

        public void setPromptType(String promptType) {
            this.promptType = promptType;
        }

        public boolean isClean() {
            return clean;
        }

        public void setClean(boolean clean) {
            this.clean = clean;
        }

        public String getOutputType() {
            return outputType;
        }

        public void setOutputType(String outputType) {
            this.outputType = outputType;
        }

        public boolean isMeta() {
            return isMeta;
        }

        public void setMeta(boolean meta) {
            isMeta = meta;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public boolean isInline() {
            return inline;
        }

        public void setInline(boolean inline) {
            this.inline = inline;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static void main(String[] args) {
        List<Prompt> prompts = generatePrompts(100);
        sendPromptsToApi(prompts);
    }

    private static List<Prompt> generatePrompts(int count) {
        List<Prompt> prompts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String promptType = "Type_" + i;
            String jsonData = "{\"key\": \"value_" + i + "\"}"; // Example JSON data
            Prompt prompt = new Prompt(promptType, jsonData);
            prompts.add(prompt);
        }
        return prompts;
    }

    private static void sendPromptsToApi(List<Prompt> prompts) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (Prompt prompt : prompts) {
            try {
                HttpEntity<Prompt> request = new HttpEntity<>(prompt, headers);
                ResponseEntity<String> response = restTemplate.exchange(API_ENDPOINT, HttpMethod.POST, request, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Successfully inserted: " + prompt.getPromptType());
                } else {
                    System.err.println("Failed to insert: " + prompt.getPromptType());
                }
            } catch (Exception e) {
                System.err.println("Error occurred while inserting: " + prompt.getPromptType());
                e.printStackTrace();
            }
        }
    }
}
