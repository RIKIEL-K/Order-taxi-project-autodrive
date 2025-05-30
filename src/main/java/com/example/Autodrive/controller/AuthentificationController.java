package com.example.Autodrive.controller;

import com.example.Autodrive.model.User;
import com.example.Autodrive.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthentificationController {
    private final UserService userService;

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
    
}
