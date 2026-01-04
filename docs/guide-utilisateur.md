# Guide Utilisateur - PharmaSys

**Version 1.0.0**  
**Date:** 4 janvier 2026

---

## Table des Matières

1. [Introduction](#introduction)
2. [Connexion](#connexion)
3. [Gestion des Produits](#gestion-des-produits)
4. [Gestion des Clients](#gestion-des-clients)
5. [Gestion des Factures](#gestion-des-factures)
6. [Déconnexion](#déconnexion)
7. [FAQ](#faq)

---

## Introduction

PharmaSys est un système de gestion de pharmacie qui vous permet de :

- Gérer votre stock de médicaments
- Enregistrer vos clients
- Créer et gérer des factures
- Suivre les produits proches de la péremption

---

## Connexion

### Première utilisation

1. Lancez l'application PharmaSys
2. Sur l'écran de connexion, utilisez un des comptes par défaut :

| Utilisateur   | Mot de passe | Rôle           |
| ------------- | ------------ | -------------- |
| `admin`       | `admin123`   | Administrateur |
| `pharmacien1` | `pharma123`  | Pharmacien     |
| `user1`       | `user123`    | Utilisateur    |

3. Cliquez sur **"Se connecter"** ou appuyez sur **Entrée**

### En cas d'erreur

- **"Identifiant ou mot de passe incorrect"** : Vérifiez que vous avez bien saisi vos identifiants
- **Champs vides** : Assurez-vous de remplir les deux champs

---

## Gestion des Produits

### Accéder aux produits

1. Une fois connecté, cliquez sur l'onglet **"Produits"**
2. Vous verrez la liste de tous les médicaments en stock

### Rechercher un produit

1. Dans le champ de recherche en haut, tapez le nom ou la référence du produit
2. Cliquez sur **"Rechercher"** ou appuyez sur **Entrée**
3. Les résultats s'affichent dans le tableau

### Ajouter un nouveau produit

1. Cliquez sur le bouton vert **"+ Ajouter un produit"**
2. Remplissez le formulaire :
   - **Nom\*** : Nom du médicament (ex: Paracétamol 500mg)
   - **Référence\*** : Code unique (ex: PAR-500)
   - **Quantité\*** : Nombre d'unités en stock
   - **Prix\*** : Prix unitaire en FCFA
   - **Date de péremption\*** : Sélectionnez la date
   - **Emplacement\*** : Rayon ou étagère (ex: Rayon A)
3. Cliquez sur **"Enregistrer"**

> **Note** : Les champs avec \* sont obligatoires

### Modifier un produit

1. Dans la liste des produits, repérez le produit à modifier
2. Cliquez sur le bouton orange **"Modifier"**
3. Modifiez les informations nécessaires
4. Cliquez sur **"Enregistrer"**

### Supprimer un produit

1. Cliquez sur le bouton rouge **"Supprimer"**
2. Confirmez la suppression dans la boîte de dialogue
3. Le produit est définitivement supprimé

> **Attention** : Cette action est irréversible !

---

## Gestion des Clients

### Accéder aux clients

1. Cliquez sur l'onglet **"Clients"**
2. Vous verrez la liste de tous les clients enregistrés

### Rechercher un client

1. Tapez le nom, prénom ou numéro de téléphone dans le champ de recherche
2. Cliquez sur **"Rechercher"**

### Ajouter un nouveau client

1. Cliquez sur **"+ Ajouter un client"**
2. Remplissez le formulaire :
   - **Nom\*** : Nom de famille
   - **Prénom\*** : Prénom
   - **Quartier** : Quartier de résidence (optionnel)
   - **Âge** : Âge du client
   - **Téléphone\*** : Numéro de téléphone (minimum 8 chiffres)
3. Cliquez sur **"Enregistrer"**

### Règles de validation

- Le téléphone doit contenir au moins 8 chiffres
- L'âge doit être entre 0 et 150 ans
- Nom et prénom sont obligatoires

---

## Gestion des Factures

### Créer une nouvelle facture

1. Cliquez sur l'onglet **"Factures"**
2. Cliquez sur **"Nouvelle facture"**
3. Sélectionnez un client (ou créez-en un nouveau)
4. Ajoutez des produits :
   - Recherchez le produit
   - Indiquez la quantité
   - Cliquez sur **"Ajouter"**
5. Le total se calcule automatiquement
6. Cliquez sur **"Valider"** pour créer la facture

### Vérifications automatiques

- Le système vérifie que le stock est suffisant
- Si le stock est insuffisant, un message d'erreur s'affiche
- Le stock est automatiquement mis à jour après validation

### Imprimer une facture

1. Dans la liste des factures, sélectionnez une facture
2. Cliquez sur **"Imprimer"**
3. La facture s'ouvre prête à être imprimée

---

## Déconnexion

1. Cliquez sur le bouton rouge **"Déconnexion"** en haut à droite
2. Vous revenez à l'écran de connexion

> **Note** : Pensez à vous déconnecter en quittant l'application pour des raisons de sécurité

---

## FAQ

### Comment changer mon mot de passe ?

Contactez un administrateur pour modifier votre mot de passe.

### Que faire si j'ai supprimé un produit par erreur ?

La suppression est définitive. Il faudra recréer le produit manuellement.

### Puis-je annuler une facture ?

Dans la version actuelle, les factures ne peuvent pas être annulées une fois créées.

### Le stock ne se met pas à jour

Vérifiez que la facture a bien été validée. Le stock se met à jour uniquement à la validation.

### L'application ne démarre pas

Vérifiez que :

1. Java 17 ou supérieur est installé
2. Le fichier `pharmasys.db` n'est pas corrompu
3. Vous avez les droits d'écriture dans le dossier

### Comment sauvegarder mes données ?

Les données sont automatiquement sauvegardées dans le fichier `pharmasys.db`. Copiez ce fichier régulièrement pour faire des sauvegardes.

---

## Support

Pour toute question ou problème, contactez l'équipe de développement.

**Bonne utilisation de PharmaSys !** 💊
