/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quiz.app.repositories;

import com.quiz.app.entities.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Handy
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Permet de récupérer uniquement les questions de "Maths" par exemple
    List<Question> findByCategorie(String categorie);
}