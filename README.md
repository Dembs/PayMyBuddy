# Pay My Buddy

Application web de transfert d'argent entre amis.

## Fonctionnalités

- Transfert d'argent entre utilisateurs
- Virements entrants/sortants
- Gestion des relations
- Historique des transactions
- Gestion de profil

*Frais de service : 0.5% par transaction*

## Technologies

- Java 17
- Spring Boot 3
- Spring Security
- MySQL 8.0
- Thymeleaf
- Maven

## Démarrage

1. Cloner le repository : `git clone https://github.com/Dembs/PayMyBuddy`
2. Configurer la base de données MySQL : `CREATE DATABASE paymybuddy;`
3. Configurer les variables d'environnement : `DB_USERNAME=votre_user` et `DB_PASSWORD=votre_mdp`
4. Lancer l'application : `mvn spring-boot:run`
5. Accéder à l'application : `http://localhost:8080`

## Base de données

### Modèle Physique de Données
![MPD](/src/main/resources/readme/UML.png)
### Schéma de la base de données
L'application utilise MySQL 8.0 avec les tables suivantes :
- `users` - Informations des utilisateurs
- `connections` - Relations entre utilisateurs
- `transactions` - Historique des transferts et virements
- `account` - Compte en banque des utilisateurs