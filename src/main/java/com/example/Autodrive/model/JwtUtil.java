package com.example.Autodrive.model;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
// cette classe permet de creer le token lorsqu'il va se connecter et l'enregistrer dans une session
public class JwtUtil {

    // Clé secrète d'au moins 32 caractères pour HS256
    private static final String SECRET_KEY = "MaCleJWTultraSecreteDe32Caracteres!";

    //Cette ligne génère une clé cryptographique sécurisée à partir de ta chaîne secrète (SECRET_KEY), qui est utilisée pour signer les tokens JWT.
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    //classe pour generer le Token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}



