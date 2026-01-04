# CAHIER DE CHARGE FONCTIONNEL

**Système de Gestion de Pharmacie (PharmaSys)**

---

## PAGE DE GARDE

**Titre du projet :** Système de Gestion de Pharmacie (PharmaSys)  
**Version :** 1.0  
**Date :** 30 décembre 2025  
**Groupe :** [Noms et matricules des membres à compléter]  
**Lien du dépôt GitHub :** [À compléter]

### Résumé Exécutif

PharmaSys est une solution informatisée pour automatiser la gestion quotidienne d'une pharmacie. Le système permet de gérer efficacement les stocks de médicaments, l'enregistrement des clients, la facturation et l'authentification sécurisée du personnel. L'objectif est de réduire les erreurs manuelles, d'améliorer la traçabilité des opérations et d'accélérer le processus de vente de 50% par rapport à une gestion papier. Les utilisateurs cibles sont les pharmaciens, les vendeurs et les administrateurs de pharmacie.

---

## 1. INTRODUCTION

### 1.1 Contexte

Aujourd'hui, le monde connaît une avancée considérable de la technologie dans tous les secteurs, y compris celui de la santé. La pharmacie et les dispensaires sont des établissements incontournables de l'informatique pour beaucoup d'usages.

La mauvaise organisation dans le travail de pharmaciens nous a inspiré à concevoir et à réaliser une application informatique pour une meilleure gestion de stock et des achats.

### 1.2 Problématique

Les pharmacies traditionnelles font face à plusieurs défis :

- Gestion manuelle des stocks sujette aux erreurs
- Difficultés à suivre les dates de péremption
- Temps de traitement des ventes trop long
- Manque de traçabilité des opérations
- Risques d'erreurs dans la facturation
- Absence de système de sauvegarde fiable

### 1.3 Objectifs SMART

1. **Réduire de 50%** le temps de traitement d'une vente (de 5 minutes à 2,5 minutes en moyenne) d'ici la mise en production
2. **Éliminer 90%** des erreurs de facturation liées aux calculs manuels
3. **Assurer 100%** de traçabilité des opérations critiques (connexions, ventes, modifications de stock)
4. **Permettre** la recherche d'un produit en moins de 5 secondes
5. **Garantir** la sauvegarde automatique de toutes les données avec possibilité de récupération

---

## 2. PÉRIMÈTRE

### 2.1 In-Scope (Inclus dans la V1 - 4 janvier 2026)

#### Fonctionnalités incluses :

- ✅ Authentification sécurisée avec gestion de session
- ✅ Gestion complète des produits (médicaments)
  - Recherche par nom, référence
  - Filtrage par quantité, date de péremption
  - Ajout, modification, suppression
  - Consultation des détails
- ✅ Gestion des clients
  - Enregistrement (nom, prénom, quartier, âge, téléphone)
  - Consultation et modification
- ✅ Système de facturation
  - Création de facture
  - Calcul automatique des totaux
  - Affichage des informations client et produit
  - Impression/Export de facture
- ✅ Journalisation des actions critiques
- ✅ Interface graphique intuitive
- ✅ Base de données locale (SQLite)

### 2.2 Out-of-Scope (Exclus de la V1)

- ❌ Gestion des bons de commande aux fournisseurs
- ❌ Statistiques et rapports avancés
- ❌ Accès distant/cloud
- ❌ Application mobile
- ❌ Gestion des prescriptions médicales
- ❌ Intégration avec des systèmes externes (assurance, comptabilité)
- ❌ Multi-pharmacie

### 2.3 Hypothèses et Contraintes

#### Hypothèses :

- L'application fonctionne sur un poste fixe Windows
- Connexion Internet non requise (mode hors ligne)
- Un seul utilisateur à la fois
- Langue : Français
- Données saisies en français

#### Contraintes :

- Budget limité : logiciels open source uniquement
- Délai : livraison avant le 4 janvier 2026 à 23h59
- Technologies imposées : Java, JavaFX
- Formation minimale des utilisateurs (interface intuitive)
- Conformité RGPD de base (pas de données médicales sensibles)

---

## 3. ACTEURS, RÔLES ET DROITS

### 3.1 Liste des Acteurs

| Acteur             | Description                                             | Nombre |
| ------------------ | ------------------------------------------------------- | ------ |
| **Administrateur** | Gère l'ensemble du système, configuration, utilisateurs | 1-2    |
| **Pharmacien**     | Effectue toutes les opérations courantes                | 2-5    |
| **Utilisateur**    | Accès limité en consultation                            | 0-3    |

### 3.2 Droits par Acteur

| Fonctionnalité     | Administrateur | Pharmacien                | Utilisateur |
| ------------------ | -------------- | ------------------------- | ----------- |
| Connexion          | ✅             | ✅                        | ✅          |
| Créer produit      | ✅             | ✅                        | ❌          |
| Modifier produit   | ✅             | ✅                        | ❌          |
| Supprimer produit  | ✅             | ⚠️ (seulement ses ajouts) | ❌          |
| Consulter produit  | ✅             | ✅                        | ✅          |
| Créer client       | ✅             | ✅                        | ❌          |
| Modifier client    | ✅             | ✅                        | ❌          |
| Créer facture      | ✅             | ✅                        | ❌          |
| Consulter factures | ✅             | ✅                        | ✅          |
| Consulter logs     | ✅             | ❌                        | ❌          |

