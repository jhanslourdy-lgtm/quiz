/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quiz.app.controllers;

import com.quiz.app.entities.Question;
import com.quiz.app.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Handy
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private QuestionService questionService;

    // Ajouter une question : POST http://localhost:8080/api/admin/questions
    @PostMapping("/questions")
    public ResponseEntity<Question> creerQuestion(@RequestBody Question question) {
        Question nouvelleQuestion = questionService.ajouterQuestion(question);
        return ResponseEntity.ok(nouvelleQuestion);
    }

@DeleteMapping("/questions/{id}")
public ResponseEntity<String> supprimerQuestion(@PathVariable Long id) {
    questionService.supprimerQuestion(id);
    return ResponseEntity.ok("Question supprimée avec succès.");
}
}
