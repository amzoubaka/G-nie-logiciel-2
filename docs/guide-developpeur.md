# Guide Développeur - PharmaSys

**Version 1.0.0**  
**Date:** 4 janvier 2026

---

## Architecture

PharmaSys suit une architecture MVC (Model-View-Controller) avec une couche DAO pour la persistance.

### Structure des packages

```
com.pharmasys/
├── MainApp.java                 # Point d'entrée
├── models/                      # Entités métier
│   ├── User.java
│   ├── Product.java
│   ├── Client.java
│   ├── Invoice.java
│   ├── InvoiceItem.java
│   └── SystemLog.java
├── dao/                         # Data Access Objects
│   ├── UserDAO.java
│   ├── ProductDAO.java
│   └── ClientDAO.java
├── services/                    # Logique métier
│   ├── AuthService.java
│   ├── ProductService.java
│   ├── ClientService.java
│   └── LogService.java
├── controllers/                 # Contrôleurs JavaFX
│   ├── LoginController.java
│   └── DashboardController.java
├── views/                       # Gestion des vues
│   └── ViewManager.java
└── utils/                       # Utilitaires
    ├── DatabaseManager.java
    ├── PasswordUtil.java
    ├── SessionManager.java
    ├── ValidationUtil.java
    └── ValidationException.java
```

---

## Configuration et Démarrage

### Prérequis

- JDK 17+
- Maven 3.8+
- IDE (IntelliJ IDEA ou Eclipse recommandé)

### Installation

```bash
# Cloner le projet
git clone [URL]
cd geni_logiciel

# Compiler
mvn clean install

# Lancer l'application
mvn javafx:run
```

### Configuration Base de Données

La base de données SQLite est créée automatiquement au premier lancement dans `pharmasys.db`.

Schéma : voir `src/main/resources/db/schema.sql`

---

## Conventions de Code

### Nommage

- **Classes** : PascalCase (ex: `ProductService`)
- **Méthodes** : camelCase (ex: `createProduct()`)
- **Constantes** : UPPER_SNAKE_CASE (ex: `SESSION_TIMEOUT`)
- **Variables** : camelCase (ex: `productName`)

### Git Commits

Format : `Type: Description`

Types :

- `Add:` Nouvelle fonctionnalité
- `Fix:` Correction de bug
- `Update:` Mise à jour
- `Docs:` Documentation
- `Test:` Tests

Exemple : `Add: Product search feature`

---

## Ajout de Fonctionnalités

### Exemple : Ajouter un nouveau modèle

1. **Créer l'entité dans `models/`**

```java
public class Supplier {
    private int id;
    private String name;
    private String phone;
    // getters/setters
}
```

2. **Créer le DAO dans `dao/`**

```java
public class SupplierDAO {
    public int insert(Supplier supplier) throws SQLException {
        // Implémentation
    }

    public List<Supplier> findAll() throws SQLException {
        // Implémentation
    }
}
```

3. **Créer le Service dans `services/`**

```java
public class SupplierService {
    private SupplierDAO supplierDAO;

    public int createSupplier(Supplier supplier) {
        // Validation
        // Appel DAO
        // Logging
    }
}
```

4. **Ajouter dans le Controller**

Ajouter un nouvel onglet dans `DashboardController` ou créer un nouveau contrôleur.

---

## Tests

### Exécuter les tests

```bash
# Tous les tests
mvn test

# Tests avec couverture
mvn jacoco:report
```

Le rapport de couverture est généré dans `target/site/jacoco/index.html`

### Écrire un test

```java
@Test
void testCreateProduct_WithValidData_ShouldSucceed() throws Exception {
    // Given
    Product product = new Product();
    product.setNom("Test");
    product.setReference("TST-001");
    // ... autres champs

    // When
    int id = productService.createProduct(product);

    // Then
    assertTrue(id > 0);
}
```

---

## Base de Données

### Connexion

La connexion est gérée par `DatabaseManager.getConnection()`.

### Transactions

Pour les opérations critiques (factures), utiliser des transactions :

```java
Connection conn = DatabaseManager.getConnection();
try {
    conn.setAutoCommit(false);

    // Opérations
    invoiceDAO.insert(invoice);
    productDAO.updateStock(productId, newQuantity);

    conn.commit();
} catch (SQLException e) {
    conn.rollback();
    throw e;
}
```

### Requêtes Préparées

**Toujours** utiliser des PreparedStatements pour éviter les injections SQL :

```java
String sql = "SELECT * FROM products WHERE nom LIKE ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, "%" + searchTerm + "%");
ResultSet rs = pstmt.executeQuery();
```

---

## Sécurité

### Mots de Passe

- **Jamais** stocker en clair
- Utiliser `PasswordUtil.hashPassword()` pour hasher
- Utiliser `PasswordUtil.verifyPassword()` pour vérifier

### Sessions

- Gérées par `SessionManager`
- Timeout de 2 heures
- Vérifier avec `SessionManager.isLoggedIn()`

### Validation

- Double validation (Controller + Service)
- Utiliser `ValidationUtil` pour les validations communes

---

## Logging

### Actions journalisées

- Connexion/Déconnexion
- Création/Modification/Suppression de produits
- Création de factures

### Utilisation

```java
logService.log(userId, "CREATE_PRODUCT", "Détails de l'action");
```

---

## Débogage

### Logs applicatifs

Les logs sont affichés dans la console.

### Base de données

Utiliser un client SQLite (DB Browser for SQLite) pour inspecter `pharmasys.db`.

### Points de rupture

Dans votre IDE, placer des breakpoints dans :

- Services (logique métier)
- DAO (requêtes SQL)
- Controllers (actions utilisateur)

---

## Build et Déploiement

### Créer un JAR exécutable

```bash
mvn clean package
```

Le JAR est généré dans `target/pharmasys-1.0.0.jar`

### Exécuter le JAR

```bash
java -jar target/pharmasys-1.0.0.jar
```

---

## Dépendances Principales

| Dépendance  | Version  | Usage                 |
| ----------- | -------- | --------------------- |
| JavaFX      | 21.0.1   | Interface graphique   |
| SQLite JDBC | 3.44.1.0 | Base de données       |
| BCrypt      | 0.10.2   | Hashage mots de passe |
| JUnit       | 5.10.1   | Tests unitaires       |
| Mockito     | 5.8.0    | Mocks pour tests      |

---

## Contribution

### Workflow

1. Créer une branche : `git checkout -b feature/nom-feature`
2. Développer et tester
3. Commiter : `git commit -m "Add: description"`
4. Pusher : `git push origin feature/nom-feature`
5. Créer une Pull Request

### Revue de Code

Avant de merger :

- Code respecte les conventions
- Tests passent (`mvn test`)
- Documentation à jour
- Pas de code commenté

---

## Ressources

- [JavaFX Documentation](https://openjfx.io/)
- [SQLite SQL Syntax](https://www.sqlite.org/lang.html)
- [Maven Guide](https://maven.apache.org/guides/)

---

**Bon développement !** 🚀
