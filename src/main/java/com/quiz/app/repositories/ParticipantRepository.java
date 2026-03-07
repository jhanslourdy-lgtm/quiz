/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quiz.app.repositories;

import com.quiz.app.entities.Participant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Handy
 */
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    // Pratique pour vérifier si un email existe déjà avant l'inscription
    Optional<Participant> findByEmail(String email);
}