package com.quiz.app.controllers;

import com.quiz.app.repositories.ResultatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminResultatController {

    @Autowired
    private ResultatRepository resultatRepository;

    @DeleteMapping("/resultats/vider")
    public ResponseEntity<?> viderHistorique() {
        try {
            long count = resultatRepository.count();
            resultatRepository.deleteAll();
            return ResponseEntity.ok().body("Historique vidé avec succès. " + count + " résultats supprimés.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de la suppression: " + e.getMessage());
        }
    }
}