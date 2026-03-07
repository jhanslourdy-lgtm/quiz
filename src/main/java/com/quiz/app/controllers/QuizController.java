
package com.quiz.app.controllers;

import com.quiz.app.entities.Participant;
import com.quiz.app.entities.Resultat;
import com.quiz.app.service.QuizService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(@RequestBody Map<String, String> reponses, HttpSession session) {
        System.out.println("=== REQUÊTE REÇUE DANS QUIZ CONTROLLER ===");
        
        // 1. Récupération du participant en session
        Participant participant = (Participant) session.getAttribute("participant");
        String categorie = (String) session.getAttribute("categorieChoisie");

        System.out.println("Participant en session: " + (participant != null ? participant.getEmail() : "null"));
        System.out.println("Catégorie en session: " + categorie);
        System.out.println("Réponses reçues: " + reponses);

        if (participant == null) {
            return ResponseEntity.status(403).body(Map.of(
                "error", "Session expirée",
                "message", "Veuillez vous reconnecter"
            ));
        }
        
        if (categorie == null) {
            categorie = "GENERALE";
            System.out.println("Catégorie par défaut utilisée: GENERALE");
        }

        try {
            // 2. Traitement via le service
            Resultat resultat = quizService.calculerEtEnregistrer(participant, reponses, categorie);

            // 3. Réponse propre pour le JavaScript
            Map<String, Object> response = new HashMap<>();
            response.put("score", resultat.getScore());
            response.put("status", resultat.getScore() >= 50 ? "SUCCESS" : "FAIL");
            response.put("message", resultat.getScore() >= 50 ? "Félicitations, test réussi !" : "Travaillez encore, vous y êtes presque !");

            System.out.println("Réponse envoyée: " + response);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "error", "Erreur serveur",
                "message", e.getMessage()
            ));
        }
    }
}