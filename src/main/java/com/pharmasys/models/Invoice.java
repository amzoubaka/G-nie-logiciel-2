package com.pharmasys.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Modèle représentant une facture
 */
public class Invoice {
    private int id;
    private int clientId;
    private String nomClient;
    private Date dateFacture;
    private double montantTotal;
    private int createdBy;
    private List<InvoiceItem> items;
    
    public Invoice() {
        this.items = new ArrayList<>();
        this.dateFacture = new Date();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getClientId() {
        return clientId;
    }
    
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    
    public String getNomClient() {
        return nomClient;
    }
    
    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }
    
    public Date getDateFacture() {
        return dateFacture;
    }
    
    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }
    
    public double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    public int getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    public List<InvoiceItem> getItems() {
        return items;
    }
    
    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }
    
    public void addItem(InvoiceItem item) {
        this.items.add(item);
    }
}
