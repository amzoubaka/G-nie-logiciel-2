package com.pharmasys.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Tests pour PasswordUtil
 */
class PasswordUtilTest {
    
    @Test
    void testHashPassword_ShouldReturnNonEmptyHash() {
        String password = "testPassword123";
        String hash = PasswordUtil.hashPassword(password);
        
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
        assertNotEquals(password, hash); // Hash ne doit pas être le mot de passe en clair
    }
    
    @Test
    void testVerifyPassword_WithCorrectPassword_ShouldReturnTrue() {
        String password = "mySecurePassword";
        String hash = PasswordUtil.hashPassword(password);
        
        boolean result = PasswordUtil.verifyPassword(password, hash);
        
        assertTrue(result);
    }
    
    @Test
    void testVerifyPassword_WithIncorrectPassword_ShouldReturnFalse() {
        String password = "correctPassword";
        String wrongPassword = "wrongPassword";
        String hash = PasswordUtil.hashPassword(password);
        
        boolean result = PasswordUtil.verifyPassword(wrongPassword, hash);
        
        assertFalse(result);
    }
    
    @Test
    void testHashPassword_SamePlainPassword_ShouldGenerateDifferentHashes() {
        String password = "samePassword";
        String hash1 = PasswordUtil.hashPassword(password);
        String hash2 = PasswordUtil.hashPassword(password);
        
        // Les hashes doivent être différents à cause du salt aléatoire
        assertNotEquals(hash1, hash2);
        
        // Mais tous deux doivent être valides
        assertTrue(PasswordUtil.verifyPassword(password, hash1));
        assertTrue(PasswordUtil.verifyPassword(password, hash2));
    }
}
