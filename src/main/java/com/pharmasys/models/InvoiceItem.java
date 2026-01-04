package com.pharmasys.models;

/**
 * Modèle représentant une ligne de facture
 */
public class InvoiceItem {
    private int id;
    private int factureId;
    private int produitId;
    private String nomProduit;
    private String reference;
    private int quantite;
    private double prixUnitaire;
    private double sousTotal;
    
    public InvoiceItem() {}
    
    public InvoiceItem(int produitId, String nomProduit, String reference, 
                       int quantite, double prixUnitaire) {
        this.produitId = produitId;
        this.nomProduit = nomProduit;
        this.reference = reference;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.sousTotal = quantite * prixUnitaire;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getFactureId() {
        return factureId;
    }
    
    public void setFactureId(int factureId) {
        this.factureId = factureId;
    }
    
    public int getProduitId() {
        return produitId;
    }
    
    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }
    
    public String getNomProduit() {
        return nomProduit;
    }
    
    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
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
        this.sousTotal = quantite * prixUnitaire;
    }
    
    public double getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        this.sousTotal = quantite * prixUnitaire;
    }
    
    public double getSousTotal() {
        return sousTotal;
    }
    
    public void setSousTotal(double sousTotal) {
        this.sousTotal = sousTotal;
    }
}
