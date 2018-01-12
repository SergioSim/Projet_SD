package env.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import env.test.ArretSocket;
import env.test.Repartiteur;
import env.test.ServiceClient;

/**
 * 
 * @author Idryss Bourdier
 * 
 * Cette classe sert à dispatcher les clients
 * lorsque qu'un utilisateur se connecte
 * 
 */

public class Repartiteur {
	private int port;
	private ServerSocket connection;
	private int timeout;
	
	public Repartiteur(int port, int timeout) throws IOException {
		this.port = port;
		this.connection = new ServerSocket(this.port);
		this.timeout = timeout;
	}
	
	public ServerSocket getConnection() {
		return this.connection;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public int getTimeout() {
		return this.timeout;
	}
	
	public static void main(String[] args) throws IOException {
		Socket ma_connection = null;
		Repartiteur repat = new Repartiteur(12000, 1000);
		System.out.format("Creation du répartiteur sur le port %s \n", repat.getPort());
		while(true) {
			ma_connection = repat.getConnection().accept();
			System.out.println("Connexion établie.");
			System.out.format("Le client avec l'ip : %s est arrivé sur le port %d \n", ma_connection.getInetAddress(), repat.getPort());
			ServiceClient client = new ServiceClient(ma_connection, new ArretSocket(), repat.getPort(),repat.getTimeout());
			Thread le_thread = new Thread(client);
			System.out.println("On cree le thread");
			le_thread.start();
			System.out.println("Thread démarré");
		}
	}
}
