package tekno;

import org.jsoup.Jsoup;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.json.*;

/**
 * This class is concerned with implementing an interface to communicate with the Wikipedia API.
 *
 */
public class WikipediaIntegration {
	
	private static  String base_URL = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=";

    /**
     * this method, researches the String given in input on Wikipedia, by adding it to the base URL attribute. The HTTP request is done with the library Jsoup. This process returns a JSON object from the Wikipedia API; if it contains the attribute “extract” the method returns it, otherwise it returns an empty string.
     * @param entity the string to be searched on Wikipedia (e.g. "bill gates")
     * @return the extract of the Wikipedia page correspondig to the input string, empty string if the page is not found
     */
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
    	
    
    	
	    	System.out.println("writing file: "+ entity);
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
    

    public static boolean isSameWiki (String entity1, String entity2) {
    	
    	try {

    		String json1 = Jsoup.connect(base_URL+entity1.replace(" ", "%20")).ignoreContentType(true).execute().body();
    		String json2 = Jsoup.connect(base_URL+entity2.replace(" ", "%20")).ignoreContentType(true).execute().body();

    		JSONObject obj1 = (new JSONObject(json1)).getJSONObject("query").getJSONObject("pages");
    		JSONObject obj2 = (new JSONObject(json2)).getJSONObject("query").getJSONObject("pages");

    		int pageId1 = obj1.getJSONObject(obj1.keySet().toArray()[0].toString()).getInt("pageid");
    		int pageId2 = obj2.getJSONObject(obj2.keySet().toArray()[0].toString()).getInt("pageid");

    		if (pageId1 == pageId2)
    			return true;
    	
    	} catch (Exception e) {
			//System.err.println(e.getMessage());
		}
		return false; 
    }

    
    /**
     * researches the String given in input on Wikipedia, by adding it to the base URL attribute. The HTTP request is done with the library Jsoup. This process returns a JSON object from the Wikipedia API from which the matched page ID (positive integer) is extracted and then returned by the method. If the String doesn’t match any page, it returns a control value of -1.
     * @param entity the string to be searched on Wikipedia (e.g. "bill gates")
     * @return matched page ID (positive integer), -1 if nothing is found.

     */
    public static int getWikiID (String entity) {
    	int pageId = -1;
    	try {

    		String json1 = Jsoup.connect(base_URL+entity.replace(" ", "%20")).ignoreContentType(true).execute().body();

    		JSONObject obj1 = (new JSONObject(json1)).getJSONObject("query").getJSONObject("pages");

    		pageId = obj1.getJSONObject(obj1.keySet().toArray()[0].toString()).getInt("pageid");
    	} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return pageId; 
    }
    
}
