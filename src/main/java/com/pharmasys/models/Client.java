package com.pharmasys.models;

import java.util.Date;

/**
 * Modèle représentant un client
 */
public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String quartier;
    private int age;
    private String telephone;
    private Date createdAt;
    
    public Client() {}
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getQuartier() {
        return quartier;
    }
    
    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getFullName() {
        if (prenom != null && nom != null) {
            return prenom + " " + nom;
        }
        return "";
    }
    
    public void setFullName(String fullName) {
        // Simple split - could be improved
        if (fullName != null && fullName.contains(" ")) {
            String[] parts = fullName.split(" ", 2);
            this.prenom = parts[0];
            this.nom = parts.length > 1 ? parts[1] : "";
        } else {
            this.nom = fullName;
            this.prenom = fullName;
        }
    }
    
    // English aliases
    public String getPhoneNumber() {
        return telephone;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.telephone = phoneNumber;
    }
    
    public String getAddress() {
        return quartier;
    }
    
    public void setAddress(String address) {
        this.quartier = address;
    }
    
    @Override
    public String toString() {
        return getFullName() + " - " + telephone;
    }
}
