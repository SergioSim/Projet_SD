package bd;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
		//gdb.AjouterObjet("HP ENVY","Ordinateur portable");
		// gdb.lireEnBase();
		// gdb.supprimerDansBase(2);
		// gdb.modifierDescr(3, "Phone by Apple");
		// gdb.lireEnBase();
		//gdb.AjouterObjet("Hunger Games", "Le premier tome introduit Katniss Everdeen", "Livres",2,10,"2018-02-01");
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
	public boolean AjouterObjet(String titre, String descr, String cat, int idvendeur, int prix,String dateFinEnchere) {
		try {
			CategorieBD catbd = new CategorieBD();
			int catKey = catbd.recupIdAvecLib(cat);
			
			LocalDateTime ladate = LocalDateTime.now();
			String datestring = ladate.format(DateTimeFormatter.ISO_DATE);
			String sql = "INSERT INTO `objet_en_vente` (`Titre`,`Description`,`IdCategorie`, `Vendeur`, `Prix`, `DateMiseVente`,`DateFinEnchere` ) VALUES ('"
					+ titre + "', '" + descr + "', '" + catKey + "', '" + idvendeur + "', '" + prix + "', '"+datestring+"', '"+dateFinEnchere+"')";
			st.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Permet de lire toutes les informations pr�sentes dans la base
	 */
	public ArrayList<String> afficherObjets() {
		try {
			String sql = "SELECT * FROM objet_en_vente";
			ArrayList<String> resultat = new ArrayList<String>();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				resultat.add(String.format("Identifiant de l'objet: %s Titre: %s Description: %s Categorie: %s Prix: %s Euros",
						rs.getString("IdObjet"),rs.getString("Titre"),rs.getString("Description"),rs.getString("IdCategorie"),rs.getString("Prix")) );
			} // Vendeur, Ench, Prix, DateMiseVente, DateFinEnchere
			return resultat;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	/**
	 * Permet de charger les informations d'un objet
	 * 
	 * @param id
	 *            Identifiant de l'objet
	 */
	public ArrayList<String> chargeInfo(int id) {
		try {
			String sql = "SELECT * FROM objet_en_vente WHERE IdObjet ='" + id + "'";
			ArrayList<String> resultat = new ArrayList<String>();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				resultat.add(String.format("IdObjet: %s Titre: %s Description: %s IdCategorie: %s Vendeur: %s Ench: %s Prix: %s DateMiseVente: %s DateFinEnchere: %s",rs.getInt("IdObjet"), rs.getString("Titre"), rs.getString("Description"),
						rs.getInt("IdCategorie"), rs.getInt("Vendeur"), rs.getBoolean("Ench"), rs.getInt("Prix"),
						rs.getDate("DateMiseVente"), rs.getDate("DateFinEnchere")));
			}
			return resultat;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	/**
	 * Permet de supprimer un objet dans la base en connaissant uniquement sont
	 * identifiant
	 * 
	 * @param id
	 *            Identifiant de l'objet
	 */
	public boolean supprimerDansBase(int id) {
		try {
			String sql = "DELETE FROM `objet_en_vente` WHERE `IdObjet` ='" + id + "'";
			st.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
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
