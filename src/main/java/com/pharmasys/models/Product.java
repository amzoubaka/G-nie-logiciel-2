package com.pharmasys.models;

import java.util.Date;

/**
 * Modèle représentant un produit (médicament)
 */
public class Product {
    private int id;
    private String nom;
    private String reference;
    private int quantite;
    private double prix;
    private Date datePeremption;
    private Date validite;
    private String emplacement;
    private int createdBy;
    private Date createdAt;
    private Date updatedAt;
    
    public Product() {}
    
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
    
    public String getReference() {
        return reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    public double getPrix() {
        return prix;
    }
    
    public void setPrix(double prix) {
        this.prix = prix;
    }
    
    public Date getDatePeremption() {
        return datePeremption;
    }
    
    public void setDatePeremption(Date datePeremption) {
        this.datePeremption = datePeremption;
    }
    
    public Date getValidite() {
        return validite;
    }
    
    public void setValidite(Date validite) {
        this.validite = validite;
    }
    
    public String getEmplacement() {
        return emplacement;
    }
    
    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }
    
    public int getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return nom + " - " + reference;
    }
}
