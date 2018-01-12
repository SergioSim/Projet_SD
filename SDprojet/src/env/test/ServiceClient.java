package env.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import env.test.ArretSocket;

public class ServiceClient implements Runnable {
	private Socket la_connection;
	private ArretSocket arret;
	private int port;
	private int timeout;
	
	public ServiceClient(Socket connection, ArretSocket socket, int port, int timeout) {
		this.la_connection = connection;
		this.arret = socket;
		this.port = port;
		this.timeout = timeout;
	}
	
	private void terminer(Socket ma_connection){
		if (ma_connection != null) {
			try {
				ma_connection.close();
				arret.setFalse();
				System.out.format("Socket fermé \n"); 
		    }
			catch ( IOException e ) {
				System.out.println("weird, nawak .... \n ");
			}
		}
	}

	@Override
	public void run() {
		try {
			// Definition du timeout
			this.la_connection.setSoTimeout(timeout);
			
			/* On Associe une file d'entrée a la connection */
	        InputStreamReader isr = null;
	        isr = new InputStreamReader(la_connection.getInputStream());
	        
			/* On transforme cette file en file avec tampon */
		    BufferedReader flux_entrant = new BufferedReader(isr);
		    System.out.println("Tampon entree attache ");
		    PrintWriter ma_sortie = null;
		    ma_sortie = new PrintWriter(la_connection.getOutputStream(), true);
		    
		    System.out.println("Sortie attachée");
		    System.out.println("Prêt à servir le Client : "+ la_connection.getRemoteSocketAddress());
		    
		    String clientName = la_connection.getRemoteSocketAddress().toString();
		    String message_lu = new String();
		    int line_num = 0;
		    /*
		     * On lit le flux_entrant ligne � ligne ATTENTION : La fonction readline
		     * est Bloquante readline retourne null si il y a souci avec la
		     * connexion On s arrete aussisi connexion_non_terminee est vraie
		     */
		    ma_sortie.format("Bonjour %s j attends tes données  \n",clientName);
		    while ((message_lu = flux_entrant.readLine()) != null) {
				System.out.format("%d: ->  [%s]\n", line_num, message_lu);
			    line_num++;
			/* si on recoit Finish on clot et annonce cette terminaison */
			    if (message_lu.contains("Finish")) {
			    	System.out.println("Reception de  " + "Finish" + " -> Transmission finie");
				    // On ferme la connection
				    System.out.format("Je clos la connection  %s :\n",clientName);
				    terminer(la_connection);
				    //return (true);
			    } else if (message_lu.contains("ATTENDRE")) {
			    	ma_sortie.println("Le serveur répond");
			    }
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			terminer(la_connection);
			System.out.println("Timeout, connexion rompue.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
