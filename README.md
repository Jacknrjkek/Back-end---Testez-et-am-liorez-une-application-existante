# 📚 Système de Gestion des Étudiants (Backend)

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)

Ce projet est le backend de l'application de gestion des étudiants pour la bibliothèque universitaire. Il expose une API REST pour gérer les utilisateurs et les opérations CRUD sur les étudiants, sécurisée par JWT.

---

## 🚀 Fonctionnalités Clés

- **Authentification Sécurisée** : Inscription et connexion via JWT (JSON Web Token).
- **Gestion des Utilisateurs** : Création d'agents de bibliothèque.
- **Gestion des Étudiants** : Opérations CRUD complètes (Créer, Lire, Mettre à jour, Sopprimer).
- **Architecture Robuste** : Basée sur Spring Boot, Spring Data JPA, et MySQL.
- **Conteneurisation** : Déploiement simplifié avec Docker Compose.

---

## 🛠️ Stack Technique

- **Langage** : Java 21
- **Framework** : Spring Boot 3.5.5
- **Base de Données** : MySQL
- **Sécurité** : Spring Security 6, JWT (jjwt)
- **Outils** : Maven, Docker, Jacoco (Couverture de code), Lombok, MapStruct

---

## 📋 Pré-requis

Avant de commencer, assurez-vous d'avoir installé :

- **Java JDK 21** : [Télécharger](https://jdk.java.net/21/)
- **Docker & Docker Compose** : [Télécharger](https://www.docker.com/products/docker-desktop/)
- **Maven 3.9+** : [Télécharger](https://maven.apache.org/download.cgi)

---

## ⚡ Démarrage Rapide

### 1. Cloner le projet
```bash
git clone https://github.com/votre-repo/etudiant-backend.git
cd etudiant-backend
```

### 2. Lancer l'application
La commande suivante compile le projet, lance les conteneurs Docker nécessaires (MySQL) et démarre le serveur :

```bash
mvn spring-boot:run
```

L'API sera accessible sur **http://localhost:8080**.

---

## 🧪 Exécution des Tests

Le projet inclut une suite complète de tests unitaires et d'intégration.
**Note** : Docker doit être lancé pour les tests d'intégration (Testcontainers).

### Lancer tous les tests
```bash
mvn clean test
```

### Rapport de Couverture (Jacoco)
Après l'exécution des tests, un rapport de couverture de code est généré :
- **Chemin** : `target/site/jacoco/index.html`
- Ouvrez ce fichier dans votre navigateur pour visualiser les métriques.

---

## 🗄️ Accès Base de Données (Docker)

Lorsque l'application tourne via `mvn spring-boot:run`, un conteneur MySQL est automatiquement créé.

### Vérification via Docker Desktop
Vous devriez voir le conteneur `etudiant-backend-mysql-1`.

![Docker Desktop](pictures/1-docker-desktop.png)

### Connexion Manuelle (Ligne de Commande)
Pour inspecter la base de données directement :

1. **Ouvrir un terminal dans le conteneur** :
   (Ou via l'onglet "Exec" de Docker Desktop)

2. **Connexion MySQL** :
   ```bash
   mysql -u etudiant_db -p
   # Mot de passe : etudiant_db
   ```

3. **Vérifier les tables** :
   ```sql
   USE etudiant_db;
   SHOW TABLES;
   SELECT * FROM user;
   ```

![Connexion BDD](pictures/2-docker-desktop-bdd.png)

---

## 📝 Endpoints Principaux

| Méthode | Endpoint        | Description                          | Accès |
|---------|----------------|--------------------------------------|-------|
| POST    | `/api/register` | Créer un nouvel utilisateur          | Public|
| POST    | `/api/login`    | Authentification et récupération JWT | Public|
| GET     | `/api/students` | Liste des étudiants                  | Privé |
| POST    | `/api/students` | Ajouter un étudiant                  | Privé |

---

## 👥 Auteur
Projet réalisé dans le cadre du parcours **Expert DevOps** d'OpenClassrooms.
**Projet 2** : "Testez et améliorez une application existante".
