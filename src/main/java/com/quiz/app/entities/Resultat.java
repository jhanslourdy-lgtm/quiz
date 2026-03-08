package com.quiz.app.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultat")
public class Resultat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categorie;
    private int score;
    
    @Column(name = "date_passage")
    private LocalDateTime datePassage;

    // L'erreur venait probablement de l'absence de cette déclaration
    @ManyToOne
    @JoinColumn(name = "email_participant", referencedColumnName = "email")
    private Participant participant; 

    // Constructeurs
    public Resultat() {}

    // Getters et Setters corrigés
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public LocalDateTime getDatePassage() { return datePassage; }
    public void setDatePassage(LocalDateTime datePassage) { this.datePassage = datePassage; }

    // La méthode qui posait problème (UnsupportedOperationException)
    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant; // Plus de "Not supported yet"
    }
}