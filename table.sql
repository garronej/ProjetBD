@dd.sql;

CREATE TABLE ADRESSES(
	IdAdresse INTEGER GENERATED BY DEFAULT ON NULL AS IDENTITY,
	numRue INTEGER NOT NULL,
	nomRue VARCHAR(20) NOT NULL,
	cp NCHAR(5) NOT NULL,
	ville VARCHAR(20) NOT NULL,
	CONSTRAINT PK_ID_ADRESSE 
		PRIMARY KEY (IdAdresse),
	CONSTRAINT VIOLATION_MUST_BE_POSITIVE
		CHECK(NumRue>0)
);

CREATE TABLE PIZZERIAS(
	IdPizzeria INTEGER GENERATED BY DEFAULT ON NULL AS IDENTITY,
	NomPizzeria VARCHAR(20),
	IdAdresse INTEGER,
		CONSTRAINT PK_ID_PIZZERIA
			PRIMARY KEY (IdPizzeria),
		CONSTRAINT FK_ADRESSE_PIZZERIAS
			FOREIGN KEY(IdAdresse) REFERENCES ADRESSES
);

-------------------------------
--Creation de la table personne
-------------------------------
CREATE TABLE PERSONNES(
	mail VARCHAR(40),
	nom VARCHAR(20) NOT NULL,
	prenom VARCHAR(20) NOT NULL,
	telephone VARCHAR(15) NOT NULL,
	idAdresse INTEGER,
		CONSTRAINT PK_MAIL 
			PRIMARY KEY (mail),
		CONSTRAINT FK_ADRESSE_PERSSONES
			FOREIGN KEY (idAdresse) REFERENCES ADRESSES,
		CONSTRAINT DUPLICATE_NOM_PRENOM_ADRESSE
			UNIQUE (nom, prenom, idAdresse),
		CONSTRAINT DUPLICATE_NOM_PRENOM_TELEPHONE
			UNIQUE (nom, prenom, telephone)
);


-------------------------------
--Creation de la table clients
-------------------------------
CREATE TABLE CLIENTS(
	idPizzeria INTEGER,
	noClient INTEGER NOT NULL,
	mail VARCHAR(40),
		CONSTRAINT PK_ID_Clients
			PRIMARY KEY(idPizzeria, noClient),
		CONSTRAINT FK_Pizzeria_Clients
			FOREIGN KEY(idPizzeria) REFERENCES PIZZERIAS,
		CONSTRAINT FK_Mail_Clients
			FOREIGN KEY(mail) REFERENCES PERSONNES,
		CONSTRAINT VIOLATION_POSITIV_CLIENT
			CHECK (noClient>0),
		CONSTRAINT VIOL_UNIQUE_PIZ_MAIL --Une perrsone n'est client qu'une fois d'une meme pizeria.
			UNIQUE (idPizzeria, mail)
);

-------------------------------
--Creation de la table employes
-------------------------------
CREATE TABLE EMPLOYES(
	idPizzeria INTEGER NOT NULL,
	noEmploye INTEGER NOT NULL,
	DateNaissance DATE NOT NULL,
	mail VARCHAR(40) NOT NULL,
		CONSTRAINT PK_ID_EMPLOYE
			PRIMARY KEY(idPizzeria, noEmploye),
		CONSTRAINT FK_CodePersonne_Employes 
			FOREIGN KEY(mail) REFERENCES PERSONNES,
		CONSTRAINT FK_IdPizzeria_Employes
			FOREIGN KEY(IdPizzeria) REFERENCES PIZZERIAS,
		CONSTRAINT UNIQUE_IDPIZZERIA_MAIL --Un employe ne peux travailler que dans une pizzeria et a un seul poste
			UNIQUE (mail),
		CONSTRAINT VIOLATION_POSITIV_NOEMPLOYE 
			CHECK (NoEmploye>0)
);



-------------------------------
--Creation de la table livreurs
-------------------------------

