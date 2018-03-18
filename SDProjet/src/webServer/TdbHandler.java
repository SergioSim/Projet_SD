package webServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TdbHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange he) throws IOException {
		BufferedReader br = null;
    	br = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File("tableauDeBord.html"))));
	
			String maligne;
			String response = "";
			while((maligne = br.readLine()) != null){
				response = response + maligne;
			}

            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
            br.close();
            System.out.println("fin de connection...");

	}

}
