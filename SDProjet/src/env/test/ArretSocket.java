package env.test;

/**
 * 
 * @author Witcher
 * Cette classe permet l'arret du serveur principal
 * Lorsqu'un utilisateur en fait la demande.
 */

public class ArretSocket {
	private boolean arret;
	
	public ArretSocket() {
		this.arret = false;
	}
	
	public synchronized void setTrue() {
		this.arret = true;
	}
	
	public synchronized void setFalse() {
		this.arret = false;
	}
	
	public synchronized boolean getState() {
		return this.arret;
	}
}
