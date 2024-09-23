package com.michel.examenpratique.controller;

import com.michel.examenpratique.model.Patrimoine;
import com.michel.examenpratique.service.PatrimoineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patrimoines")
public class PatrimoineController {
    private final PatrimoineService patrimoineService;

    public PatrimoineController(PatrimoineService patrimoineService) {
        this.patrimoineService = patrimoineService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> createOrUpdatePatrimoine(@PathVariable String id, @RequestBody(required = false) Patrimoine patrimoine) {
        if (patrimoine == null || patrimoine.getPossesseur() == null || patrimoine.getPossesseur().isEmpty()) {
            return ResponseEntity.badRequest().body("Le champ patrimoine ne peut pas être vide.");
        }

        try {
            patrimoineService.createOrUpdatePatrimoine(id, patrimoine);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("Patrimoine créé/mis à jour avec succès.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patrimoine> getPatrimoine(@PathVariable String id) {
        Patrimoine patrimoine = patrimoineService.getPatrimoine(id);
        if (patrimoine == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(patrimoine);
    }
}