package com.pharmasys.controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.pharmasys.models.Client;
import com.pharmasys.models.Invoice;
import com.pharmasys.models.InvoiceItem;
import com.pharmasys.models.Product;
import com.pharmasys.models.User;
import com.pharmasys.services.AuthService;
import com.pharmasys.services.ClientService;
import com.pharmasys.services.InvoiceService;
import com.pharmasys.services.ProductService;
import com.pharmasys.utils.SessionManager;
import com.pharmasys.views.ViewManager;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

public class DashboardController {
    private BorderPane view;
    private final ProductService productService;
    private final ClientService clientService;
    private final InvoiceService invoiceService;
    private final AuthService authService;
    private User currentUser;
    
    public DashboardController() {
        this.productService = new ProductService();
        this.clientService = new ClientService();
        this.invoiceService = new InvoiceService();
        this.authService = new AuthService();
        this.currentUser = SessionManager.getCurrentUser();
        createView();
    }
    
    private void createView() {
        view = new BorderPane();
        view.getStyleClass().add("root");
        view.setTop(createHeader());
        view.setCenter(createContent());
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.getStyleClass().add("header-container");
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(5);
        Label title = new Label("PharmaSys");
        title.getStyleClass().add("header-title");
        Label subtitle = new Label("Système de Gestion Pharmaceutique");
        subtitle.getStyleClass().add("header-subtitle");
        titleBox.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        VBox userBox = new VBox(5);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        Label userName = new Label(currentUser.getFullName());
        userName.getStyleClass().add("user-info-label");
        Label userRole = new Label(currentUser.getRole().toUpperCase());
        userRole.getStyleClass().addAll("role-badge", currentUser.getRole().toLowerCase());
        userBox.getChildren().addAll(userName, userRole);
        
        Button logoutBtn = new Button("Déconnexion");
        logoutBtn.getStyleClass().addAll("button", "logout-button");
        logoutBtn.setOnAction(e -> handleLogout());
        
        header.getChildren().addAll(titleBox, spacer, userBox, logoutBtn);
        return header;
    }
    
    private Parent createContent() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Admin gets all tabs
        if (currentUser.isAdmin()) {
            tabPane.getTabs().addAll(
                createStatisticsTab(),
                createProductsTab(),
                createClientsTab(),
                createInvoicesTab(),
                createUsersTab()
            );
        }
        // Pharmacien gets products, clients, invoices
        else if ("pharmacien".equalsIgnoreCase(currentUser.getRole())) {
            tabPane.getTabs().addAll(
                createProductsTab(),
                createClientsTab(),
                createInvoicesTab()
            );
        }
        // Regular user only gets read access
        else {
            tabPane.getTabs().addAll(
                createProductsTab(),
                createClientsTab()
            );
        }
        
