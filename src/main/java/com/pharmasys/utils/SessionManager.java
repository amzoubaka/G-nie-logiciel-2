package com.pharmasys.utils;

import java.util.Date;

import com.pharmasys.models.User;

/**
 * Gestionnaire de session utilisateur
 */
public class SessionManager {
    private static User currentUser = null;
    private static Date loginTime = null;
    private static final long SESSION_TIMEOUT = 2 * 60 * 60 * 1000; // 2 heures en millisecondes
    
    /**
     * Crée une nouvelle session pour l'utilisateur
     */
    public static void createSession(User user) {
        currentUser = user;
        loginTime = new Date();
    }
    
    /**
     * Retourne l'utilisateur de la session courante
     */
    public static User getCurrentUser() {
        if (isSessionExpired()) {
            logout();
            return null;
        }
        return currentUser;
    }
    
    /**
     * Vérifie si la session a expiré
     */
    public static boolean isSessionExpired() {
        if (loginTime == null) {
            return true;
        }
        long elapsed = new Date().getTime() - loginTime.getTime();
        return elapsed > SESSION_TIMEOUT;
    }
    
    /**
     * Déconnecte l'utilisateur et supprime la session
     */
    public static void logout() {
        currentUser = null;
        loginTime = null;
    }
    
    /**
     * Vérifie si un utilisateur est connecté
     */
    public static boolean isLoggedIn() {
        return currentUser != null && !isSessionExpired();
    }
    
    /**
     * Vérifie si l'utilisateur courant est un administrateur
     */
    public static boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
    
    /**
     * Vérifie si l'utilisateur courant est un pharmacien ou admin
     */
    public static boolean isPharmacien() {
        return currentUser != null && currentUser.isPharmacien();
    }
}
