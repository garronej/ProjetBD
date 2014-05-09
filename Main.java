import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.sql.*;

public class Main {

	static  private Boolean continuer=true;
	static  private String commande;
	static  private String commandeSQL;
	static  private String finalQuery;
	static  private PreparedStatement query=null;
	static  private String baseQuery;
	static  private Requete requete = new Requete();
	static  private Scanner entree = new Scanner(System.in);

	public static void main(String[] args) throws Exception {



		requete.startConnection();

		System.out.println("Projet Base de Donnée:\n");


		do {
			System.out.println("Entrer votre commande:");

			commande = entree.nextLine();

			switch (commande) {
				case "stat":
					stat();
				break;
				case "help":
					help();
				break;

				case "exit":
					continuer=false;
				break;
				case "sql":
					sql();
				break;
				case "personne":
					personne();
				break;
				case "adresse":
					adresse();
				break;
				case "pizzeria":
					pizzeria();
				break;
				case "client":
					client();
				break;
				case "employe":
					employe();
				break;
				case "livreur":
					livreur();
				break;
				case "responsable":
					responsable();
				break;
				case "permis":
					permis();
				break;
				case "vehicule":
					vehicule();
				break;
				case "pizza":
					pizza();
				break;
				case "commande":
					commande();
				break;
				default:
				System.out.println("Mauvaise commande, entrer \"help\"pour afficher les commandes possibles.");
			}
		} while( continuer );
		requete.endConnection();
		entree.close();

	}

	static private void help() {
		System.out.println("Menu d'aide.\n Liste des commandes:");
		System.out.println("exit pour quitter");
		System.out.println("help pour afficher l'aide");
		System.out.println("personne pour créer, modifier ou supprimer une personne");
		System.out.println("adresse pour créer, modidier ou supprimer une adresse");
		System.out.println("pizzeria pour créer, modifier ou supprimer une pizzeria");
		System.out.println("client pour créer, modifier ou supprimer un client");
		System.out.println("employe pour créer, modifier ou supprimer un employé");
		System.out.println("livreur pour créer, modifier ou supprimer un livreur");
		System.out.println("");
		System.out.println("");


	}



	static private void sql() throws Exception  {
		System.out.println("Tapez votre requete Sql (1 ligne sans ';')");
		commandeSQL = entree.nextLine();
		requete.setQuery(commandeSQL);
		requete.requete();
		System.out.println("fin de requete");

	}



