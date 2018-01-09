package bd;

import java.sql.SQLException;

/**
 * 
 * @author Melvin Moreau
 *
 */
public class EnchereBD extends ConnectionBD {
	public int idEnchere;
	public int idObjet;
	public int Offre;
	public int encherisseur;
	public String DateEnchere;

	public EnchereBD() {
		super();
	}

	public static void main(String[] args) {
		EnchereBD ebd = new EnchereBD();
		ebd.AjouterEnch(5, 14, 2);
		// ebd.recupDerniEnchObj(5);
		// ebd.recupOffre(5);
		// System.out.println(ebd.getOffre());
		ebd.fermerCo();
	}

	/**
	 * Permet de récupérer une enchère grace a son identifiant
	 * 
	 * @param idEnch
	 *            Identifiant de l'enchere
	 */
	public void recupEnch(int idEnch) {
		try {
			String sql = "SELECT * FROM enchere WHERE IdEnchere LIKE '" + idEnch + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				this.setAll(rs.getInt("IdEnchere"), rs.getInt("IdObj"), rs.getInt("Offre"), rs.getInt("Encherisseur"),
						rs.getString("DateEnchere"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de récupérer l'offre maximal d'un objet
	 * 
	 * @param idobj
	 *            Identifiant d'un objet
	 */
	public void recupOffre(int idobj) {
		try {
			String sql = "SELECT MAX(Offre) FROM `enchere` WHERE `IdObj` LIKE '" + idobj + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				this.setOffre(rs.getInt("MAX(Offre)"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet d'ajouter une enchere a un objet en controlant que l'offre est bien
	 * suppérieur a celle actuel
	 * 
	 * @param idobj
	 *            Identifiant de l'objet
	 * @param Offre
	 *            L'offre pour l'objet
	 * @param ench
	 *            Identifiant de l'encherisseur
	 */
	public boolean AjouterEnch(int idobj, int Offre, int ench) {
		ObjetBD obj = new ObjetBD();
		if (!obj.aEnch(idobj)) {
			obj.AjouterEnch(idobj);
		}
		this.recupOffre(idobj);
		if (this.getOffre()< Offre) {
			try {
				String sql = "INSERT INTO `enchere` (`IdObj`,`Offre`, Encherisseur, DateEnchere) VALUES ('" + idobj
						+ "', '" + Offre + "', '" + ench + "', NOW())";
				st.executeUpdate(sql);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("L'offre donnée est inférieurs a l'offre en cours");
		}
		return false;
	}

	/**
	 * Permet de récupérer la dernière enchere d'un objet
	 * 
	 * @param idobj
	 *            Identifiant de l'objet
	 */
	public void recupDerniEnchObj(int idobj) {
		try {
			String sql = "SELECT * FROM enchere WHERE IdObj LIKE '" + idobj + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				this.setAll(rs.getInt("IdEnchere"), rs.getInt("IdObj"), rs.getInt("Offre"), rs.getInt("Encherisseur"),
						rs.getString("DateEnchere"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de d'initialiser tous les attributs
	 */
	public void setAll(int idEnch, int idObj, int Offr, int Ench, String dateEnch) {
		this.idEnchere = idEnch;
		this.idObjet = idObj;
		this.Offre = Offr;
		this.encherisseur = Ench;
		this.DateEnchere = dateEnch;
	}

	public int getIdEnchere() {
		return idEnchere;
	}

	public int getIdObjet() {
		return idObjet;
	}

	public int getOffre() {
		return Offre;
	}

	public int getEncherisseur() {
		return encherisseur;
	}

	public String getDateEnchere() {
		return DateEnchere;
	}

	public void setOffre(int offre) {
		Offre = offre;
	}
}