### 3.3 Règles d'Accès

1. Toute connexion nécessite un identifiant et un mot de passe valides
2. Les sessions expirent après 2 heures d'inactivité
3. Les mots de passe doivent contenir au minimum 6 caractères
4. Un utilisateur ne peut pas supprimer les données créées par un autre (sauf administrateur)
5. Les actions critiques sont journalisées avec horodatage et utilisateur

---

## 4. BESOINS FONCTIONNELS

### 4.1 Parcours Utilisateurs (User Journeys)

#### Parcours 1 : Vente de médicament

1. **Connexion** → Saisie identifiants → Validation
2. **Accueil** → Choix "Nouvelle vente"
3. **Recherche produit** → Saisie nom/référence → Sélection
4. **Saisie client** → Recherche ou nouvel enregistrement
5. **Facturation** → Vérification quantités → Calcul total → Validation
6. **Confirmation** → Impression facture → Mise à jour stock

#### Parcours 2 : Ajout d'un nouveau médicament

1. **Connexion** → Authentification
2. **Menu produits** → Choix "Ajouter produit"
3. **Formulaire** → Saisie détails (nom, référence, quantité, prix, péremption, emplacement)
4. **Validation** → Vérification unicité référence
5. **Confirmation** → Enregistrement → Message succès

#### Parcours 3 : Recherche et consultation de stock

1. **Connexion** → Authentification
2. **Interface recherche** → Saisie critères
3. **Résultats** → Affichage liste filtrée
4. **Détails** → Sélection produit → Consultation complète
5. **Actions** → Modifier ou Supprimer (selon droits)

---

### 4.2 User Stories

#### US-01 : Authentification

**En tant que** pharmacien,  
**Je veux** me connecter avec mon identifiant et mot de passe,  
**Afin de** accéder aux fonctionnalités du système de manière sécurisée.

**Priorité :** Must  
**Critères d'acceptation :**

- **Given** un utilisateur enregistré avec identifiant "pharmacien1" et mot de passe correct,  
  **When** il saisit ses identifiants et clique sur "Connexion",  
  **Then** il accède au tableau de bord principal.
- **Given** un utilisateur avec un mot de passe incorrect,  
  **When** il tente de se connecter,  
  **Then** un message d'erreur "Identifiant ou mot de passe incorrect" s'affiche.
- **Given** un utilisateur non enregistré,  
  **When** il tente de se connecter,  
  **Then** l'accès est refusé avec un message explicite.

**Cas limites :**

- Tentative de connexion avec champs vides → Message "Veuillez remplir tous les champs"
- Plus de 5 tentatives échouées → Compte temporairement bloqué (15 minutes)
- Caractères spéciaux dans l'identifiant → Acceptés sans problème

---

#### US-02 : Recherche de produit

**En tant que** pharmacien,  
**Je veux** rechercher un produit par son nom ou sa référence,  
**Afin de** le trouver rapidement pour effectuer une vente ou consulter le stock.

**Priorité :** Must  
**Critères d'acceptation :**

- **Given** un produit "Paracétamol" existant dans la base,  
  **When** je saisis "Para" dans le champ de recherche,  
  **Then** tous les produits contenant "Para" s'affichent (recherche insensible à la casse).
- **Given** une référence unique "REF-001",  
  **When** je recherche par cette référence,  
  **Then** le produit exact correspondant s'affiche.
- **Given** une recherche sans résultats,  
  **When** aucun produit ne correspond,  
  **Then** le message "Aucun produit trouvé" s'affiche.

**Cas limites :**

- Recherche avec espaces multiples → Traités correctement
- Recherche avec caractères spéciaux → Gérée sans erreur
- Recherche vide → Affiche tous les produits

---

#### US-03 : Ajout d'un produit

**En tant que** pharmacien,  
**Je veux** ajouter un nouveau médicament dans le système,  
**Afin de** maintenir le stock à jour.

**Priorité :** Must  
**Critères d'acceptation :**

- **Given** les informations complètes d'un produit (nom, référence unique, quantité, prix, date péremption, emplacement),  
  **When** je valide l'ajout,  
  **Then** le produit est enregistré et un message "Produit ajouté avec succès" s'affiche.
- **Given** une référence déjà existante,  
  **When** je tente d'ajouter le produit,  
  **Then** le système refuse avec le message "Cette référence existe déjà".
- **Given** un champ obligatoire vide,  
  **When** je tente de valider,  
  **Then** un message indique le champ manquant.

**Cas limites :**

- Prix négatif → Message "Le prix doit être positif"
- Quantité négative → Message "La quantité doit être positive"
- Date de péremption passée → Avertissement mais enregistrement autorisé
- Nom > 100 caractères → Tronqué ou refusé

---

#### US-04 : Modification d'un produit

**En tant que** pharmacien,  
**Je veux** modifier les informations d'un produit existant,  
**Afin de** corriger une erreur ou mettre à jour les données.

**Priorité :** Must  
**Critères d'acceptation :**

