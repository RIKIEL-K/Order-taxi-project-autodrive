package com.example.Autodrive.service;

import com.example.Autodrive.model.Compte;
import com.example.Autodrive.model.Role;
import com.example.Autodrive.model.User;
import com.example.Autodrive.repository.CompteRepository;
import com.example.Autodrive.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
// Un service implementant la logique pour la gestion des utilisateurs
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CompteRepository compteRepository;
    private PasswordEncoder passwordEncoder;

    // Méthode pour enregistrer un nouvel utilisateur
    public void registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        // Hachage du mot de passe
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Définition du rôle par défaut si non spécifié
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        // Enregistrement de l'utilisateur dans la base de données
        userRepository.save(user);

        // Création du compte associé à cette utilisateur
        Compte compte = new Compte();
        compte.setUserId(user.getId());
        compte.setDateCreation(new Date());
        compte.setId_compte(generateCompteId());

        // Enregistrer le compte dans la base de données
        compteRepository.save(compte);
    }

    private String generateCompteId() {
        return "COMPTE-" + System.currentTimeMillis();
    }
}