	static private void personne() throws Exception {
		System.out.println("Voulez vous a:ajouter, m:modifier ou s:supprimer une personne ?");
		commandeSQL=entree.nextLine();
		if ( commandeSQL.contentEquals("a") ){
			baseQuery="INSERT INTO Personnes(mail, nom, prenom, telephone, idAdresse) VALUES(?, ?, ?, ?, ?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			do{
				System.out.println("adresse mail ?");
				commandeSQL = entree.nextLine();
			}while(!commandeSQL.contains("@"));
			query.setString(1,commandeSQL);
			do {
				System.out.println("nom ?");
				commandeSQL = entree.nextLine();
			}while (commandeSQL.contentEquals(""));
			query.setString(2,commandeSQL);
			do {
				System.out.println("prenom ?");
				commandeSQL = entree.nextLine();
			}while (commandeSQL.contentEquals(""));
			query.setString(3,commandeSQL);
			do {
				System.out.println("numéro de téléphone ?");
				commandeSQL = entree.nextLine();
			}while (commandeSQL.contentEquals(""));
			query.setString(4,commandeSQL);
			do{
				do {
					System.out.println("IdAdresse:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(5,Integer.parseInt(commandeSQL));
			query.executeUpdate();
		} else if (commandeSQL.contentEquals("m") ) {
			baseQuery="SELECT mail, nom, prenom FROM Personnes";
			requete.setQuery(baseQuery);
			System.out.println("Liste des personnes :");
			requete.requete();
			do{
				System.out.println("mail de la personne à modifier");
				commandeSQL=entree.nextLine();
			}while(!commandeSQL.contains("@"));
			baseQuery="SELECT mail, nom, prenom FROM Personnes WHERE mail=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setString(1,commandeSQL);
			try {
				if(query.executeUpdate()==0) {
					System.out.println("personne non trouvée");
					return;
				}
				requete.dumpResultSet(query.getResultSet());
			} catch(Exception e) {
				System.out.println("Problème lors de la récupération de la personne");
			}
			String mail=commandeSQL;
			System.out.println("Quels sont les champs que vous voulez modifier ? (mettre tous les champs, séparés par un espace)");
			String modifications=entree.nextLine();
			if (modifications.contains("nom")) {
				baseQuery="UPDATE Personnes SET nom=? WHERE mail=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					System.out.println("Nouveau nom de la personne :");
					commandeSQL=entree.nextLine();
				}while (commandeSQL.contentEquals(""));
				query.setString(1, commandeSQL);
				query.setString(2,mail);
				try {
					if(query.executeUpdate()>0)
						System.out.println("nom a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du nom");
				}

			}
			if (modifications.contains("prenom")) {
				baseQuery="UPDATE Personnes SET prenom=? WHERE mail=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					System.out.println("Nouveau prenom de la personne :");
					commandeSQL=entree.nextLine();
				}while (commandeSQL.contentEquals(""));
				query.setString(1, commandeSQL);
				query.setString(2,mail);
				try {
					if(query.executeUpdate()>0)
						System.out.println("Prénom a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du Prénom");
				}

			}
			if (modifications.contains("telephone")) {
				baseQuery="UPDATE Personnes SET telephone=? WHERE mail=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					System.out.println("Nouveau téléphone de la personne :");
					commandeSQL=entree.nextLine();
				}while (commandeSQL.contentEquals(""));
				query.setString(1, commandeSQL);
				query.setString(2,mail);
				try {
					if(query.executeUpdate()>0)
						System.out.println("Numéro de téléphone a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du numéro de téléphone");
				}

			}

			if (modifications.contains("idadresse")) {
				baseQuery="UPDATE Personnes SET IdAdresse=? WHERE mail=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do{
					do {
						System.out.println("Nouvelle IdAdresse:");
						commandeSQL = entree.nextLine();
					}while((!commandeSQL.matches("^[0-9]+$")));
				}while( Integer.parseInt(commandeSQL)==0);
				query.setInt(1,Integer.parseInt(commandeSQL));
				query.setString(2,mail);
				try {
					if(query.executeUpdate()>0)
						System.out.println("IdAdresse a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de l'idAdresse");
				}

			}

		} else if (commandeSQL.contentEquals("s")) {
			baseQuery="SELECT mail, nom, prenom FROM Personnes";
			requete.setQuery(baseQuery);
			System.out.println("Liste des membres :");
			requete.requete();
			do{
				System.out.println("mail de la personne à modifier");
				commandeSQL=entree.nextLine();
			}while(!commandeSQL.contains("@"));
			baseQuery="DELETE FROM Personnes WHERE mail=?"; 
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setString(1,commandeSQL);
			try {
				if(query.executeUpdate()>0)
					System.out.println("Suppression du membre faite avec succès");
			} catch(Exception e) {
				System.out.println("Poblème lors de la suppression de la personne.");
			}

		}
		requete.nextTransaction();
	}

	static private void adresse() throws Exception {
		System.out.println("Voulez vous a:ajouter, m:modifier ou s:supprimer une adresse?");
		commandeSQL=entree.nextLine();
		if ( commandeSQL.contentEquals("a") ){
			baseQuery="INSERT INTO ADRESSES(NumRue,NomRue,CP,Ville) VALUES(?, ?, ?, ?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			do{
				do {
					System.out.println("Numéro de rue:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(1,Integer.parseInt(commandeSQL));
			do { 
				System.out.println("Nom de rue:");
				commandeSQL = entree.nextLine();
			}while ( commandeSQL.contentEquals(""));
			query.setString(2, commandeSQL);
			do { 
				System.out.println("Code Postal:");
				commandeSQL = entree.nextLine();
			}while ( commandeSQL.contentEquals(""));
			query.setString(3, commandeSQL);
			do { 
				System.out.println("Ville:");
				commandeSQL = entree.nextLine();
			}while ( commandeSQL.contentEquals(""));
			query.setString(4, commandeSQL);
			query.executeUpdate();

		}else if ( commandeSQL.contentEquals("m") ){
			baseQuery="SELECT IdAdresse, NumRue NomRue, Ville FROM ADRESSES";
			requete.setQuery(baseQuery);
			System.out.println("Liste des Adresses :");
			requete.requete();
			do{
				do{
					System.out.println("IdAdresse:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while((Integer.parseInt(commandeSQL)==0));
			baseQuery="SELECT IdAdresse, NumRue NomRue, Ville FROM ADRESSES WHERE IdAdresse=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setInt(1,Integer.parseInt(commandeSQL));
			try {
				if(query.executeUpdate()==0) {
					System.out.println("Adresse non trouvée");
					return;
				}
				requete.dumpResultSet(query.getResultSet());
			} catch(Exception e) {
				System.out.println("Problème lors de la récupération de l'Adresse.");
			}
			int idAdresse=Integer.parseInt(commandeSQL); 
			System.out.println("Quels sont les champs que vous voulez modifier ? (mettre tous les champs, séparés par un espace)");
			String modifications=entree.nextLine();
			if (modifications.contains("numrue")) {
				baseQuery="UPDATE ADRESSES SET numRue=? WHERE IdAdresse=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					do {
						System.out.println("Nouveau numéro de rue :");
						commandeSQL=entree.nextLine();
					}while ((!commandeSQL.matches("[0-9]+")));
				}while ((Integer.parseInt(commandeSQL)==0));
				query.setInt(1, Integer.parseInt(commandeSQL));
				query.setInt(2,idAdresse);
				try {
					if(query.executeUpdate()>0)
						System.out.println("Numéro de rue a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du numéro de rue");
				}

			}

			if (modifications.contains("nomrue")) {
				baseQuery="UPDATE ADRESSES SET nomRue=? WHERE IdAdresse=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					System.out.println("Nouveau nom de rue :");
					commandeSQL=entree.nextLine();
				}while (commandeSQL.contentEquals(""));
				query.setString(1, commandeSQL);
				query.setInt(2,idAdresse);
				try {
					if(query.executeUpdate()>0)
						System.out.println("Nom de rue a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du nom  de rue");
				}

			}

			if (modifications.contains("cp")) {
				baseQuery="UPDATE ADRESSES SET cp=? WHERE IdAdresse=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					System.out.println("Nouveau CodePostal :");
					commandeSQL=entree.nextLine();
				}while (commandeSQL.contentEquals(""));
				query.setString(1, commandeSQL);
				query.setInt(2,idAdresse);
				try {
					if(query.executeUpdate()>0)
						System.out.println("Code postal a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du code postal");
				}

			}

			if (modifications.contains("ville")) {
				baseQuery="UPDATE ADRESSES SET Ville=? WHERE IdAdresse=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					System.out.println("Nouvelle ville:");
					commandeSQL=entree.nextLine();
				}while (commandeSQL.contentEquals(""));
				query.setString(1, commandeSQL);
				query.setInt(2,idAdresse);
				try {
					if(query.executeUpdate()>0)
						System.out.println("Ville a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de la ville");
				}

			}
			if (modifications.contains("idadresse")) {
				baseQuery="UPDATE ADRESSES SET IdAdresse=? WHERE IdAdresse=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					do {
						System.out.println("Nouveau idAdresse :");
						commandeSQL=entree.nextLine();
					}while ((!commandeSQL.matches("[0-9]+")));
				}while ((Integer.parseInt(commandeSQL)==0));
				query.setInt(1, Integer.parseInt(commandeSQL));
				query.setInt(2,idAdresse);
				try {
					if(query.executeUpdate()>0)
						System.out.println("Numéro de rue a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du numéro de rue");
				}

			}

		} else if (commandeSQL.contentEquals("s")) {
			baseQuery="SELECT IdAdresse, NumRue, NomRue, Ville FROM ADRESSES";
			requete.setQuery(baseQuery);
			System.out.println("Liste des adresses :");
			requete.requete();
			do{
				do{
					System.out.println("IdAdresse");
					commandeSQL=entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);

			baseQuery="DELETE FROM ADRESSES WHERE IdAdresse=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setInt(1,Integer.parseInt(commandeSQL));
			try {
				if(query.executeUpdate()>0)
					System.out.println("Suppression de l'adresse faite avec succès");
			} catch(Exception e) {
				System.out.println("Poblème lors de la suppression de l'adresse.");
			}


		}
		requete.nextTransaction();
	}

	static private void pizzeria() throws Exception {
		System.out.println("Voulez vous a:ajouter, m:modifier ou s:supprimer une pizzeria ?");
		commandeSQL=entree.nextLine();
		if ( commandeSQL.contentEquals("a") ){
			baseQuery="INSERT INTO Pizzerias(nomPizzeria, idAdresse) VALUES(?, ?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			do {
				System.out.println("nom de la pizzeria ?");
				commandeSQL = entree.nextLine();
			}while (commandeSQL.contentEquals(""));
			query.setString(1,commandeSQL);
			do{
				do {
					System.out.println("IdAdresse:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(2,Integer.parseInt(commandeSQL));
			query.executeUpdate();

		} else if (commandeSQL.contentEquals("m") ) {
			baseQuery="SELECT IdPizzeria, nomPizzeria  FROM Pizzerias";
			requete.setQuery(baseQuery);
			System.out.println("Liste des membres :");
			requete.requete();
			do{
				do {
					System.out.println("Id de la pizzeria à modifier:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);

			baseQuery="SELECT IdPizzeria, nomPizzeria  FROM Pizzerias WHERE IdPizzeria=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setString(1,commandeSQL);
			try {
				if(query.executeUpdate()==0) {
					System.out.println("Pizzeria non trouvée");
					return;
				}
				requete.dumpResultSet(query.getResultSet());
			} catch(Exception e) {
				System.out.println("Problème lors de la récupération de la Pizzeria");
			}
			int id=Integer.parseInt(commandeSQL); 
			System.out.println("Quels sont les champs que vous voulez modifier ? (mettre tous les champs, séparés par un espace)");
			String modifications=entree.nextLine();

			if (modifications.contains("nompizzeria")) {
				baseQuery="UPDATE Pizzerias SET nomPizzeria=? WHERE idPizzeria=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					System.out.println("Nouveau nom de la pizzeria :");
					commandeSQL=entree.nextLine();
				}while (commandeSQL.contentEquals(""));
				query.setString(1, commandeSQL);
				query.setInt(2,id);
				try {
					if(query.executeUpdate()>0)
						System.out.println("nomPizzeria a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du nom");
				}

			}

			if (modifications.contains("idpizzeria")) {
				baseQuery="UPDATE Pizzerias SET idPizzeria=? WHERE idPizzeria=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do{
					do {
						System.out.println("Nouvelle IdPizzeria:");
						commandeSQL = entree.nextLine();
					}while((!commandeSQL.matches("^[0-9]+$")));
				}while( Integer.parseInt(commandeSQL)==0);
				query.setInt(1,Integer.parseInt(commandeSQL));
				query.setInt(2,id);
				try {
					if(query.executeUpdate()>0)
						System.out.println("idPizzeria a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du idPizzeria");
				}

			}

			if (modifications.contains("idadresse")) {
				baseQuery="UPDATE Pizzerias SET idAdresse=? WHERE idPizzeria=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do{
					do {
						System.out.println("Nouvelle IdAdresse:");
						commandeSQL = entree.nextLine();
					}while((!commandeSQL.matches("^[0-9]+$")));
				}while( Integer.parseInt(commandeSQL)==0);
				query.setInt(1,Integer.parseInt(commandeSQL));
				query.setInt(2,id);
				try {
					if(query.executeUpdate()>0)
						System.out.println("idPizzeria a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du idPizzeria");
				}

			}

		} else if (commandeSQL.contentEquals("s")) {
			baseQuery="SELECT IdPizzeria, NomPizzeria FROM PIZZERIA";
			requete.setQuery(baseQuery);
			System.out.println("Liste des Pizzerias :");
			requete.requete();
			do{
				do{
					System.out.println("IdPizzeria");
					commandeSQL=entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);

			baseQuery="DELETE FROM PIZZERIAS WHERE IdPizzeria=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setInt(1,Integer.parseInt(commandeSQL));
			try {
				if(query.executeUpdate()>0)
					System.out.println("Suppression de la pizzeria faite avec succès");
			} catch(Exception e) {
				System.out.println("Poblème lors de la suppression de la pizzeria.");
			}

		}
		requete.nextTransaction();
	}


	static private void client() throws Exception {
		System.out.println("Voulez vous a:ajouter, m:modifier ou s:supprimer une personne ?");
		commandeSQL=entree.nextLine();
		if ( commandeSQL.contentEquals("a") ){
			baseQuery="INSERT INTO Clients(idPizzeria, mail) VALUES(?, ?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			do{
				do {
					System.out.println("IdPizzeria du client:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(1,Integer.parseInt(commandeSQL));

			do{
				System.out.println("adresse mail ?");
				commandeSQL = entree.nextLine();
			}while(!commandeSQL.contains("@"));
			query.setString(2,commandeSQL);
			query.executeUpdate();

		} else if (commandeSQL.contentEquals("m") ) {

			baseQuery="SELECT mail, IdPizzeria, noClient FROM Clients";
			requete.setQuery(baseQuery);
			System.out.println("Liste des clients :");
			requete.requete();
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			baseQuery="SELECT mail FROM Clients WHERE IdPizzeria=? AND NoClient=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			int idPizzeria=Integer.parseInt(commandeSQL);
			query.setInt(1,idPizzeria);
			do{
				do {
					System.out.println("NoClient:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			int noClient=Integer.parseInt(commandeSQL);
			query.setInt(2,noClient);
			try {
				if(query.executeUpdate()==0) {
					System.out.println("Client non trouvée");
					return;
				}
				requete.dumpResultSet(query.getResultSet());
			} catch(Exception e) {
				System.out.println("Problème lors de la récupération du client");
			}
			System.out.println("Quels sont les champs que vous voulez modifier ? (mettre tous les champs, séparés par un espace)");
			String modifications=entree.nextLine();
			if (modifications.contains("mail")) {
				baseQuery="UPDATE CLIENTS SET mail=? WHERE idPizzeria=? AND noClient=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					System.out.println("Nouveau mail du client :");
					commandeSQL=entree.nextLine();
				}while (!commandeSQL.contains("@"));
				query.setString(1, commandeSQL);
				query.setInt(2,idPizzeria);
				query.setInt(3,noClient);
				try {
					if(query.executeUpdate()>0)
						System.out.println("mail a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du mail");
				}

			}
			if (modifications.contains("idpizzeria")) {
				baseQuery="UPDATE CLIENTS SET idPizzeria=? WHERE idPizzeria=? AND noClient=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					do {
						System.out.println("Nouveau idPizzeria :");
						commandeSQL=entree.nextLine();
					}while ((!commandeSQL.matches("[0-9]+")));
				}while ((Integer.parseInt(commandeSQL)==0));
				query.setInt(1, Integer.parseInt(commandeSQL));
				query.setInt(2,idPizzeria);
				query.setInt(3,noClient);
				try {
					if(query.executeUpdate()>0)
						System.out.println("idPizzeria a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de idPizzeria");
				}

			}
			if (modifications.contains("noclient")) {
				baseQuery="UPDATE CLIENTS SET noClient=? WHERE idPizzeria=? AND noClient=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					do {
						System.out.println("Nouveau idPizzeria :");
						commandeSQL=entree.nextLine();
					}while ((!commandeSQL.matches("[0-9]+")));
				}while ((Integer.parseInt(commandeSQL)==0));
				query.setInt(1, Integer.parseInt(commandeSQL));
				query.setInt(2,idPizzeria);
				query.setInt(3,noClient);
				try {
					if(query.executeUpdate()>0)
						System.out.println("noClient a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de noClient");
				}

			}

		} else if (commandeSQL.contentEquals("s")) {
			baseQuery="SELECT mail, IdPizzeria, noClient FROM Clients";
			requete.setQuery(baseQuery);
			System.out.println("Liste des clients :");
			requete.requete();
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			baseQuery="DELETE FROM Clients WHERE idPizzeria=? AND noClient=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setInt(1,Integer.parseInt(commandeSQL));
			do{
				do {
					System.out.println("noEmploye:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(2,Integer.parseInt(commandeSQL));
			try {
				if(query.executeUpdate()>0)
					System.out.println("Suppression du client faite avec succès");
			} catch(Exception e) {
				System.out.println("Poblème lors de la suppression du client.");
			}

		}

		requete.nextTransaction();
	}


	static private void employe() throws Exception {
		System.out.println("Voulez vous a:ajouter, m:modifier ou s:supprimer un employe ?");
		commandeSQL=entree.nextLine();
		if ( commandeSQL.contentEquals("a") ){
			baseQuery="INSERT INTO Employes(idPizzeria, dateNaissance, mail) VALUES(?, ?, ?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			do{
				System.out.println("mail:");
				commandeSQL=entree.nextLine();
			}while(!commandeSQL.contains("@"));
			query.setString(3,commandeSQL);
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(1,Integer.parseInt(commandeSQL));
			System.out.println("date de naissance? (JJ-MM-AAAA)");
			commandeSQL = entree.nextLine();
			Date birth_date= new SimpleDateFormat("dd-MM-yyyy").parse(commandeSQL);
			java.sql.Date sql_date = new java.sql.Date(birth_date.getTime());
			query.setDate(2, sql_date);
			query.executeUpdate();
		} else if (commandeSQL.contentEquals("m") ) {
			baseQuery="SELECT mail, dateNaissance  FROM Employes";
			requete.setQuery(baseQuery);
			System.out.println("Liste des employes :");
			requete.requete();
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			baseQuery="SELECT mail FROM EMPLOYES WHERE IdPizzeria=? AND NoEmploye=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			int idPizzeria=Integer.parseInt(commandeSQL);
			query.setInt(1,idPizzeria);
			do{
				do {
					System.out.println("NoEmploye:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			int noEmploye=Integer.parseInt(commandeSQL);
			query.setInt(2,noEmploye);
			try {
				if(query.executeUpdate()==0) {
					System.out.println("Employé non trouvé");
					return;
				}
				requete.dumpResultSet(query.getResultSet());
			} catch(Exception e) {
				System.out.println("Problème lors de la récupération de l'employé");
			}
			System.out.println("Quels sont les champs que vous voulez modifier ? (mettre tous les champs, séparés par un espace)");
			String modifications=entree.nextLine();
			if (modifications.contains("mail")) {
				baseQuery="UPDATE EMPLOYES SET mail=? WHERE idPizzeria=? AND noEmploye=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					System.out.println("Nouveau mail:");
					commandeSQL=entree.nextLine();
				}while (!commandeSQL.contains("@"));
				query.setString(1, commandeSQL);
				query.setInt(2,idPizzeria);
				query.setInt(3,noEmploye);
				try {
					if(query.executeUpdate()>0)
						System.out.println("mail a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du mail");
				}

			}
			if (modifications.contains("idpizzeria")) {
				baseQuery="UPDATE EMPLOYES SET idPizzeria=? WHERE idPizzeria=? AND noEmploye=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					do {
						System.out.println("Nouveau idPizzeria :");
						commandeSQL=entree.nextLine();
					}while ((!commandeSQL.matches("[0-9]+")));
				}while ((Integer.parseInt(commandeSQL)==0));
				query.setInt(1, Integer.parseInt(commandeSQL));
				query.setInt(2,idPizzeria);
				query.setInt(3,noEmploye);
				try {
					if(query.executeUpdate()>0)
						System.out.println("idPizzeria a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de idPizzeria");
				}

			}
			if (modifications.contains("noemploye")) {
				baseQuery="UPDATE EMPLOYES SET noEmploye=? WHERE idPizzeria=? AND noEmploye=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					do {
						System.out.println("Nouveau noEmploye :");
						commandeSQL=entree.nextLine();
					}while ((!commandeSQL.matches("[0-9]+")));
				}while ((Integer.parseInt(commandeSQL)==0));
				query.setInt(1, Integer.parseInt(commandeSQL));
				query.setInt(2,idPizzeria);
				query.setInt(3,noEmploye);
				try {
					if(query.executeUpdate()>0)
						System.out.println("noEmploye a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de noEmploye");
				}

			}
			if (modifications.contains("datenaissance")) {
				baseQuery="UPDATE EMPLOYES SET dateNaissance=? WHERE idPizzeria=? AND noEmploye=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					System.out.println("Nouvelle date de naissance (JJ-MM-AAAA :");
					commandeSQL=entree.nextLine();
				}while (commandeSQL.contentEquals(""));
				Date birth_date= new SimpleDateFormat("dd-MM-yyyy").parse(commandeSQL);
				java.sql.Date sql_date = new java.sql.Date(birth_date.getTime());
				query.setDate(1, sql_date);

				query.setInt(2,idPizzeria);
				query.setInt(3,noEmploye);
				try {
					if(query.executeUpdate()>0)
						System.out.println("dateNaissance a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de dateNaissance");
				}

			}
		} else if (commandeSQL.contentEquals("s")) {
			baseQuery="SELECT mail, IdPizzeria, noEmploye FROM EMPLOYES";
			requete.setQuery(baseQuery);
			System.out.println("Liste des employés :");
			requete.requete();
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			baseQuery="DELETE FROM EMPLOYES WHERE idPizzeria=? AND noEmploye=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setInt(1,Integer.parseInt(commandeSQL));
			do{
				do {
					System.out.println("noEmploye:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(2,Integer.parseInt(commandeSQL));
			try {
				if(query.executeUpdate()>0)
					System.out.println("Suppression de l'employé faite avec succès");
			} catch(Exception e) {
				System.out.println("Poblème lors de la suppression de l'employé.");
			}

		}
		requete.nextTransaction();

	}

	static private void livreur() throws Exception {
		System.out.println("Voulez vous a:ajouter, m:modifier ou s:supprimer un livreur ?");
		commandeSQL=entree.nextLine();
		if ( commandeSQL.contentEquals("a") ){
			baseQuery="INSERT INTO Livreurs(idPizzeria) VALUES(?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(1,Integer.parseInt(commandeSQL));
			query.executeUpdate();
		} else if (commandeSQL.contentEquals("m") ) {
			baseQuery="SELECT IdPizzeria, noLivreur  FROM Livreurs";
			requete.setQuery(baseQuery);
			System.out.println("Liste des livreurs :");
			requete.requete();
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			baseQuery="SELECT IdPizzeria  FROM EMPLOYES WHERE IdPizzeria=? AND NoLivreur=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			int idPizzeria=Integer.parseInt(commandeSQL);
			query.setInt(1,idPizzeria);
			do{
				do {
					System.out.println("NoLivreur:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			int noLivreur=Integer.parseInt(commandeSQL);
			query.setInt(2,noLivreur);
			try {
				if(query.executeUpdate()==0) {
					System.out.println("Livreur non trouvé");
					return;
				}
				requete.dumpResultSet(query.getResultSet());
			} catch(Exception e) {
				System.out.println("Problème lors de la récupération du livreur");
			}
			System.out.println("Quels sont les champs que vous voulez modifier ? (mettre tous les champs, séparés par un espace)");
			String modifications=entree.nextLine();
			if (modifications.contains("idpizzeria")) {
				baseQuery="UPDATE LIVREURS SET idPizzeria=? WHERE idPizzeria=? AND noLivreur=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					do {
						System.out.println("Nouveau idPizzeria :");
						commandeSQL=entree.nextLine();
					}while ((!commandeSQL.matches("[0-9]+")));
				}while ((Integer.parseInt(commandeSQL)==0));
				query.setInt(1, Integer.parseInt(commandeSQL));
				query.setInt(2,idPizzeria);
				query.setInt(3,noLivreur);
				try {
					if(query.executeUpdate()>0)
						System.out.println("idPizzeria a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de idPizzeria");
				}

			}
			if (modifications.contains("nolivreur")) {
				baseQuery="UPDATE LIVREURS SET noLivreur=? WHERE idPizzeria=? AND noLivreur=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					do {
						System.out.println("Nouveau noLivreur :");
						commandeSQL=entree.nextLine();
					}while ((!commandeSQL.matches("[0-9]+")));
				}while ((Integer.parseInt(commandeSQL)==0));
				query.setInt(1, Integer.parseInt(commandeSQL));
				query.setInt(2,idPizzeria);
				query.setInt(3,noLivreur);
				try {
					if(query.executeUpdate()>0)
						System.out.println("noLivreur a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de noLivreur");
				}

			}
		} else if (commandeSQL.contentEquals("s")) {
			baseQuery="SELECT IdPizzeria FROM LIVREURS";
			requete.setQuery(baseQuery);
			System.out.println("Liste des livreurs :");
			requete.requete();
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			baseQuery="DELETE FROM EMPLOYES WHERE idPizzeria=? AND noLivreur=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setInt(1,Integer.parseInt(commandeSQL));
			do{
				do {
					System.out.println("noLivreur:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(2,Integer.parseInt(commandeSQL));
			try {
				if(query.executeUpdate()>0)
					System.out.println("Suppression du livreur faite avec succès");
			} catch(Exception e) {
				System.out.println("Poblème lors de la suppression du livreur.");
			}


		}
		requete.nextTransaction();

	}

	static private void responsable() throws Exception {
		System.out.println("Voulez vous a:ajouter, m:modifier ou s:supprimer un responsable ?");
		commandeSQL=entree.nextLine();
		if ( commandeSQL.contentEquals("a") ){
			baseQuery="INSERT INTO Responsables(idPizzeria,NoEmploye) VALUES(?,?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(1,Integer.parseInt(commandeSQL));
			do{
				do {
					System.out.println("noEmploye:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(2,Integer.parseInt(commandeSQL));
			query.executeUpdate();
		} else if (commandeSQL.contentEquals("m") ) {
			baseQuery="SELECT IdPizzeria, noEmploye  FROM Responsables";
			requete.setQuery(baseQuery);
			System.out.println("Liste des responsables :");
			requete.requete();
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			baseQuery="SELECT noEmploye FROM LIVREUR WHERE IdPizzeria=? AND NoEmploye=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			int idPizzeria=Integer.parseInt(commandeSQL);
			query.setInt(1,idPizzeria);
			do{
				do {
					System.out.println("NoEmploye:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			int noEmploye=Integer.parseInt(commandeSQL);
			query.setInt(2,noEmploye);
			try {
				if(query.executeUpdate()==0) {
					System.out.println("Responsable non trouvé");
					return;
				}
				requete.dumpResultSet(query.getResultSet());
			} catch(Exception e) {
				System.out.println("Problème lors de la récupération du responsable");
			}
			System.out.println("Quels sont les champs que vous voulez modifier ? (mettre tous les champs, séparés par un espace)");
			String modifications=entree.nextLine();
			if (modifications.contains("idpizzeria")) {
				baseQuery="UPDATE RESPONSABLE SET idPizzeria=? WHERE idPizzeria=? AND noEmploye=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					do {
						System.out.println("Nouveau idPizzeria :");
						commandeSQL=entree.nextLine();
					}while ((!commandeSQL.matches("[0-9]+")));
				}while ((Integer.parseInt(commandeSQL)==0));
				query.setInt(1, Integer.parseInt(commandeSQL));
				query.setInt(2,idPizzeria);
				query.setInt(3,noEmploye);
				try {
					if(query.executeUpdate()>0)
						System.out.println("idPizzeria a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de idPizzeria");
				}

			}
			if (modifications.contains("noemploye")) {
				baseQuery="UPDATE RESPONSABLE SET noEmploye=? WHERE idPizzeria=? AND noEmploye=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					do {
						System.out.println("Nouveau noEmploye :");
						commandeSQL=entree.nextLine();
					}while ((!commandeSQL.matches("[0-9]+")));
				}while ((Integer.parseInt(commandeSQL)==0));
				query.setInt(1, Integer.parseInt(commandeSQL));
				query.setInt(2,idPizzeria);
				query.setInt(3,noEmploye);
				try {
					if(query.executeUpdate()>0)
						System.out.println("noEmploye a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de noEmploye");
				}

			}
		} else if (commandeSQL.contentEquals("s")) {
			baseQuery="SELECT IdPizzeria FROM RESPONSABLES";
			requete.setQuery(baseQuery);
			System.out.println("Liste des responsable :");
			requete.requete();
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			baseQuery="DELETE FROM EMPLOYES WHERE idPizzeria=? AND noEmploye=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setInt(1,Integer.parseInt(commandeSQL));
			do{
				do {
					System.out.println("noEmploye:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(2,Integer.parseInt(commandeSQL));
			try {
				if(query.executeUpdate()>0)
					System.out.println("Suppression du responsable faite avec succès");
			} catch(Exception e) {
				System.out.println("Poblème lors de la suppression du responsable.");
			}


		}
		requete.nextTransaction();

	}

	static private void permis() throws Exception {
		System.out.println("Voulez vous a:ajouter ou s:supprimer un permis ?");
		commandeSQL=entree.nextLine();
		if ( commandeSQL.contentEquals("a") ){
			baseQuery="INSERT INTO Permis_Vehicules(TypeVehicule) VALUES(?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			do{
				System.out.println("TypeVehicule A,B,C,D ou E:");
				commandeSQL=entree.nextLine();
			}while((!commandeSQL.contentEquals("A"))||(!commandeSQL.contentEquals("B"))||(!commandeSQL.contentEquals("C"))||(!commandeSQL.contentEquals("D"))||(!commandeSQL.contentEquals("E")));
			query.setString(1,commandeSQL);
			query.executeUpdate();

		} else if (commandeSQL.contentEquals("s")) {
			baseQuery="SELECT TypeVehicule FROM Permis_Vehicule";
			requete.setQuery(baseQuery);
			System.out.println("Liste des permis :");
			requete.requete();
			do{
				System.out.println("TypeVehicule A,B,C,D ou E:");
				commandeSQL=entree.nextLine();
			}while((!commandeSQL.contentEquals("A"))||(!commandeSQL.contentEquals("B"))||(!commandeSQL.contentEquals("C"))||(!commandeSQL.contentEquals("D"))||(!commandeSQL.contentEquals("E")));
			baseQuery="DELETE FROM Permis_Vehicule WHERE TypeVehicule=?";
			query.setString(1,commandeSQL);
			try {
				if(query.executeUpdate()>0)
					System.out.println("Suppression du permis faite avec succès");
			} catch(Exception e) {
				System.out.println("Poblème lors de la suppression du permis.");
			}

		}


	}

	static private void vehicule() throws Exception {
		System.out.println("Voulez vous a:ajouter, m:modifier ou s:supprimer un véhicule ?");
		commandeSQL=entree.nextLine();
		if ( commandeSQL.contentEquals("a") ){
			baseQuery="INSERT INTO Vehicules(idPizzeria, noPlaque, TypeVehicule) VALUES(?, ?, ?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(1,Integer.parseInt(commandeSQL));
			do{
				System.out.println("noPlaque");
				commandeSQL=entree.nextLine();
			}while(!commandeSQL.contentEquals(""));
			query.setString(2,commandeSQL);
			do{
				System.out.println("TypeVehicule A,B,C,D ou E:");
				commandeSQL=entree.nextLine();
			}while((!commandeSQL.contentEquals("A"))||(!commandeSQL.contentEquals("B"))||(!commandeSQL.contentEquals("C"))||(!commandeSQL.contentEquals("D"))||(!commandeSQL.contentEquals("E")));
			query.setString(3,commandeSQL);
			query.executeUpdate();
		} else if (commandeSQL.contentEquals("m") ) {
			baseQuery="SELECT *  FROM Vehicules";
			requete.setQuery(baseQuery);
			System.out.println("Liste des vehicules :");
			requete.requete();
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			baseQuery="SELECT IdPizzeria FROM Vehicules WHERE IdPizzeria=? AND NoPlaque=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			int idPizzeria=Integer.parseInt(commandeSQL);
			query.setInt(1,idPizzeria);
			do{
				System.out.println("noPlaque");
				commandeSQL=entree.nextLine();
			}while(!commandeSQL.contentEquals(""));
			String plaque=commandeSQL;
			query.setString(2,commandeSQL);
			try {
				if(query.executeUpdate()==0) {
					System.out.println("Vehicule non trouvé");
					return;
				}
				requete.dumpResultSet(query.getResultSet());
			} catch(Exception e) {
				System.out.println("Problème lors de la récupération du vehicule");
			}
			System.out.println("Quels sont les champs que vous voulez modifier ? (mettre tous les champs, séparés par un espace)");
			String modifications=entree.nextLine();
			if (modifications.contains("idpizzeria")) {
				baseQuery="UPDATE Vehicule  SET idPizzeria=? WHERE idPizzeria=? AND noPlaque=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					do {
						System.out.println("Nouveau idPizzeria :");
						commandeSQL=entree.nextLine();
					}while ((!commandeSQL.matches("[0-9]+")));
				}while ((Integer.parseInt(commandeSQL)==0));
				query.setInt(1, Integer.parseInt(commandeSQL));
				query.setInt(2,idPizzeria);
				query.setString(3,plaque);
				try {
					if(query.executeUpdate()>0)
						System.out.println("idPizzeria a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour de idPizzeria");
				}

			}
			if (modifications.contains("noplaque")) {
				baseQuery="UPDATE Vehicule  SET noplaque=? WHERE idPizzeria=? AND noPlaque=?";
				query=requete.getConnection().prepareStatement(baseQuery);
				do {
					System.out.println("Nouveau numero de plaque:");
					commandeSQL=entree.nextLine();
				}while (!commandeSQL.contentEquals(""));
				query.setString(1, commandeSQL);
				query.setInt(2,idPizzeria);
				query.setString(3,plaque);
				try {
					if(query.executeUpdate()>0)
						System.out.println("noplaque a été mis à jour avec succès");
				} catch (Exception e) {
					System.out.println("Erreur lors de la mise à jour du noplaque");
				}

			}

		} else if (commandeSQL.contentEquals("s")) {
			baseQuery="SELECT IdPizzeria FROM Vehicules";
			requete.setQuery(baseQuery);
			System.out.println("Liste des véhicules:");
			requete.requete();
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			baseQuery="DELETE FROM Vehicules WHERE idPizzeria=? AND noPlaque=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setInt(1,Integer.parseInt(commandeSQL));
			do{
				System.out.println("noPlaque");
				commandeSQL=entree.nextLine();
			}while(!commandeSQL.contentEquals(""));
			query.setString(2,commandeSQL);
			try {
				if(query.executeUpdate()>0)
					System.out.println("Suppression du véhicule faite avec succès");
			} catch(Exception e) {
				System.out.println("Poblème lors de la suppression du responsable.");
			}



		}

		requete.nextTransaction();

	}



	static private void pizza() throws Exception {
		System.out.println("Voulez vous a:ajouter ou s:supprimer une pizza ?");
		commandeSQL=entree.nextLine();
		if ( commandeSQL.contentEquals("a") ){
			baseQuery="INSERT INTO Pizzas(IdPizzeria,NomPizza,Taille,Prix) VALUES(?,?,?,?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(1,Integer.parseInt(commandeSQL));
			do{
				System.out.println("nomPizza");
				commandeSQL=entree.nextLine();
			}while(!commandeSQL.contentEquals(""));
			query.setString(2,commandeSQL);
			do{
				System.out.println("Taille (Mini, Moyenne ou Grande):");
				commandeSQL=entree.nextLine();
			}while(!commandeSQL.contentEquals("Mini")||!commandeSQL.contentEquals("Moyenne")||!commandeSQL.contentEquals("Grande"));
			query.setString(3,commandeSQL);
			do{
				System.out.println("Prix:");
				commandeSQL=entree.nextLine();
			}while(!commandeSQL.contentEquals(""));
			query.setFloat(4,Float.parseFloat(commandeSQL));
			query.executeUpdate();


		}
		requete.nextTransaction();

	}
	static private void commande() throws Exception {
		System.out.println("Voulez vous a:ajouter ou s:supprimer une commande ?");
		commandeSQL=entree.nextLine();
		if ( commandeSQL.contentEquals("a") ){
			baseQuery="INSERT INTO Commandes(IdPizzeria,prixCommande,heurDebut,noClient,noCommande) VALUES(?,?,?,?,?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			int id=Integer.parseInt(commandeSQL);
			query.setInt(1,Integer.parseInt(commandeSQL));
			do{
				do {
					System.out.println("NumeroClient:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			int numCl=Integer.parseInt(commandeSQL);
			query.setInt(4,Integer.parseInt(commandeSQL));
			/*do{
			  System.out.println("Prix:");
			  commandeSQL=entree.nextLine();
			  }while(commandeSQL.contentEquals(""));*/
			do{
				System.out.println("heure de début(yyyy-mm-dd hh:mm:ss):");
				commandeSQL=entree.nextLine();
			}while(commandeSQL.contentEquals(""));
			query.setTimestamp(3,Timestamp.valueOf(commandeSQL));
			float prix=1;
			int nbp;
			do{
				do {
					System.out.println("Combien de pizzas différentes voulez vous?:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			nbp= Integer.parseInt(commandeSQL);
			int numCo =(int) (Math.random()*1500000+50);
			query.setFloat(2,prix);
			query.setInt(5,numCo);
			query.executeUpdate();
			prix=0;
			for (int i=1; i<=nbp;i++)
				prix+=ligneCommande(i,id,numCo);
			
			baseQuery="UPDATE COMMANDES SET prixCommande=? WHERE idPizzeria=? AND noClient=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setFloat(1,prix);
			query.setInt(2,id);
			query.setInt(3,numCl);

		} else if (commandeSQL.contentEquals("s")) {
			baseQuery="SELECT IdPizzeria, noCommande FROM Commandes";
			requete.setQuery(baseQuery);
			System.out.println("Liste des commandes:");
			requete.requete();
			do{
				do {
					System.out.println("IdPizzeria:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			baseQuery="DELETE FROM Commandes WHERE idPizzeria=? AND noCommande=?";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setInt(1,Integer.parseInt(commandeSQL));
			do{
				do {
					System.out.println("noCommande:");
					commandeSQL = entree.nextLine();
				}while((!commandeSQL.matches("^[0-9]+$")));
			}while( Integer.parseInt(commandeSQL)==0);
			query.setInt(2,Integer.parseInt(commandeSQL));
			try {
				if(query.executeUpdate()>0)
					System.out.println("Suppression de la commande faite avec succès");
			} catch(Exception e) {
				System.out.println("Poblème lors de la suppression de la commande.");
			}




		}
		requete.nextTransaction();
	}

	static private float ligneCommande(int numLigne, int idp, int num) throws Exception { 
		System.out.println("Vous allez choisir une nouvelle ligne de commande");
		commandeSQL=entree.nextLine();
		baseQuery="INSERT INTO LIGNES_COMMANDE(IdPizzeria,noCommande,NomPizza,Taille,nbPizza,noLigne) VALUES(?,?,?,?,?,?)";
		
		// query.setInt(1,Integer.parseInt(commandeSQL));
		//query.setInt(2,id);
		do{
			System.out.println("nomPizza");
			commandeSQL=entree.nextLine();
		}while(commandeSQL.contentEquals(""));
		String nom=commandeSQL;
		//query.setString(3,nom);
		do{
			System.out.println("Taille (Mini, Moyenne ou Grande):");
			commandeSQL=entree.nextLine();
		}while(!((commandeSQL.contentEquals("Mini"))||(commandeSQL.contentEquals("Moyenne"))||(commandeSQL.contentEquals("Grande"))));
		String taille=commandeSQL;
		do{
			do {
				System.out.println("nombre Pizza:");
				commandeSQL = entree.nextLine();
			}while((!commandeSQL.matches("^[0-9]+$")));
		}while( Integer.parseInt(commandeSQL)==0);
		int nb=Integer.parseInt(commandeSQL);
		//query.setString(4,commandeSQL);
		PreparedStatement stmt=null;
		String req="SELECT PRIX FROM PIZZAS WHERE ((idPizzeria= ?) AND( nomPizza= ?) AND( Taille= ?) )";
		stmt=requete.getConnection().prepareStatement(req);
		stmt.setInt(1,idp);
		stmt.setString(2,nom);
		stmt.setString(3,taille);
		ResultSet rs = stmt.executeQuery();
		float prix=0;
		if (rs.next()){
			prix=rs.getFloat("PRIX");
			//query.setFloat(4,rs.getFloat("Prix"));
		}
		System.out.println("lml");
		stmt.close();
		query=requete.getConnection().prepareStatement(baseQuery);
		query.setInt(1,idp);
		query.setInt(2,num);
		query.setString(3,nom);
		query.setString(4,taille);
		query.setInt(5,nb);
		query.setInt(6,numLigne);
		query.executeUpdate();

		int compt=0;
		switch (taille) {
			case "Mini":
				while ( compt<2){
					System.out.println("Voulez un ingrédient suppémentaire?");
					commandeSQL = entree.nextLine();
					if (commandeSQL.contentEquals("")){
					return (nb*prix+2*compt);
					}
						
					extra(idp,num,numLigne);
					compt++;
				}
			break;
			case "Moyenne":
				while (compt<4) {
					System.out.println("Voulez un ingrédient suppémentaire?");
					if (commandeSQL.contentEquals(""))
							break;
					extra(idp,num,numLigne);
					compt++;
				}
			break;
			case "Grande":
				while (true) {
					System.out.println("Voulez un ingrédient suppémentaire?");
					if (commandeSQL.contentEquals(""))
							break;
					extra(idp,num,numLigne);compt++;

				}	
				break;

		}
		requete.nextTransaction();
		return (nb*prix+2*compt);
	}


	static private void extra(int id, int num, int ligne) throws Exception {
	
			baseQuery="INSERT INTO Extras(idPizzeria,nomIngredient,noCommande,noLigne) VALUES(?, ?, ?, ?)";
			query=requete.getConnection().prepareStatement(baseQuery);
			query.setInt(1,id);
			query.setInt(3,num);
			query.setInt(4,ligne);
			do {
				System.out.println("Nommez votre ingrédient supplémentaire:");
				commandeSQL = entree.nextLine();
			}while (commandeSQL.contentEquals(""));
			query.setString(2,commandeSQL);
			query.executeUpdate();

	}


	static private void stat() throws Exception {
				
		System.out.println("nombre de pizzas commandé par jour");


			baseQuery="SELECT (HeurDebut+0) AS \"JOUR\", COUNT(*) AS \"QUANTITE\" FROM COMMANDES GROUP BY (HeurDebut+0)";
			requete.setQuery(baseQuery);
			requete.requete();

		System.out.println("pizza préférée de clients");
			baseQuery= " ELECT NomPizza AS \"La pizza prefere\" FROM LIGNES_COMMANDE GROUP BY NomPizza HAVING SUM (NbPizza)= ( SELECT MAX (SUM (NbPizza)) FROM LIGNEs_COMMANDE GROUP BY NomPizza)";
			requete.setQuery(baseQuery);
			requete.requete();

		System.out.println("Véhicules les plus utilisés");

			baseQuery=" SELECT NoPlaque ROM LIVRAISONS GROUP BY NoPlaque HAVING COUNT (HeurDepart)= (SELECT MAX (COUNT (HeurDepart)) FROM LIVRAISONS GROUP BY NoPlaque) ";
			requete.setQuery(baseQuery);
			requete.requete();

		System.out.println("Temps moyen de livraison");

			baseQuery="SELECT (AVG((HeurRetour + 0)-(HeurDepart + 0)))*24*60 AS \"MOYEN MINUTES\"FROM LIVRAISONS ";
			requete.setQuery(baseQuery);
			requete.requete();

	}



}
