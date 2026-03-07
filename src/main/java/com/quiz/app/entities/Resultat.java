package com.quiz.app.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resultat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Au lieu d'une relation complexe pour l'instant, utilisons des String 
    // pour que ton HTML th:text="${res.emailParticipant}" fonctionne direct.
    private String emailParticipant; 
    private String categorie;
    private Integer score;
    private LocalDateTime datePassage = LocalDateTime.now();

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmailParticipant(String emailParticipant) {
        this.emailParticipant = emailParticipant;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setDatePassage(LocalDateTime datePassage) {
        this.datePassage = datePassage;
    }

    // Supprime les méthodes "UnsupportedOperationException" et laisse Lombok (@Data) 
    // ou génère des getters/setters classiques.

    public Long getId() {
        return id;
    }

    public String getEmailParticipant() {
        return emailParticipant;
    }

    public String getCategorie() {
        return categorie;
    }

    public Integer getScore() {
        return score;
    }

    public LocalDateTime getDatePassage() {
        return datePassage;
    }
}