# Architecture et Conception - PharmaSys

**Version:** 1.0  
**Date:** 1er janvier 2026  
**Projet:** Système de Gestion de Pharmacie

---

## Table des Matières

1. [Architecture Globale](#1-architecture-globale)
2. [Diagrammes](#2-diagrammes)
3. [Conception des API et Services](#3-conception-des-api-et-services)
4. [Stratégie de Tests](#4-stratégie-de-tests)
5. [Décisions Techniques](#5-décisions-techniques)

---

## 1. Architecture Globale

### 1.1 Type d'Architecture

**Architecture choisie : MVC (Model-View-Controller) avec couche DAO**

```
┌─────────────────────────────────────────────────────┐
│                   Présentation                      │
│              (JavaFX Controllers + FXML)            │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│                   Services                          │
│         (Business Logic & Validation)               │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│                     DAO                             │
│          (Data Access Objects)                      │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│                 Base de Données                     │
│                  (SQLite)                           │
└─────────────────────────────────────────────────────┘
```

### 1.2 Découpage en Couches

#### **Couche Présentation (View + Controller)**

- **Responsabilité:** Affichage et interaction utilisateur
- **Technologies:** JavaFX (FXML + Controllers)
- **Composants:**
  - `LoginController` : Gestion de l'authentification
  - `DashboardController` : Tableau de bord principal
  - `ProductController` : Gestion des produits
  - `ClientController` : Gestion des clients
  - `InvoiceController` : Création et gestion des factures
- **Principe:** Les contrôleurs ne contiennent PAS de logique métier, uniquement de la logique de présentation

#### **Couche Services (Business Logic)**

- **Responsabilité:** Logique métier, validation, orchestration
- **Technologies:** Java POJO
- **Composants:**
  - `AuthService` : Authentification et autorisation
  - `ProductService` : Gestion business des produits
  - `ClientService` : Gestion business des clients
  - `InvoiceService` : Logique de facturation
  - `LogService` : Journalisation
- **Principe:** Valide les données, applique les règles métier, coordonne les DAO

#### **Couche DAO (Data Access)**

- **Responsabilité:** Accès et persistance des données
- **Technologies:** JDBC + PreparedStatements
- **Composants:**
  - `UserDAO` : CRUD utilisateurs
  - `ProductDAO` : CRUD produits
  - `ClientDAO` : CRUD clients
  - `InvoiceDAO` : CRUD factures
  - `LogDAO` : Insertion des logs
- **Principe:** Une classe DAO par entité, méthodes CRUD standard

#### **Couche Modèles (Entities)**

- **Responsabilité:** Représentation des données
- **Technologies:** Java POJO avec getters/setters
- **Composants:**
  - `User`, `Product`, `Client`, `Invoice`, `InvoiceItem`, `SystemLog`
- **Principe:** Classes simples, pas de logique métier

#### **Couche Utilitaires**

- **Responsabilité:** Fonctions transverses
- **Composants:**
  - `DatabaseManager` : Gestion connexion SQLite
  - `PasswordUtil` : Hashage BCrypt
  - `ValidationUtil` : Validation des données
  - `SessionManager` : Gestion session utilisateur
  - `Logger` : Wrapper logging

### 1.3 Flux de Données Typique

**Exemple : Création d'un produit**

```
1. User saisit formulaire
   ↓
2. ProductController.handleSave()
   ↓
3. Validation basique (champs vides)
   ↓
4. ProductService.createProduct(product)
   ↓
5. Validation métier (référence unique, prix > 0)
   ↓
6. ProductDAO.insert(product)
   ↓
7. Exécution SQL avec PreparedStatement
   ↓
8. LogService.log("CREATE_PRODUCT", details)
   ↓
9. Retour succès au Controller
   ↓
10. Affichage message confirmation + MAJ vue
```

### 1.4 Contraintes de Sécurité

- **Mots de passe:** Jamais stockés en clair, uniquement hash BCrypt
- **Injection SQL:** Utilisation exclusive de PreparedStatements
- **Validation:** Double validation (côté contrôleur + service)
- **Sessions:** Gestion via `SessionManager` avec timeout
- **Logs:** Toutes les actions sensibles journalisées

### 1.5 Justification des Choix

| Décision             | Justification                                                                                                                                    |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Architecture MVC** | - Séparation claire des responsabilités<br>- Facilite les tests<br>- Maintenabilité à long terme<br>- Standard de l'industrie                    |
| **SQLite embarqué**  | - Pas de serveur externe requis<br>- Installation simple<br>- Performance suffisante pour 1 utilisateur<br>- Fichier unique facile à sauvegarder |
| **JavaFX**           | - Framework moderne pour desktop Java<br>- Riche en composants UI<br>- FXML pour séparation vue/logique<br>- Multiplateforme                     |
| **Maven**            | - Gestion automatique des dépendances<br>- Build standardisé<br>- Intégration CI facile<br>- Large écosystème de plugins                         |
| **BCrypt**           | - Algorithme éprouvé<br>- Salt automatique<br>- Résistant aux attaques brute-force<br>- Standard de sécurité                                     |

---

## 2. Diagrammes

### 2.1 Modèle de Données (ERD)

```
┌─────────────────────┐
│       User          │
├─────────────────────┤
│ PK id               │
│    username         │
│    password_hash    │
│    role             │
│    created_at       │
│    last_login       │
└──────────┬──────────┘
           │
           │ created_by (FK)
           │
┌──────────▼──────────┐          ┌─────────────────────┐
│      Product        │          │      Client         │
├─────────────────────┤          ├─────────────────────┤
│ PK id               │          │ PK id               │
│    nom              │          │    nom              │
│    reference (UQ)   │          │    prenom           │
│    quantite         │          │    quartier         │
│    prix             │          │    age              │
│    date_peremption  │          │    telephone        │
│    validite         │          │    created_at       │
│    emplacement      │          └──────────┬──────────┘
│ FK created_by       │                     │
│    created_at       │                     │
│    updated_at       │                     │
└──────────┬──────────┘                     │
           │                                │
           │ produit_id (FK)                │ client_id (FK)
           │                                │
           │          ┌─────────────────────▼──────────┐
           │          │       Invoice                   │
           │          ├────────────────────────────────┤
           │          │ PK id                          │
           │          │ FK client_id                   │
           │          │    nom_client                  │
           │          │    date_facture                │
           │          │    montant_total               │
           │          │ FK created_by                  │
           │          └────────────┬───────────────────┘
           │                       │
           │                       │ facture_id (FK)
           │                       │
           │          ┌────────────▼───────────────────┐
           └─────────►│     InvoiceItem                │
                      ├────────────────────────────────┤
                      │ PK id                          │
                      │ FK facture_id                  │
                      │ FK produit_id                  │
                      │    nom_produit (snapshot)      │
                      │    reference (snapshot)        │
                      │    quantite                    │
                      │    prix_unitaire (snapshot)    │
                      │    sous_total                  │
                      └────────────────────────────────┘

┌─────────────────────┐
│    SystemLog        │
├─────────────────────┤
│ PK id               │
│ FK user_id          │
│    action           │
│    details          │
│    timestamp        │
└─────────────────────┘
```

### 2.2 Diagramme de Classes (Simplifié)

```
┌──────────────────┐         ┌──────────────────┐
│   <<Entity>>     │         │   <<Entity>>     │
│      User        │         │     Product      │
├──────────────────┤         ├──────────────────┤
│ -id: int         │         │ -id: int         │
│ -username: String│         │ -nom: String     │
│ -passwordHash    │         │ -reference: Str  │
│ -role: String    │         │ -quantite: int   │
│ -createdAt: Date │         │ -prix: double    │
│ -lastLogin: Date │         │ -datePeremption  │
├──────────────────┤         │ -emplacement     │
│ +getters()       │         ├──────────────────┤
│ +setters()       │         │ +getters()       │
└──────────────────┘         │ +setters()       │
                             └──────────────────┘

┌──────────────────────────┐      ┌──────────────────────────┐
│      <<Service>>         │      │       <<DAO>>            │
│    ProductService        │─────►│      ProductDAO          │
├──────────────────────────┤      ├──────────────────────────┤
│ -productDAO: ProductDAO  │      │ -connection: Connection  │
│ -logService: LogService  │      ├──────────────────────────┤
├──────────────────────────┤      │ +insert(Product): int    │
│ +createProduct(Product)  │      │ +update(Product): void   │
│ +updateProduct(Product)  │      │ +delete(int): void       │
│ +deleteProduct(int)      │      │ +findById(int): Product  │
│ +searchProducts(String)  │      │ +findAll(): List<>       │
│ +validateProduct(Product)│      │ +search(String): List<>  │
└──────────────────────────┘      └──────────────────────────┘

┌──────────────────────────┐
│    <<Controller>>        │
│   ProductController      │
├──────────────────────────┤
│ -productService: Service │
│ -tableView: TableView    │
│ -searchField: TextField  │
├──────────────────────────┤
│ +initialize()            │
│ +handleSearch()          │
│ +handleAdd()             │
│ +handleEdit()            │
│ +handleDelete()          │
│ +refreshTable()          │
└──────────────────────────┘
```

### 2.3 Diagramme de Séquence : Authentification

```
User        LoginController    AuthService      UserDAO       Database
 │                │                 │              │              │
 │ Enter creds    │                 │              │              │
 ├───────────────►│                 │              │              │
 │                │                 │              │              │
 │                │ authenticate()  │              │              │
 │                ├────────────────►│              │              │
 │                │                 │              │              │
 │                │                 │ findByUsername()           │
 │                │                 ├─────────────►│              │
 │                │                 │              │              │
 │                │                 │              │ SELECT ...   │
 │                │                 │              ├─────────────►│
 │                │                 │              │              │
 │                │                 │              │ User data    │
 │                │                 │              │◄─────────────┤
 │                │                 │              │              │
 │                │                 │ User object  │              │
 │                │                 │◄─────────────┤              │
 │                │                 │              │              │
 │                │                 │ verify password (BCrypt)    │
 │                │                 ├──────────────┐              │
 │                │                 │              │              │
 │                │                 │◄─────────────┘              │
 │                │                 │              │              │
 │                │                 │ createSession()             │
 │                │                 ├──────────────┐              │
 │                │                 │◄─────────────┘              │
 │                │                 │              │              │
 │                │ User + Session  │              │              │
 │                │◄────────────────┤              │              │
 │                │                 │              │              │
 │  Navigate to   │                 │              │              │
 │   Dashboard    │                 │              │              │
 │◄───────────────┤                 │              │              │
 │                │                 │              │              │
```

### 2.4 Diagramme de Séquence : Création de Facture

```
User    InvoiceController   InvoiceService   ProductService   InvoiceDAO   ProductDAO
 │             │                   │                │               │            │
 │ Select      │                   │                │               │            │
 │ client +    │                   │                │               │            │
 │ products    │                   │                │               │            │
 ├────────────►│                   │                │               │            │
 │             │                   │                │               │            │
 │             │ createInvoice()   │                │               │            │
 │             ├──────────────────►│                │               │            │
 │             │                   │                │               │            │
 │             │                   │ validateStock()│               │            │
 │             │                   ├───────────────►│               │            │
 │             │                   │                │               │            │
 │             │                   │                │ checkQuantity()           │
 │             │                   │                ├──────────────────────────►│
 │             │                   │                │               │            │
 │             │                   │                │ OK / Stock available      │
 │             │                   │                │◄──────────────────────────┤
 │             │                   │                │               │            │
 │             │                   │   Stock OK     │               │            │
 │             │                   │◄───────────────┤               │            │
 │             │                   │                │               │            │
 │             │                   │ calculateTotal()               │            │
 │             │                   ├────────────┐   │               │            │
 │             │                   │◄───────────┘   │               │            │
 │             │                   │                │               │            │
 │             │                   │ BEGIN TRANSACTION             │            │
 │             │                   ├──────────────────────────────►│            │
 │             │                   │                │               │            │
 │             │                   │ insertInvoice()│               │            │
 │             │                   ├──────────────────────────────►│            │
 │             │                   │                │               │            │
 │             │                   │ insertInvoiceItems()          │            │
 │             │                   ├──────────────────────────────►│            │
 │             │                   │                │               │            │
 │             │                   │ updateStock()  │               │            │
 │             │                   ├───────────────►│               │            │
 │             │                   │                │               │            │
 │             │                   │                │ UPDATE stock  │            │
 │             │                   │                ├──────────────────────────►│
 │             │                   │                │               │            │
 │             │                   │ COMMIT TRANSACTION            │            │
 │             │                   ├──────────────────────────────►│            │
 │             │                   │                │               │            │
 │             │   Invoice saved   │                │               │            │
 │             │◄──────────────────┤                │               │            │
 │             │                   │                │               │            │
 │  Display    │                   │                │               │            │
 │  success    │                   │                │               │            │
 │◄────────────┤                   │                │               │            │
 │             │                   │                │               │            │
```

### 2.5 Diagramme de Composants

```
┌─────────────────────────────────────────────────────────┐
│                    PharmaSys Application                 │
│                                                          │
│  ┌────────────────────────────────────────────────────┐ │
│  │              UI Components (JavaFX)                 │ │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐           │ │
│  │  │  Login   │ │Dashboard │ │ Product  │  ...      │ │
│  │  │  View    │ │  View    │ │  View    │           │ │
│  │  └──────────┘ └──────────┘ └──────────┘           │ │
│  └────────────────────────────────────────────────────┘ │
│                          │                               │
│  ┌────────────────────────────────────────────────────┐ │
│  │          Controllers (Presentation Logic)          │ │
│  │  [LoginController] [ProductController] ...         │ │
│  └────────────────────────────────────────────────────┘ │
│                          │                               │
│  ┌────────────────────────────────────────────────────┐ │
│  │           Services (Business Logic)                │ │
│  │  [AuthService] [ProductService] [InvoiceService]   │ │
│  └────────────────────────────────────────────────────┘ │
│                          │                               │
│  ┌────────────────────────────────────────────────────┐ │
│  │              DAO (Data Access)                      │ │
│  │  [UserDAO] [ProductDAO] [InvoiceDAO] ...           │ │
│  └────────────────────────────────────────────────────┘ │
│                          │                               │
│  ┌────────────────────────────────────────────────────┐ │
│  │              Utilities                              │ │
│  │  [DatabaseManager] [PasswordUtil] [Validator]      │ │
│  └────────────────────────────────────────────────────┘ │
│                          │                               │
│  ┌────────────────────────────────────────────────────┐ │
│  │           SQLite Database (pharmasys.db)           │ │
│  └────────────────────────────────────────────────────┘ │
│                                                          │
└─────────────────────────────────────────────────────────┘

External Dependencies:
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│   JavaFX     │  │ SQLite JDBC  │  │    BCrypt    │
└──────────────┘  └──────────────┘  └──────────────┘
```

---

## 3. Conception des API et Services

### 3.1 AuthService

**Responsabilité:** Authentification et gestion des sessions

```java
public class AuthService {
    /**
     * Authentifie un utilisateur
     * @param username Nom d'utilisateur
     * @param password Mot de passe en clair
     * @return User si authentification réussie, null sinon
     */
    public User authenticate(String username, String password)

    /**
     * Vérifie si l'utilisateur a le droit requis
     * @param user Utilisateur
     * @param requiredRole Rôle minimum requis
     * @return true si autorisé
     */
    public boolean hasPermission(User user, String requiredRole)

    /**
     * Déconnecte l'utilisateur courant
     */
    public void logout()

    /**
     * Obtient l'utilisateur de la session courante
     * @return User connecté ou null
     */
    public User getCurrentUser()
}
```

### 3.2 ProductService

**Responsabilité:** Gestion métier des produits

```java
public class ProductService {
    /**
     * Crée un nouveau produit
     * @param product Produit à créer
     * @throws ValidationException si données invalides
     * @throws DuplicateReferenceException si référence existe
     */
    public int createProduct(Product product) throws Exception

    /**
     * Met à jour un produit existant
     * @param product Produit avec modifications
     * @throws ValidationException si données invalides
     */
    public void updateProduct(Product product) throws Exception

    /**
     * Supprime un produit
     * @param productId ID du produit
     * @throws InvoiceExistsException si produit lié à factures
     */
    public void deleteProduct(int productId) throws Exception

    /**
     * Recherche des produits
     * @param query Terme de recherche (nom ou référence)
     * @return Liste de produits correspondants
     */
    public List<Product> searchProducts(String query)

    /**
     * Obtient les produits en stock faible
     * @param threshold Seuil (défaut: 10)
     * @return Liste de produits
     */
    public List<Product> getLowStockProducts(int threshold)

    /**
     * Obtient les produits proches de la péremption
     * @param daysBeforeExpiry Jours avant péremption (défaut: 30)
     * @return Liste de produits
     */
    public List<Product> getExpiringProducts(int daysBeforeExpiry)

    /**
     * Valide un produit selon les règles métier
     * @param product Produit à valider
     * @throws ValidationException si invalide
     */
    private void validateProduct(Product product) throws ValidationException
}
```

### 3.3 InvoiceService

**Responsabilité:** Logique de facturation

```java
public class InvoiceService {
    /**
     * Crée une nouvelle facture
     * Transaction: insert facture + items + update stock
     * @param invoice Facture avec items
     * @return ID de la facture créée
     * @throws InsufficientStockException si stock insuffisant
     * @throws ValidationException si données invalides
     */
    public int createInvoice(Invoice invoice, List<InvoiceItem> items)
        throws Exception

    /**
     * Calcule le montant total d'une facture
     * @param items Liste des items
     * @return Montant total
     */
    public double calculateTotal(List<InvoiceItem> items)

    /**
     * Vérifie la disponibilité du stock pour les items
     * @param items Items à vérifier
     * @throws InsufficientStockException si stock insuffisant
     */
    public void validateStock(List<InvoiceItem> items) throws Exception

    /**
     * Obtient toutes les factures
     * @return Liste des factures
     */
    public List<Invoice> getAllInvoices()

    /**
     * Obtient les détails d'une facture
     * @param invoiceId ID de la facture
     * @return Facture avec items
     */
    public Invoice getInvoiceDetails(int invoiceId)

    /**
     * Exporte une facture en HTML pour impression
     * @param invoiceId ID de la facture
     * @return Contenu HTML
     */
    public String exportInvoiceToHTML(int invoiceId)
}
```

### 3.4 ClientService

**Responsabilité:** Gestion des clients

```java
public class ClientService {
    /**
     * Crée un nouveau client
     * @param client Client à créer
     * @return ID du client créé
     * @throws ValidationException si données invalides
     */
    public int createClient(Client client) throws ValidationException

    /**
     * Met à jour un client
     * @param client Client avec modifications
     * @throws ValidationException si données invalides
     */
    public void updateClient(Client client) throws ValidationException

    /**
     * Recherche des clients
     * @param query Terme de recherche (nom, prénom, téléphone)
     * @return Liste de clients
     */
    public List<Client> searchClients(String query)

    /**
     * Obtient tous les clients
     * @return Liste des clients
     */
    public List<Client> getAllClients()

    /**
     * Valide un client selon les règles métier
     * @param client Client à valider
     * @throws ValidationException si invalide
     */
    private void validateClient(Client client) throws ValidationException
}
```

### 3.5 LogService

**Responsabilité:** Journalisation des actions

```java
public class LogService {
    /**
     * Enregistre une action dans les logs
     * @param action Type d'action (LOGIN, CREATE_PRODUCT, etc.)
     * @param details Détails supplémentaires (JSON ou texte)
     */
    public void log(String action, String details)

    /**
     * Obtient les logs avec filtres
     * @param userId Filtre par utilisateur (null = tous)
     * @param fromDate Date début (null = depuis le début)
     * @param toDate Date fin (null = jusqu'à maintenant)
     * @return Liste des logs
     */
    public List<SystemLog> getLogs(Integer userId, Date fromDate, Date toDate)
}
```

---

## 4. Stratégie de Tests

### 4.1 Pyramide de Tests

```
         ┌─────────────┐
         │   UI Tests  │  (Manuels - 10%)
         │  (Manuels)  │
         └─────────────┘
       ┌─────────────────┐
       │ Integration Tests│ (20%)
       │   (Services +    │
       │      DAO)        │
       └─────────────────┘
    ┌────────────────────────┐
    │    Unit Tests          │ (70%)
    │  (Services, Utils)     │
    │                        │
    └────────────────────────┘
```

### 4.2 Tests Unitaires

**Objectif:** Couverture minimale 60%

#### Classes à tester en priorité:

- **PasswordUtil** : Hash et vérification BCrypt
- **ValidationUtil** : Validations (téléphone, âge, prix, etc.)
- **ProductService** : Logique métier produits
- **InvoiceService** : Calcul totaux, validation stock
- **ClientService** : Validation clients

#### Exemple de test:

```java
@Test
public void testCreateProduct_WithValidData_ShouldSucceed() {
    // Given
    Product product = new Product();
    product.setNom("Paracétamol");
    product.setReference("PAR-500");
    product.setQuantite(100);
    product.setPrix(2500.0);
    // ... autres champs

    // When
    int productId = productService.createProduct(product);

    // Then
    assertTrue(productId > 0);
    verify(productDAO).insert(product);
    verify(logService).log("CREATE_PRODUCT", anyString());
}

@Test
public void testCreateProduct_WithDuplicateReference_ShouldThrow() {
    // Given
    when(productDAO.findByReference("PAR-500"))
        .thenReturn(new Product());

    Product product = new Product();
    product.setReference("PAR-500");

    // When & Then
    assertThrows(DuplicateReferenceException.class,
        () -> productService.createProduct(product));
}
```

### 4.3 Tests d'Intégration

**Objectif:** Vérifier l'interaction entre couches

#### Scénarios clés:

1. **Test complet de création de facture**
   - Insert facture → Insert items → Update stock → Verify
2. **Test CRUD produit avec DAO réel**
   - Create → Read → Update → Read → Delete
3. **Test transaction rollback**
   - Simuler erreur → Vérifier rollback → Vérifier état

#### Exemple:

```java
@Test
public void testCreateInvoice_WithRealDatabase_ShouldUpdateStock() {
    // Given: Produit avec stock = 100
    Product product = createTestProduct(100);
    InvoiceItem item = new InvoiceItem();
    item.setProductId(product.getId());
    item.setQuantite(10);

    // When: Création facture
    int invoiceId = invoiceService.createInvoice(
        new Invoice(), List.of(item)
    );

    // Then: Stock doit être 90
    Product updated = productDAO.findById(product.getId());
    assertEquals(90, updated.getQuantite());
}
```

### 4.4 Tests Manuels (Recette)

**Scénarios à valider manuellement avant livraison:**

1. **Authentification**

   - Login valide
   - Login invalide
   - Déconnexion

2. **Produits**

   - Recherche par nom
   - Ajout avec tous les champs
   - Modification
   - Suppression avec confirmation
   - Filtre stock faible
   - Filtre péremption proche

3. **Clients**

   - Ajout client
   - Recherche
   - Modification

4. **Factures**

   - Création facture complète
   - Vérification calcul total
   - Vérification mise à jour stock
   - Export/Impression
   - Tentative avec stock insuffisant (doit échouer)

5. **Logs**
   - Vérification journalisation connexion
   - Vérification journalisation création produit

### 4.5 Intégration Continue (CI)

**Configuration Maven Surefire:**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.3</version>
    <configuration>
        <includes>
            <include>**/*Test.java</include>
            <include>**/*Tests.java</include>
        </includes>
    </configuration>
</plugin>
```

**Commandes:**

- `mvn test` : Exécute tous les tests
- `mvn verify` : Tests + vérifications
- `mvn jacoco:report` : Génère rapport de couverture

---

## 5. Décisions Techniques

### 5.1 Décisions Architecturales

| Décision                 | Alternatives considérées | Raison du choix                                                                         |
| ------------------------ | ------------------------ | --------------------------------------------------------------------------------------- |
| **MVC avec couche DAO**  | MVVM, Clean Architecture | - Simplicité pour projet court terme<br>- Familiarité de l'équipe<br>- Adapté à JavaFX  |
| **SQLite embarqué**      | MySQL, PostgreSQL, H2    | - Aucun serveur requis<br>- Setup instantané<br>- Suffisant pour usage mono-utilisateur |
| **JavaFX natif**         | Swing, Electron+Web      | - Moderne et maintenu<br>- Séparation FXML/Code<br>- Riche en composants                |
| **Pas de framework IoC** | Spring, Guice            | - Overhead inutile<br>- Instantiation manuelle suffisante<br>- Simplicité               |

### 5.2 Gestion des Erreurs

**Stratégie:**

- **Exceptions métier custom:** `ValidationException`, `DuplicateReferenceException`, `InsufficientStockException`
- **Catch à la couche Controller:** Affichage messages utilisateur
- **Pas de stack traces à l'utilisateur:** Messages explicites en français
- **Rollback automatique:** Transactions pour opérations critiques

**Exemple:**

```java
// Service
public int createProduct(Product product) throws ValidationException {
    validateProduct(product);
    if (productDAO.existsByReference(product.getReference())) {
        throw new DuplicateReferenceException(
            "La référence " + product.getReference() + " existe déjà"
        );
    }
    return productDAO.insert(product);
}

// Controller
try {
    productService.createProduct(product);
    showSuccess("Produit ajouté avec succès");
} catch (DuplicateReferenceException e) {
    showError(e.getMessage());
} catch (ValidationException e) {
    showError(e.getMessage());
} catch (Exception e) {
    showError("Une erreur est survenue");
    logger.error("Erreur création produit", e);
}
```

### 5.3 Gestion de la Base de Données

**Schéma de création:**

```sql
-- Script: src/main/resources/db/schema.sql

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK(role IN ('admin', 'pharmacien', 'utilisateur')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom VARCHAR(100) NOT NULL,
    reference VARCHAR(50) UNIQUE NOT NULL,
    quantite INTEGER NOT NULL CHECK(quantite >= 0),
    prix DECIMAL(10,2) NOT NULL CHECK(prix > 0),
    date_peremption DATE NOT NULL,
    validite DATE,
    emplacement VARCHAR(50) NOT NULL,
    created_by INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS clients (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    quartier VARCHAR(100),
    age INTEGER CHECK(age BETWEEN 0 AND 150),
    telephone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS invoices (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    client_id INTEGER,
    nom_client VARCHAR(100) NOT NULL,
    date_facture TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    montant_total DECIMAL(10,2) NOT NULL,
    created_by INTEGER,
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS invoice_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    facture_id INTEGER NOT NULL,
    produit_id INTEGER NOT NULL,
    nom_produit VARCHAR(100) NOT NULL,
    reference VARCHAR(50) NOT NULL,
    quantite INTEGER NOT NULL CHECK(quantite > 0),
    prix_unitaire DECIMAL(10,2) NOT NULL,
    sous_total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (facture_id) REFERENCES invoices(id),
    FOREIGN KEY (produit_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS system_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    action VARCHAR(100) NOT NULL,
    details TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Index pour performance
CREATE INDEX idx_products_reference ON products(reference);
CREATE INDEX idx_products_nom ON products(nom);
CREATE INDEX idx_clients_telephone ON clients(telephone);
CREATE INDEX idx_invoices_date ON invoices(date_facture);
CREATE INDEX idx_logs_timestamp ON system_logs(timestamp);
```

**Données initiales:**

```sql
-- Script: src/main/resources/db/data.sql

-- Utilisateurs (mots de passe hashés avec BCrypt)
INSERT INTO users (username, password_hash, role) VALUES
('admin', '$2a$10$...', 'admin'),
('pharmacien1', '$2a$10$...', 'pharmacien'),
('user1', '$2a$10$...', 'utilisateur');

-- Produits de test
INSERT INTO products (nom, reference, quantite, prix, date_peremption, emplacement, created_by) VALUES
('Paracétamol 500mg', 'PAR-500', 100, 2500.00, '2026-12-31', 'Rayon A', 1),
('Paracétamol 250mg', 'PAR-250', 150, 1000.00, '2026-06-30', 'Rayon A', 1),
('Aspirine 100mg', 'ASP-100', 80, 1500.00, '2025-03-15', 'Rayon B', 1);

-- Client de test
INSERT INTO clients (nom, prenom, quartier, age, telephone) VALUES
('Dupont', 'Jean', 'Centre-ville', 35, '12345678');
```

### 5.4 Gestion de la Session

```java
public class SessionManager {
    private static User currentUser = null;
    private static Date loginTime = null;
    private static final long SESSION_TIMEOUT = 2 * 60 * 60 * 1000; // 2 heures

    public static void createSession(User user) {
        currentUser = user;
        loginTime = new Date();
    }

    public static User getCurrentUser() {
        if (isSessionExpired()) {
            logout();
            return null;
        }
        return currentUser;
    }

    public static boolean isSessionExpired() {
        if (loginTime == null) return true;
        long elapsed = new Date().getTime() - loginTime.getTime();
        return elapsed > SESSION_TIMEOUT;
    }

    public static void logout() {
        currentUser = null;
        loginTime = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null && !isSessionExpired();
    }
}
```

---

## Conclusion

Cette architecture garantit :

- ✅ **Séparation des responsabilités** (MVC + DAO)
- ✅ **Testabilité** (couches découplées)
- ✅ **Maintenabilité** (code structuré et documenté)
- ✅ **Sécurité** (validation, hash, PreparedStatements)
- ✅ **Performance** (index, transactions)
- ✅ **Simplicité** (pas de sur-engineering)

Le système est prêt pour l'implémentation selon le planning établi dans le CDC.

---

**Document validé le:** 1er janvier 2026  
**Version:** 1.0