CREATE TABLE LIVREURS(
	IdPizzeria INTEGER NOT NULL,
	NoLivreur INTEGER NOT NULL,
			PRIMARY KEY(IdPizzeria, NoLivreur),
		CONSTRAINT FK_IdPizzeria_NoLivreurs
			FOREIGN KEY(IdPizzeria, NoLivreur) REFERENCES EMPLOYES(IdPizzeria, NoEmploye)
);

CREATE TABLE RESPONSABLES(
	idPizzeria INTEGER,
	noEmploye INTEGER,
		CONSTRAINT PK_IDPIZZERIA_NOEMPLOYE
			PRIMARY KEY(idPizzeria, noEmploye),
		CONSTRAINT FK_IDPIZZ_NOEMP 
			FOREIGN KEY(IdPizzeria, NoEmploye) REFERENCES EMPLOYES
);



--------------------------------------
--Creation de la table permis_vehicule
--------------------------------------
CREATE TABLE PERMIS_VEHICULE(
	typeVehicule NCHAR(1) NOT NULL,
		CONSTRAINT PK_TYPEVEHICULE
			PRIMARY KEY(typeVehicule),
		CONSTRAINT Non_Exist_TypeVehicule 
			CHECK ( typeVehicule IN ('A', 'B', 'C', 'D', 'E'))
);

-------------------------------
--Creation de la table vehicule
-------------------------------
CREATE TABLE VEHICULES(
	IdPizzeria INTEGER,
	NoPlaque VARCHAR(10),
	TypeVehicule NCHAR(1),
	CONSTRAINT PK_IDPIZZERIA_NOPLAQUE
		PRIMARY KEY(noPlaque),
	CONSTRAINT FK_IdPizzeria_Vehicules
		FOREIGN KEY(IdPizzeria) REFERENCES PIZZERIAS,
	CONSTRAINT FK_TypeVehicule_Vehicules 
		FOREIGN KEY(typeVehicule) REFERENCES PERMIS_VEHICULE
);



-----------------------------
--Creation de la table pizzas
-----------------------------
CREATE TABLE PIZZAS(
	IdPizzeria INTEGER,
	NomPizza VARCHAR(20) NOT NULL,
	taille VARCHAR(7) NOT NULL,
	Prix NUMBER(*,2) NOT NULL,
		CONSTRAINT PK_ID_Pizzas 
			PRIMARY KEY (IdPizzeria, NomPizza, taille),
		CONSTRAINT VIOLATION_POSITIVE_PIZZA 
			CHECK (Prix>0),
		CONSTRAINT Non_Exist_Taille 
			CHECK (Taille IN ('Mini','Moyenne','Grande'))
);


--------------------------------
--Creation de la table commandes
--------------------------------
CREATE TABLE COMMANDES(
	idPizzeria INTEGER,
	noCommande INTEGER,
	noClient INTEGER,
	prixCommande NUMBER(*,2), --sommes des lignes commandes fait en java
	heurDebut TIMESTAMP NOT NULL,
	CONSTRAINT PK_ID_Commandes 
		PRIMARY KEY (idPizzeria, noCommande),
	CONSTRAINT VIOLATION_POSITIV_COMMANDE
		CHECK (NoCommande > 0),
	CONSTRAINT FK_IdPizzeria_Commandes 
		FOREIGN KEY(IdPizzeria) REFERENCES PIZZERIAS,
	CONSTRAINT FK_NOLIVREUR
		FOREIGN KEY(idPizzeria,noClient) REFERENCES CLIENTS
);

--CREATE FUNCTION getPrixCommande( idPizzeriaIn in INTEGER, noCommandeIn in INTEGER) 
--RETURNS  AS '
--
--SELECT SUM(prixLigne) AS result 
--FROM LIGNES_COMANDE
--WHERE idPizzeria=$1 AND noCommande=$2;
--
--UPDATE COMMANDES
--SET prixCommande = result
--WHERE idPizzeria=$1 ANS noCommande=$2
--' LANGUAGE SQL;





