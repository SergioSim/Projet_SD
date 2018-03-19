package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GetInfosHandler implements HttpHandler {

	private BufferedReader tampon_Lecture;
	private PrintWriter ma_sortie;
	private String port;
	private long id;

	public GetInfosHandler(BufferedReader tampon_Lecture, PrintWriter ma_sortie, String port, long id) {
		this.tampon_Lecture = tampon_Lecture;
		this.ma_sortie = ma_sortie;
		this.port = port;
		this.id = id;
	}

	@Override
	public void handle(HttpExchange he) throws IOException {
		while(tampon_Lecture.ready()){
			System.out.println(tampon_Lecture.readLine());
		}
		String response = "";
			//while(response == "") {
				response = getResponse();
				
			//}
			response = "<table>" + response + "</table>";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
            System.out.println("getinfoshandler fini...");
	}

	public String getResponse() throws IOException {
		ma_sortie.println("Afficher");
		String response = "";
		String lecture = "";
		String form = "<td>  <form action=\"/tableauDeBord"+"Port="+port+"Id="+id+"\" method=\"POST\">\r\n" + 
				"    Placer plus:<br>\r\n" + 
				"    <input type=\"text\" name=\"placer\">\r\n" + 
				"    <br>\r\n" + 
				"<input type=\"hidden\" name=\"id\" value=\"magique\">" +
				"<input type=\"hidden\" name=\"type\" value=\"placer\">" +
				"    <input type=\"submit\" value=\"Confirmer!\">\r\n" + 
				"  </form> </td>";
		String objId = "";
		while(!tampon_Lecture.ready()) {
			//WAIT
		}
		while(tampon_Lecture.ready()) {
			lecture = tampon_Lecture.readLine();
			objId = lecture.split(" ")[3];
			System.out.println(objId);
			System.out.println(lecture);
			response = response + "<tr><td>" + lecture + form.replace("magique", objId) + "</td></tr>";
		}
		return response;
	}

}
