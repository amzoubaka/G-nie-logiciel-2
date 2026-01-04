# Système de Gestion de Pharmacie (PharmaSys)

## 📋 Informations du Projet

**Projet:** Génie Logiciel II - Année Académique 2025-2026  
**Équipe:** [Noms des membres à compléter]  
**Date de livraison:** 4 janvier 2026  
**Lien du dépôt:** [À compléter avec votre URL GitHub]

## 🎯 Objectif

PharmaSys est un système de gestion informatisée pour pharmacie permettant de gérer les stocks de médicaments, l'enregistrement des clients, la facturation et l'authentification sécurisée du personnel avec **contrôle d'accès basé sur les rôles**.

## ✨ Fonctionnalités Principales

- ✅ **Authentification sécurisée** - Connexion avec nom d'utilisateur et mot de passe (hashage BCrypt)
- ✅ **Contrôle d'accès par rôles** - Interfaces différentes pour Admin, Pharmacien et Utilisateur
- ✅ **Gestion des produits** - Recherche, ajout, modification, suppression de médicaments
- ✅ **Gestion des clients** - Enregistrement et suivi des informations clients
- ✅ **Facturation complète** - Création de factures avec gestion des articles et stock
- ✅ **Gestion des stocks** - Suivi des quantités, dates de péremption et alertes
- ✅ **Tableau de bord statistiques** - Vue d'ensemble pour les administrateurs
- ✅ **Journalisation** - Traçabilité complète des actions critiques
- ✅ **Interface moderne** - UI professionnelle avec CSS personnalisé

## 👥 Rôles et Permissions

### Administrateur

- 📊 Tableau de bord avec statistiques (CA, produits, clients, factures)
- 💊 Gestion complète des produits (CRUD)
- 👥 Gestion complète des clients (CRUD)
- 🧾 Gestion complète des factures
- 👤 Gestion des utilisateurs du système

### Pharmacien

- 💊 Gestion complète des produits (CRUD)
- 👥 Gestion complète des clients (CRUD)
- 🧾 Création et consultation des factures

### Utilisateur

- 👀 Consultation des produits (lecture seule)
- 👀 Consultation des clients (lecture seule)
- ❌ Pas d'accès aux factures ni aux modifications

## 🛠️ Stack Technique

- **Langage:** Java 17+
- **Framework UI:** JavaFX 21.0.1 avec CSS personnalisé
- **Base de données:** SQLite 3.44.1.0 (embarquée)
- **Build:** Maven 3.8+
- **Sécurité:** BCrypt pour hashage de mots de passe
- **Tests:** JUnit 5 + Mockito
- **Couverture:** JaCoCo
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

## 👤 Comptes de Test Accès |

| -------------- | ----------- | --------- | ----- |
| Administrateur | admin | admin123 | Complet (Statistiques + Produits + Clients + Factures + Utilisateurs) |
| Pharmacien | pharmacien1 | pharma123 | Produits + Clients + Factures |
| Utilisateur | user1 | user123 | Lecture seule (Produits + Clients) |

**Note:** Chaque rôle a une interface différente adaptée à ses permissions!
| Pharmacien | pharmacien1 | pharma123 |
| Utilisateur | user1 | user123 |

## 📖 Documentation - Spécifications complètes

- [Architecture et Conception](docs/conception/Architecture.md) - Diagrammes UML et design
- [Guide utilisateur](docs/guide-utilisateur.md) - Manuel d'utilisation
- [Guide développeur](docs/guide-developpeur.md) - Documentation technique
- [Présentation](docs/presentation/PRESENTATION.md) - Slides pour la défense
- [Installation](INSTALLATION.md) - Guide d'installation détaillé

## 🎨 Fonctionnalités UI

- 🎨 **Design moderne** avec thème professionnel bleu et vert
- 💳 **Cartes statistiques** animées pour les métriques clés
- 🔍 **Recherche en temps réel** dans tous les tableaux
- ✏️ **Édition inline** avec boutons d'action
- 📱 **Interface responsive** qui s'adapte à la taille de l'écran
- 🎯 **Validation de formulaires** avec retour visuel
- 🔔 **Alertes** pour stock bas et produits en expiration
- 🎭 **Animations** et effets de survol fluidesrchitecture.md)
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
- `Refactor:` Refactoring 4 janvier 2026  
  **Statut:** ✅ Production Ready

## 🚀 Points Forts du Projet

1. ✨ **Interface moderne et professionnelle** avec CSS personnalisé
2. 🔐 **Sécurité renforcée** (BCrypt, PreparedStatements, RBAC)
3. 🎯 **Architecture MVC solide** avec séparation des responsabilités
4. 📊 **Tableau de bord statistiques** pour suivi en temps réel
5. 🧪 **Tests unitaires** avec couverture de code
6. 📚 **Documentation exhaustive** (>120 pages)
7. 👥 **Contrôle d'accès par rôles** avec interfaces différenciées
8. 💾 **Base de données embarquée** sans configuration externe

---

_Projet réalisé avec passion pour le cours de Génie Logiciel II_ 🎓

## 📝 License

Ce projet est réalisé dans le cadre académique - Tous droits réservés.

## 📧 Contact

Pour toute question, contactez l'équipe via [email à compléter]

---

**Version:** 1.0.0  
**Dernière mise à jour:** 3 janvier 2026
