package com.pharmasys.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.pharmasys.models.Product;
import com.pharmasys.utils.DatabaseManager;

/**
 * DAO pour la gestion des produits
 */
public class ProductDAO {
    
    /**
     * Insère un nouveau produit
     */
    public int insert(Product product) throws SQLException {
        String sql = "INSERT INTO products (nom, reference, quantite, prix, date_peremption, validite, emplacement, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        try {
            pstmt.setString(1, product.getNom());
            pstmt.setString(2, product.getReference());
            pstmt.setInt(3, product.getQuantite());
            pstmt.setDouble(4, product.getPrix());
            pstmt.setDate(5, new java.sql.Date(product.getDatePeremption().getTime()));
            
            if (product.getValidite() != null) {
                pstmt.setDate(6, new java.sql.Date(product.getValidite().getTime()));
            } else {
                pstmt.setNull(6, Types.DATE);
            }
            
            pstmt.setString(7, product.getEmplacement());
            pstmt.setInt(8, product.getCreatedBy());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } finally {
            if (pstmt != null) pstmt.close();
        }
        
        return -1;
    }
    
    /**
     * Met à jour un produit existant
     */
    public void update(Product product) throws SQLException {
        String sql = "UPDATE products SET nom = ?, reference = ?, quantite = ?, prix = ?, " +
                     "date_peremption = ?, validite = ?, emplacement = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getNom());
            pstmt.setString(2, product.getReference());
            pstmt.setInt(3, product.getQuantite());
            pstmt.setDouble(4, product.getPrix());
            pstmt.setDate(5, new java.sql.Date(product.getDatePeremption().getTime()));
            
            if (product.getValidite() != null) {
                pstmt.setDate(6, new java.sql.Date(product.getValidite().getTime()));
            } else {
                pstmt.setNull(6, Types.DATE);
            }
            
            pstmt.setString(7, product.getEmplacement());
            pstmt.setInt(8, product.getId());
            
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Supprime un produit
     */
    public void delete(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Trouve un produit par son ID
     */
    public Product findById(int productId) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractProductFromResultSet(rs);
            }
        }
        
        return null;
    }
    
    /**
     * Trouve un produit par sa référence
     */
    public Product findByReference(String reference) throws SQLException {
        String sql = "SELECT * FROM products WHERE reference = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reference);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractProductFromResultSet(rs);
            }
        }
        
        return null;
    }
    
    /**
     * Retourne tous les produits
     */
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY nom";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        }
        
        return products;
    }
    
    /**
     * Recherche des produits par nom ou référence
     */
    public List<Product> search(String query) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(nom) LIKE ? OR LOWER(reference) LIKE ? ORDER BY nom";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + query.toLowerCase() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        }
        
        return products;
    }
    
    /**
     * Retourne les produits en stock faible
     */
    public List<Product> findLowStock(int threshold) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE quantite < ? ORDER BY quantite";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, threshold);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        }
        
        return products;
    }
    
    /**
     * Retourne les produits proches de la péremption
     */
    public List<Product> findExpiringSoon(int daysBeforeExpiry) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE julianday(date_peremption) - julianday('now') < ? ORDER BY date_peremption";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, daysBeforeExpiry);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        }
        
        return products;
    }
    
    /**
     * Vérifie si une référence existe déjà
     */
    public boolean existsByReference(String reference, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE reference = ? AND id != ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reference);
            pstmt.setInt(2, excludeId);
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    
    /**
     * Extrait un produit d'un ResultSet
     */
    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setNom(rs.getString("nom"));
        product.setReference(rs.getString("reference"));
        product.setQuantite(rs.getInt("quantite"));
        product.setPrix(rs.getDouble("prix"));
        product.setDatePeremption(rs.getDate("date_peremption"));
        
        Date validite = rs.getDate("validite");
        if (validite != null) {
            product.setValidite(validite);
        }
        
        product.setEmplacement(rs.getString("emplacement"));
        product.setCreatedBy(rs.getInt("created_by"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return product;
    }
}
