package com.pharmasys.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.pharmasys.utils.DatabaseManager;

/**
 * Service de journalisation
 */
public class LogService {
    
    /**
     * Enregistre une action dans les logs
     */
    public void log(int userId, String action, String details) {
        String sql = "INSERT INTO system_logs (user_id, action, details) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, action);
            pstmt.setString(3, details);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            // Ne pas propager l'erreur de logging
            System.err.println("Erreur lors de la journalisation: " + e.getMessage());
        }
    }
}
