package bd;

import java.sql.SQLException;

/**
 * 
 * @author Melvin Moreau
 *
 */
public class ClientBD extends ConnectionBD {

	public int idClient;
	public String nom;
	public String prenom;
	public String user;
	private String mdp;

	public ClientBD() {
		super();
	}

	public static void main(String[] args) {
		ClientBD cbd = new ClientBD();
		// cbd.ajouterClient("Melvin", "Moreau", "alkair83", "test");
		cbd.chargerInfoClient("alkair83");
		System.out.println(cbd.connectionClient("alkair83", "test"));
		// System.out.println(cbd.checkUser("alkair"));
		System.out.println(cbd.getNom() + " " + cbd.getPrenom());

		cbd.fermerCo();
	}

	/**
	 * Permet de charger les informations d'un client grace a son nom d'utilisateur
	 * 
	 * @param username
	 *            Le nom d'utilisateur du client
	 */
	public void chargerInfoClient(String username) {
		try {
			String sql = "SELECT * FROM client WHERE User LIKE '" + username + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				setAll(rs.getInt("IdCli"), rs.getString("Nom"), rs.getString("Prenom"), rs.getString("User"),
						rs.getString("MotDePasse"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de vérifier qu'un nom d'utilisateur n'est pas présent dans la base
	 * 
	 * @param username
	 *            Nom d'utilisateur
	 * @return retourne vrais si le nom d'utilisateur est libre, faux sinon
	 */
	private boolean checkUser(String username) {
		try {
			String sql = "SELECT User FROM client WHERE User LIKE '" + username + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("User") != null) {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Permet de connecter un client en fonction de son nom d'utilisateur et de son
	 * mot de passe
	 * 
	 * @param username
	 *            Nom d'utilisateur
	 * @param mdp
	 *            Mot de passe
	 * @return retourne vrais si le nom d'utilisateur et le mot de passe sont
	 *         correct, faux sinon
	 */
	public boolean connectionClient(String username, String mdp) {
		try {
			String sql = "SELECT User FROM client WHERE User LIKE '" + username + "' AND MotDePasse LIKE '" + mdp + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("User") != null) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Permet d'ajouter une client a la base de donnée
	 * 
	 * @param nom
	 *            Nom du client
	 * @param prenom
	 *            Prénom du client
	 * @param user
	 *            Nom d'utilisateur du client
	 * @param motdepasse
	 *            Mot de passe du client
	 */
	public void ajouterClient(String nom, String prenom, String user, String motdepasse) {
		try {
			if (checkUser(user)) {
				String sql = "INSERT INTO `client` (`Nom`,`Prenom`, User, MotDePasse ) VALUES ('" + nom + "', '"
						+ prenom + "', '" + user + "', '" + motdepasse + "')";
				st.executeUpdate(sql);
			} else {
				System.out.println("Nom d'utilisateur deja utilise");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de d'initialiser tous les attributs
	 */
	private void setAll(int idCli, String nom, String prenom, String user, String motPasse) {
		this.idClient = idCli;
		this.nom = nom;
		this.prenom = prenom;
		this.user = user;
		this.mdp = motPasse;
	}

	public int getIdClient() {
		return idClient;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getUser() {
		return user;
	}

	private String getMdp() {
		return mdp;
	}

}
