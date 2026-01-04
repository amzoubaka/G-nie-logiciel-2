package com.pharmasys;

import com.pharmasys.utils.DatabaseManager;
import com.pharmasys.views.ViewManager;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Point d'entrée principal de l'application PharmaSys
 * 
 * @author Équipe PharmaSys
 * @version 1.0.0
 */
public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialiser la base de données
            DatabaseManager.initialize();
            
            // Configurer et afficher la fenêtre principale
            primaryStage.setTitle("PharmaSys - Système de Gestion de Pharmacie");
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);
            
            // Afficher l'écran de connexion
            ViewManager.showLoginView(primaryStage);
            primaryStage.show();
            
        } catch (Exception e) {
            System.err.println("Erreur au démarrage de l'application: " + e.getMessage());
            System.exit(1);
        }
    }
    
    @Override
    public void stop() {
        // Fermer les connexions à la base de données
        DatabaseManager.closeConnection();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
