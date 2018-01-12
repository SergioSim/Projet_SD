package serveurCentraliseSimplifie;

import java.io.IOException;

public class Gen_Clients implements Runnable{

	public static void main(String[] args) {
			Thread monrepartiteur = new Thread(new Gen_Clients());
			monrepartiteur.start();
		for (int i = 0; i < 3; i++) {
			String mon_id = String.format("Client_%d", i);
			Thread_Client currrent_client = new Thread_Client(mon_id);
			Thread myThread = new Thread(currrent_client);
			myThread.start();
		}

	}

	@Override
	public void run() {
		Repartiteur connectionManager;
		try {
			connectionManager = new Repartiteur();
			connectionManager.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}