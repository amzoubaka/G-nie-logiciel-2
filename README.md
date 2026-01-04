# Système de Gestion de Pharmacie (PharmaSys)

## 📋 Informations du Projet

**Projet:** Génie Logiciel II - Année Académique 2025-2026  
**Équipe:** [Noms des membres à compléter]  
**Date de livraison:** 4 janvier 2026  
**Lien du dépôt:** [À compléter avec votre URL GitHub]

## 🎯 Objectif

PharmaSys est un système de gestion informatisée pour pharmacie permettant de gérer les stocks de médicaments, l'enregistrement des clients, la facturation et l'authentification sécurisée du personnel.

## ✨ Fonctionnalités Principales

- ✅ **Authentification sécurisée** - Connexion avec nom d'utilisateur et mot de passe
- ✅ **Gestion des produits** - Recherche, ajout, modification, suppression de médicaments
- ✅ **Gestion des clients** - Enregistrement et suivi des informations clients
- ✅ **Facturation** - Création et impression de factures
- ✅ **Gestion des stocks** - Suivi des quantités, dates de péremption et emplacements
- ✅ **Journalisation** - Traçabilité des actions critiques

## 🛠️ Stack Technique

- **Langage:** Java 17+
- **Framework:** JavaFX pour l'interface graphique
- **Base de données:** SQLite (embarquée)
- **Build:** Maven
- **Tests:** JUnit 5
- **IDE recommandé:** Eclipse / IntelliJ IDEA

## 📦 Prérequis

- JDK 17 ou supérieur
- Maven 3.8+
- 2 Go RAM minimum
- 500 Mo d'espace disque

## 🚀 Installation

### 1. Cloner le dépôt

```bash
git clone [URL_DE_VOTRE_DEPOT]
cd geni_logiciel
```

### 2. Compiler le projet

```bash
mvn clean install
```

### 3. Lancer l'application

```bash
mvn javafx:run
```

Ou après compilation:

```bash
java -jar target/pharmasys-1.0.0.jar
```

## 👤 Comptes de Test

| Rôle           | Username    | Password  |
| -------------- | ----------- | --------- |
| Administrateur | admin       | admin123  |
| Pharmacien     | pharmacien1 | pharma123 |
| Utilisateur    | user1       | user123   |

## 📖 Documentation

- [Cahier des charges (CDC)](docs/cdc/CDC.md)
- [Architecture et Conception](docs/conception/Architecture.md)
- [Guide utilisateur](docs/guide-utilisateur.md)
- [Guide développeur](docs/guide-developpeur.md)

## 🧪 Tests

Pour exécuter les tests unitaires:

```bash
mvn test
```

Pour générer le rapport de couverture:

```bash
mvn jacoco:report
```

Le rapport sera disponible dans `target/site/jacoco/index.html`

## 📁 Structure du Projet

```
geni_logiciel/
├── docs/
│   ├── cdc/                    # Cahier des charges
│   ├── conception/             # Documents de conception
│   │   ├── diagrammes/        # Diagrammes UML
│   │   └── wireframes/        # Maquettes UI
│   ├── presentation/          # Slides de présentation
│   ├── guide-utilisateur.md
│   └── guide-developpeur.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/pharmasys/
│   │   │       ├── models/        # Entités métier
│   │   │       ├── dao/           # Accès aux données
│   │   │       ├── services/      # Logique métier
│   │   │       ├── controllers/   # Contrôleurs JavaFX
│   │   │       ├── views/         # FXML
│   │   │       └── utils/         # Utilitaires
│   │   └── resources/
│   │       ├── fxml/              # Fichiers FXML
│   │       ├── css/               # Styles
│   │       └── db/                # Scripts SQL
│   └── test/
│       └── java/                  # Tests unitaires
├── target/                        # Fichiers compilés
├── pom.xml                        # Configuration Maven
├── README.md
└── CHANGELOG.md
```

## 🤝 Contribution

### Workflow Git

1. Créer une branche feature: `git checkout -b feature/nom-fonctionnalite`
2. Commiter les changes: `git commit -m "Add: description"`
3. Pusher la branche: `git push origin feature/nom-fonctionnalite`
4. Créer une Pull Request

### Conventions de commits

- `Add:` Nouvelle fonctionnalité
- `Fix:` Correction de bug
- `Update:` Mise à jour d'une fonctionnalité existante
- `Docs:` Documentation
- `Test:` Ajout/modification de tests
- `Refactor:` Refactoring du code

## 📝 License

Ce projet est réalisé dans le cadre académique - Tous droits réservés.

## 📧 Contact

Pour toute question, contactez l'équipe via [email à compléter]

---

**Version:** 1.0.0  
**Dernière mise à jour:** 3 janvier 2026
