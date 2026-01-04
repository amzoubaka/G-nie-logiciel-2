package com.pharmasys.services;

import java.sql.SQLException;
import java.util.List;

import com.pharmasys.dao.ProductDAO;
import com.pharmasys.models.Product;
import com.pharmasys.utils.SessionManager;
import com.pharmasys.utils.ValidationException;
import com.pharmasys.utils.ValidationUtil;

/**
 * Service de gestion des produits
 */
public class ProductService {
    private final ProductDAO productDAO;
    private final LogService logService;
    
    public ProductService() {
        this.productDAO = new ProductDAO();
        this.logService = new LogService();
    }
    
    /**
     * Crée un nouveau produit
     */
    public int createProduct(Product product) throws ValidationException, SQLException {
        // Valider le produit
        ValidationUtil.validateProduct(product);
        
        // Vérifier l'unicité de la référence
        Product existing = productDAO.findByReference(product.getReference());
        if (existing != null) {
            throw new ValidationException("La référence " + product.getReference() + " existe déjà");
        }
        
        // Définir le créateur
        product.setCreatedBy(SessionManager.getCurrentUser().getId());
        
        // Insérer le produit
        int productId = productDAO.insert(product);
        
        // Logger l'action
        logService.log(product.getCreatedBy(), "CREATE_PRODUCT", 
                      "Produit créé: " + product.getNom() + " (" + product.getReference() + ")");
        
        return productId;
    }
    
    /**
     * Met à jour un produit
     */
    public void updateProduct(Product product) throws ValidationException, SQLException {
        ValidationUtil.validateProduct(product);
        
        // Vérifier l'unicité de la référence (sauf pour ce produit)
        if (productDAO.existsByReference(product.getReference(), product.getId())) {
            throw new ValidationException("La référence " + product.getReference() + " existe déjà");
        }
        
        productDAO.update(product);
        
        logService.log(SessionManager.getCurrentUser().getId(), "UPDATE_PRODUCT",
                      "Produit modifié: " + product.getNom());
    }
    
    /**
     * Supprime un produit
     */
    public void deleteProduct(int productId) throws SQLException {
        Product product = productDAO.findById(productId);
        if (product != null) {
            productDAO.delete(productId);
            
            logService.log(SessionManager.getCurrentUser().getId(), "DELETE_PRODUCT",
                          "Produit supprimé: " + product.getNom());
        }
    }
    
    /**
     * Recherche des produits
     */
    public List<Product> searchProducts(String query) throws SQLException {
        if (query == null || query.trim().isEmpty()) {
            return productDAO.findAll();
        }
        return productDAO.search(query);
    }
    
    /**
     * Retourne tous les produits
     */
    public List<Product> getAllProducts() throws SQLException {
        return productDAO.findAll();
    }
    
    /**
     * Retourne les produits en stock faible
     */
    public List<Product> getLowStockProducts(int threshold) throws SQLException {
        return productDAO.findLowStock(threshold);
    }
    
    /**
     * Retourne les produits proches de la péremption
     */
    public List<Product> getExpiringProducts(int daysBeforeExpiry) throws SQLException {
        return productDAO.findExpiringSoon(daysBeforeExpiry);
    }
    
    /**
     * Trouve un produit par ID
     */
    public Product getProductById(int productId) throws SQLException {
        return productDAO.findById(productId);
    }
}
