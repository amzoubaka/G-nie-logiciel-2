package com.pharmasys.views;

import com.pharmasys.controllers.DashboardController;
import com.pharmasys.controllers.LoginController;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Gestionnaire de navigation entre les vues
 */
public class ViewManager {
    private static Stage primaryStage;
    
    /**
     * Affiche la vue de connexion (without Stage parameter)
     */
    public static void showLoginView() {
        if (primaryStage != null) {
            showLoginView(primaryStage);
        }
    }
    
    /**
     * Affiche la vue de connexion
     */
    public static void showLoginView(Stage stage) {
        primaryStage = stage;
        
        try {
            LoginController controller = new LoginController();
            Scene scene = new Scene(controller.getView(), 600, 500);
            scene.getStylesheets().add(
                ViewManager.class.getResource("/styles.css").toExternalForm()
            );
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("PharmaSys - Connexion");
            primaryStage.setResizable(false);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la vue de connexion");
        }
    }
    
    /**
     * Affiche le tableau de bord principal
     */
    public static void showDashboard() {
        try {
            DashboardController controller = new DashboardController();
            Scene scene = new Scene(controller.getView(), 1200, 800);
            scene.getStylesheets().add(
                ViewManager.class.getResource("/styles.css").toExternalForm()
            );
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("PharmaSys - Tableau de Bord");
            primaryStage.setResizable(true);
            primaryStage.setMaximized(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement du tableau de bord");
        }
    }
    
    /**
     * Retourne le stage principal
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
