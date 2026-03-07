
package com.quiz.app.controllers;

import com.quiz.app.entities.Participant;
import com.quiz.app.entities.Question;
import com.quiz.app.entities.Resultat;
import com.quiz.app.repositories.ParticipantRepository;
import com.quiz.app.service.QuestionService;
import com.quiz.app.service.QuizService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class ParticipantController {

    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private QuizService quizService;
    
    @Autowired
    private ParticipantRepository participantRepository;
    
    @GetMapping("/questions/{categorie}")
    public List<Question> chargerQuiz(@PathVariable String categorie) {
        return questionService.getQuestionsParCategorie(categorie);
    }

    @PostMapping("/soumettre")
    public ResponseEntity<?> validerQuiz(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String categorie,
            @RequestBody(required = false) Map<String, String> reponses) {
        
        System.out.println("=== REQUÊTE REÇUE DANS PARTICIPANT CONTROLLER ===");
        System.out.println("Email reçu: " + email);
        System.out.println("Catégorie reçue: " + categorie);
        System.out.println("Réponses reçues: " + reponses);
        
        // Vérifier les paramètres
        if (email == null || email.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Email non fourni");
            error.put("details", "L'email du participant est requis");
            return ResponseEntity.badRequest().body(error);
        }
        
        if (categorie == null || categorie.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Catégorie non fournie");
            error.put("details", "La catégorie du quiz est requise");
            return ResponseEntity.badRequest().body(error);
        }
        
        if (reponses == null || reponses.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Aucune réponse fournie");
            error.put("details", "Les réponses au quiz sont requises");
            return ResponseEntity.badRequest().body(error);
        }
        
        // Chercher le participant
        Participant p = participantRepository.findByEmail(email).orElse(null);
        
        if (p == null) {
            System.out.println("ERREUR: Participant non trouvé avec email: " + email);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Participant non trouvé");
            error.put("email", email);
            return ResponseEntity.status(404).body(error);
        }
        
        System.out.println("Participant trouvé: " + p.getNom());
        
        try {
            Resultat resultat = quizService.calculerEtEnregistrer(p, reponses, categorie);

            Map<String, Object> response = new HashMap<>();
            response.put("score", resultat.getScore());
            response.put("status", resultat.getScore() >= 50 ? "SUCCESS" : "FAIL");
            response.put("message", resultat.getScore() >= 50 ? "Félicitations !" : "Essaie encore !");
            
            System.out.println("Réponse envoyée: " + response);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erreur lors du calcul du score");
            error.put("details", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}