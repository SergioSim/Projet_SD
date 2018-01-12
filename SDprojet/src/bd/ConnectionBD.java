package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Melvin Moreau
 *
 */
public class ConnectionBD {
	
	protected String url = "jdbc:mysql://localhost/projetsd";
	protected String login = "root";
	protected String passwd = "";
	protected Connection cn = null;
	protected Statement st = null;
	protected ResultSet rs = null;
	
	/**
	 * Permet d'initialiser une connection avec la base de donn�e
	 */
	public ConnectionBD() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			cn = DriverManager.getConnection(url, login, passwd);
			st = cn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Permet de fermer la connection a la base de donnee
	 */
		public void fermerCo() {
			try {
				cn.close();
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	

}