- **Given** un produit existant sélectionné,  
  **When** je modifie la quantité de 10 à 50 et valide,  
  **Then** la quantité est mise à jour et confirmée.
- **Given** une modification de la référence vers une référence déjà utilisée,  
  **When** je valide,  
  **Then** le système refuse et indique le conflit.
- **Given** la modification d'un champ,  
  **When** je valide,  
  **Then** l'action est journalisée avec date et utilisateur.

**Cas limites :**

- Annulation de modification → Aucune donnée n'est altérée
- Modification simultanée (peu probable en mono-utilisateur) → Dernière sauvegarde gagne

---

#### US-05 : Suppression d'un produit

**En tant qu'** administrateur,  
**Je veux** supprimer un produit du système,  
**Afin de** retirer un médicament discontinué.

**Priorité :** Should  
**Critères d'acceptation :**

- **Given** un produit sélectionné,  
  **When** je clique sur "Supprimer",  
  **Then** une confirmation est demandée "Êtes-vous sûr de vouloir supprimer ce produit ?".
- **Given** la confirmation acceptée,  
  **When** je valide,  
  **Then** le produit est supprimé et un message "Produit supprimé" s'affiche.
- **Given** un produit lié à des factures existantes,  
  **When** je tente de le supprimer,  
  **Then** le système peut refuser ou marquer comme "inactif" (selon implémentation).

**Cas limites :**

- Annulation de la confirmation → Aucune suppression
- Tentative de suppression par utilisateur non autorisé → Message "Droits insuffisants"

---

#### US-06 : Enregistrement d'un client

**En tant que** pharmacien,  
**Je veux** enregistrer les informations d'un nouveau client,  
**Afin de** faciliter les ventes futures et garder un historique.

**Priorité :** Must  
**Critères d'acceptation :**

- **Given** les informations client (nom, prénom, quartier, âge, téléphone),  
  **When** je valide l'enregistrement,  
  **Then** le client est créé et un message "Client enregistré" apparaît.
- **Given** un numéro de téléphone invalide (moins de 8 chiffres),  
  **When** je valide,  
  **Then** un message "Numéro de téléphone invalide" s'affiche.
- **Given** un âge négatif ou supérieur à 150,  
  **When** je valide,  
  **Then** un message "Âge invalide" s'affiche.

**Cas limites :**

- Téléphone en double → Avertissement mais enregistrement autorisé
- Champs nom/prénom vides → Refus avec message explicite
- Caractères spéciaux dans le nom → Acceptés

---

#### US-07 : Création d'une facture

**En tant que** pharmacien,  
**Je veux** créer une facture pour une vente,  
**Afin de** formaliser la transaction et déduire du stock.

**Priorité :** Must  
**Critères d'acceptation :**

- **Given** un client sélectionné et des produits ajoutés au panier,  
  **When** je génère la facture,  
  **Then** le total est calculé automatiquement et la facture est affichée.
- **Given** une quantité demandée supérieure au stock,  
  **When** je tente de valider,  
  **Then** un message "Stock insuffisant" empêche la vente.
- **Given** une facture validée,  
  **When** l'enregistrement est confirmé,  
  **Then** le stock est automatiquement mis à jour.

**Cas limites :**

- Aucun produit dans le panier → Message "Veuillez ajouter au moins un produit"
- Client non sélectionné → Option de vente sans client ou obligation de sélection
- Annulation en cours de création → Aucun impact sur le stock

---

#### US-08 : Consultation de facture

**En tant que** utilisateur,  
**Je veux** consulter les factures existantes,  
**Afin de** vérifier l'historique des ventes.

**Priorité :** Should  
**Critères d'acceptation :**

- **Given** des factures enregistrées,  
  **When** j'accède à la liste des factures,  
  **Then** toutes les factures s'affichent avec numéro, date, client et montant.
- **Given** une facture sélectionnée,  
  **When** je clique sur "Détails",  
  **Then** tous les détails (produits, quantités, prix) s'affichent.
- **Given** une facture,  
  **When** je clique sur "Imprimer",  
  **Then** la facture est formatée pour impression.

**Cas limites :**

- Aucune facture → Message "Aucune facture enregistrée"
- Facture corrompue → Message d'erreur explicite

---

### 4.3 Exigences Fonctionnelles (RF)

