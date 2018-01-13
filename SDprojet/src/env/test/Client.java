package env.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Random;
import java.util.Scanner;

import env.secure.GenSig;
import env.secure.SigData;

/**
 * 
 * @author Idryss Bourdier
 * Simule un client.
 * 
 */

public class Client implements Runnable {
	private String hote;
	private int port;
	private int delay;
	private int limit;
	private String name;
	private String pwd;
	private GenSig keypair;
	
	public Client(int delay_msg, int msg_limit, String name, String pwd) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		this.hote = "127.0.0.1";
		this.port = 12000;
		this.delay = delay_msg;
		this.limit = msg_limit;
		this.name = name;
		this.pwd = pwd;
		this.secureClient();
	}
	
	public void secureClient() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		this.keypair = new GenSig();
		this.keypair.genKeyPair(this.name);
	}
	
	public String randomAction() {
		Random rand = new Random();
		EActionList[] actions = EActionList.values();
		int max = (actions.length) - 1;
		int min = 1;
		int randomNum = rand.nextInt((max - min) + 1) + min;
		//System.out.println(EActionList.valueOf(actions[randomNum].name()).theAction());
		//return EActionList.valueOf(actions[randomNum].name()).theAction();
		return EActionList.ACTION.getContent(EActionList.valueOf(actions[randomNum].name()));
	}
	
	/**
	 * Execute une action aleatoire puis envoit la commande au serveur.
	 * Une signature DSA est generee et est egalement envoyee.
	 * @throws IOException 
	 */
	
	public void doAction(Socket connexion, File f) throws IOException {
		PrintWriter ma_sortie = new PrintWriter(connexion.getOutputStream(), true);
		System.out.format(" Le client %s envoit une commande sur le serveur %s sur le port %d\n",this.name, hote ,port);
		String msg = this.randomAction();
		int encrypted = 0;
		try {
			encrypted = new SigData(this.keypair.getPrivate(), msg).SignData();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		ma_sortie.println(new String (msg + "//" + encrypted));
	}
	
	public void LogOn(Socket connexion, File f) throws IOException {
		PrintWriter ma_sortie = new PrintWriter(connexion.getOutputStream(), true);
		System.out.format(" Le client %s se connecte au serveur %s sur le port %d \n", this.name, hote ,port);
		ma_sortie.println("Oui");
		ma_sortie.println(this.name);
		ma_sortie.println(this.pwd);
	}
	
	public void LogOut(Socket connexion, File f) throws IOException {
		PrintWriter ma_sortie = new PrintWriter(connexion.getOutputStream(), true);
		System.out.format(" Le client %s se deconnecte au serveur %s sur le port %d \n",this.name, hote ,port);
		ma_sortie.println("" + (char) 4);
	}
	
//	public void receiveAction(Socket connexion) throws IOException {
//		InputStream in = connexion.getInputStream();
//		PrintWriter ma_sortie = new PrintWriter(connexion.getOutputStream(), true);
//		ma_sortie.println(in.toString());
//	}

	@Override
	public void run() {
		Socket laConnection = null;
		int i;
		try {
			laConnection = new Socket(this.hote, this.port);
			LogOn(laConnection, null);
			for (i = 0; i < this.limit; i++) {
				doAction(laConnection, null);
				Thread.sleep(this.delay);
			}
			LogOut(laConnection, null);
			Thread.sleep(2000);
			laConnection.close();
		} catch (IOException e) {
			System.out.format("Probleme de connection avec serveur fontionne : %s",e);
			System.exit(-1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