--------------------------------------
--Creation de la table lignes commande
--------------------------------------
CREATE TABLE LIGNES_COMMANDE(
	idPizzeria INTEGER,
	noCommande INTEGER,
	noLigne INTEGER, 
	NomPizza VARCHAR(20), 
	Taille VARCHAR(7),
	nbPizza INTEGER NOT NULL,
		CONSTRAINT PK_IDPIZZ_NOCOM_NOLI
			PRIMARY KEY (idPizzeria, noCommande, noLigne),
		CONSTRAINT VIOLATION_POSITIV_LIGNE
			CHECK (NoLigne>0 AND NbPizza>0 ),
		CONSTRAINT FK_IdPizzaeria_NoCommande 
			FOREIGN KEY(IdPizzeria, NoCommande) REFERENCES COMMANDES,
		CONSTRAINT FK_NomPizza_Taille 
			FOREIGN KEY(IdPizzeria, NomPizza, Taille) REFERENCES PIZZAS
);


--------------------------------
--Creation de la table livraison
--------------------------------
CREATE TABLE LIVRAISONS(
	idPizzeria INTEGER,
	noLivraison INTEGER NOT NULL,
	noPlaque VARCHAR(10), --JAVA verifier que çe vehicule n'effectue pas une autres livraison dans la meme tranche horaire. + Le vehicule appartien a la pizzeria
	noLivreur INTEGER, --JAVA verifier que la meme chose pour le livreur.
	heurDepart TIMESTAMP NOT NULL, 
	heurRetour TIMESTAMP NOT NULL,
		CONSTRAINT PK_ID_LIvraisons 
			PRIMARY KEY (IdPizzeria, NoLivraison),
		CONSTRAINT VIOLATING_NOLIVRESON_POSITIVE 
			CHECK (NoLivraison>0),
		CONSTRAINT VIOLATIONG_TIME_CONSTRAINT 
			CHECK (HeurDepart<HeurRetour),
		CONSTRAINT FK_IdPiz_NoPlaque 
			FOREIGN KEY(noPlaque) REFERENCES VEHICULES,
		CONSTRAINT FK_IdPizzeria_NoLivreur 
			FOREIGN KEY(IdPizzeria, NoLivreur) REFERENCES EMPLOYES(IdPizzeria, NoEmploye)
);


--------------------------------
--Creation de la table ingredient
--------------------------------
CREATE TABLE INGREDIENTS (
	idPizzeria INTEGER,
	nomIngredient VARCHAR(20) NOT NULL,
		CONSTRAINT PK_ID_Ingredients 
			PRIMARY KEY(IdPizzeria, NomIngredient),
		CONSTRAINT FK_IdPizzeria_Ingredients 
			FOREIGN KEY (IdPizzeria) REFERENCES PIZZERIAS
);



-----------------------------
--Creation de la table extras
-----------------------------
CREATE TABLE EXTRAS(
	idPizzeria INTEGER,
	nomIngredient VARCHAR(20),
	noCommande INTEGER,
	noLigne INTEGER,
		CONSTRAINT PK_ID_Extras 
			PRIMARY KEY (idPizzeria, nomIngredient, noCommande, noLigne),
		CONSTRAINT FK_IdPiz_Extras
			FOREIGN KEY (IdPizzeria, nomIngredient) REFERENCES INGREDIENTS,
		CONSTRAINT FK_IDLigne_Comm_Extras 
			FOREIGN KEY(IdPizzeria, NoCommande, NoLigne) REFERENCES LIGNES_COMMANDE
);


----------------------------------
--Creation de la table abilitation
----------------------------------
CREATE TABLE ABILITATION(
	IdPizzeria INTEGER,
	NoLivreur INTEGER,
	typeVehicule NCHAR(1),
		CONSTRAINT PK_ID_Abilitation 
			PRIMARY KEY (IdPizzeria, NoLivreur, TypeVehicule),
		CONSTRAINT FK_IDLivreur_Abilitation 
			FOREIGN KEY (IdPizzeria, NoLivreur) REFERENCES LIVREURS,
		CONSTRAINT FK_TypeVehicule_Abilitation 
			FOREIGN KEY (typeVehicule) REFERENCES PERMIS_VEHICULE
);