| ID    | Exigence                         | Entrée                                                      | Traitement                                     | Sortie                                | Erreurs                         |
| ----- | -------------------------------- | ----------------------------------------------------------- | ---------------------------------------------- | ------------------------------------- | ------------------------------- |
| RF-01 | Authentification utilisateur     | Identifiant + mot de passe                                  | Vérification en base hashée                    | Session créée / Accès tableau de bord | Identifiants invalides          |
| RF-02 | Recherche de produit             | Nom ou référence partiel(le)                                | Requête SQL LIKE insensible à la casse         | Liste de produits correspondants      | Aucun résultat                  |
| RF-03 | Ajout de produit                 | Formulaire complet (nom, ref, qté, prix, date, emplacement) | Validation unicité référence + insertion       | Confirmation + ID produit             | Référence existante, champ vide |
| RF-04 | Modification de produit          | ID produit + champs modifiés                                | Validation + mise à jour                       | Confirmation                          | Référence en conflit            |
| RF-05 | Suppression de produit           | ID produit                                                  | Confirmation + suppression ou marquage inactif | Confirmation                          | Produit lié à factures          |
| RF-06 | Filtrage par date de péremption  | Date limite                                                 | Requête WHERE date_peremption < date_limite    | Liste produits périmés/proches        | Date invalide                   |
| RF-07 | Filtrage par quantité minimale   | Quantité seuil                                              | Requête WHERE quantite < seuil                 | Liste produits en rupture             | Seuil négatif                   |
| RF-08 | Enregistrement client            | Formulaire (nom, prénom, quartier, âge, tel)                | Validation format téléphone/âge + insertion    | ID client + confirmation              | Format invalide                 |
| RF-09 | Modification client              | ID client + champs modifiés                                 | Validation + mise à jour                       | Confirmation                          | Client inexistant               |
| RF-10 | Création facture                 | Client + liste produits + quantités                         | Calcul total + déduction stock + insertion     | Facture générée                       | Stock insuffisant               |
| RF-11 | Calcul automatique du total      | Liste [produit, quantité, prix]                             | Somme (quantité \* prix unitaire)              | Montant total                         | Prix négatif                    |
| RF-12 | Mise à jour automatique du stock | ID produit + quantité vendue                                | stock_actuel - quantité_vendue                 | Nouveau stock                         | Stock négatif                   |
| RF-13 | Impression/Export facture        | ID facture                                                  | Formatage en HTML/PDF                          | Fichier ou dialogue impression        | Facture inexistante             |
| RF-14 | Journalisation actions critiques | Action + utilisateur + timestamp                            | Insertion dans table logs                      | Log enregistré                        | Erreur disque                   |
| RF-15 | Consultation des logs            | Filtres date/utilisateur                                    | Requête SELECT avec filtres                    | Liste logs                            | Aucun log                       |
| RF-16 | Déconnexion                      | Commande déconnexion                                        | Suppression session                            | Retour écran connexion                | -                               |

---

## 5. DONNÉES ET RÈGLES MÉTIER

### 5.1 Entités Principales

#### Entité : Utilisateur (User)

| Champ         | Type         | Contraintes                                               | Description                 |
| ------------- | ------------ | --------------------------------------------------------- | --------------------------- |
| id            | INTEGER      | PRIMARY KEY, AUTO_INCREMENT                               | Identifiant unique          |
| username      | VARCHAR(50)  | UNIQUE, NOT NULL                                          | Nom d'utilisateur           |
| password_hash | VARCHAR(255) | NOT NULL                                                  | Hash BCrypt du mot de passe |
| role          | VARCHAR(20)  | NOT NULL, CHECK IN ('admin', 'pharmacien', 'utilisateur') | Rôle                        |
| created_at    | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP                                 | Date de création            |
| last_login    | TIMESTAMP    | NULL                                                      | Dernière connexion          |

#### Entité : Produit (Product)

| Champ           | Type          | Contraintes                 | Description           |
| --------------- | ------------- | --------------------------- | --------------------- |
| id              | INTEGER       | PRIMARY KEY, AUTO_INCREMENT | Identifiant unique    |
| nom             | VARCHAR(100)  | NOT NULL                    | Nom du médicament     |
| reference       | VARCHAR(50)   | UNIQUE, NOT NULL            | Référence produit     |
| quantite        | INTEGER       | NOT NULL, CHECK >= 0        | Quantité en stock     |
| prix            | DECIMAL(10,2) | NOT NULL, CHECK > 0         | Prix unitaire         |
| date_peremption | DATE          | NOT NULL                    | Date de péremption    |
| validite        | DATE          | NULL                        | Date de validité      |
| emplacement     | VARCHAR(50)   | NOT NULL                    | Rayon/Étagère         |
| created_by      | INTEGER       | FOREIGN KEY → User          | Créateur              |
| created_at      | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP   | Date d'ajout          |
| updated_at      | TIMESTAMP     | ON UPDATE CURRENT_TIMESTAMP | Dernière modification |

#### Entité : Client

| Champ      | Type         | Contraintes                 | Description           |
| ---------- | ------------ | --------------------------- | --------------------- |
| id         | INTEGER      | PRIMARY KEY, AUTO_INCREMENT | Identifiant unique    |
| nom        | VARCHAR(50)  | NOT NULL                    | Nom                   |
| prenom     | VARCHAR(50)  | NOT NULL                    | Prénom                |
| quartier   | VARCHAR(100) | NULL                        | Quartier/Adresse      |
| age        | INTEGER      | CHECK BETWEEN 0 AND 150     | Âge                   |
| telephone  | VARCHAR(20)  | NOT NULL                    | Numéro de téléphone   |
| created_at | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP   | Date d'enregistrement |

#### Entité : Facture (Invoice)

| Champ         | Type          | Contraintes                 | Description        |
| ------------- | ------------- | --------------------------- | ------------------ |
| id            | INTEGER       | PRIMARY KEY, AUTO_INCREMENT | Numéro de facture  |
| client_id     | INTEGER       | FOREIGN KEY → Client        | Client             |
| nom_client    | VARCHAR(100)  | NOT NULL                    | Nom complet client |
| date_facture  | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP   | Date et heure      |
| montant_total | DECIMAL(10,2) | NOT NULL                    | Montant total      |
| created_by    | INTEGER       | FOREIGN KEY → User          | Vendeur            |

