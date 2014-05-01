CREATE TABLE Personne (
email VARCHAR(50) NOT NULL,
nom VARCHAR(20) NOT NULL,
prenom VARCHAR(20) NOT NULL,
telPersonne VARCHAR(10) NOT NULL,
idAdresse INTEGER NOT NULL?
PRIMARY KEY (email)
);

CREATE TABLE Employe (
emailEmploye VARCHAR(50) NOT NULL,
idPizzeria INTEGER NOT NULL,
dateDeNaissance DATE NOT NULL,
PRIMARY KEY(emailEmploye),
FOREIN KEY (emailEmploye) REFERENCE Personee,
FOREIN KEY (idPizzeria) REFERENCE Pizzeria
);

Livreur (
emailLivreur VARCHAR(50) NOT NULL,
noLivreur, INTEGER NOT NULL,
PRIMARY KEY(emailLivreur),
FOREIN KEY (emailLivreur) REFERENCE Employe
);

CREATE TABLE Client (
idPizzeria INTEGER NOT NULL,
noClient INTEGER NOT NULL,
email VARCHAR(50)
PRIMARY KEY (idPizzeia, noClient),
FOREIN KEY (idPizzeria) Pizzeria
);

CREATE TABLE Commande (
idPizzeria INTEGER NOT NULL,
noLivraison INTEGER NOT NULL,
heurDepart DATE NOT NULL,
heurArrive DATE NOT NULL,
noPlaque VARCHAR(10) NOT NULL,


//Attention le numero livreur ne peut pas être mis a jours.

CREATE TABLE Client(
idPizzeria INTEGER NOT NULL, //PK CE
noClient INTEGER NOT NULL, //PK
email VARCHAR(50) NOT NULL //CE)

CREATE TABLE Commande (
idPizzeria INTEGER NOT NULL, //PK CE




                         
