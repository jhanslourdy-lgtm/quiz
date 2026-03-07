/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quiz.app.service;

import com.quiz.app.entities.Question;
import com.quiz.app.repositories.QuestionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Handy
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    // Ajouter une nouvelle question (Maths, Chimie, etc.)
    public Question ajouterQuestion(Question question) {
        return questionRepository.save(question);
    }
    
    // Récupérer toutes les questions d'une catégorie spécifique
    public List<Question> getQuestionsParCategorie(String categorie) {
        return questionRepository.findByCategorie(categorie.toUpperCase());
    }

    // Supprimer une question par son ID
    public void supprimerQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public List<Question> getAllQuestions() {
    return questionRepository.findAll(); // Retourne la liste complète des questions
}

public void saveQuestion(Question question) {
    questionRepository.save(question); // Pour enregistrer une nouvelle question
}

    public Question getQuestionById(Long id) {
    return questionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Question non trouvée"));
}
}
