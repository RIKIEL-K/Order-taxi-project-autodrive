package com.example.Autodrive.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

//j'ai cree cette classe pour le model de connexion
public class LoginRequest {
    private String email;
    private String password;

    // Getters et setters
    public String getEmail() {
        return email0;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
