# Suivi Scolaire Parent-Enfant

Application mobile permettant aux parents d'élèves de suivre en temps réel la scolarité de leurs enfants dans un établissement primaire (du CP1 au CM2) : notes, paiements, absences, annonces et notifications de l'école.

Ce projet est **directement connecté** à l'application web de gestion scolaire **EcolePrime** — les deux partagent la même base de données MySQL. Toute action effectuée par le gestionnaire ou l'enseignant sur l'app web (saisie de notes, enregistrement de paiements, publication d'annonces) est immédiatement visible par le parent depuis l'app mobile.

Projet réalisé dans le cadre du cours de **Développement Mobile**, UJKZ — Cahier des charges du 16 juin 2026.

---

## Membres du groupe

| Nom | Rôle |
|---|---|
| **SOME Firmin** | Développeur |
| **MOYENGA Aziz** | Développeur |

---

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Architecture technique](#architecture-technique)
- [Structure du dépôt](#structure-du-dépôt)
- [Installation — Backend (API Laravel)](#installation--backend-api-laravel)
- [Installation — Application mobile (Android)](#installation--application-mobile-android)
- [Comptes de test](#comptes-de-test)
- [Liaison entre les deux applications](#liaison-entre-les-deux-applications)

---

## Fonctionnalités

### Authentification parent
- Connexion sécurisée par email et mot de passe (token Bearer via Laravel Sanctum)
- Identification de l'enfant suivi après connexion (nom + classe)
- Déconnexion
- Modification du mot de passe

### Tableau de bord
- Message de bienvenue personnalisé (Papa/Maman)
- Informations de l'élève (nom, prénom, classe)
- Moyenne générale et rang dans la classe
- Résumé des dernières notes obtenues
- Accès rapide à toutes les rubriques

### Consultation des notes
- Notes par matière (Français, Mathématiques, Sciences, Histoire-Géo, Anglais, EPS)
- Regroupement par trimestre (T1, T2, T3)
- Calcul et affichage des moyennes trimestrielles
- Téléchargement du bulletin scolaire au format PDF (en-tête établissement)

### Suivi des paiements
- Montant total dû (basé sur les frais de la classe), montant payé, montant restant
- Historique des versements effectués
- Simulation de paiement (formulaire : montant + mode de paiement)
- Génération et consultation du reçu de paiement au format PDF (avec infos élève, parent et établissement)

### Suivi des absences
- Liste des absences de l'élève
- Motif de l'absence (si renseigné) et statut justifiée / non justifiée

### Annonces et notifications
- Réception des annonces publiées par le gestionnaire depuis l'app web
- Notifications personnalisées par élève (réunions, examens, paiements, etc.)
- Badge "Nouveau" sur les notifications non lues
- Marquage automatique comme lu au clic

### Côté application web (EcolePrime — gestionnaire)
- Publication d'annonces avec envoi automatique de notifications à tous les élèves
- Les paiements effectués depuis l'app mobile sont immédiatement visibles dans l'app web
- Les notes saisies par les enseignants dans l'app web sont immédiatement consultables par les parents dans l'app mobile

---

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
| Stockage local | DataStore Preferences (token, élève sélectionné, civilité) |

---

## Structure du dépôt

```
suivi-scolaire-parent-enfant/
├── backend/        # API REST Laravel (projet web EcolePrime avec routes API ajoutées)
│   ├── app/
│   │   ├── Http/Controllers/Api/   # Contrôleurs API mobile
│   │   └── Models/
│   ├── resources/views/
│   │   ├── annonces/               # Vues web pour la gestion des annonces
│   │   └── pdf/                    # Templates PDF (reçu, bulletin)
│   └── routes/
│       ├── api.php                 # Routes API consommées par l'app mobile
│       └── web.php                 # Routes web (app EcolePrime)
├── mobile/         # Application Android (Kotlin / Jetpack Compose)
│   └── app/src/main/java/
│       ├── network/                # Modèles et service Retrofit
│       ├── repository/             # Logique métier et appels API
│       ├── viewmodel/              # ViewModels (MVVM)
│       └── ui/                     # Écrans Compose
└── README.md
```

---

## Installation — Backend (API Laravel)

### Prérequis
- PHP 8.2 ou supérieur
- Composer
- MySQL (XAMPP recommandé sur Windows)

### Étapes

```bash
cd backend
composer install
cp .env.example .env
php artisan key:generate
```

Configurer `.env` avec la base de données :
```
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=gestion_scolaire
DB_USERNAME=root
DB_PASSWORD=
```

Créer la base `gestion_scolaire` dans phpMyAdmin, puis :

```bash
php artisan migrate
php artisan storage:link
php artisan serve
```

L'API est accessible sur `http://127.0.0.1:8000/api`.
L'application web est accessible sur `http://127.0.0.1:8000`.

---

## Installation — Application mobile (Android)

### Prérequis
- Android Studio (dernière version stable)
- SDK Android (API 24 minimum, compileSdk 37)
- Le backend Laravel doit être lancé (`php artisan serve`)

### Étapes

1. Ouvrir le dossier `mobile/` dans Android Studio (**File → Open**).
2. Laisser Gradle synchroniser le projet.
3. Configurer l'URL de l'API dans `app/src/main/java/.../network/ApiConfig.kt` :

| Environnement | URL à utiliser |
|---|---|
| Émulateur Android | `http://10.0.2.2:8000/api/` |
| Téléphone physique via USB | `http://127.0.0.1:8000/api/` + tunnel `adb reverse tcp:8000 tcp:8000` |
| Réseau local (WiFi) | `http://192.168.x.x:8000/api/` (IP du serveur) |

4. Pour le tunnel USB (téléphone physique) :
```bash
adb reverse tcp:8000 tcp:8000
```
*(Relancer après chaque redémarrage de l'app ou déconnexion USB)*

5. Lancer l'application (**bouton Run ▶** dans Android Studio).

---

## Comptes de test

### Application web (EcolePrime)
| Rôle | Email |
|---|---|
| Gestionnaire | `gestionnaire@example.com` |
| Enseignant | `enseignant@example.com` |

### Application mobile (parent)
| Champ | Valeur |
|---|---|
| Email | `aziz@gmail.com` |
| Mot de passe | `password123` |
| Nom de l'enfant | `toe` |
| Classe | `CP2` |

---

## Liaison entre les deux applications

Les deux applications partagent la **même base de données MySQL** (`gestion_scolaire`) via une API REST ajoutée au projet web Laravel existant.

| Action côté app web | Résultat côté app mobile |
|---|---|
| Enseignant saisit les notes | Parent voit les nouvelles notes et moyennes |
| Gestionnaire enregistre un paiement | Parent voit le solde mis à jour |
| Gestionnaire publie une annonce | Parent reçoit une notification dans l'onglet Annonces |
| Parent effectue un paiement depuis l'app mobile | Gestionnaire voit le versement dans l'app web |

Aucune synchronisation manuelle n'est nécessaire — les deux interfaces lisent et écrivent dans la même base en temps réel.