#### Entité : Ligne_Facture (InvoiceItem)

| Champ         | Type          | Contraintes                 | Description               |
| ------------- | ------------- | --------------------------- | ------------------------- |
| id            | INTEGER       | PRIMARY KEY, AUTO_INCREMENT | Identifiant               |
| facture_id    | INTEGER       | FOREIGN KEY → Invoice       | Facture associée          |
| produit_id    | INTEGER       | FOREIGN KEY → Product       | Produit vendu             |
| nom_produit   | VARCHAR(100)  | NOT NULL                    | Nom produit (snapshot)    |
| reference     | VARCHAR(50)   | NOT NULL                    | Référence (snapshot)      |
| quantite      | INTEGER       | NOT NULL, CHECK > 0         | Quantité vendue           |
| prix_unitaire | DECIMAL(10,2) | NOT NULL                    | Prix unitaire (snapshot)  |
| sous_total    | DECIMAL(10,2) | NOT NULL                    | quantite \* prix_unitaire |

#### Entité : Log_Systeme (SystemLog)

| Champ     | Type         | Contraintes                 | Description             |
| --------- | ------------ | --------------------------- | ----------------------- |
| id        | INTEGER      | PRIMARY KEY, AUTO_INCREMENT | Identifiant             |
| user_id   | INTEGER      | FOREIGN KEY → User          | Utilisateur             |
| action    | VARCHAR(100) | NOT NULL                    | Type d'action           |
| details   | TEXT         | NULL                        | Détails supplémentaires |
| timestamp | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP   | Horodatage              |

### 5.2 Règles Métier

1. **Unicité des références** : Chaque produit doit avoir une référence unique dans le système
2. **Stock non négatif** : La quantité d'un produit ne peut jamais être négative
3. **Prix positif** : Le prix d'un produit doit être strictement positif
4. **Validation téléphone** : Le numéro de téléphone doit contenir au moins 8 chiffres
5. **Âge valide** : L'âge d'un client doit être entre 0 et 150 ans
6. **Facture immuable** : Une fois créée, une facture ne peut plus être modifiée (intégrité)
7. **Mise à jour stock automatique** : Lors de la création d'une facture, le stock est automatiquement déduit
8. **Snapshot des prix** : Les lignes de facture conservent le prix au moment de la vente (pas de référence dynamique)
9. **Journalisation obligatoire** : Toutes les actions critiques (connexion, création, modification, suppression) sont journalisées
10. **Session unique** : Un utilisateur ne peut avoir qu'une session active à la fois

---

## 6. EXIGENCES NON FONCTIONNELLES (RNF)

### 6.1 Performance

| ID     | Exigence                      | Objectif                                            |
| ------ | ----------------------------- | --------------------------------------------------- |
| RNF-01 | Temps de réponse recherche    | < 2 secondes pour 95% des requêtes                  |
| RNF-02 | Temps de chargement interface | < 3 secondes au démarrage                           |
| RNF-03 | Temps de création facture     | < 5 secondes du panier à la confirmation            |
| RNF-04 | Capacité base de données      | Support jusqu'à 10 000 produits et 100 000 factures |

### 6.2 Sécurité

| ID     | Exigence                  | Description                           |
| ------ | ------------------------- | ------------------------------------- |
| RNF-05 | Hashage des mots de passe | Algorithme BCrypt avec salt           |
| RNF-06 | Protection injection SQL  | Utilisation de PreparedStatements     |
| RNF-07 | Validation des entrées    | Validation côté client et serveur     |
| RNF-08 | Gestion des sessions      | Timeout après 2 heures d'inactivité   |
| RNF-09 | Contrôle d'accès          | Vérification des droits selon le rôle |
| RNF-10 | Traçabilité               | Logs de toutes les actions sensibles  |

### 6.3 Disponibilité et Fiabilité

| ID     | Exigence                  | Description                                        |
| ------ | ------------------------- | -------------------------------------------------- |
| RNF-11 | Gestion des erreurs       | Aucun crash non géré, messages explicites          |
| RNF-12 | Sauvegarde données        | Base de données SQLite avec sauvegarde automatique |
| RNF-13 | Récupération après erreur | Rollback des transactions en cas d'échec           |
| RNF-14 | Mode dégradé              | Fonctionnement hors ligne garanti                  |

### 6.4 Qualité du Code

| ID     | Exigence                | Description                              |
| ------ | ----------------------- | ---------------------------------------- |
| RNF-15 | Couverture de tests     | Minimum 60% du code couvert              |
| RNF-16 | Documentation du code   | Javadoc sur toutes les classes publiques |
| RNF-17 | Conventions de nommage  | Respect des conventions Java (CamelCase) |
| RNF-18 | Architecture            | Séparation Model-View-Controller         |
| RNF-19 | Complexité cyclomatique | Maximum 10 par méthode                   |

### 6.5 Ergonomie et UX

