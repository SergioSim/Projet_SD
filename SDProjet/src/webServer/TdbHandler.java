package webServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TdbHandler implements HttpHandler {

	public Map<String, Object> myparameters = new HashMap<String, Object>();
	public String querryresult = "";
	private BufferedReader tampon_Lecture;
	private PrintWriter ma_sortie;
	private String port;
	private long id;

	public TdbHandler(BufferedReader tampon_Lecture, PrintWriter ma_sortie, String port, long id) {
		this.tampon_Lecture = tampon_Lecture;
		this.ma_sortie = ma_sortie;
		this.port = port;
		this.id = id;
	}
	
	@Override
	public void handle(HttpExchange he) throws IOException {
		PostHandler ph = new PostHandler();
    	ph.handle(he);
    	myparameters = ph.parameters;
    	executequerry();
		BufferedReader br = null;
    	br = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File("tableauDeBord.html"))));
	
			String maligne;
			String response = "";
			while((maligne = br.readLine()) != null){
				if(maligne.contains("supersecreturl")) {
					maligne = maligne.replace("supersecreturl", "Port="+port+"Id="+id);
				}
				if(maligne.contains("messageImprtant")){
					maligne = maligne.replace("messageImprtant",querryresult);
				}
				response = response + maligne;
			}
//			querryresult = "";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
            br.close();
            System.out.println("fin de connection...");

	}

	private void executequerry() {
		if(myparameters.containsKey("type")) {
			if(myparameters.get("type").equals("placer")) {
				ma_sortie.println("Placer "+ myparameters.get("id") + " " + myparameters.get("placer"));
				getResult();
			}else if(myparameters.get("type").equals("mettre")) {
				ma_sortie.println("Mettre "+ myparameters.get("titre") + " " + myparameters.get("description") + " " + myparameters.get("categorie") + " " + myparameters.get("prix")+ " " + myparameters.get("fin"));
				getResult();
			}else {
				querryresult = "pas des messages importantes!";
			}
		}
	}

	public void getResult() {
		String res = "";
		try {
			while(!tampon_Lecture.ready()) {
				//WAIT
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			while(tampon_Lecture.ready()) {
				res = tampon_Lecture.readLine();
				System.out.println("RES = " + res);
				querryresult = res;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
