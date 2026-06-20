# 📚 Gestion Scolaire — Guide d'installation

Bienvenue ! Ce guide explique comment installer et lancer ce projet Laravel sur votre machine.

---

## ✅ Prérequis — Logiciels à installer

Avant de commencer, assurez-vous d'avoir installé ces logiciels :

| Logiciel | Lien de téléchargement |
|---|---|
| **XAMPP** (PHP + MySQL) | https://www.apachefriends.org/fr/index.html |
| **Composer** (gestionnaire PHP) | https://getcomposer.org/download/ |
| **VS Code** | https://code.visualstudio.com/ |
| **Node.js** (si besoin) | https://nodejs.org/ |

---

## 📁 Étape 1 — Placer le projet

1. Extraire le fichier `.zip` reçu
2. Copier le dossier `gestion-scolaire` dans :
   ```
   C:\xampp\htdocs\
   ```
   Résultat : `C:\xampp\htdocs\gestion-scolaire\`

---

## ⚙️ Étape 2 — Installer les dépendances

Ouvrir le dossier dans **VS Code**, puis ouvrir le terminal (`Ctrl + J`) et taper :

```bash
composer install
```

> ⏳ Cela peut prendre quelques minutes la première fois.

---

## 🔧 Étape 3 — Configurer l'environnement

### 3.1 Créer le fichier `.env`

```bash
copy .env.example .env
```

### 3.2 Générer la clé de l'application

```bash
php artisan key:generate
```

### 3.3 Configurer la base de données

Ouvrir le fichier `.env` et modifier ces lignes :

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=gestion_scolaire
DB_USERNAME=root
DB_PASSWORD=
```

> 💡 Par défaut avec XAMPP : nom d'utilisateur = `root`, mot de passe = vide

---

## 🗄️ Étape 4 — Créer la base de données

1. Ouvrir **XAMPP Control Panel** et démarrer **Apache** et **MySQL**
2. Aller sur http://localhost/phpmyadmin
3. Cliquer sur **"Nouvelle base de données"**
4. Nommer la base : `gestion_scolaire`
5. Cliquer sur **Créer**

---

## 🚀 Étape 5 — Lancer les migrations

Dans le terminal VS Code :

```bash
php artisan migrate
```

Si vous voulez aussi remplir la base avec des données de test :

```bash
php artisan db:seed
```

---

## ▶️ Étape 6 — Lancer l'application

```bash
php artisan serve
```

Puis ouvrir votre navigateur et aller sur :

```
http://127.0.0.1:8000
```

🎉 **L'application est lancée !**

---

## ❓ Problèmes fréquents

| Problème | Solution |
|---|---|
| `composer: command not found` | Réinstaller Composer et redémarrer VS Code |
| Erreur de connexion base de données | Vérifier que MySQL est démarré dans XAMPP |
| Page blanche ou erreur 500 | Vérifier que le fichier `.env` existe et que la clé est générée |
| `php: command not found` | Ajouter PHP au PATH Windows (voir ci-dessous) |

### Ajouter PHP au PATH Windows

1. Ouvrir **Paramètres Windows** → Rechercher "Variables d'environnement"
2. Cliquer sur **"Variables d'environnement"**
3. Dans "Variables système", sélectionner **Path** → **Modifier**
4. Ajouter : `C:\xampp\php`
5. Redémarrer VS Code

---

## 📞 Contact

Pour toute question, contacter le développeur du projet.