| ID     | Exigence             | Description                                  |
| ------ | -------------------- | -------------------------------------------- |
| RNF-20 | Interface responsive | Adaptation écrans 1024x768 minimum           |
| RNF-21 | Messages d'erreur    | Clairs et en français                        |
| RNF-22 | Confirmation actions | Confirmation avant suppression               |
| RNF-23 | Feedback utilisateur | Indication visuelle chargement/succès/erreur |
| RNF-24 | Navigation intuitive | Maximum 3 clics pour toute action            |
| RNF-25 | Raccourcis clavier   | Support des touches Entrée et Échap          |

---

## 7. MAQUETTES (UI/UX)

### 7.1 Écran de Connexion

```
┌────────────────────────────────────────────┐
│           PHARMASYS - Connexion            │
│                                            │
│  ┌──────────────────────────────────────┐ │
│  │  Nom d'utilisateur:                  │ │
│  │  [________________________]          │ │
│  │                                      │ │
│  │  Mot de passe:                       │ │
│  │  [________________________]          │ │
│  │                                      │ │
│  │         [  Se connecter  ]           │ │
│  │                                      │ │
│  │  Message d'erreur (si échec)         │ │
│  └──────────────────────────────────────┘ │
│                                            │
│         Version 1.0.0 - 2026              │
└────────────────────────────────────────────┘
```

### 7.2 Tableau de Bord Principal

```
┌────────────────────────────────────────────────────────┐
│ PharmaSys │ Utilisateur: Admin │ [Déconnexion]        │
├────────────────────────────────────────────────────────┤
│  Menu:                                                  │
│  [Produits] [Clients] [Factures] [Rapports]           │
├────────────────────────────────────────────────────────┤
│                                                         │
│  Statistiques du jour:                                 │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐               │
│  │ Ventes   │ │ Produits │ │ Alertes  │               │
│  │   12     │ │   156    │ │    3     │               │
│  └──────────┘ └──────────┘ └──────────┘               │
│                                                         │
│  Actions rapides:                                      │
│  [Nouvelle vente] [Ajouter produit] [Nouveau client]  │
│                                                         │
│  Alertes:                                              │
│  ⚠️ 3 produits arrivent à péremption                   │
│  ⚠️ 5 produits en stock faible                         │
│                                                         │
└────────────────────────────────────────────────────────┘
```

### 7.3 Interface de Recherche de Produit

```
┌────────────────────────────────────────────────────────┐
│ Gestion des Produits                    [← Retour]    │
├────────────────────────────────────────────────────────┤
│  Recherche: [________________] [🔍] [Ajouter produit] │
│                                                         │
│  Filtres: [Tous] [Stock faible] [Proche péremption]   │
│                                                         │
│  ┌──────────────────────────────────────────────────┐ │
│  │ N° │ Nom          │ Ref    │ Qté │ Prix │ Action│ │
│  ├────┼──────────────┼────────┼─────┼──────┼───────┤ │
│  │ 1  │ Paracétamol  │ 500mg  │ 10  │ 2500 │[Voir] │ │
│  │ 2  │ Paracétamol  │ 250mg  │ 18  │ 1000 │[Voir] │ │
│  │... │ ...          │ ...    │ ... │ ...  │ ...   │ │
│  └──────────────────────────────────────────────────┘ │
│                                                         │
│  Pagination: [<] 1 2 3 [>]                             │
└────────────────────────────────────────────────────────┘
```

### 7.4 Formulaire d'Ajout/Modification de Produit

```
┌────────────────────────────────────────────────────────┐
│ Ajouter un Produit                      [× Fermer]    │
├────────────────────────────────────────────────────────┤
│                                                         │
│  Nom du produit: *                                     │
│  [_______________________________________]             │
│                                                         │
│  Référence: *                                          │
│  [_______________________________________]             │
│                                                         │
│  Quantité: *          Prix unitaire (FCFA): *          │
│  [__________]         [__________]                     │
│                                                         │
│  Date de péremption: *                                 │
│  [__________] 📅                                       │
│                                                         │
│  Date de validité:                                     │
│  [__________] 📅                                       │
│                                                         │
│  Emplacement: *                                        │
│  [_______________________________________]             │
│                                                         │
│  * Champs obligatoires                                │
│                                                         │
│         [Annuler]        [Enregistrer]                 │
│                                                         │
└────────────────────────────────────────────────────────┘
```

### 7.5 Interface d'Enregistrement Client

```
┌────────────────────────────────────────────────────────┐
│ Enregistrement Client                   [× Fermer]    │
├────────────────────────────────────────────────────────┤
│                                                         │
│  Nom: *                      Prénom: *                 │
│  [__________________]        [__________________]      │
│                                                         │
│  Quartier:                                             │
│  [_______________________________________]             │
│                                                         │
│  Âge:                        Téléphone: *              │
│  [__________________]        [__________________]      │
│                                                         │
│                                                         │
│         [Annuler]        [Enregistrer]                 │
│                                                         │
└────────────────────────────────────────────────────────┘
```

### 7.6 Interface de Facturation

