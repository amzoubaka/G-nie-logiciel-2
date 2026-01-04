package com.pharmasys.models;

import java.util.Date;

/**
 * Modèle représentant un utilisateur du système
 */
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String role; // admin, pharmacien, utilisateur
    private Date createdAt;
    private Date lastLogin;
    
    public User() {}
    
    public User(String username, String passwordHash, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public boolean isAdmin() {
        return "admin".equals(role);
    }
    
    public boolean isPharmacien() {
        return "pharmacien".equals(role) || isAdmin();
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
