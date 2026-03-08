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

public Resultat calculerEtEnregistrer(Participant participant, Map<String, String> reponses, String categorie) {
    int bonnesReponses = 0;
    int questionsRepondues = 0;

    for (Map.Entry<String, String> entry : reponses.entrySet()) {
        String key = entry.getKey(); // ex: "q14"
        if (key.startsWith("q")) {
            Long qId = Long.parseLong(key.substring(1));
            String reponseUser = entry.getValue();

            // Récupérer la question spécifique par son ID
            Question q = questionRepository.findById(qId).orElse(null);

            if (q != null) {
                questionsRepondues++;
                if (reponseUser != null && reponseUser.trim().equalsIgnoreCase(q.getReponseCorrecte().trim())) {
                    bonnesReponses++;
                }
            }
        }
    }

    // Calcul sur 10 questions (ou le nombre de questions traitées)
    int scoreFinal = (questionsRepondues > 0) ? (bonnesReponses * 100) / questionsRepondues : 0;

    // Création du résultat
    Resultat res = new Resultat();
    res.setParticipant(participant);
    res.setScore(scoreFinal);
    res.setCategorie(categorie);
    res.setDatePassage(LocalDateTime.now());

    return resultatRepository.save(res);
}}