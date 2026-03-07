//package com.quiz.app.service;
//
//import com.quiz.app.entities.Participant;
//import com.quiz.app.entities.Question;
//import com.quiz.app.entities.Resultat;
//import com.quiz.app.repositories.QuestionRepository;
//import com.quiz.app.repositories.ResultatRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class QuizService {
//
//    @Autowired
//    private QuestionRepository questionRepository;
//
//    @Autowired
//    private ResultatRepository resultatRepository;
//
//   public Resultat calculerEtEnregistrer(Participant p, Map<String, String> reponses, String categorie) {
//    List<Question> questions = questionRepository.findByCategorie(categorie.toUpperCase());
//    int bonnesReponses = 0;
//
//    for (Question q : questions) {
//        String reponseUtilisateur = reponses.get("q" + q.getId());
//        
//        if (reponseUtilisateur == null) {
//            reponseUtilisateur = reponses.get(String.valueOf(q.getId()));
//        }
//
//        if (reponseUtilisateur != null && reponseUtilisateur.equalsIgnoreCase(q.getReponseCorrecte())) {
//            bonnesReponses++;
//        }
//    }
//
//    int pourcentage = 0;
//    if (!questions.isEmpty()) {
//        pourcentage = (bonnesReponses * 100) / questions.size();
//    }
//
//    Resultat res = new Resultat();
//    res.setEmailParticipant(p.getEmail());
//    res.setScore(pourcentage);
//    res.setCategorie(categorie.toUpperCase());
//    res.setDatePassage(LocalDateTime.now());
//
//    return resultatRepository.save(res);
//}
//}
package com.quiz.app.service;

import com.quiz.app.entities.Participant;
import com.quiz.app.entities.Question;
import com.quiz.app.entities.Resultat;
import com.quiz.app.repositories.QuestionRepository;
import com.quiz.app.repositories.ResultatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResultatRepository resultatRepository;

    public Resultat calculerEtEnregistrer(Participant p, Map<String, String> reponses, String categorie) {
        System.out.println("=== Début calculEtEnregistrer ===");
        System.out.println("Participant: " + (p != null ? p.getEmail() : "null"));
        System.out.println("Catégorie: " + categorie);
        System.out.println("Réponses reçues: " + reponses);
        
        if (p == null) {
            throw new RuntimeException("Participant non trouvé");
        }
        
        List<Question> questions = questionRepository.findByCategorie(categorie.toUpperCase());
        System.out.println("Questions trouvées: " + questions.size());
        
        int bonnesReponses = 0;

        for (Question q : questions) {
            String reponseUtilisateur = reponses.get("q" + q.getId());
            
            if (reponseUtilisateur == null) {
                reponseUtilisateur = reponses.get(String.valueOf(q.getId()));
            }

            System.out.println("Question ID " + q.getId() + 
                             " - Réponse user: " + reponseUtilisateur + 
                             " - Réponse correcte: " + q.getReponseCorrecte());

            if (reponseUtilisateur != null && reponseUtilisateur.equalsIgnoreCase(q.getReponseCorrecte())) {
                bonnesReponses++;
                System.out.println("✓ Bonne réponse");
            } else {
                System.out.println("✗ Mauvaise réponse");
            }
        }

        int pourcentage = 0;
        if (!questions.isEmpty()) {
            pourcentage = (bonnesReponses * 100) / questions.size();
        }
        
        System.out.println("Bonnes réponses: " + bonnesReponses + "/" + questions.size());
        System.out.println("Pourcentage: " + pourcentage + "%");

        Resultat res = new Resultat();
        res.setEmailParticipant(p.getEmail());
        res.setScore(pourcentage);
        res.setCategorie(categorie.toUpperCase());
        res.setDatePassage(LocalDateTime.now());

        Resultat saved = resultatRepository.save(res);
        System.out.println("Résultat sauvegardé avec ID: " + saved.getId());
        System.out.println("=== Fin calculEtEnregistrer ===");
        
        return saved;
    }
}