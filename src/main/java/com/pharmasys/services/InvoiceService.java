package com.pharmasys.services;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.pharmasys.dao.InvoiceDAO;
import com.pharmasys.dao.ProductDAO;
import com.pharmasys.models.Invoice;
import com.pharmasys.models.InvoiceItem;
import com.pharmasys.models.Product;
import com.pharmasys.utils.ValidationException;

/**
 * Service pour la gestion de la logique métier des factures
 */
public class InvoiceService {
    
    private final InvoiceDAO invoiceDAO;
    private final ProductDAO productDAO;
    private final LogService logService;
    
    public InvoiceService() {
        this.invoiceDAO = new InvoiceDAO();
        this.productDAO = new ProductDAO();
        this.logService = new LogService();
    }
    
    /**
     * Crée une nouvelle facture avec validation
     */
    public Invoice createInvoice(int clientId, int userId, List<InvoiceItem> items, 
                                 String paymentMethod, String notes) throws SQLException, ValidationException {
        
        // Validation
        if (items == null || items.isEmpty()) {
            throw new ValidationException("La facture doit contenir au moins un article");
        }
        
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new ValidationException("Le mode de paiement est requis");
        }
        
        // Vérifier la disponibilité des produits
        for (InvoiceItem item : items) {
            Product product = productDAO.findById(item.getProductId());
            if (product == null) {
                throw new ValidationException("Produit non trouvé: ID " + item.getProductId());
            }
            
            if (product.getQuantity() < item.getQuantity()) {
                throw new ValidationException(
                    String.format("Stock insuffisant pour %s. Disponible: %d, Demandé: %d",
                        product.getName(), product.getQuantity(), item.getQuantity())
                );
            }
            
            // Calculer le sous-total
            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice() * item.getQuantity());
        }
        
        // Calculer le montant total
        double totalAmount = items.stream()
            .mapToDouble(InvoiceItem::getSubtotal)
            .sum();
        
        // Générer le numéro de facture
        String invoiceNumber = invoiceDAO.generateInvoiceNumber();
        
        // Créer la facture
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setClientId(clientId);
        invoice.setUserId(userId);
        invoice.setTotalAmount(totalAmount);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setNotes(notes);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        
        // Enregistrer dans la base de données
        Invoice createdInvoice = invoiceDAO.create(invoice, items);
        
        // Logger l'action
        logService.log(userId, "CREATE_INVOICE", 
            "Création facture: " + invoiceNumber + " - Montant: " + totalAmount + " FCFA");
        
        return createdInvoice;
    }
    
    /**
     * Récupère toutes les factures
     */
    public List<Invoice> getAllInvoices() throws SQLException {
        return invoiceDAO.findAll();
    }
    
    /**
     * Recherche des factures
     */
    public List<Invoice> searchInvoices(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllInvoices();
        }
        return invoiceDAO.search(keyword);
    }
    
    /**
     * Récupère une facture par ID
     */
    public Optional<Invoice> getInvoiceById(int id) throws SQLException {
        return invoiceDAO.findById(id);
    }
    
    /**
     * Récupère les articles d'une facture
     */
    public List<InvoiceItem> getInvoiceItems(int invoiceId) throws SQLException {
        return invoiceDAO.findItemsByInvoiceId(invoiceId);
    }
    
    /**
     * Récupère les factures d'un client
     */
    public List<Invoice> getClientInvoices(int clientId) throws SQLException {
        return invoiceDAO.findByClientId(clientId);
    }
    
    /**
     * Compte le nombre total de factures
     */
    public int getTotalInvoiceCount() throws SQLException {
        return invoiceDAO.countAll();
    }
    
    /**
     * Obtient le chiffre d'affaires total
     */
    public double getTotalRevenue() throws SQLException {
        return invoiceDAO.getTotalRevenue();
    }
    
    /**
     * Obtient le chiffre d'affaires mensuel
     */
    public double getMonthlyRevenue() throws SQLException {
        return invoiceDAO.getMonthlyRevenue();
    }
}
