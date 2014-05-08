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
					System.out.println("Liste des membres :");
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
					System.out.println("Liste des membres :");
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

}
