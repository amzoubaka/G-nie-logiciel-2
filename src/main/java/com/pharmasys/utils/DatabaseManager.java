package com.pharmasys.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Gestionnaire de la connexion à la base de données SQLite
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:pharmasys.db";
    private static Connection connection;
    
    /**
     * Initialise la base de données et crée les tables si nécessaire
     */
    public static void initialize() {
        try {
            // Charger le driver JDBC SQLite
            Class.forName("org.sqlite.JDBC");
            
            // Établir la connexion
            connection = DriverManager.getConnection(DB_URL);
            
            System.out.println("Connexion à la base de données établie.");
            
            // Créer les tables
            createTables();
            
            // Insérer les données initiales si la base est vide
            initializeData();
            
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC SQLite non trouvé: " + e.getMessage());
            // e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données: " + e.getMessage());
            // e.printStackTrace();
        }
    }
    
    /**
     * Retourne la connexion active
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }
    
    /**
     * Crée les tables de la base de données
     */
    private static void createTables() {
        try (InputStream is = DatabaseManager.class.getResourceAsStream("/db/schema.sql")) {
            if (is == null) {
                System.err.println("Fichier schema.sql non trouvé");
                return;
            }
            
            Scanner scanner = new Scanner(is).useDelimiter(";");
            Statement stmt = connection.createStatement();
            
            while (scanner.hasNext()) {
                String sql = scanner.next().trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }
            
            scanner.close();
            stmt.close();
            
            System.out.println("Tables créées avec succès.");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la création des tables: " + e.getMessage());
            // e.printStackTrace();
        }
    }
    
    /**
     * Insère les données initiales
     */
    private static void initializeData() {
        try {
            // Vérifier si des utilisateurs existent déjà
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            
            if (rs.next() && rs.getInt("count") == 0) {
                // Insérer les utilisateurs par défaut
                String insertUser = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(insertUser);
                
                // Admin (mot de passe: admin123)
                pstmt.setString(1, "admin");
                pstmt.setString(2, PasswordUtil.hashPassword("admin123"));
                pstmt.setString(3, "admin");
                pstmt.executeUpdate();
                
                // Pharmacien (mot de passe: pharma123)
                pstmt.setString(1, "pharmacien1");
                pstmt.setString(2, PasswordUtil.hashPassword("pharma123"));
                pstmt.setString(3, "pharmacien");
                pstmt.executeUpdate();
                
                // Utilisateur (mot de passe: user123)
                pstmt.setString(1, "user1");
                pstmt.setString(2, PasswordUtil.hashPassword("user123"));
                pstmt.setString(3, "utilisateur");
                pstmt.executeUpdate();
                
                pstmt.close();
                
                System.out.println("Utilisateurs par défaut créés.");
                
                // Insérer quelques produits de test
                String insertProduct = "INSERT INTO products (nom, reference, quantite, prix, date_peremption, emplacement, created_by) VALUES (?, ?, ?, ?, ?, ?, 1)";
                pstmt = connection.prepareStatement(insertProduct);
                
                pstmt.setString(1, "Paracétamol 500mg");
                pstmt.setString(2, "PAR-500");
                pstmt.setInt(3, 100);
                pstmt.setDouble(4, 2500.00);
                pstmt.setDate(5, new java.sql.Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000));
                pstmt.setString(6, "Rayon A");
                pstmt.executeUpdate();
                
                pstmt.setString(1, "Paracétamol 250mg");
                pstmt.setString(2, "PAR-250");
                pstmt.setInt(3, 150);
                pstmt.setDouble(4, 1000.00);
                pstmt.setDate(5, new java.sql.Date(System.currentTimeMillis() + 180L * 24 * 60 * 60 * 1000));
                pstmt.setString(6, "Rayon A");
                pstmt.executeUpdate();
                
                pstmt.setString(1, "Aspirine 100mg");
                pstmt.setString(2, "ASP-100");
                pstmt.setInt(3, 80);
                pstmt.setDouble(4, 1500.00);
                pstmt.setDate(5, new java.sql.Date(System.currentTimeMillis() + 60L * 24 * 60 * 60 * 1000));
                pstmt.setString(6, "Rayon B");
                pstmt.executeUpdate();
                
                pstmt.close();
                
                System.out.println("Produits de test créés.");
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation des données: " + e.getMessage());
            // e.printStackTrace();
        }
    }
    
    /**
     * Ferme la connexion à la base de données
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        }
    }
}
