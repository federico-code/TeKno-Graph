package tekno;

import org.jsoup.Jsoup;
import org.json.*;

public class WI {
	
	private static  String base_URL = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=";

    public static String getText(String entity){
    	String text="";
    	try {

    		String json = Jsoup.connect(base_URL+entity.replace(" ", "%20")).ignoreContentType(true).execute().body();
    		JSONObject obj = (new JSONObject(json)).getJSONObject("query").getJSONObject("pages");
    		
    		text = (obj.getJSONObject(obj.keySet().toArray()[0].toString()).getString("extract").toString());
    	
    	} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return text; 
    }
    
}
