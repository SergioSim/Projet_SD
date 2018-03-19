package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class DeconnectionHandler implements HttpHandler {

	private BufferedReader tampon_Lecture;
	private PrintWriter ma_sortie;
	private String port;
	private long id;
	private HttpServer server;

	public DeconnectionHandler(BufferedReader tampon_Lecture, PrintWriter ma_sortie, String port, long id, HttpServer server) {
		this.tampon_Lecture = tampon_Lecture;
		this.ma_sortie = ma_sortie;
		this.port = port;
		this.id = id;
		this.server = server;
	}

	@Override
	public void handle(HttpExchange he) throws IOException {
		String lecture = "";
		while(tampon_Lecture.ready()) {
			lecture = tampon_Lecture.readLine();
			System.out.println(lecture);
		}
		String response = "";
		response = getResponse();
		System.out.println("Response = " + response);
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
        System.out.println("deconnectionhandler fini...");
        server.removeContext("/getInfos"+"Port="+port+"Id="+id);
        server.removeContext("/tableauDeBord"+"Port="+port+"Id="+id);

        server.removeContext("/deconnection"+"Port="+port+"Id="+id);
	}
	
	public String getResponse() throws IOException {
		ma_sortie.println("" + (char) 4);
		String response = "";
		String lecture = "";
		while(tampon_Lecture.ready()) {
			lecture = tampon_Lecture.readLine();
			System.out.println(lecture);
			response = response +  lecture;
		}
		return response;
	}

}
