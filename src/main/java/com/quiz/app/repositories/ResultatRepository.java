package com.quiz.app.repositories;

import com.quiz.app.entities.Resultat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResultatRepository extends JpaRepository<Resultat, Long> {
    
    // Remplace findByParticipantId par findByEmailParticipant
    List<Resultat> findByEmailParticipant(String email);
}