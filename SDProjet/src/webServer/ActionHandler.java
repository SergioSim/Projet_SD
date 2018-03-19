package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ActionHandler implements HttpHandler {

	private BufferedReader tampon_Lecture;
	private PrintWriter ma_sortie;
	private String port;
	private long id;

	public ActionHandler(BufferedReader tampon_Lecture, PrintWriter ma_sortie, String port, long id) {
		this.tampon_Lecture = tampon_Lecture;
		this.ma_sortie = ma_sortie;
		this.port = port;
		this.id = id;
	}

	@Override
	public void handle(HttpExchange arg0) throws IOException {
		// TODO Auto-generated method stub

	}

}
