package com.pharmasys.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Utilitaire pour le hashage et la vérification des mots de passe
 * Utilise BCrypt pour un hashage sécurisé
 */
public class PasswordUtil {
    
    private static final int BCRYPT_COST = 10;
    
    /**
     * Hash un mot de passe en utilisant BCrypt
     * 
     * @param plainPassword Mot de passe en clair
     * @return Hash du mot de passe
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, plainPassword.toCharArray());
    }
    
    /**
     * Vérifie si un mot de passe correspond à son hash
     * 
     * @param plainPassword Mot de passe en clair
     * @param hashedPassword Hash stocké
     * @return true si le mot de passe correspond
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}
