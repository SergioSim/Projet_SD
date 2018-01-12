package bd;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * 
 * @author Melvin Moreau
 *
 */
public class CategorieBD extends ConnectionBD {
	public HashMap cat = new HashMap();

	public CategorieBD() {
		super();
	}

	/**
	 * Permet de lire toutes les informations présentes dans la base
	 */
	public void recupCat() {
		try {
			String sql = "SELECT * FROM categorie";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				cat.put(rs.getInt("IdCat"), rs.getString("LibCat"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de recuperer l'identifiants avec un libelle
	 * 
	 * @param lib
	 *            le libelle dont nous cherchons l'identifiant
	 * @return retourne l'identifiant ou -1 si ce dernier n'existe pas
	 */
	public int recupIdAvecLib(String lib) {
		try {
			String sql = "SELECT IdCat FROM categorie WHERE LibCat LIKE '%" + lib + "%' ";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getInt("IdCat");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Permet d'ajouter une categorie dans la base de donnée
	 * 
	 * @param lib
	 *            Titre de la categorie
	 */
	public void ajouterCat(String lib) {
		try {
			String sql = "INSERT INTO `categorie` (`LibCat`) VALUES ('" + lib + "')";
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		CategorieBD cbd = new CategorieBD();
		cbd.recupCat();
		int key = cbd.recupIdAvecLib("portable");
		System.out.println(cbd.cat.get(key));

		cbd.fermerCo();
	}

}
