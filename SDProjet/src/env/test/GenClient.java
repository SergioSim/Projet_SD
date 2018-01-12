package env.test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;

import env.test.Client;

/**
 * 
 * @author Idryss Bourdier
 * Génère des clients à la volée
 * de la même façon que telnet.
 * 
 */

public class GenClient {
	
	private int nb_clients;
	
	public GenClient(int nb_clients) {
		this.nb_clients = nb_clients;
	}
	
	public int getNbClients() {
		return this.nb_clients;
	}
	
	public String[] genUserNames() {
		return new String[] {"alkair83", "dp_bg889"};
	}
	
	public HashMap<String, String> genPasswords() {
		HashMap<String, String> passMap = new HashMap<String, String>();
		passMap.put("alkair83", "test");
		passMap.put("dp_bg889", "test");
		return passMap;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		GenClient gen = new GenClient(2);
		String[] userNames = gen.genUserNames();
		HashMap<String, String> pass = gen.genPasswords();
		for (int i = 0; i < gen.getNbClients(); i++) {
			Client le_client = new Client(500, 2, userNames[i], pass.get(userNames[i]));
			Thread cl_thread = new Thread(le_client);
			System.out.println("Client cree : arrivé de " + userNames[i] + " sur le serveur");
			cl_thread.start();
		}
	}
}
