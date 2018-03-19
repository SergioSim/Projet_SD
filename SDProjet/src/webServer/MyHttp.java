package webServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import serveurCentraliseSimplifie.Repartiteur;

public class MyHttp {
	public static int port = 8080;
	public HttpServer server;
	
	public void startServer() {
		try {
			server = HttpServer.create(new InetSocketAddress(port), 0);
			System.out.println("http server started at " + port);
			server.createContext("/", new RootHandler());
			server.createContext("/inscription.html", new RootHandler("inscription.html"));
			server.createContext("/message", new MessageHandler(this));
			server.setExecutor(null);
			server.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		MyHttp myhttp = new MyHttp();
		new Thread(myhttp.new Tread_Repartiteur()).start();
		myhttp.startServer();
	}
	
	public class Tread_Repartiteur implements Runnable{

		@Override
		public void run() {
			Repartiteur connectionManager;
			try {
				connectionManager = new Repartiteur();
				connectionManager.execute();
				connectionManager.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