```
┌────────────────────────────────────────────────────────┐
│ Nouvelle Facture                                       │
├────────────────────────────────────────────────────────┤
│  Client: [Sélectionner...▼] [+ Nouveau]               │
│                                                         │
│  Nom Client: _________________________________         │
│  N° Produit: 200cds                                    │
│  N° Produit: 2000cds                                   │
│                                                         │
│  ┌──────────────────────────────────────────────────┐ │
│  │ Produit      │ Référence │ Quantité │ Prix │ ST │ │
│  ├──────────────┼───────────┼──────────┼──────┼────┤ │
│  │ Paracétamol  │ 500mg     │    10    │ 2500 │ 25k│ │
│  │ Paracétamol  │ 250mg     │    18    │ 1000 │ 18k│ │
│  │ ...          │ ...       │ ...      │ ...  │ ...│ │
│  └──────────────────────────────────────────────────┘ │
│                                                         │
│  [+ Ajouter produit]                                   │
│                                                         │
│                          TOTAL:  43 000 FCFA           │
│                                                         │
│         [Annuler]        [Imprimer]                    │
│                                                         │
└────────────────────────────────────────────────────────┘
```

---

## 8. CRITÈRES DE RECETTE (TESTS D'ACCEPTATION)

| Fonction             | Scénario                               | Résultat attendu                      | Statut    |
| -------------------- | -------------------------------------- | ------------------------------------- | --------- |
| Authentification     | Connexion avec identifiants valides    | Accès au tableau de bord              | ☐ OK ☐ KO |
| Authentification     | Connexion avec mot de passe incorrect  | Message d'erreur                      | ☐ OK ☐ KO |
| Authentification     | Connexion avec champs vides            | Message "Remplir tous les champs"     | ☐ OK ☐ KO |
| Recherche produit    | Recherche "Parac"                      | Liste des produits contenant "Parac"  | ☐ OK ☐ KO |
| Recherche produit    | Recherche sans résultat                | Message "Aucun produit trouvé"        | ☐ OK ☐ KO |
| Ajout produit        | Ajout avec tous les champs valides     | Produit enregistré + confirmation     | ☐ OK ☐ KO |
| Ajout produit        | Ajout avec référence existante         | Message "Référence existe déjà"       | ☐ OK ☐ KO |
| Ajout produit        | Ajout avec champ obligatoire vide      | Message d'erreur sur le champ         | ☐ OK ☐ KO |
| Ajout produit        | Ajout avec prix négatif                | Message "Prix doit être positif"      | ☐ OK ☐ KO |
| Modification produit | Modification de la quantité            | Quantité mise à jour                  | ☐ OK ☐ KO |
| Modification produit | Modification vers référence existante  | Message de conflit                    | ☐ OK ☐ KO |
| Suppression produit  | Suppression sans confirmation          | Aucune suppression                    | ☐ OK ☐ KO |
| Suppression produit  | Suppression avec confirmation          | Produit supprimé                      | ☐ OK ☐ KO |
| Filtrage             | Filtre "Stock faible" (< 10)           | Liste produits avec quantité < 10     | ☐ OK ☐ KO |
| Filtrage             | Filtre "Proche péremption" (< 30j)     | Liste produits péremption < 30 jours  | ☐ OK ☐ KO |
| Client               | Enregistrement avec infos complètes    | Client créé + confirmation            | ☐ OK ☐ KO |
| Client               | Enregistrement avec téléphone invalide | Message "Téléphone invalide"          | ☐ OK ☐ KO |
| Client               | Enregistrement avec âge < 0            | Message "Âge invalide"                | ☐ OK ☐ KO |
| Facture              | Création avec produits en stock        | Facture créée + stock mis à jour      | ☐ OK ☐ KO |
| Facture              | Création avec stock insuffisant        | Message "Stock insuffisant"           | ☐ OK ☐ KO |
| Facture              | Création sans produit                  | Message "Ajouter au moins un produit" | ☐ OK ☐ KO |
| Facture              | Calcul automatique du total            | Total = somme(qté × prix)             | ☐ OK ☐ KO |
| Facture              | Impression/Export                      | Facture formatée générée              | ☐ OK ☐ KO |
| Journalisation       | Connexion utilisateur                  | Log créé avec user + timestamp        | ☐ OK ☐ KO |
| Journalisation       | Création produit                       | Log créé avec détails                 | ☐ OK ☐ KO |
| Journalisation       | Suppression produit                    | Log créé avec détails                 | ☐ OK ☐ KO |
| Droits               | Utilisateur tente suppression          | Message "Droits insuffisants"         | ☐ OK ☐ KO |
| Déconnexion          | Clic sur déconnexion                   | Retour écran connexion                | ☐ OK ☐ KO |

---

## 9. RISQUES ET PLAN DE MITIGATION

| Risque                            | Probabilité | Impact | Mitigation                                                                                                                                                      |
| --------------------------------- | ----------- | ------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Retard de développement**       | Élevée      | Élevé  | - Découpage en sprints courts (3-4 jours)<br>- Priorisation Must/Should/Could<br>- Jalons intermédiaires avec revues<br>- Réduction périmètre si nécessaire     |
| **Difficulté technique (JavaFX)** | Moyenne     | Moyen  | - Prototypes précoces sur les parties critiques<br>- Formation rapide de l'équipe<br>- Utilisation de templates existants<br>- Support communauté/documentation |
| **Problème d'intégration**        | Moyenne     | Moyen  | - Pull Requests fréquentes (quotidiennes)<br>- Revues de code systématiques<br>- Tests d'intégration automatisés<br>- Branches feature courtes                  |
| **Perte de données**              | Faible      | Élevé  | - Commits fréquents sur GitHub<br>- Base de données avec transactions<br>- Script d'initialisation automatique<br>- Sauvegarde locale automatique               |
| **Bugs critiques en production**  | Moyenne     | Élevé  | - Tests unitaires (couverture 60%+)<br>- Tests d'acceptation avant livraison<br>- Validation manuelle scénarios critiques<br>- Gestion d'erreurs robuste        |
| **Incompréhension des besoins**   | Faible      | Moyen  | - CDC validé avant développement<br>- Maquettes partagées avec utilisateurs potentiels<br>- Démos intermédiaires si possible                                    |

