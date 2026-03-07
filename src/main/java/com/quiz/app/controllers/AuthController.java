/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quiz.app.controllers;

import com.quiz.app.entities.Participant;
import com.quiz.app.repositories.ParticipantRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Handy
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ParticipantRepository participantRepository;
    // Login simple : POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        return participantRepository.findByEmail(email)
                .map(p -> ResponseEntity.ok("Bienvenue " + p.getNom()))
                .orElse(ResponseEntity.status(401).body("Utilisateur non trouvé"));
    }

    // Inscription : POST /api/auth/register
 
    @PostMapping("/register")
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
        // 1. Vérifier si l'email existe déjà
        if (participantRepository.findByEmail(participant.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Cet email est déjà enregistré !");
        }
        
        // 2. Sauvegarder dans MySQL
        Participant savedUser = participantRepository.save(participant);
        return ResponseEntity.ok(savedUser);
    }
}