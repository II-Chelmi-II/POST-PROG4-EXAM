package com.michel.examenpratique.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michel.examenpratique.model.Patrimoine;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PatrimoineService {
    private final ObjectMapper objectMapper;

    private static final String FILE_PATH = "patrimoines.json";
    private Map<String, Patrimoine> patrimoines = new HashMap<>();

    public PatrimoineService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void createOrUpdatePatrimoine(String id, Patrimoine patrimoine) {
        patrimoine.setDerniereModification(LocalDateTime.now());
        patrimoines.put(id, patrimoine);
        saveToFile(id);
    }

    public Patrimoine getPatrimoine(String id) {
        return patrimoines.get(id);
    }

    private void saveToFile(String id) {
        try {
            Map<String, Patrimoine> patrimoinesExistant = loadExistingPatrimoines();
            patrimoinesExistant.put(id, patrimoines.get(id));
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(patrimoinesExistant);
            Files.write(Paths.get(FILE_PATH), json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du patrimoine : " + e.getMessage());
        }
    }

    private Map<String, Patrimoine> loadExistingPatrimoines() throws IOException {
        if (Files.exists(Paths.get(FILE_PATH))) {
            return objectMapper.readValue(Files.readAllBytes(Paths.get(FILE_PATH)),
                    new TypeReference<Map<String, Patrimoine>>() {});
        }
        return new HashMap<>();
    }

    // Méthode pour réinitialiser l'état
    public void reset() {
        patrimoines.clear();
    }
}