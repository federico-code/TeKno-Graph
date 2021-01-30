package tekno;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.FileOutputStream;




public class WikipediaIntegration {
	String Wikipedia_base_url ;
	Charset utf8 = StandardCharsets.UTF_8;

	
	
	static String URL_test ="https://en.wikipedia.org/w/api.php?" //main endpoint.
			+ "action=query&"
			+ "prop=revisions&"
			+ "rvprop=content&"
			+ "format=jsonfm&" //Format output
			+ "titles=Elon%20Musk&"//title
			+ "rvsection=0";
	
	//URL_test= "https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=xmlfm&titles=Elon%20Musk&rvsection=0";
	   
	
	   public WikipediaIntegration(String base_url_input) {
		   Wikipedia_base_url = base_url_input;
		   
		   }
	   
	   public void getAllPage(String topic, String folder){
    	
    	/*
    	Map<String, String> params = new HashMap<>();
    	params.put("action", "query");
    	params.put("prop", "revision");
    	params.put("rvprop", "content");
    	params.put("format", "jsonfm");
    	params.put("titles", "Elon%20Musk");
    	params.put("rvsection", "0");
    	*/
       	Writer writer = null;
    	
    	try {
    		
			//URL url = new URL(url_input);
			//HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			//connection.setRequestMethod("GET");
			
    		
	    	System.out.println("retrive page: "+ topic);
    		String url= this.Wikipedia_base_url + "/" + topic;
        	Document doc = Jsoup.connect(url).get();
        	Elements doc_paragraf = doc.select("p");
        	
        	
        	String content = "";
        	for(Iterator<Element> i =doc_paragraf.iterator(); i.hasNext(); ) {
        		Element item = i.next();
        		content = content + item.text()+ "\r\n";	
        	}
        	
	    	System.out.println("writin file: "+ topic);
	    	File f = new File (folder,topic+".txt");
	    	f.createNewFile();
	    	System.out.println("path file: "+ f.getAbsolutePath());
        	writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
        	writer.write(content);

    		
			/*
			// make parameters to byte
		    System.out.println("Adding parameters ....");
			byte[] requestDataBytes =prepareParameter(params);
			//adding parameters to request
	    	connection.setRequestProperty("Content-Length", String.valueOf(requestDataBytes.length));
	    	*/
			
			
			/*
			int responseCode = connection.getResponseCode();
		    System.out.println("\nSending 'GET' request to URL : " + url);
		    System.out.println("Response Code : " + responseCode);
			
			
		    String inline= "";
			
		    if(responseCode!= 200)
		    	throw new Exception("Response Code" + responseCode);
		    else {
		    	
		    	Scanner sc = new Scanner (url.openStream());
		    	while(sc.hasNext()) {
		    		inline+=sc.nextLine()+"\n";
		    	}
		    	
		    	
		    	 //System.out.println("\n string json format");
				 //System.out.println(inline);
				    
				 sc.close();
		    }
		    */
    	
    	
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} finally {
			   try {writer.close();} catch (Exception ex) {/*ignore*/}
		    	System.out.println("file close ");

		}
    }
    
    
    
    public static void HTMLtoJson (String HTMLString) throws IOException {
    	
    	Document doc = Jsoup.connect(HTMLString).get();   
    	Elements infobox = doc.getElementsByClass("infobox biography vcard");
    	//Attributes attr= infobox.attr("data-mw");
    	
    	
    	for (Iterator<Element> i = infobox.iterator(); i.hasNext();)
    	{
    		Element elem=i.next();
    		Attributes attr= elem.attributes();
        	System.out.println(attr.get("data-mw"));


    	}
    	
    	//System.out.println(doc.getElementsByClass("infobox biography vcard"));

    }
    
    
    // set the parameters for the request
   public static byte[] prepareParameter (Map<String, String> params) throws UnsupportedEncodingException {
	   
	//  adding parameters
		StringBuilder requestData = new StringBuilder();
    	
    	for (Map.Entry<String, String> param : params.entrySet()) {
    	    if (requestData.length() != 0) {
    	        requestData.append('&');
    	    }
    	    // Encode the parameter based on the parameter map we've defined
    	    // and append the values from the map to form a single parameter
    	    requestData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
    	    requestData.append('=');
    	    requestData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
    	}
    	
    	// Convert the requestData into bytes 
    	byte[] requestDataBytes = requestData.toString().getBytes("UTF-8");
		return requestDataBytes;	
    }
   
 
    

    
    
}
