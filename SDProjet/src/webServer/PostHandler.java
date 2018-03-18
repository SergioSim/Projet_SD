package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class PostHandler implements HttpHandler {

	public Map<String, Object> parameters = new HashMap<String, Object>();
	
    @Override
    
    public void handle(HttpExchange he) throws IOException {
            // parse request
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query);

            // send response
//            String response = "";
//            for (String key : parameters.keySet())
//                     response += key + " = " + parameters.get(key) + "\n";
//            he.sendResponseHeaders(200, response.length());
//            OutputStream os = he.getResponseBody();
//            os.write(response.toString().getBytes());
//            os.close();
    }
    
    public void parseQuery(String query) throws UnsupportedEncodingException {

    	         if (query != null) {
    	                 String pairs[] = query.split("[&]");
    	                 for (String pair : pairs) {
    	                          String param[] = pair.split("[=]");
    	                          String key = null;
    	                          String value = null;
    	                          if (param.length > 0) {
    	                          key = URLDecoder.decode(param[0], 
    	                          	System.getProperty("file.encoding"));
    	                          }

    	                          if (param.length > 1) {
    	                                   value = URLDecoder.decode(param[1], 
    	                                   System.getProperty("file.encoding"));
    	                          }

    	                          if (parameters.containsKey(key)) {
    	                                   Object obj = parameters.get(key);
    	                                   if (obj instanceof List<?>) {
    	                                            @SuppressWarnings("unchecked")
													List<String> values = (List<String>) obj;
    	                                            values.add(value);

    	                                   } else if (obj instanceof String) {
    	                                            List<String> values = new ArrayList<String>();
    	                                            values.add((String) obj);
    	                                            values.add(value);
    	                                            parameters.put(key, values);
    	                                   }
    	                          } else {
    	                                   parameters.put(key, value);
    	                          }
    	                 }
    	         }
    	}
}