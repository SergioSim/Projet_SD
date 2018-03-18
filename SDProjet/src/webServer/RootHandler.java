package webServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RootHandler implements HttpHandler {
	
	public String htmlfile = "index.html";
	
	public RootHandler(String htmlfile) {
		super();
		this.htmlfile = htmlfile;
	}
	
    public RootHandler() {
		super();
	}

	@Override

    public void handle(HttpExchange he) throws IOException {
//        System.out.println("RESPONSE : "+ph.parameters);
    	BufferedReader br = null;
    	br = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File(htmlfile))));
	
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