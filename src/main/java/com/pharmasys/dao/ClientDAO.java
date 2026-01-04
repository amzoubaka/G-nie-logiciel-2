package com.pharmasys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pharmasys.models.Client;
import com.pharmasys.utils.DatabaseManager;

/**
 * DAO pour la gestion des clients
 */
public class ClientDAO {
    
    public int insert(Client client) throws SQLException {
        String sql = "INSERT INTO clients (nom, prenom, quartier, age, telephone) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getQuartier());
            pstmt.setInt(4, client.getAge());
            pstmt.setString(5, client.getTelephone());
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return -1;
    }
    
    public void update(Client client) throws SQLException {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, quartier = ?, age = ?, telephone = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getQuartier());
            pstmt.setInt(4, client.getAge());
            pstmt.setString(5, client.getTelephone());
            pstmt.setInt(6, client.getId());
            
            pstmt.executeUpdate();
        }
    }
    
    public Client findById(int id) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractClientFromResultSet(rs);
            }
        }
        
        return null;
    }
    
    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clients.add(extractClientFromResultSet(rs));
            }
        }
        
        return clients;
    }
    
    public List<Client> search(String query) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE LOWER(nom) LIKE ? OR LOWER(prenom) LIKE ? OR telephone LIKE ? ORDER BY nom";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + query.toLowerCase() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                clients.add(extractClientFromResultSet(rs));
            }
        }
        
        return clients;
    }
    
    private Client extractClientFromResultSet(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setQuartier(rs.getString("quartier"));
        client.setAge(rs.getInt("age"));
        client.setTelephone(rs.getString("telephone"));
        client.setCreatedAt(rs.getTimestamp("created_at"));
        
        return client;
    }
}
