package com.pharmasys.controllers;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.pharmasys.models.Client;
import com.pharmasys.models.Product;
import com.pharmasys.models.User;
import com.pharmasys.services.AuthService;
import com.pharmasys.services.ClientService;
import com.pharmasys.services.ProductService;
import com.pharmasys.utils.SessionManager;
import com.pharmasys.utils.ValidationException;
import com.pharmasys.views.ViewManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Contrôleur pour le tableau de bord principal
 */
public class DashboardController {
    private BorderPane view;
    final private ProductService productService;
    final private ClientService clientService;
    final private AuthService authService;
    private Label userLabel;
    private TabPane tabPane;
    
    public DashboardController() {
        this.productService = new ProductService();
        this.clientService = new ClientService();
        this.authService = new AuthService();
        createView();
    }
    
    private void createView() {
        view = new BorderPane();
        view.setStyle("-fx-background-color: #f0f0f0;");
        
        // Header
        view.setTop(createHeader());
        
        // Content with tabs
        view.setCenter(createContent());
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #2196F3;");
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("PharmaSys");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        User currentUser = SessionManager.getCurrentUser();
        userLabel = new Label("Utilisateur: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        userLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        
        Button logoutButton = new Button("Déconnexion");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> handleLogout());
        
        header.getChildren().addAll(titleLabel, spacer, userLabel, logoutButton);
        
        return header;
    }
    
    private TabPane createContent() {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Onglet Produits
        Tab productsTab = new Tab("Produits");
        productsTab.setContent(createProductsView());
        
        // Onglet Clients
        Tab clientsTab = new Tab("Clients");
        clientsTab.setContent(createClientsView());
        
        // Onglet Factures (simplifié)
        Tab invoicesTab = new Tab("Factures");
        invoicesTab.setContent(createInvoicesPlaceholder());
        
        tabPane.getTabs().addAll(productsTab, clientsTab, invoicesTab);
        
        return tabPane;
    }
    
    // ========== GESTION DES PRODUITS ==========
    
    private VBox createProductsView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        // Barre de recherche et actions
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un produit...");
        searchField.setPrefWidth(300);
        
        Button searchButton = new Button("Rechercher");
        searchButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button addButton = new Button("+ Ajouter un produit");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        searchBox.getChildren().addAll(searchField, searchButton, spacer, addButton);
        
        // TableView des produits
        TableView<Product> productTable = new TableView<>();
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Product, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomCol.setPrefWidth(200);
        
        TableColumn<Product, String> refCol = new TableColumn<>("Référence");
        refCol.setCellValueFactory(new PropertyValueFactory<>("reference"));
        refCol.setPrefWidth(120);
        
        TableColumn<Product, Integer> qteCol = new TableColumn<>("Quantité");
        qteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        qteCol.setPrefWidth(80);
        
        TableColumn<Product, Double> prixCol = new TableColumn<>("Prix (FCFA)");
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        prixCol.setPrefWidth(100);
        
