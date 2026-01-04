package com.pharmasys.controllers;

import com.pharmasys.models.User;
import com.pharmasys.services.AuthService;
import com.pharmasys.views.ViewManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Contrôleur pour l'interface de connexion
 */
public class LoginController {
    private final AuthService authService;
    private VBox view;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;
    
    public LoginController() {
        this.authService = new AuthService();
        createView();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(40));
        view.setStyle("-fx-background-color: #f5f5f5;");
        
        // Titre
        Label titleLabel = new Label("PharmaSys");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2196F3;");
        
        Label subtitleLabel = new Label("Système de Gestion de Pharmacie");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setStyle("-fx-text-fill: #666;");
        
        // Formulaire
        VBox formBox = new VBox(15);
        formBox.setMaxWidth(300);
        formBox.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-background-radius: 10;");
        formBox.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.rgb(0, 0, 0, 0.1)));
        
        // Nom d'utilisateur
        Label usernameLabel = new Label("Nom d'utilisateur:");
        usernameLabel.setFont(Font.font("Arial", 12));
        usernameField = new TextField();
        usernameField.setPromptText("Entrez votre nom d'utilisateur");
        usernameField.setPrefHeight(35);
        
        // Mot de passe
        Label passwordLabel = new Label("Mot de passe:");
        passwordLabel.setFont(Font.font("Arial", 12));
        passwordField = new PasswordField();
        passwordField.setPromptText("Entrez votre mot de passe");
        passwordField.setPrefHeight(35);
        
        // Message d'erreur
        errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 11px;");
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);
        
        // Bouton de connexion
        Button loginButton = new Button("Se connecter");
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(40);
        loginButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        loginButton.setOnAction(e -> handleLogin());
        
        // Appuyer sur Entrée pour se connecter
        passwordField.setOnAction(e -> handleLogin());
        
        formBox.getChildren().addAll(
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            errorLabel,
            loginButton
        );
        
        // Info comptes de test
        Label infoLabel = new Label("Comptes de test:\nadmin / admin123\npharmacien1 / pharma123");
        infoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #999;");
        infoLabel.setAlignment(Pos.CENTER);
        
        view.getChildren().addAll(titleLabel, subtitleLabel, formBox, infoLabel);
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Validation basique
        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        // Tentative d'authentification
        User user = authService.authenticate(username, password);
        
        if (user != null) {
            // Connexion réussie
            ViewManager.showDashboard();
        } else {
            // Connexion échouée
            showError("Nom d'utilisateur ou mot de passe incorrect");
            passwordField.clear();
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    public Parent getView() {
        return view;
    }
}
