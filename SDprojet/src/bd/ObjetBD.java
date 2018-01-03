package bd;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Melvin Moreau
 *
 */
public class ObjetBD extends ConnectionBD {

	public int idObjet;
	public String titre;
	public String description;
	public int idCat;
	public int vendeur;
	public boolean ench;
	public int prix;
	public Date dateMiseVente;
	public Date dateFinEnch;

	public ObjetBD() {
		super();
	}

	public static void main(String[] args) {
		ObjetBD gdb = new ObjetBD();
		// gdb.AjouterObjet("HP ENVY","Ordinateur portable");
		// gdb.lireEnBase();
		// gdb.supprimerDansBase(2);
		// gdb.modifierDescr(3, "Phone by Apple");
		// gdb.lireEnBase();
		// gdb.AjouterObjet("Hunger Games", "Le premier tome introduit Katniss Everdeen,
		// une jeune fille de 16 ans originaire du District 12 qui se porte volontaire
		// pour les 74e Hunger Games � la place de sa petite s�ur Prim. ", "Livres");
		// gdb.AffCatObjet(5);
		// gdb.modifierPrix(5, 15);
		// gdb.lireEnBase();
		System.out.println(gdb.aEnch(5));
		// gdb.chargeInfo(5);
		// System.out.println(gdb.getIdObjet());
		gdb.fermerCo();

	}

	/**
	 * Permet d'ajouter un objet dans la base de donn�e
	 * 
	 * @param titre
	 *            Titre de l'objet
	 * @param descr
	 *            Description de l'objet
	 */
	@Deprecated
	public void AjouterObjet(String titre, String descr) {
		try {
			String sql = "INSERT INTO `objet_en_vente` (`Titre`,`Description` ) VALUES ('" + titre + "', '" + descr
					+ "')";
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet d'ajouter un objet dans la base de donn�e
	 * 
	 * @param titre
	 *            Titre de l'objet
	 * @param descr
	 *            Description de l'objet
	 * @param cat
	 *            Categorie d'un objet
	 * @param idvendeur
	 *            Identifiant du vendeur
	 * @param prix
	 *            Prix de l'objet
	 */
	public void AjouterObjet(String titre, String descr, String cat, int idvendeur, int prix) {
		try {
			CategorieBD catbd = new CategorieBD();
			int catKey = catbd.recupIdAvecLib(cat);
			String sql = "INSERT INTO `objet_en_vente` (`Titre`,`Description`,`IdCategorie`, 'Vendeur', 'Prix', 'DateMiseVente' ) VALUES ('"
					+ titre + "', '" + descr + "', '" + catKey + "', '" + idvendeur + "', '" + prix + "', 'NOW()')";
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de lire toutes les informations pr�sentes dans la base
	 */
	public void afficherObjets() {
		try {
			String sql = "SELECT * FROM objet_en_vente";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				System.out.println("Identifiant de l'objet: " + rs.getString("IdObjet") + " Titre: "
						+ rs.getString("Titre") + " Description: " + rs.getString("Description") + " Categorie: "
						+ rs.getString("IdCategorie") + " Prix: " + rs.getString("Prix") + "�");
			} // Vendeur, Ench, Prix, DateMiseVente, DateFinEnchere
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de charger les informations d'un objet
	 * 
	 * @param id
	 *            Identifiant de l'objet
	 */
	public void chargeInfo(int id) {
		try {
			String sql = "SELECT * FROM objet_en_vente WHERE IdObjet ='" + id + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				setAll(rs.getInt("IdObjet"), rs.getString("Titre"), rs.getString("Description"),
						rs.getInt("IdCategorie"), rs.getInt("Vendeur"), rs.getBoolean("Ench"), rs.getInt("Prix"),
						rs.getDate("DateMiseVente"), rs.getDate("DateFinEnchere"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de supprimer un objet dans la base en connaissant uniquement sont
	 * identifiant
	 * 
	 * @param id
	 *            Identifiant de l'objet
	 */
	public void supprimerDansBase(int id) {
		try {
			String sql = "DELETE FROM `objet_en_vente` WHERE `IdObjet` ='" + id + "'";
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de modifier le titre d'un objet en connaissant son identifiant
	 * 
	 * @param id
	 *            Identifiant de l'objet
	 * @param nouvTitre
	 *            Nouveau titre de l'objet
	 */
	public void modifierTitre(int id, String nouvTitre) {
		try {
			String sql = "UPDATE `objet_en_vente` SET `Titre` = '" + nouvTitre + "' WHERE `IdObjet` ='" + id + "'";
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de modifier le description d'un objet en connaissant son identifiant
	 * 
	 * @param id
	 *            Identifiant de l'objet
	 * @param nouvDescr
	 *            Nouveau description de l'objet
	 */
	public void modifierDescr(int id, String nouvDescr) {
		try {
			String sql = "UPDATE `objet_en_vente` SET `Description` = '" + nouvDescr + "' WHERE `IdObjet` ='" + id
					+ "'";
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de modifier le prix d'un objet en connaissant son identifiant
	 * 
	 * @param id
	 *            Identifiant de l'objet
	 * @param nouvPrix
	 *            Nouveau prix de l'objet
	 */
	public void modifierPrix(int id, int nouvPrix) {
		try {
			String sql = "UPDATE `objet_en_vente` SET `Prix` = '" + nouvPrix + "' WHERE `IdObjet` ='" + id + "'";
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet d'ajouter une enchere a un objet en connaissant son identifiant
	 * 
	 * @param id
	 *            Identifiant de l'objet
	 */
	public void AjouterEnch(int id) {
		try {
			String sql = "UPDATE `objet_en_vente` SET `Ench` = '1' WHERE `IdObjet` ='" + id + "'";
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet d'ajouter une enchere a un objet en connaissant son identifiant
	 * 
	 * @param id
	 *            Identifiant de l'objet
	 * @param nouvPrix
	 *            Nouveau prix de l'objet
	 */
	public boolean aEnch(int id) {
		boolean ret = false;
		try {
			String sql = "SELECT Ench FROM objet_en_vente WHERE IdObjet ='" + id + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ret = rs.getBoolean("Ench");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Permet de connaitre la categorie d'un objet
	 * 
	 * @param id
	 *            Identifiant d'un objet
	 */
	public void AffCatObjet(int id) {
		try {
			String sql = "SELECT objet_en_vente.Titre, categorie.LibCat FROM `objet_en_vente` INNER JOIN `categorie` ON objet_en_vente.IdCategorie = categorie.IdCat wHERE objet_en_vente.IdObjet LIKE '"
					+ id + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString("Titre") + " " + rs.getString("LibCat"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void setAll(int idO, String titre, String descr, int idCat, int vendeur, boolean ench, int prix,
			Date dateMiseV, Date dateFinEnch) {
		this.idObjet = idO;
		this.titre = titre;
		this.description = descr;
		this.idCat = idCat;
		this.vendeur = vendeur;
		this.ench = ench;
		this.prix = prix;
		this.dateMiseVente = dateMiseV;
		this.dateFinEnch = dateFinEnch;
	}

	public int getIdObjet() {
		return idObjet;
	}

	public String getTitre() {
		return titre;
	}

	public String getDescription() {
		return description;
	}

	public int getIdCat() {
		return idCat;
	}

	public int getVendeur() {
		return vendeur;
	}

	public boolean isEnch() {
		return ench;
	}

	public int getPrix() {
		return prix;
	}

	public Date getDateMiseVente() {
		return dateMiseVente;
	}

	public Date getDateFinEnch() {
		return dateFinEnch;
	}

}
