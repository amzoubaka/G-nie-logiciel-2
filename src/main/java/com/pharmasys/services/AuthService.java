package com.pharmasys.services;

import com.pharmasys.dao.UserDAO;
import com.pharmasys.models.User;
import com.pharmasys.utils.PasswordUtil;
import com.pharmasys.utils.SessionManager;

/**
 * Service d'authentification
 */
public class AuthService {
    private final UserDAO userDAO;
    private final LogService logService;
    
    public AuthService() {
        this.userDAO = new UserDAO();
        this.logService = new LogService();
    }
    
    /**
     * Authentifie un utilisateur
     * @return User si authentification réussie, null sinon
     */
    public User authenticate(String username, String password) {
        try {
            User user = userDAO.findByUsername(username);
            
            if (user != null && PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                // Mettre à jour la dernière connexion
                userDAO.updateLastLogin(user.getId());
                
                // Créer la session
                SessionManager.createSession(user);
                
                // Logger la connexion
                logService.log(user.getId(), "LOGIN", "Connexion réussie");
                
                return user;
            }
            
            // Logger la tentative échouée
            if (user != null) {
                logService.log(user.getId(), "LOGIN_FAILED", "Tentative de connexion échouée");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Déconnecte l'utilisateur courant
     */
    public void logout() {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            logService.log(user.getId(), "LOGOUT", "Déconnexion");
        }
        SessionManager.logout();
    }
    
    /**
     * Vérifie si l'utilisateur a la permission
     */
    public boolean hasPermission(String requiredRole) {
        User user = SessionManager.getCurrentUser();
        if (user == null) return false;
        
        if ("admin".equals(requiredRole)) {
            return user.isAdmin();
        } else if ("pharmacien".equals(requiredRole)) {
            return user.isPharmacien();
        }
        
        return true;
    }
}
