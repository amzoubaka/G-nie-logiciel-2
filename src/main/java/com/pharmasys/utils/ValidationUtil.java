package com.pharmasys.utils;

import com.pharmasys.models.Client;
import com.pharmasys.models.Product;

/**
 * Utilitaire de validation des données
 */
public class ValidationUtil {
    
    /**
     * Valide un produit
     * @throws ValidationException si le produit est invalide
     */
    public static void validateProduct(Product product) throws ValidationException {
        if (product.getNom() == null || product.getNom().trim().isEmpty()) {
            throw new ValidationException("Le nom du produit est obligatoire");
        }
        
        if (product.getReference() == null || product.getReference().trim().isEmpty()) {
            throw new ValidationException("La référence est obligatoire");
        }
        
        if (product.getQuantite() < 0) {
            throw new ValidationException("La quantité ne peut pas être négative");
        }
        
        if (product.getPrix() <= 0) {
            throw new ValidationException("Le prix doit être positif");
        }
        
        if (product.getDatePeremption() == null) {
            throw new ValidationException("La date de péremption est obligatoire");
        }
        
        if (product.getEmplacement() == null || product.getEmplacement().trim().isEmpty()) {
            throw new ValidationException("L'emplacement est obligatoire");
        }
    }
    
    /**
     * Valide un client
     * @throws ValidationException si le client est invalide
     */
    public static void validateClient(Client client) throws ValidationException {
        if (client.getNom() == null || client.getNom().trim().isEmpty()) {
            throw new ValidationException("Le nom est obligatoire");
        }
        
        if (client.getPrenom() == null || client.getPrenom().trim().isEmpty()) {
            throw new ValidationException("Le prénom est obligatoire");
        }
        
        if (client.getTelephone() == null || client.getTelephone().trim().isEmpty()) {
            throw new ValidationException("Le numéro de téléphone est obligatoire");
        }
        
        if (!isValidPhoneNumber(client.getTelephone())) {
            throw new ValidationException("Le numéro de téléphone doit contenir au moins 8 chiffres");
        }
        
        if (client.getAge() < 0 || client.getAge() > 150) {
            throw new ValidationException("L'âge doit être entre 0 et 150 ans");
        }
    }
    
    /**
     * Valide un numéro de téléphone
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null) return false;
        String digits = phone.replaceAll("\\D", "");
        return digits.length() >= 8;
    }
    
    /**
     * Valide un mot de passe
     */
    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.length() < 6) {
            throw new ValidationException("Le mot de passe doit contenir au moins 6 caractères");
        }
    }
}