        TableColumn<Product, Date> peremptionCol = new TableColumn<>("Date Péremption");
        peremptionCol.setCellValueFactory(new PropertyValueFactory<>("datePeremption"));
        peremptionCol.setPrefWidth(120);
        peremptionCol.setCellFactory(column -> new TableCell<Product, Date>() {
            private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(format.format(item));
                }
            }
        });
        
        TableColumn<Product, String> emplacementCol = new TableColumn<>("Emplacement");
        emplacementCol.setCellValueFactory(new PropertyValueFactory<>("emplacement"));
        emplacementCol.setPrefWidth(100);
        
        TableColumn<Product, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Modifier");
            private final Button deleteBtn = new Button("Supprimer");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);
            
            {
                editBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 10px;");
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 10px;");
                
                editBtn.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleEditProduct(product, productTable);
                });
                
                deleteBtn.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleDeleteProduct(product, productTable);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
        
        productTable.getColumns().addAll(nomCol, refCol, qteCol, prixCol, peremptionCol, emplacementCol, actionsCol);
        
        // Charger les produits
        loadProducts(productTable);
        
        // Actions des boutons
        searchButton.setOnAction(e -> searchProducts(searchField.getText(), productTable));
        searchField.setOnAction(e -> searchProducts(searchField.getText(), productTable));
        addButton.setOnAction(e -> handleAddProduct(productTable));
        
        container.getChildren().addAll(searchBox, productTable);
        VBox.setVgrow(productTable, Priority.ALWAYS);
        
        return container;
    }
    
    private void loadProducts(TableView<Product> table) {
        try {
            List<Product> products = productService.getAllProducts();
            ObservableList<Product> data = FXCollections.observableArrayList(products);
            table.setItems(data);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des produits: " + e.getMessage());
        }
    }
    
    private void searchProducts(String query, TableView<Product> table) {
        try {
            List<Product> products = productService.searchProducts(query);
            ObservableList<Product> data = FXCollections.observableArrayList(products);
            table.setItems(data);
        } catch (SQLException e) {
            showError("Erreur lors de la recherche: " + e.getMessage());
        }
    }
    
    private void handleAddProduct(TableView<Product> table) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un produit");
        dialog.setHeaderText("Nouveau produit");
        
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = createProductForm(null);
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return extractProductFromForm(grid, null);
            }
            return null;
        });
        
        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> {
            try {
                productService.createProduct(product);
                showInfo("Produit ajouté avec succès");
                loadProducts(table);
            } catch (ValidationException | SQLException e) {
                showError(e.getMessage());
            }
        });
    }
    
    private void handleEditProduct(Product product, TableView<Product> table) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Modifier un produit");
        dialog.setHeaderText("Modifier: " + product.getNom());
        
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = createProductForm(product);
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return extractProductFromForm(grid, product);
            }
            return null;
        });
        
        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(updatedProduct -> {
            try {
                productService.updateProduct(updatedProduct);
                showInfo("Produit modifié avec succès");
                loadProducts(table);
            } catch (ValidationException | SQLException e) {
                showError(e.getMessage());
            }
        });
    }
    
    private void handleDeleteProduct(Product product, TableView<Product> table) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le produit");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer " + product.getNom() + " ?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                productService.deleteProduct(product.getId());
                showInfo("Produit supprimé avec succès");
                loadProducts(table);
            } catch (SQLException e) {
                showError("Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }
    
    private GridPane createProductForm(Product product) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nomField = new TextField(product != null ? product.getNom() : "");
        TextField refField = new TextField(product != null ? product.getReference() : "");
        TextField qteField = new TextField(product != null ? String.valueOf(product.getQuantite()) : "0");
        TextField prixField = new TextField(product != null ? String.valueOf(product.getPrix()) : "0.0");
        DatePicker peremptionPicker = new DatePicker();
        if (product != null && product.getDatePeremption() != null) {
            peremptionPicker.setValue(new java.sql.Date(product.getDatePeremption().getTime()).toLocalDate());
        }
        TextField emplacementField = new TextField(product != null ? product.getEmplacement() : "");
        
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Référence:"), 0, 1);
        grid.add(refField, 1, 1);
        grid.add(new Label("Quantité:"), 0, 2);
        grid.add(qteField, 1, 2);
        grid.add(new Label("Prix (FCFA):"), 0, 3);
        grid.add(prixField, 1, 3);
        grid.add(new Label("Date péremption:"), 0, 4);
        grid.add(peremptionPicker, 1, 4);
        grid.add(new Label("Emplacement:"), 0, 5);
        grid.add(emplacementField, 1, 5);
        
        // Store fields in grid properties for later retrieval
        grid.getProperties().put("nomField", nomField);
        grid.getProperties().put("refField", refField);
        grid.getProperties().put("qteField", qteField);
        grid.getProperties().put("prixField", prixField);
        grid.getProperties().put("peremptionPicker", peremptionPicker);
        grid.getProperties().put("emplacementField", emplacementField);
        
        return grid;
    }
    
    private Product extractProductFromForm(GridPane grid, Product existing) {
        Product product = existing != null ? existing : new Product();
        
        TextField nomField = (TextField) grid.getProperties().get("nomField");
        TextField refField = (TextField) grid.getProperties().get("refField");
        TextField qteField = (TextField) grid.getProperties().get("qteField");
        TextField prixField = (TextField) grid.getProperties().get("prixField");
        DatePicker peremptionPicker = (DatePicker) grid.getProperties().get("peremptionPicker");
        TextField emplacementField = (TextField) grid.getProperties().get("emplacementField");
        
        product.setNom(nomField.getText());
        product.setReference(refField.getText());
        product.setQuantite(Integer.parseInt(qteField.getText()));
        product.setPrix(Double.parseDouble(prixField.getText()));
        
        if (peremptionPicker.getValue() != null) {
            java.time.LocalDate ld = peremptionPicker.getValue();
            product.setDatePeremption(java.sql.Date.valueOf(ld));
        }
        
        product.setEmplacement(emplacementField.getText());
        
        if (existing == null) {
            product.setCreatedBy(SessionManager.getCurrentUser().getId());
        }
        
        return product;
    }
    
    // ========== GESTION DES CLIENTS ==========
    
    private VBox createClientsView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un client...");
        searchField.setPrefWidth(300);
        
        Button searchButton = new Button("Rechercher");
        searchButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button addButton = new Button("+ Ajouter un client");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        searchBox.getChildren().addAll(searchField, searchButton, spacer, addButton);
        
        TableView<Client> clientTable = new TableView<>();
        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Client, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        
        TableColumn<Client, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        
        TableColumn<Client, String> quartierCol = new TableColumn<>("Quartier");
        quartierCol.setCellValueFactory(new PropertyValueFactory<>("quartier"));
        
        TableColumn<Client, Integer> ageCol = new TableColumn<>("Âge");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        
        TableColumn<Client, String> telCol = new TableColumn<>("Téléphone");
        telCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        
        clientTable.getColumns().addAll(nomCol, prenomCol, quartierCol, ageCol, telCol);
        
        loadClients(clientTable);
        
        searchButton.setOnAction(e -> searchClients(searchField.getText(), clientTable));
        searchField.setOnAction(e -> searchClients(searchField.getText(), clientTable));
        addButton.setOnAction(e -> handleAddClient(clientTable));
        
        container.getChildren().addAll(searchBox, clientTable);
        VBox.setVgrow(clientTable, Priority.ALWAYS);
        
        return container;
    }
    
    private void loadClients(TableView<Client> table) {
        try {
            List<Client> clients = clientService.getAllClients();
            ObservableList<Client> data = FXCollections.observableArrayList(clients);
            table.setItems(data);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des clients: " + e.getMessage());
        }
    }
    
    private void searchClients(String query, TableView<Client> table) {
        try {
            List<Client> clients = clientService.searchClients(query);
            ObservableList<Client> data = FXCollections.observableArrayList(clients);
            table.setItems(data);
        } catch (SQLException e) {
            showError("Erreur lors de la recherche: " + e.getMessage());
        }
    }
    
    private void handleAddClient(TableView<Client> table) {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un client");
        
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nomField = new TextField();
        TextField prenomField = new TextField();
        TextField quartierField = new TextField();
        TextField ageField = new TextField("0");
        TextField telField = new TextField();
        
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Quartier:"), 0, 2);
        grid.add(quartierField, 1, 2);
        grid.add(new Label("Âge:"), 0, 3);
        grid.add(ageField, 1, 3);
        grid.add(new Label("Téléphone:"), 0, 4);
        grid.add(telField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Client client = new Client();
                client.setNom(nomField.getText());
                client.setPrenom(prenomField.getText());
                client.setQuartier(quartierField.getText());
                client.setAge(Integer.parseInt(ageField.getText()));
                client.setTelephone(telField.getText());
                return client;
            }
            return null;
        });
        
        Optional<Client> result = dialog.showAndWait();
        result.ifPresent(client -> {
            try {
                clientService.createClient(client);
                showInfo("Client ajouté avec succès");
                loadClients(table);
            } catch (ValidationException | SQLException e) {
                showError(e.getMessage());
            }
        });
    }
    
    // ========== PLACEHOLDER FACTURES ==========
    
    private VBox createInvoicesPlaceholder() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        
        Label label = new Label("Module de facturation");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Label info = new Label("La fonctionnalité de facturation complète sera ajoutée ici.\nPour le moment, consultez les produits et clients disponibles.");
        info.setStyle("-fx-text-alignment: center;");
        
        container.getChildren().addAll(label, info);
        
        return container;
    }
    
    // ========== UTILITAIRES ==========
    
    private void handleLogout() {
        authService.logout();
        ViewManager.showLoginView(ViewManager.getPrimaryStage());
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public Parent getView() {
        return view;
    }
}
