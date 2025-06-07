package com.example.Autodrive.controller;

import com.example.Autodrive.model.LoginRequest;
import com.example.Autodrive.model.Token;
import com.example.Autodrive.model.User;
import com.example.Autodrive.repository.TokenRepository;
import com.example.Autodrive.service.MailService;
import com.example.Autodrive.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthentificationController {
    @Autowired
    private final UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private TokenRepository tokenRepository;

    // Endpoint pour enregistrer un nouvel utilisateur
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) { //@RequestBody pour lier l'objet JSON envoyé dans la requête au paramètre user
        try {
            userService.registerUser(user); // Appel de la méthode registerUser du service pour enregistrer l'utilisateur
            return ResponseEntity.ok("Utilisateur enregistré avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'enregistrement de l'utilisateur : " + e.getMessage());
        }
    }

    // Demande de réinitialisation du mot de passe
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) { //RequestParam pour récupérer le paramètre email de la requête sous forme de chaîne de caractères
        // Vérification si l'utilisateur existe
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(400).body("Email not found");
        }

        // Vérifier si un token existe déjà pour cet utilisateur
        Token existingToken = tokenRepository.findByUser(user);
        if (existingToken != null) {
            return ResponseEntity.status(400).body("A password reset request has already been sent to this email.");
        }

        // Générer un token pour la réinitialisation
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetToken(user, token);

        // Envoyer un email avec le lien de réinitialisation
        mailService.sendResetPasswordEmail(email, token);

        return ResponseEntity.ok("Password reset email sent.");
    }

    // Réinitialisation du mot de passe avec le token
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody String token, @RequestBody String newPassword) {
        Token resetToken = userService.findPasswordResetToken(token);

        if (resetToken == null) {
            return "Invalid token.";
        }

        User user = resetToken.getUser();

        // Hachage du nouveau mot de passe
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(newPassword);

        // Mise à jour du mot de passe de l'utilisateur
        user.setPassword(hashedPassword);
        userService.updateUser(user);

        return "Password successfully reset.";
    }

    //connexion avec email et mot de passe
    // sa retourne le token de l'utilisateur et nous permet de le stocker dans une session avec ceci
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok("Connexion réussie avec0 son  Token : " + token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Échec de la connexion : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur : " + e.getMessage());
        }
    }
    
}
