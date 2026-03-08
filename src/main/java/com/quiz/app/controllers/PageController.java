package com.quiz.app.controllers;

import com.quiz.app.entities.Participant;
import com.quiz.app.entities.Question;
import com.quiz.app.entities.Resultat;
import com.quiz.app.repositories.ParticipantRepository;
import com.quiz.app.repositories.ResultatRepository;
import com.quiz.app.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ResultatRepository resultatRepository;

    @GetMapping("/")
    public String loginPage() {
        return "Login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, 
                               @RequestParam(required = false) String password, 
                               HttpSession session, 
                               Model model) {
        
        String user = (username != null) ? username.trim() : "";
        String pass = (password != null) ? password.trim() : "";

        // Vérification ADMIN
        if ("admin".equals(user) && "admin123".equals(pass)) {
            session.setAttribute("adminConnecte", true);
            session.setAttribute("role", "ADMIN");
            return "redirect:/admin/dashboard";
        }

        // Vérification PARTICIPANT
        Optional<Participant> p = participantRepository.findByEmail(user);
        if (p.isPresent()) {
            session.setAttribute("participant", p.get());
            session.setAttribute("role", "USER");
            return "redirect:/choix-categorie";
        }

        model.addAttribute("erreur", "Identifiant ou mot de passe incorrect.");
        return "Login";
    }

    @GetMapping("/choix-categorie")
    public String choixCategorie(HttpSession session) {
        if (session.getAttribute("participant") == null) {
            return "redirect:/";
        }
        return "choix-categorie";
    }
    
    @GetMapping("/quiz/{categorie}")
public String afficherQuiz(@PathVariable String categorie, HttpSession session, Model model) {
    if (session.getAttribute("participant") == null) {
        return "redirect:/";
    }
    
    // 1. Récupérer toutes les questions de la catégorie
    List<Question> toutesLesQuestions = questionService.getQuestionsParCategorie(categorie.toUpperCase());
    
    // 2. Mélanger la liste pour l'aléatoire
    Collections.shuffle(toutesLesQuestions);
    
    // 3. Sélectionner maximum 10 questions
    List<Question> selectionAleatoire = toutesLesQuestions.stream()
                                         .limit(10)
                                         .collect(Collectors.toList());
    
    model.addAttribute("questions", selectionAleatoire);
    model.addAttribute("categorie", categorie.toUpperCase());
    
    // Stocker en session pour le traitement du score
    session.setAttribute("categorieChoisie", categorie.toUpperCase());
    
    return "quiz";
}

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {
        System.out.println("===== DASHBOARD ACCESS =====");
        System.out.println("Admin connecté: " + (session.getAttribute("adminConnecte") != null));
        
        if (session.getAttribute("adminConnecte") == null) {
            System.out.println("Admin non connecté - redirection");
            return "redirect:/";
        }

        List<Question> questions = questionService.getAllQuestions();
        List<Participant> participants = participantRepository.findAll();
        List<Resultat> resultats = resultatRepository.findAll();

        System.out.println("Questions trouvées: " + questions.size());
        System.out.println("Participants trouvés: " + participants.size());
        System.out.println("Résultats trouvés: " + resultats.size());
        
        model.addAttribute("questions", questions);
        model.addAttribute("participants", participants);
        model.addAttribute("resultats", resultats);
        
        return "dashboard";
    }

    @PostMapping("/admin/questions/ajouter")
    public String ajouterQuestion(@RequestParam String titre, 
                                  @RequestParam String categorie,
                                  @RequestParam String option1,
                                  @RequestParam String option2,
                                  @RequestParam String option3,
                                  @RequestParam int correctIndex) {
        
        Question q = new Question();
        q.setTitre(titre);
        q.setCategorie(categorie);
        q.setOption1(option1);
        q.setOption2(option2);
        q.setOption3(option3);
        q.setNiveauDifficulte("Normal");

        if (correctIndex == 1) q.setReponseCorrecte(option1);
        else if (correctIndex == 2) q.setReponseCorrecte(option2);
        else q.setReponseCorrecte(option3);
        
        questionService.saveQuestion(q);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/questions/supprimer/{id}")
    public String supprimerQuestion(@PathVariable Long id) {
        questionService.supprimerQuestion(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/participants/supprimer/{id}")
    public String supprimerParticipant(@PathVariable Long id) {
        participantRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/questions/modifier")
    public String modifierQuestion(@RequestParam Long id,
                                   @RequestParam String titre,
                                   @RequestParam String categorie,
                                   @RequestParam String option1,
                                   @RequestParam String option2,
                                   @RequestParam String option3,
                                   @RequestParam int correctIndex) {
        
        Question q = questionService.getQuestionById(id);
        q.setTitre(titre);
        q.setCategorie(categorie);
        q.setOption1(option1);
        q.setOption2(option2);
        q.setOption3(option3);
        
        if (correctIndex == 1) q.setReponseCorrecte(option1);
        else if (correctIndex == 2) q.setReponseCorrecte(option2);
        else q.setReponseCorrecte(option3);
        
        questionService.saveQuestion(q);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}