package com.michel.examenpratique.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Patrimoine {
    private String possesseur;
    private LocalDateTime derniereModification;
}
