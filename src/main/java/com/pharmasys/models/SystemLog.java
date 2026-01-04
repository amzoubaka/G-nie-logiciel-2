package com.pharmasys.models;

import java.util.Date;

/**
 * Modèle représentant un log système
 */
public class SystemLog {
    private int id;
    private int userId;
    private String action;
    private String details;
    private Date timestamp;
    
    public SystemLog() {
        this.timestamp = new Date();
    }
    
    public SystemLog(int userId, String action, String details) {
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.timestamp = new Date();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
