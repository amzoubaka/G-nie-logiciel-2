package com.pharmasys.utils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.pharmasys.models.Client;
import com.pharmasys.models.Product;

/**
 * Tests pour ValidationUtil
 */
class ValidationUtilTest {
    
    @Test
    void testValidateProduct_WithValidProduct_ShouldNotThrow() {
        Product product = new Product();
        product.setNom("Paracétamol");
        product.setReference("PAR-500");
        product.setQuantite(10);
        product.setPrix(2500.0);
        product.setDatePeremption(new Date());
        product.setEmplacement("Rayon A");
        
        assertDoesNotThrow(() -> ValidationUtil.validateProduct(product));
    }
    
    @Test
    void testValidateProduct_WithEmptyName_ShouldThrow() {
        Product product = new Product();
        product.setNom("");
        product.setReference("REF-001");
        product.setQuantite(10);
        product.setPrix(1000.0);
        product.setDatePeremption(new Date());
        product.setEmplacement("Rayon A");
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateProduct(product)
        );
        
        assertEquals("Le nom du produit est obligatoire", exception.getMessage());
    }
    
    @Test
    void testValidateProduct_WithNegativePrice_ShouldThrow() {
        Product product = new Product();
        product.setNom("Test");
        product.setReference("REF-001");
        product.setQuantite(10);
        product.setPrix(-100.0);
        product.setDatePeremption(new Date());
        product.setEmplacement("Rayon A");
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateProduct(product)
        );
        
        assertEquals("Le prix doit être positif", exception.getMessage());
    }
    
    @Test
    void testValidateClient_WithValidClient_ShouldNotThrow() {
        Client client = new Client();
        client.setNom("Dupont");
        client.setPrenom("Jean");
        client.setTelephone("12345678");
        client.setAge(30);
        
        assertDoesNotThrow(() -> ValidationUtil.validateClient(client));
    }
    
    @Test
    void testValidateClient_WithInvalidPhone_ShouldThrow() {
        Client client = new Client();
        client.setNom("Dupont");
        client.setPrenom("Jean");
        client.setTelephone("123"); // Trop court
        client.setAge(30);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateClient(client)
        );
        
        assertEquals("Le numéro de téléphone doit contenir au moins 8 chiffres", exception.getMessage());
    }
    
    @Test
    void testValidateClient_WithInvalidAge_ShouldThrow() {
        Client client = new Client();
        client.setNom("Dupont");
        client.setPrenom("Jean");
        client.setTelephone("12345678");
        client.setAge(200); // Trop âgé
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateClient(client)
        );
        
        assertEquals("L'âge doit être entre 0 et 150 ans", exception.getMessage());
    }
    
    @Test
    void testIsValidPhoneNumber() {
        assertTrue(ValidationUtil.isValidPhoneNumber("12345678"));
        assertTrue(ValidationUtil.isValidPhoneNumber("+237 12 34 56 78"));
        assertTrue(ValidationUtil.isValidPhoneNumber("(123) 456-7890"));
        
        assertFalse(ValidationUtil.isValidPhoneNumber("1234567")); // 7 chiffres
        assertFalse(ValidationUtil.isValidPhoneNumber("abc"));
        assertFalse(ValidationUtil.isValidPhoneNumber(null));
    }
}