        return tabPane;
    }
    
    private Tab createStatisticsTab() {
        Tab tab = new Tab("📊 Tableau de Bord");
        VBox content = new VBox(20);
        content.getStyleClass().add("padding-large");
        
        HBox headerBox = new HBox(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("Statistiques Générales");
        title.getStyleClass().add("section-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button refreshBtn = new Button("🔄 Actualiser");
        refreshBtn.getStyleClass().addAll("button", "secondary-button");
        
        headerBox.getChildren().addAll(title, spacer, refreshBtn);
        
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        
        refreshBtn.setOnAction(e -> {
            try {
                int productCount = productService.getAllProducts().size();
                int clientCount = clientService.getAllClients().size();
                int invoiceCount = invoiceService.getTotalInvoiceCount();
                double revenue = invoiceService.getMonthlyRevenue();
                
                statsRow.getChildren().clear();
                statsRow.getChildren().addAll(
                    createStatCard("Produits", String.valueOf(productCount), "primary"),
                    createStatCard("Clients", String.valueOf(clientCount), "success"),
                    createStatCard("Factures (mois)", String.valueOf(invoiceCount), "warning"),
                    createStatCard("CA Mensuel", String.format("%.0f FCFA", revenue), "danger")
                );
            } catch (SQLException ex) {
                showError("Erreur", "Impossible de charger les statistiques");
            }
        });
        
        try {
            int productCount = productService.getAllProducts().size();
            int clientCount = clientService.getAllClients().size();
            int invoiceCount = invoiceService.getTotalInvoiceCount();
            double revenue = invoiceService.getMonthlyRevenue();
            
            statsRow.getChildren().addAll(
                createStatCard("Produits", String.valueOf(productCount), "primary"),
                createStatCard("Clients", String.valueOf(clientCount), "success"),
                createStatCard("Factures (mois)", String.valueOf(invoiceCount), "warning"),
                createStatCard("CA Mensuel", String.format("%.0f FCFA", revenue), "danger")
            );
        } catch (SQLException e) {
            showError("Erreur", "Impossible de charger les statistiques");
        }
        
        content.getChildren().addAll(headerBox, statsRow);
        tab.setContent(new ScrollPane(content));
        return tab;
    }
    
    private VBox createStatCard(String label, String value, String type) {
        VBox card = new VBox(10);
        card.getStyleClass().addAll("stat-card", type);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");
        Label nameLabel = new Label(label);
        nameLabel.getStyleClass().add("stat-label");
        
        card.getChildren().addAll(valueLabel, nameLabel);
        return card;
    }
    
    private Tab createProductsTab() {
        Tab tab = new Tab("💊 Produits");
        VBox content = new VBox(15);
        content.getStyleClass().add("padding-medium");
        
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");
        toolbar.setAlignment(Pos.CENTER_LEFT);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un produit...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(300);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button refreshBtn = new Button("🔄 Actualiser");
        refreshBtn.getStyleClass().addAll("button", "secondary-button");
        
        Button addBtn = new Button("➕ Nouveau Produit");
        addBtn.getStyleClass().addAll("button", "success-button");
        
        // Only admin and pharmacien can add products
        addBtn.setVisible(currentUser.isAdmin() || "pharmacien".equalsIgnoreCase(currentUser.getRole()));
        
        toolbar.getChildren().addAll(searchField, spacer, refreshBtn, addBtn);
        
        TableView<Product> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Product, String> refCol = new TableColumn<>("Référence");
        refCol.setCellValueFactory(new PropertyValueFactory<>("reference"));
        
        TableColumn<Product, String> nameCol = new TableColumn<>("Nom");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Product, Double> priceCol = new TableColumn<>("Prix");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty ? null : String.format("%.0f FCFA", price));
            }
        });
        
        TableColumn<Product, Integer> qtyCol = new TableColumn<>("Stock");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        TableColumn<Product, String> expCol = new TableColumn<>("Expiration");
        expCol.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        expCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(date);
                    try {
                        LocalDate expDate = LocalDate.parse(date);
                        if (expDate.isBefore(LocalDate.now().plusMonths(3))) {
                            setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold;");
                        }
                    } catch (Exception ignored) {}
                }
            }
        });
        
        TableColumn<Product, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            
            {
                editBtn.getStyleClass().add("icon-button");
                deleteBtn.getStyleClass().add("icon-button");
                deleteBtn.setStyle("-fx-text-fill: #E74C3C;");
                
                editBtn.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    showProductDialog(product, table);
                });
                
                deleteBtn.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    deleteProduct(product, table);
                });
                
                // Only admin and pharmacien can edit/delete
                boolean canModify = currentUser.isAdmin() || "pharmacien".equalsIgnoreCase(currentUser.getRole());
                editBtn.setVisible(canModify);
                deleteBtn.setVisible(canModify);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editBtn, deleteBtn);
                    buttons.setAlignment(Pos.CENTER);
                    setGraphic(buttons);
                }
            }
        });
        
        table.getColumns().addAll(refCol, nameCol, priceCol, qtyCol, expCol, actionsCol);
        loadProducts(table);
        
        searchField.textProperty().addListener((obs, old, val) -> {
            try {
                List<Product> products = val.isEmpty() ? 
                    productService.getAllProducts() : 
                    productService.searchProducts(val);
                table.setItems(FXCollections.observableArrayList(products));
            } catch (SQLException e) {
                showError("Erreur", "Erreur de recherche");
            }
        });
                refreshBtn.setOnAction(e -> {
            searchField.clear();
            loadProducts(table);
        });
                refreshBtn.setOnAction(e -> {
            searchField.clear();
            loadProducts(table);
        });
        
        addBtn.setOnAction(e -> showProductDialog(null, table));
        
        VBox.setVgrow(table, Priority.ALWAYS);
        content.getChildren().addAll(toolbar, table);
        tab.setContent(content);
        return tab;
    }
    
    private Tab createClientsTab() {
        Tab tab = new Tab("👥 Clients");
        VBox content = new VBox(15);
        content.getStyleClass().add("padding-medium");
        
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");
        
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un client...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(300);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button refreshBtn = new Button("🔄 Actualiser");
        refreshBtn.getStyleClass().addAll("button", "secondary-button");
        
        Button addBtn = new Button("➕ Nouveau Client");
        addBtn.getStyleClass().addAll("button", "success-button");
        addBtn.setVisible(currentUser.isAdmin() || "pharmacien".equalsIgnoreCase(currentUser.getRole()));
        
        toolbar.getChildren().addAll(searchField, spacer, refreshBtn, addBtn);
        
        TableView<Client> table = new TableView<>();
        TableColumn<Client, String> nameCol = new TableColumn<>("Nom");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        TableColumn<Client, String> phoneCol = new TableColumn<>("Téléphone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        
        TableColumn<Client, Integer> ageCol = new TableColumn<>("Âge");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageCol.setPrefWidth(60);
        
        TableColumn<Client, String> addressCol = new TableColumn<>("Adresse");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        
        table.getColumns().addAll(nameCol, phoneCol, ageCol, addressCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadClients(table);
        
        searchField.textProperty().addListener((obs, old, val) -> {
            try {
                List<Client> clients = clientService.searchClients(val);
                table.setItems(FXCollections.observableArrayList(clients));
            } catch (SQLException e) {
                showError("Erreur", "Erreur de recherche");
            }
        });
        
        refreshBtn.setOnAction(e -> {
            searchField.clear();
            loadClients(table);
            showSuccess("Succès", "Liste des clients actualisée");
        });
        
        addBtn.setOnAction(e -> showClientDialog(table));
        
        VBox.setVgrow(table, Priority.ALWAYS);
        content.getChildren().addAll(toolbar, table);
        tab.setContent(content);
        return tab;
    }
    
    private Tab createInvoicesTab() {
        Tab tab = new Tab("🧾 Factures");
        VBox content = new VBox(15);
        content.getStyleClass().add("padding-medium");
        
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");
        
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher une facture...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(300);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button refreshBtn = new Button("🔄 Actualiser");
        refreshBtn.getStyleClass().addAll("button", "secondary-button");
        
        Button addBtn = new Button("➕ Nouvelle Facture");
        addBtn.getStyleClass().addAll("button", "success-button");
        addBtn.setOnAction(e -> showInvoiceDialog());
        
        toolbar.getChildren().addAll(searchField, spacer, refreshBtn, addBtn);
        
        TableView<Invoice> table = new TableView<>();
        TableColumn<Invoice, String> numCol = new TableColumn<>("N° Facture");
        numCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        
        TableColumn<Invoice, Double> amountCol = new TableColumn<>("Montant");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        amountCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty ? null : String.format("%.0f FCFA", amount));
            }
        });
        
        TableColumn<Invoice, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        
        table.getColumns().addAll(numCol, amountCol, dateCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadInvoices(table);
        
        refreshBtn.setOnAction(e -> {
            searchField.clear();
            loadInvoices(table);
        });
        
        VBox.setVgrow(table, Priority.ALWAYS);
        content.getChildren().addAll(toolbar, table);
        tab.setContent(content);
        return tab;
    }
    
    private Tab createUsersTab() {
        Tab tab = new Tab("👤 Utilisateurs");
        VBox content = new VBox(15);
        content.getStyleClass().add("padding-medium");
        
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");
        
        Label title = new Label("Gestion des utilisateurs du système");
        title.getStyleClass().add("section-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button refreshBtn = new Button("🔄 Actualiser");
        refreshBtn.getStyleClass().addAll("button", "secondary-button");
        
        toolbar.getChildren().addAll(title, spacer, refreshBtn);
        
        TableView<User> table = new TableView<>();
        TableColumn<User, String> usernameCol = new TableColumn<>("Nom d'utilisateur");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        TableColumn<User, String> roleCol = new TableColumn<>("Rôle");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        TableColumn<User, String> createdCol = new TableColumn<>("Créé le");
        createdCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        
        table.getColumns().addAll(usernameCol, roleCol, createdCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        try {
            List<User> users = authService.getAllUsers();
            table.setItems(FXCollections.observableArrayList(users));
        } catch (Exception e) {
            showError("Erreur", "Impossible de charger les utilisateurs");
        }
        
        refreshBtn.setOnAction(e -> {
            try {
                List<User> users = authService.getAllUsers();
                table.setItems(FXCollections.observableArrayList(users));
            } catch (Exception ex) {
                showError("Erreur", "Impossible de charger les utilisateurs");
            }
        });
        
        VBox.setVgrow(table, Priority.ALWAYS);
        content.getChildren().addAll(toolbar, table);
        tab.setContent(content);
        return tab;
    }
    
    private void showProductDialog(Product product, TableView<Product> table) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle(product == null ? "Nouveau Produit" : "Modifier Produit");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField refField = new TextField(product != null ? product.getReference() : "");
        TextField nameField = new TextField(product != null ? product.getName() : "");
        TextField priceField = new TextField(product != null ? String.valueOf(product.getPrice()) : "");
        TextField qtyField = new TextField(product != null ? String.valueOf(product.getQuantity()) : "");
        DatePicker expPicker = new DatePicker();
        
        grid.add(new Label("Référence:"), 0, 0);
        grid.add(refField, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Prix:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Quantité:"), 0, 3);
        grid.add(qtyField, 1, 3);
        grid.add(new Label("Expiration:"), 0, 4);
        grid.add(expPicker, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    if (product == null) {
                        Product newProduct = new Product();
                        newProduct.setReference(refField.getText());
                        newProduct.setName(nameField.getText());
                        newProduct.setPrice(Double.parseDouble(priceField.getText()));
                        newProduct.setQuantity(Integer.parseInt(qtyField.getText()));
                        productService.createProduct(newProduct);
                    } else {
                        product.setName(nameField.getText());
                        product.setPrice(Double.parseDouble(priceField.getText()));
                        product.setQuantity(Integer.parseInt(qtyField.getText()));
                        productService.updateProduct(product);
                    }
                    loadProducts(table);
                    showSuccess("Succès", "Produit enregistré");
                } catch (Exception e) {
                    showError("Erreur", e.getMessage());
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showClientDialog(TableView<Client> table) {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Nouveau Client");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        TextField ageField = new TextField("0");
        
        grid.add(new Label("Nom complet:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Téléphone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Adresse:"), 0, 2);
        grid.add(addressField, 1, 2);
        grid.add(new Label("Âge:"), 0, 3);
        grid.add(ageField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    Client client = new Client();
                    client.setFullName(nameField.getText());
                    client.setPhoneNumber(phoneField.getText());
                    client.setAddress(addressField.getText());
                    
                    // Set age with default value
                    try {
                        int age = Integer.parseInt(ageField.getText());
                        client.setAge(age > 0 ? age : 0);
                    } catch (NumberFormatException e) {
                        client.setAge(0);
                    }
                    
                    clientService.createClient(client);
                    loadClients(table);
                    showSuccess("Succès", "Client enregistré");
                } catch (Exception e) {
                    showError("Erreur", e.getMessage());
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showInvoiceDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Nouvelle Facture");
        dialog.setWidth(700);
        dialog.setHeight(600);
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setMinWidth(650);
        
        // Client selection
        HBox clientBox = new HBox(10);
        clientBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        ComboBox<Client> clientCombo = new ComboBox<>();
        clientCombo.setPromptText("Sélectionnez un client...");
        clientCombo.setPrefWidth(400);
        clientCombo.setConverter(new javafx.util.StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                return client == null ? "" : client.getFullName() + " - " + client.getPhoneNumber();
            }
            @Override
            public Client fromString(String string) {
                return null;
            }
        });
        
        try {
            clientCombo.setItems(FXCollections.observableArrayList(clientService.getAllClients()));
        } catch (SQLException e) {
            showError("Erreur", "Impossible de charger les clients");
        }
        
        clientBox.getChildren().addAll(new Label("Client:"), clientCombo);
        
        // Product selection
        HBox productBox = new HBox(10);
        productBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        ComboBox<Product> productCombo = new ComboBox<>();
        productCombo.setPromptText("Sélectionnez un produit...");
        productCombo.setPrefWidth(300);
        productCombo.setConverter(new javafx.util.StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                return product == null ? "" : product.getName() + " - " + product.getPrice() + " FCFA (Stock: " + product.getQuantity() + ")";
            }
            @Override
            public Product fromString(String string) {
                return null;
            }
        });
        
        try {
            productCombo.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
        } catch (SQLException e) {
            showError("Erreur", "Impossible de charger les produits");
        }
        
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantité");
        quantityField.setPrefWidth(80);
        
        Button addProductBtn = new Button("Ajouter");
        addProductBtn.getStyleClass().add("success-button");
        
        productBox.getChildren().addAll(new Label("Produit:"), productCombo, new Label("Quantité:"), quantityField, addProductBtn);
        
        // Invoice items table
        TableView<InvoiceItem> itemsTable = new TableView<>();
        itemsTable.setPrefHeight(200);
        
        TableColumn<InvoiceItem, String> productCol = new TableColumn<>("Produit");
        productCol.setCellValueFactory(data -> {
            try {
                Product product = productService.getProductById(data.getValue().getProductId());
                return new javafx.beans.property.SimpleStringProperty(product != null ? product.getName() : "");
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });
        productCol.setPrefWidth(250);
        
        TableColumn<InvoiceItem, Integer> quantityCol = new TableColumn<>("Quantité");
        quantityCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(100);
        
        TableColumn<InvoiceItem, Double> priceCol = new TableColumn<>("Prix unitaire");
        priceCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("unitPrice"));
        priceCol.setPrefWidth(120);
        
        TableColumn<InvoiceItem, Double> subtotalCol = new TableColumn<>("Sous-total");
        subtotalCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("subtotal"));
        subtotalCol.setPrefWidth(120);
        
        TableColumn<InvoiceItem, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(80);
        
        // Total and payment
        HBox totalBox = new HBox(10);
        totalBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        Label totalLabel = new Label("Total: 0 FCFA");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27AE60;");
        totalBox.getChildren().add(totalLabel);
        
        HBox paymentBox = new HBox(10);
        paymentBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        ComboBox<String> paymentCombo = new ComboBox<>();
        paymentCombo.setItems(FXCollections.observableArrayList("Espèces", "Carte bancaire", "Mobile Money", "Crédit"));
        paymentCombo.setValue("Espèces");
        paymentCombo.setPrefWidth(200);
        
        TextField notesField = new TextField();
        notesField.setPromptText("Notes (optionnel)");
        notesField.setPrefWidth(300);
        
        paymentBox.getChildren().addAll(new Label("Paiement:"), paymentCombo, new Label("Notes:"), notesField);
        
        // Update total function - define before TableCell
        Runnable updateTotal = () -> {
            double total = itemsTable.getItems().stream()
                .mapToDouble(InvoiceItem::getSubtotal)
                .sum();
            totalLabel.setText(String.format("Total: %.0f FCFA", total));
        };
        
        // Action column with delete button
        actionCol.setCellFactory(col -> new TableCell<InvoiceItem, Void>() {
            private final Button deleteBtn = new Button("🗑️");
            {
                deleteBtn.getStyleClass().add("danger-button");
                deleteBtn.setOnAction(e -> {
                    InvoiceItem item = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(item);
                    updateTotal.run();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
        
        itemsTable.getColumns().addAll(productCol, quantityCol, priceCol, subtotalCol, actionCol);
        
        // Add product button action
        addProductBtn.setOnAction(e -> {
            Product selectedProduct = productCombo.getValue();
            String quantityText = quantityField.getText();
            
            if (selectedProduct == null) {
                showError("Erreur", "Veuillez sélectionner un produit");
                return;
            }
            
            if (quantityText.isEmpty()) {
                showError("Erreur", "Veuillez saisir une quantité");
                return;
            }
            
            try {
                int quantity = Integer.parseInt(quantityText);
                if (quantity <= 0) {
                    showError("Erreur", "La quantité doit être supérieure à 0");
                    return;
                }
                
                if (quantity > selectedProduct.getQuantity()) {
                    showError("Erreur", "Stock insuffisant (disponible: " + selectedProduct.getQuantity() + ")");
                    return;
                }
                
                // Create invoice item
                InvoiceItem item = new InvoiceItem();
                item.setProductId(selectedProduct.getId());
                item.setQuantity(quantity);
                item.setUnitPrice(selectedProduct.getPrice());
                item.setSubtotal(quantity * selectedProduct.getPrice());
                
                itemsTable.getItems().add(item);
                updateTotal.run();
                
                // Clear fields
                productCombo.setValue(null);
                quantityField.clear();
                
            } catch (NumberFormatException ex) {
                showError("Erreur", "Quantité invalide");
            }
        });
        
        content.getChildren().addAll(
            clientBox,
            new javafx.scene.control.Separator(),
            productBox,
            new Label("Articles de la facture:"),
            itemsTable,
            totalBox,
            new javafx.scene.control.Separator(),
            paymentBox
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Create invoice on OK
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                Client selectedClient = clientCombo.getValue();
                
                if (selectedClient == null) {
                    showError("Erreur", "Veuillez sélectionner un client");
                    return null;
                }
                
                if (itemsTable.getItems().isEmpty()) {
                    showError("Erreur", "Veuillez ajouter au moins un produit");
                    return null;
                }
                
                try {
                    // Create invoice with correct method signature
                    Invoice invoice = invoiceService.createInvoice(
                        selectedClient.getId(),
                        SessionManager.getCurrentUser().getId(),
                        new java.util.ArrayList<>(itemsTable.getItems()),
                        paymentCombo.getValue(),
                        notesField.getText()
                    );
                    
                    showSuccess("Succès", "Facture créée avec succès!\nNuméro: " + invoice.getInvoiceNumber());
                    
                } catch (Exception ex) {
                    ex.printStackTrace(); // Print full stack trace to console
                    showError("Erreur", "Impossible de créer la facture: " + ex.getMessage() + 
                             (ex.getCause() != null ? "\nCause: " + ex.getCause().getMessage() : ""));
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void deleteProduct(Product product, TableView<Product> table) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le produit?");
        alert.setContentText(product.getName());
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    productService.deleteProduct(product.getId());
                    loadProducts(table);
                    showSuccess("Succès", "Produit supprimé");
                } catch (Exception e) {
                    showError("Erreur", e.getMessage());
                }
            }
        });
    }
    
    private void loadProducts(TableView<Product> table) {
        try {
            List<Product> products = productService.getAllProducts();
            table.setItems(FXCollections.observableArrayList(products));
        } catch (SQLException e) {
            showError("Erreur", "Impossible de charger les produits");
        }
    }
    
    private void loadClients(TableView<Client> table) {
        try {
            List<Client> clients = clientService.getAllClients();
            table.setItems(FXCollections.observableArrayList(clients));
        } catch (SQLException e) {
            showError("Erreur", "Impossible de charger les clients");
        }
    }
    
    private void loadInvoices(TableView<Invoice> table) {
        try {
            List<Invoice> invoices = invoiceService.getAllInvoices();
            table.setItems(FXCollections.observableArrayList(invoices));
        } catch (SQLException e) {
            showError("Erreur", "Impossible de charger les factures");
        }
    }
    
    private void handleLogout() {
        authService.logout();
        ViewManager.showLoginView();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public Parent getView() {
        return view;
    }
}
