# Suivi Scolaire Parent-Enfant

Application mobile permettant aux parents d'élèves de suivre en temps réel la scolarité de leurs enfants dans un établissement primaire (du CP1 au CM2) : notes, paiements, absences et annonces de l'école.

Projet réalisé dans le cadre du cours de **Développement Mobile**, UJKZ — Cahier des charges du 16 juin 2026.

## Membres du groupe

- **SOME Firmin** — Développeur
- **MOYENGA Aziz** — Développeur

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Architecture technique](#architecture-technique)
- [Structure du dépôt](#structure-du-dépôt)
- [Installation — Backend (API Laravel)](#installation--backend-api-laravel)
- [Installation — Application mobile (Android)](#installation--application-mobile-android)
- [Compte de test](#compte-de-test)

## Fonctionnalités

### Authentification parent
- Connexion sécurisée par email et mot de passe (token API via Laravel Sanctum)
- Identification de l'enfant suivi (nom + classe) après connexion
- Déconnexion
- Modification du mot de passe

### Tableau de bord
- Message de bienvenue personnalisé (Papa/Maman) et description de l'application
- Informations de l'élève (nom, prénom, classe)
- Moyenne générale et rang dans la classe
- Résumé des dernières notes obtenues
- Accès rapide à toutes les rubriques

### Consultation des notes
- Liste des matières (Mathématiques, Français, Anglais, Histoire-Géographie, Sciences d'Observation, Éducation Civique et Morale, Sport/EPS)
- Notes par matière avec coefficients, regroupées par trimestre
- Calcul et affichage des moyennes trimestrielles par matière
- Téléchargement du bulletin scolaire au format PDF

### Suivi des paiements
- Historique des versements effectués
- Montant total dû, montant payé, montant restant
- Simulation de paiement (formulaire : montant + mode de paiement)
- Génération et consultation/impression du reçu de paiement au format PDF (en-tête établissement, infos élève et parent)

### Suivi des absences
- Liste des absences de l'élève
- Motif de l'absence (si renseigné) et statut justifiée / non justifiée

### Notifications et annonces
- Réception des annonces de l'école (réunions, examens, échéances de paiement)

## Architecture technique

| Couche | Technologie |
|---|---|
| Backend / API REST | Laravel 12 (PHP 8.2+), MySQL |
| Authentification API | Laravel Sanctum (token Bearer) |
| Génération PDF | barryvdh/laravel-dompdf |
| Application mobile | Android natif — Kotlin, Jetpack Compose |
| Architecture mobile | MVVM (UI → ViewModel → Repository → Retrofit) |
| Appels réseau | Retrofit2 + OkHttp + Gson |
| Asynchrone | Coroutines Kotlin + StateFlow |
| Navigation | Navigation Compose |
| Stockage local (token, session) | DataStore Preferences |

## Structure du dépôt

```
suivi-scolaire-parent-enfant/
├── backend/        # API REST Laravel
├── mobile/         # Application Android (Kotlin / Jetpack Compose)
└── README.md
```

## Installation — Backend (API Laravel)

### Prérequis
- PHP 8.2 ou supérieur
- Composer
- MySQL (XAMPP, WAMP, Laragon ou équivalent)

### Étapes

```bash
cd backend
composer install
cp .env.example .env
php artisan key:generate
```

Configurer la connexion à la base de données dans `.env` :
```
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=suivi_scolaire
DB_USERNAME=root
DB_PASSWORD=
```

Créer la base de données `suivi_scolaire` (via phpMyAdmin ou en ligne de commande), puis :

```bash
php artisan migrate
php artisan storage:link
php artisan serve
```

L'API est alors accessible sur `http://127.0.0.1:8000/api`.

## Installation — Application mobile (Android)

### Prérequis
- Android Studio (dernière version stable)
- SDK Android (API 24 minimum, compileSdk 37)

### Étapes

1. Ouvrir le dossier `mobile/` dans Android Studio (**Open**).
2. Laisser Gradle synchroniser le projet.
3. Configurer l'URL de l'API dans `app/src/main/java/.../network/ApiConfig.kt` (constante `BASE_URL`) selon l'environnement de test :
   - Émulateur Android : `http://10.0.2.2:8000/api/`
   - Téléphone physique via câble USB : `http://127.0.0.1:8000/api/` avec un tunnel `adb reverse tcp:8000 tcp:8000`
   - Réseau local : adresse IP du serveur, ex. `http://192.168.x.x:8000/api/`
4. Lancer l'application sur un émulateur ou un appareil physique (bouton **Run**).

## Compte de test

| Champ | Valeur |
|---|---|
| Email | `parent@test.com` |
| Mot de passe | `password123` |
| Nom de l'enfant | `Kaboré` |
| Classe | `CM1` |
