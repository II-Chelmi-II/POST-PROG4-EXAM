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
    private final ObjectMapper objectMapper; // Utilisé pour la sérialisation/desérialisation JSON

    private static final String FILE_PATH = "patrimoines.json";
    private Map<String, Patrimoine> patrimoines = new HashMap<>(); // Stockage des patrimoines en mémoire

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
            patrimoinesExistant.put(id, patrimoines.get(id)); // Mise à jour de la collection
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(patrimoinesExistant);
            Files.write(Paths.get(FILE_PATH), json.getBytes()); // Écriture dans le fichier
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du patrimoine : " + e.getMessage());
        }
    }

    private Map<String, Patrimoine> loadExistingPatrimoines() throws IOException {
        if (Files.exists(Paths.get(FILE_PATH))) {
            return objectMapper.readValue(Files.readAllBytes(Paths.get(FILE_PATH)),
                    new TypeReference<Map<String, Patrimoine>>() {});
        }
        return new HashMap<>(); // Retourne une carte vide si le fichier n'existe pas
    }

    // Réinitialisation de l'état des patrimoines
    public void reset() {
        patrimoines.clear();
    }
}