package com.michel.examenpratique;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michel.examenpratique.model.Patrimoine;
import com.michel.examenpratique.service.PatrimoineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExamenPratiqueApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Utilise l'ObjectMapper configuré

    @Autowired
    private PatrimoineService patrimoineService;

    @BeforeEach
    public void setUp() {
        patrimoineService = Mockito.mock(PatrimoineService.class);
        patrimoineService.reset();
    }

    @Test
    public void testCreateOrUpdatePatrimoine() throws Exception {
        Patrimoine patrimoine = new Patrimoine();
        patrimoine.setPossesseur("John Doe");

        mockMvc.perform(put("/patrimoines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patrimoine)))
                .andExpect(status().isOk())
                .andExpect(content().string("Patrimoine créé/mis à jour avec succès."));
    }

    @Test
    public void testGetPatrimoine() throws Exception {
        // Créer d'abord le patrimoine
        Patrimoine patrimoine = new Patrimoine();
        patrimoine.setPossesseur("John Doe");
        patrimoine.setDerniereModification(LocalDateTime.now());
        mockMvc.perform(put("/patrimoines/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patrimoine)));

        // Tester la récupération
        mockMvc.perform(get("/patrimoines/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"possesseur\":\"John Doe\"}"));
    }

    @Test
    public void testGetNonExistentPatrimoine() throws Exception {
        mockMvc.perform(get("/patrimoines/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOrUpdatePatrimoineWithNull() throws Exception {
        mockMvc.perform(put("/patrimoines/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")) // Vérifie que le contenu est bien une chaîne vide
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Le champ patrimoine ne peut pas être vide."));
    }

}