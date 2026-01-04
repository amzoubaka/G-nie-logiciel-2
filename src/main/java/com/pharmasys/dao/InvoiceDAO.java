package com.pharmasys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pharmasys.models.Invoice;
import com.pharmasys.models.InvoiceItem;
import com.pharmasys.utils.DatabaseManager;

/**
 * DAO pour la gestion des factures dans la base de données
 */
public class InvoiceDAO {
    
    /**
     * Crée une nouvelle facture avec ses articles
     */
    public Invoice create(Invoice invoice, List<InvoiceItem> items) throws SQLException {
        Connection conn = DatabaseManager.getConnection();
        conn.setAutoCommit(false);
        
        try {
            // Insérer la facture
            String sql = "INSERT INTO invoices (invoice_number, client_id, user_id, total_amount, " +
                        "payment_method, notes, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, datetime('now'), datetime('now'))";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, invoice.getInvoiceNumber());
            stmt.setInt(2, invoice.getClientId());
            stmt.setInt(3, invoice.getUserId());
            stmt.setDouble(4, invoice.getTotalAmount());
            stmt.setString(5, invoice.getPaymentMethod());
            stmt.setString(6, invoice.getNotes());
            
            stmt.executeUpdate();
            
            // Get invoice ID using last_insert_rowid()
            try (Statement idStmt = conn.createStatement();
                 ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    invoice.setId(rs.getInt(1));
                }
            }
            stmt.close();
            
            // Insérer les articles de la facture
            String itemSql = "INSERT INTO invoice_items (invoice_id, product_id, quantity, " +
                            "unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
            
            PreparedStatement itemStmt = conn.prepareStatement(itemSql);
            
            for (InvoiceItem item : items) {
                itemStmt.setInt(1, invoice.getId());
                itemStmt.setInt(2, item.getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getUnitPrice());
                itemStmt.setDouble(5, item.getSubtotal());
                itemStmt.executeUpdate();
                
                // Get item ID
                try (Statement idStmt = conn.createStatement();
                     ResultSet itemRs = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (itemRs.next()) {
                        item.setId(itemRs.getInt(1));
                        item.setInvoiceId(invoice.getId());
                    }
                }
            }
            itemStmt.close();
            
            // Mettre à jour les quantités de produits
            String updateStockSql = "UPDATE products SET quantite = quantite - ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateStockSql);
            
            for (InvoiceItem item : items) {
                updateStmt.setInt(1, item.getQuantity());
                updateStmt.setInt(2, item.getProductId());
                updateStmt.executeUpdate();
            }
            
            conn.commit();
            return invoice;
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    /**
     * Récupère toutes les factures
     */
    public List<Invoice> findAll() throws SQLException {
        String sql = "SELECT * FROM invoices ORDER BY created_at DESC";
        
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            List<Invoice> invoices = new ArrayList<>();
            while (rs.next()) {
                invoices.add(extractInvoice(rs));
            }
            return invoices;
        }
    }
    
    /**
     * Recherche des factures par numéro, client ou date
     */
    public List<Invoice> search(String keyword) throws SQLException {
        String sql = "SELECT i.* FROM invoices i " +
                    "LEFT JOIN clients c ON i.client_id = c.id " +
                    "WHERE i.invoice_number LIKE ? OR c.full_name LIKE ? " +
                    "ORDER BY i.created_at DESC";
        
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            List<Invoice> invoices = new ArrayList<>();
            while (rs.next()) {
                invoices.add(extractInvoice(rs));
            }
            return invoices;
        }
    }
    
    /**
     * Récupère une facture par son ID
     */
    public Optional<Invoice> findById(int id) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE id = ?";
        
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(extractInvoice(rs));
            }
            return Optional.empty();
        }
    }
    
    /**
     * Récupère les articles d'une facture
     */
    public List<InvoiceItem> findItemsByInvoiceId(int invoiceId) throws SQLException {
        String sql = "SELECT ii.*, p.name as product_name FROM invoice_items ii " +
                    "JOIN products p ON ii.product_id = p.id " +
                    "WHERE ii.invoice_id = ?";
        
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            ResultSet rs = stmt.executeQuery();
            
            List<InvoiceItem> items = new ArrayList<>();
            while (rs.next()) {
                InvoiceItem item = new InvoiceItem();
                item.setId(rs.getInt("id"));
                item.setInvoiceId(rs.getInt("invoice_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getDouble("unit_price"));
                item.setSubtotal(rs.getDouble("subtotal"));
                items.add(item);
            }
            return items;
        }
    }
    
    /**
     * Récupère les factures d'un client
     */
    public List<Invoice> findByClientId(int clientId) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE client_id = ? ORDER BY created_at DESC";
        
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            List<Invoice> invoices = new ArrayList<>();
            while (rs.next()) {
                invoices.add(extractInvoice(rs));
            }
            return invoices;
        }
    }
    
    /**
     * Génère un nouveau numéro de facture
     */
    public String generateInvoiceNumber() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM invoices WHERE " +
                    "strftime('%Y-%m', created_at) = strftime('%Y-%m', 'now')";
        
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int count = rs.getInt("count") + 1;
                LocalDateTime now = LocalDateTime.now();
                return String.format("INV-%04d%02d-%04d", 
                    now.getYear(), now.getMonthValue(), count);
            }
            return "INV-000001";
        }
    }
    
    /**
     * Compte le nombre total de factures
     */
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM invoices";
        
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        }
    }
    
    /**
     * Calcule le chiffre d'affaires total
     */
    public double getTotalRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM invoices";
        
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0.0;
        }
    }
    
    /**
     * Calcule le chiffre d'affaires du mois en cours
     */
    public double getMonthlyRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM invoices " +
                    "WHERE strftime('%Y-%m', created_at) = strftime('%Y-%m', 'now')";
        
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0.0;
        }
    }
    
    /**
     * Extrait une facture depuis un ResultSet
     */
    private Invoice extractInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getInt("id"));
        invoice.setInvoiceNumber(rs.getString("invoice_number"));
        invoice.setClientId(rs.getInt("client_id"));
        invoice.setUserId(rs.getInt("user_id"));
        invoice.setTotalAmount(rs.getDouble("total_amount"));
        invoice.setPaymentMethod(rs.getString("payment_method"));
        invoice.setNotes(rs.getString("notes"));
        invoice.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        invoice.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        return invoice;
    }
}
