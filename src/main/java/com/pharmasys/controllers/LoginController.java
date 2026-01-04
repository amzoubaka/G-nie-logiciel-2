package com.pharmasys.controllers;

import com.pharmasys.models.User;
import com.pharmasys.services.AuthService;
import com.pharmasys.views.ViewManager;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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
        view = new VBox();
        view.getStyleClass().add("login-container");
        view.setAlignment(Pos.CENTER);
        
        VBox loginBox = new VBox(20);
        loginBox.getStyleClass().add("login-box");
        loginBox.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("PharmaSys");
        titleLabel.getStyleClass().add("login-title");
        
        Label subtitleLabel = new Label("Système de Gestion de Pharmacie");
        subtitleLabel.getStyleClass().add("login-subtitle");
        
        VBox formBox = new VBox(15);
        formBox.setMaxWidth(350);
        
        Label usernameLabel = new Label("Nom d'utilisateur:");
        usernameLabel.getStyleClass().add("form-label");
        usernameField = new TextField();
        usernameField.setPromptText("Entrez votre nom d'utilisateur");
        
        Label passwordLabel = new Label("Mot de passe:");
        passwordLabel.getStyleClass().add("form-label");
        passwordField = new PasswordField();
        passwordField.setPromptText("Entrez votre mot de passe");
        
        errorLabel = new Label("");
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);
        
        Button loginButton = new Button("Se connecter");
        loginButton.getStyleClass().add("login-button");
        loginButton.setOnAction(e -> handleLogin());
        
        
        passwordField.setOnAction(e -> handleLogin());
        
        formBox.getChildren().addAll(
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            errorLabel,
            loginButton
        );
        
        Label infoLabel = new Label("Comptes de test:\nadmin / admin123\npharmacien1 / pharma123");
        infoLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: white;");
        infoLabel.setAlignment(Pos.CENTER);
        
        loginBox.getChildren().addAll(titleLabel, subtitleLabel, formBox);
        view.getChildren().addAll(loginBox, infoLabel);
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