---

## 10. PLANIFICATION

### 10.1 Planning Global

| Livrable                          | Date limite            | Responsable      | Statut      |
| --------------------------------- | ---------------------- | ---------------- | ----------- |
| **CDC v1.0**                      | 30 décembre 2025       | Équipe complète  | ✅ Complété |
| **Architecture + Conception**     | 1 janvier 2026         | [Chef de projet] | 🔄 En cours |
| **Implémentation couche données** | 2 janvier 2026         | [Dev Backend]    | ⏳ À venir  |
| **Implémentation UI**             | 3 janvier 2026         | [Dev Frontend]   | ⏳ À venir  |
| **Tests + Documentation**         | 4 janvier 2026 (matin) | [QA]             | ⏳ À venir  |
| **Application finale v1.0.0**     | 4 janvier 2026 (23h59) | Équipe complète  | ⏳ À venir  |
| **Présentation**                  | 5 janvier 2026         | Équipe complète  | ⏳ À venir  |

### 10.2 Découpage en Lots

#### Lot 1 : Fondations (1-2 janvier)

- Setup projet Maven + JavaFX
- Structure base de données SQLite
- Modèles de données (Entity classes)
- DAO (Data Access Objects)
- Utilitaires (validation, hash, logs)

#### Lot 2 : Authentification (2 janvier)

- Interface de connexion
- Service d'authentification
- Gestion de session
- Tests d'authentification

#### Lot 3 : Gestion Produits (2-3 janvier)

- Interface liste produits
- Recherche et filtres
- Formulaire ajout/modification
- Suppression avec confirmation
- Tests CRUD produits

#### Lot 4 : Gestion Clients (3 janvier)

- Interface liste clients
- Formulaire ajout/modification
- Tests CRUD clients

#### Lot 5 : Facturation (3 janvier)

- Interface création facture
- Sélection client et produits
- Calcul automatique
- Mise à jour stock
- Génération/Export
- Tests facturation

#### Lot 6 : Finalisation (4 janvier)

- Intégration complète
- Tests d'acceptation
- Documentation (README, guides)
- Préparation présentation
- Validation finale

---

## 11. ANNEXES

### 11.1 Glossaire

| Terme          | Définition                                                      |
| -------------- | --------------------------------------------------------------- |
| **CDC**        | Cahier Des Charges - Document de spécifications                 |
| **CRUD**       | Create, Read, Update, Delete - Opérations de base               |
| **DAO**        | Data Access Object - Couche d'accès aux données                 |
| **Hash**       | Fonction cryptographique à sens unique (BCrypt)                 |
| **Péremption** | Date à partir de laquelle un médicament ne doit plus être vendu |
| **Référence**  | Code unique identifiant un produit                              |
| **Session**    | Connexion active d'un utilisateur                               |
| **Snapshot**   | Copie des données à un instant T (prix, nom...)                 |
| **Stock**      | Quantité disponible d'un produit                                |

### 11.2 Références

- [JavaFX Documentation](https://openjfx.io/)
- [SQLite Documentation](https://www.sqlite.org/docs.html)
- [BCrypt Java Library](https://github.com/patrickfav/bcrypt)
- [Maven Getting Started](https://maven.apache.org/guides/getting-started/)

### 11.3 Conventions de Nommage

#### Base de données :

- Tables : snake_case pluriel (ex: `products`, `invoice_items`)
- Colonnes : snake_case (ex: `date_peremption`, `created_at`)

#### Code Java :

- Classes : PascalCase (ex: `ProductService`, `InvoiceController`)
- Méthodes : camelCase (ex: `createInvoice()`, `searchProducts()`)
- Constantes : UPPER_SNAKE_CASE (ex: `MAX_LOGIN_ATTEMPTS`)
- Variables : camelCase (ex: `productName`, `totalAmount`)

#### Git :

- Branches : type/description (ex: `feature/add-product`, `fix/login-bug`)
- Commits : "Type: Description" (ex: "Add: Product search feature")

---

## CONCLUSION

Ce cahier des charges définit de manière exhaustive les besoins, les fonctionnalités, les contraintes et les exigences du système PharmaSys. Il constitue la base contractuelle pour le développement et servira de référence pour la validation finale le 4 janvier 2026.

**Signatures :** (À compléter lors de la validation)

- Chef de projet : ******\_\_\_\_******
- Responsable qualité : ******\_\_\_\_******
- Développeurs : ******\_\_\_\_******

---

**Document approuvé le :** 30 décembre 2025  
**Version :** 1.0  
**Tag GitHub :** `cdc-v1.0`
