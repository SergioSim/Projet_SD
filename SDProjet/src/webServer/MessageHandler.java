package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MessageHandler implements HttpHandler {

	public String errors = null;
	private int serveurPort = 12000;
	private String serveurIp = "127.0.0.1";
	BufferedReader tampon_Lecture = null;
	public Map<String, Object> myparameters = new HashMap<String, Object>();
	private HttpServer server;
	
	public MessageHandler(MyHttp myHttp) {
		this.server = myHttp.server;
	}

	@Override
	public void handle(HttpExchange he) throws IOException {
		errors = null;
        String response = "<H1>Bonjour ";
        PostHandler ph = new PostHandler();
    	ph.handle(he);
    	myparameters = ph.parameters;
    	
    	Socket la_connection = null;
    	PrintWriter ma_sortie = null;
    	
		try {
			la_connection = new Socket(serveurIp, serveurPort);
			tampon_Lecture = new BufferedReader(new InputStreamReader(la_connection.getInputStream()));
			ma_sortie = new PrintWriter(la_connection.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(-1);
		}
        System.out.println("RESPONSE : "+myparameters);
        if(myparameters.containsKey("nom")) {
	        printLine(1);
	        ma_sortie.println("Non");
	        printLine(4);
	        ma_sortie.println(myparameters.get("nom"));
	        printLine(1);
	        ma_sortie.println(myparameters.get("prenom"));
	        printLine(1);
	        ma_sortie.println(myparameters.get("login"));
	        printLine(1);
	        ma_sortie.println(myparameters.get("mdp1"));
	        printLine(1);
	        ma_sortie.println(myparameters.get("mdp2"));
	        printLine(2);
	        ma_sortie.println("Oui");
	        printLine(1);
	        if(errors == null) {
	        	response = response + myparameters.get("prenom") + " " + myparameters.get("nom") +
	        			" ! </H1> </br> <h3> votre compte vient d'etre cree ! </h3> ";
	        }
	        
        }else if(myparameters.containsKey("mdp")) {
        	String port = tampon_Lecture.readLine();
        	port = port.replace("[", "");
        	port = port.split("]")[0];
        	printLine(2);
        	ma_sortie.println("Oui");
        	printLine(2);
        	ma_sortie.println(myparameters.get("login"));
	        printLine(1);
	        ma_sortie.println(myparameters.get("mdp"));
	        printLine(1);
	        long id = Math.round(Math.random() * 1000000 );
	        if(errors == null) {
	        	response = response + myparameters.get("login") +
	        			" ! </H1> </br> <h3> login et mdp corrects ! </h3> " + 
	        			" <p> Go Shopping! <a href=\"/tableauDeBord"+"Port="+port+"Id="+id+"\">here</a></p>";
	        	server.createContext("/getInfos"+"Port="+port+"Id="+id, new GetInfosHandler(tampon_Lecture,ma_sortie,port,id));
	        	server.createContext("/tableauDeBord"+"Port="+port+"Id="+id, new TdbHandler(tampon_Lecture,ma_sortie,port,id));
	        	server.createContext("/deconnection"+"Port="+port+"Id="+id, new DeconnectionHandler(tampon_Lecture,ma_sortie,port,id,server));
	        }
        }
        
        if(errors != null){
        	response = "<H1> Sorry something went wrong :[ </H1> </br> <h3> " + errors+ "</h3>";
        }
        response = response + " <p> go back! <a href=\"/\">here</a></p>";
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
        System.out.println("fini?");
	}
	
	public void printLine(int i) {
		String response;
			try {
				while(tampon_Lecture.ready()){
				response = tampon_Lecture.readLine();
				System.out.println(response);
				if(errors == null && 
						(	response.contains("Error") || 
							response.contains("invalide") ||
							response.contains("incorrect") ||
							response.contains("recommence") || 
							response.contains("je termine"))) {
					errors = response;
				}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
