package serveurCentraliseSimplifie;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Repartiteur extends ServerSocket{
	private final static int port = 12000; /* Port d'écoute */

	public Repartiteur() throws IOException {
		super(port);
		System.out.println("[Serveur] : Server started on port: " + (port));
	}

	public void execute() throws IOException {
		Socket maConnection;
		while (true) {
			System.out.println("[Serveur]:  waiting for connexion");
			maConnection = accept();
			String c_ip = maConnection.getInetAddress().toString();
			int c_port = maConnection.getPort();
			System.out.format("[Serveur] : Arrival of Client with IP %s on port %d\n", c_ip, c_port);
			System.out.format("[Serveur]: Creating thread T_%d...\n", c_port);
			new Thread(new ServiceClient(maConnection, "T_" + c_port)).start();
		}
	}

	public static void main(String[] args) throws IOException {
		Repartiteur connectionManager = new Repartiteur();
		connectionManager.execute();
		connectionManager.close();
	}
}