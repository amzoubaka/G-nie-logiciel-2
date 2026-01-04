# Instructions de Lancement - PharmaSys

## Méthode 1 : Avec Maven (Recommandée)

### Prérequis

- JDK 17 ou supérieur installé
- Maven 3.8+ installé
- Variable d'environnement JAVA_HOME configurée

### Étapes

1. **Ouvrir un terminal dans le dossier du projet**

```cmd
cd "c:\Users\Emmanuel Adoum\Desktop\geni_logiciel"
```

2. **Compiler le projet**

```cmd
mvn clean install
```

3. **Lancer l'application**

```cmd
mvn javafx:run
```

## Méthode 2 : Avec votre IDE

### IntelliJ IDEA

1. Ouvrir IntelliJ IDEA
2. File → Open → Sélectionner le dossier `geni_logiciel`
3. Attendre que Maven télécharge les dépendances
4. Clic droit sur `MainApp.java` → Run 'MainApp.main()'

### Eclipse

1. Ouvrir Eclipse
2. File → Import → Existing Maven Projects
3. Sélectionner le dossier `geni_logiciel`
4. Clic droit sur `MainApp.java` → Run As → Java Application

## Méthode 3 : JAR Exécutable

### Créer le JAR

```cmd
mvn clean package
```

### Exécuter le JAR

```cmd
java -jar target\pharmasys-1.0.0.jar
```

## Comptes de Test

Une fois l'application lancée, utilisez ces comptes :

| Utilisateur | Mot de passe | Rôle           |
| ----------- | ------------ | -------------- |
| admin       | admin123     | Administrateur |
| pharmacien1 | pharma123    | Pharmacien     |
| user1       | user123      | Utilisateur    |

## Résolution de Problèmes

### Erreur : "JAVA_HOME not set"

Définir JAVA_HOME :

```cmd
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%
```

### Erreur : "JavaFX components are missing"

Vérifier que les dépendances JavaFX sont bien téléchargées par Maven :

```cmd
mvn dependency:resolve
```

### L'application ne démarre pas

1. Vérifier que le port 8080 n'est pas utilisé
2. Vérifier les logs dans `logs/pharmasys.log`
3. Supprimer `pharmasys.db` et relancer

## Base de Données

Le fichier `pharmasys.db` est créé automatiquement au premier lancement dans le dossier racine du projet.

Pour réinitialiser : Supprimer `pharmasys.db` et relancer l'application.

## Support

En cas de problème, vérifiez :

1. Version de Java : `java -version` (doit être 17+)
2. Version de Maven : `mvn -version` (doit être 3.8+)
3. Les logs dans `logs/pharmasys.log`
