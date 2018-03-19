package serveurCentraliseSimplifie;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;

import bd.ClientBD;
import bd.EnchereBD;
import bd.ObjetBD;
import messeges.Message;
import env.secure.VerSig;

public class ServiceClient implements Runnable {
	// String to finish the communication -> ctrl-d
	// private static final String Finish="end";

	private Socket my_connection;
	private String Finish = "" + (char) 4;
	private String id;
	private String login;
	private String password;
	private ClientBD cbd = new ClientBD();
	private Message message;

	private void terminer() {
		try {
			if (my_connection != null) {
				System.out.format("Terminaison pour %s\n", id);
				if (message != null) {
					message.save();
				}
				my_connection.close();
			}
		} catch (IOException e) {
			System.out.format("Terminaison pour %s\n", id);
			e.printStackTrace();
		}
		return;
	}

	public ServiceClient(Socket la_connection, String mid) {
		my_connection = la_connection;
		id = mid;
		System.out.format("[Serveur]: Thread %s created for connection\n", id);
	}

	public void run() {
		// Phase d initialisation
		BufferedReader flux_entrant = null;
		PrintWriter ma_sortie = null;
		try {
			Charset chrs = Charset.forName("UTF-8");
			InputStreamReader isr = new InputStreamReader(my_connection.getInputStream(), chrs);
			flux_entrant = new BufferedReader(isr); // file d'entrée
			// flux de sortie en mode autoflush
			ma_sortie = new PrintWriter(my_connection.getOutputStream(), true);
			String c_ip = my_connection.getInetAddress().toString();
			int c_port = my_connection.getPort();
			System.out.format("[%s] client admis IP %s  sur le port %d\n", id, c_ip, c_port);
			ma_sortie.format("[%s] : Hello %s  sur le port %d, \n", id, c_ip, c_port);
		} catch (Exception e1) {
			System.out.println("Initialisation Error");
			e1.printStackTrace();
		}

		login(flux_entrant, ma_sortie);
		String message_lu = null;
		// Fin de l initialisation
		// Boucle principale
		if (message != null) {
			System.out.println("le client a reussi de se connecter");
			while (true) {
				try {
					//if(flux_entrant.ready()) {
						message_lu = flux_entrant.readLine();
						if (message_lu == null) {
							System.out.println("Client deconnecté, je termine\n");
							terminer();
							return;
						}
						if (message_lu.contains(Finish)) {
							System.out.format("[%s] :  [%s] recu, Transmission finie\n", id, "CTRL-D");
							ma_sortie.println("Fermeture de la connexion");
							terminer();
							return;
						}
						messageListener(message_lu, ma_sortie);
						message.addMessage(message_lu.toString());
					//}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

	}

	public boolean check(String str) {
		if (str == null || str.contains(Finish)) {
			System.out.println("Client deconnecté, je termine\n");
			terminer();
			return false;
		} else {
			return true;
		}
	}

	private void login(BufferedReader flux_entrant, PrintWriter ma_sortie) {
		ma_sortie.println("");
		ma_sortie.println("Vous etez deja client chez nous? Oui/Non");
		try {
			String reponse = flux_entrant.readLine();
			if (reponse.contains("Oui")) {
				continuerLogin(flux_entrant, ma_sortie);
			} else if (reponse.contains("Non")) {
				creationNouveauClient(flux_entrant, ma_sortie);
				continuerLogin(flux_entrant, ma_sortie);
			} else {
				ma_sortie.println("Response invalide, veuilles repondre par Oui ou Non!");
				login(flux_entrant, ma_sortie);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void creationNouveauClient(BufferedReader flux_entrant, PrintWriter ma_sortie) {
		boolean confirm = false;
		String nom = "", prenom = "", user = "", motdepasse = "", motdepasse2 = "";
		do {
			try {
				ma_sortie.println("");
				ma_sortie.println("Entrez votre nom svp: ");
				nom = flux_entrant.readLine();
				if (!check(nom)) {
					return;
				}
				ma_sortie.println("Entrez votre prenom svp: ");
				prenom = flux_entrant.readLine();
				if (!check(prenom)) {
					return;
				}
				ma_sortie.println("Entrez votre login svp: ");
				user = flux_entrant.readLine();
				if (!check(user)) {
					return;
				}
				ma_sortie.println("Entrez votre motdepasse svp: ");
				motdepasse = flux_entrant.readLine();
				if (!check(motdepasse)) {
					return;
				}
				ma_sortie.println("Confirmez votre motdepasse svp: ");
				motdepasse2 = flux_entrant.readLine();
				if (motdepasse.contentEquals(motdepasse2)) {
					ma_sortie.println("Confirmez svp par Oui/Non: ");
					ma_sortie.println(String.format("nom = %s , prenom = %s , login = %s , motdepasse = %s",
							nom, prenom, user, motdepasse));
					String reponseConfirmant = flux_entrant.readLine();
					if (reponseConfirmant.contains("Oui")) {
						confirm = true;
					} else if (reponseConfirmant.contains("Non")) {
						ma_sortie.println("Alors on recommence...");
					} else {
						ma_sortie.println("Il falait repondre par Oui ou Non... On recomence!");
					}
				} else {
					ma_sortie.println("les mot de passes ne sont pas identiques... on recommence!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (!confirm);
		boolean ajoute = cbd.ajouterClient(nom, prenom, user, motdepasse);
		if(!ajoute) {
			ma_sortie.println("Nom d'utilisateur deja utilise! on recommence!");
		}else {
			ma_sortie.println("compte cree!");
		}
		System.out.println("[Serveur]: Nouveau Client!");
		System.out.println(String.format("nom = %s , prenom = %s , login = %s , motdepasse = %s", nom, prenom,
				user, motdepasse));
	}

	private void continuerLogin(BufferedReader flux_entrant, PrintWriter ma_sortie) {
		while (!cbd.connectionClient(login, password)) {
			try {
				ma_sortie.println("");
				ma_sortie.println("Please enter your login:");
				login = flux_entrant.readLine();
				if (!check(login)) {
					return;
				}
				ma_sortie.println("Please enter your password:");
				password = flux_entrant.readLine();
				if (!check(password)) {
					return;
				}
				if (!cbd.connectionClient(login, password)) {
					ma_sortie.println("login ou mot de pass est incorrect! veuillez reessayer...");
				}else {
					ma_sortie.println("vous etes connecte!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		cbd.chargerInfoClient(login);
		
		message = new Message(cbd.getIdClient(),cbd.getNom(),cbd.getPrenom(), login, password);
		System.out.format("[%s] : Client logged with login: %s and password: %s \n", id, login, password);
	}
	
	private boolean validSignature(String msg, int id) {
		//System.out.println("ID de la signature : " + id);
		File publicKey = new File("public/" + this.login + "pubKey.pub");
		VerSig validator = new VerSig(publicKey, id, msg);
		if(validator.ValidSig()) {
			return true;
		} else {
			return false;
		}
	}
	
	private void deleteSignature(String msg, int id) {
		File publicKey = new File("public/" + this.login + "pubKey.pub");
		VerSig validator = new VerSig(publicKey, id, msg);
		validator.deleteSig();
	}
	
	private void messageListener(String tabStr, PrintWriter ma_sortie) {		
		String m = "Mettre ";
		String r = "Retrait ";
		String p = "Placer ";
		String a = "Achat ";
		String c = "Chercher ";
		String e = "Auto Enrechir ";
		String af = "Afficher";
		String[] commande;
		String[] str = tabStr.split("//");
		String message_lu = str[0];
		int signature = 0;
		if(str.length == 2) {
			signature = Integer.parseInt(str[1]);
		} else {
			signature = -1;
		}
		
		System.out.format("%s --> A fait : [%s]\n", login, message_lu);

		if( (signature == -1) || (this.validSignature(message_lu, signature)) ) {
			if(signature != -1) {
				this.deleteSignature(message_lu, signature);
			}
			if (message_lu.startsWith(m)) {
				//Mettre Titre Desscription categorie prix dateDeFin
				message_lu = message_lu.replace(m,"");
				commande = message_lu.split(" ");
				if(commande.length != 5) {
					ma_sortie.println("demande du client inconnue...");
				} else {
					try {
					ObjetBD gdb = new ObjetBD();
					boolean reussiteRequette = gdb.AjouterObjet(commande[0], commande[1], commande[2],cbd.getIdClient(),Integer.parseInt(commande[3]),commande[4]);
					if(reussiteRequette) {
						ma_sortie.println("la demmande du client est pris en compte");
					} else {
						ma_sortie.println("la demmande du client est invalide");
					}
					gdb.fermerCo();
					}catch(NumberFormatException ex) {
						ma_sortie.println("la demmande du client est invalide");
					}
				}
			} if (message_lu.startsWith(r)) {
				//Retrait idObject
				message_lu = message_lu.replace(r,"");
				commande = message_lu.split(" ");
				if(commande.length != 1) {
					ma_sortie.println("demande du client inconnue...");
				}else {
					try {
						ObjetBD gdb = new ObjetBD();
						boolean reussiteRequette = gdb.supprimerDansBase(Integer.parseInt(commande[0]));
						if(reussiteRequette) {
							ma_sortie.println("la demmande du client est pris en compte");
						} else {
							ma_sortie.println("L'offre donn�e est inf�rieurs a l'offre en cours");
						}
						gdb.fermerCo();
					}catch(NumberFormatException ex) {
						ma_sortie.println("la demmande du client est invalide");
					}
				}
			} if (message_lu.startsWith(p)) {
				//Placer IdObjet Prix
				message_lu = message_lu.replace(p,"");
				commande = message_lu.split(" ");
				if(commande.length != 2) {
					ma_sortie.println("demande du client inconnue...");
				} else {
					EnchereBD ebd = new EnchereBD();
					try {
						boolean reussiteRequette = ebd.AjouterEnch(Integer.parseInt(commande[0]), Integer.parseInt(commande[1]), cbd.getIdClient());
						if(reussiteRequette) {
							ma_sortie.println("la demmande du client est pris en compte");
						} else {
							ma_sortie.println("L'offre donn�e est inf�rieurs a l'offre en cours");
						}
					} catch(NumberFormatException ex) {
						ma_sortie.println("la demmande du client est invalide");
					}
				}
			} if (message_lu.startsWith(a)) {
				message_lu = message_lu.replace(a,"");
				commande = message_lu.split(" ");
				if(commande.length != 2) {
					ma_sortie.println("demande du client inconnue...");
				} else {
					
				}
			} if (message_lu.startsWith(c)) {
				//Chercher idObjet
				message_lu = message_lu.replace(c,"");
				commande = message_lu.split(" ");
				if(commande.length != 1) {
					ma_sortie.println("demande du client inconnue...");
				} else {
					try {
					ObjetBD gdb = new ObjetBD();
					ArrayList<String> reussiteRequette = gdb.chargeInfo(Integer.parseInt(commande[0]));
					if(reussiteRequette.size() != 0) {
						ma_sortie.println(reussiteRequette.toString());
					} else {
						ma_sortie.println("l'objet n'existe pas...");
					}
					gdb.fermerCo();
				}catch(NumberFormatException ex) {
					ma_sortie.println("la demmande du client est invalide");
				}
				}
			} if (message_lu.startsWith(e)) {
				message_lu = message_lu.replace(e,"");
				commande = message_lu.split(" ");
			} if (message_lu.startsWith(af)) {
				ArrayList<String> reponse = new ObjetBD().afficherObjets();
				if(reponse.size()!=0) {
					reponse.forEach((s)-> ma_sortie.println(s));
				}
			}
		} else {
			ma_sortie.println("ERREUR : SIGNATURE DSA INVALIDE !!!");
			System.out.println("ERREUR : SIGNATURE DSA INVALIDE !!!");
//			System.out.println("Je suis le client n°" + this.id + ", j'ai fais la commande " + message_lu + " qui a pour ID de signature : " + signature + ". Mais ca a plante.");
		}
	}
}