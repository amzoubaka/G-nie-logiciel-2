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
        return prenom + " " + nom;
    }
    
    @Override
    public String toString() {
        return getFullName() + " - " + telephone;
    }
}
