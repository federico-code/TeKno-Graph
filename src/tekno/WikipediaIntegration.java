package tekno;

import org.jsoup.Jsoup;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.json.*;

public class WikipediaIntegration {
	
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
    

    public static void getText(String entity, String folder){
    	String text="";
       	Writer writer = null;

    	try {

    		String json = Jsoup.connect(base_URL+entity.replace(" ", "%20")).ignoreContentType(true).execute().body();
    		JSONObject obj = (new JSONObject(json)).getJSONObject("query").getJSONObject("pages");
    		
    		text = (obj.getJSONObject(obj.keySet().toArray()[0].toString()).getString("extract").toString());
    	
    
    	
	    	System.out.println("writin file: "+ entity);
	    	File f = new File (folder,entity+".txt");
	    	f.createNewFile();
	    	System.out.println("path file: "+ f.getAbsolutePath());
	    	writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
	    	writer.write(text);
	    	} catch (Exception e) {
				System.err.println(e.getMessage());
			} finally {
				   try {writer.close();} catch (Exception ex) {/*ignore*/}
			    	System.out.println("file"+entity +".txt close ");
	
			}
    }

    
    
}