package tekno;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

public class KnowledgeGraph implements AutoCloseable {
    private Driver driverNeo4j;

    private String URI_NEO4J;
    private String USR_NEO4J;
    private String PSW_NEO4J; 



    public KnowledgeGraph() {
    	Scanner scanner = new Scanner(System.in);
        System.out.print("Enter connection URI: ");
        String uri = scanner.next();
        System.out.print("Enter neo4J USER: ");
        String usr = scanner.next();
        System.out.print("Enter neo4J user PASSWORD: ");
        String pwd = scanner.next();
        scanner.close();
        Console console = System.console();
        
        if(console != null){
          console.readPassword("Enter Password: ");
        }
        
    	this.URI_NEO4J = uri;
    	this.USR_NEO4J = usr;
    	this.PSW_NEO4J = pwd;
        this.driverNeo4j = GraphDatabase.driver(this.URI_NEO4J, AuthTokens.basic(this.USR_NEO4J, this.PSW_NEO4J));
    }
    
    public KnowledgeGraph(String uri, String usr, String pwd) {
    	this.URI_NEO4J = uri;
    	this.USR_NEO4J = usr;
    	this.PSW_NEO4J = pwd;
        this.driverNeo4j = GraphDatabase.driver(this.URI_NEO4J, AuthTokens.basic(this.USR_NEO4J, this.PSW_NEO4J));
    }
    
    public KnowledgeGraph(String uri, String usr, String pwd, String xml_source) {
    	this.URI_NEO4J = uri;
    	this.USR_NEO4J = usr;
    	this.PSW_NEO4J = pwd;
        this.driverNeo4j = GraphDatabase.driver(this.URI_NEO4J, AuthTokens.basic(this.USR_NEO4J, this.PSW_NEO4J));
        XMLtoKnowledgeGraph.loadGraphFromXML(xml_source, this);
    }
 
    public void loadGraphFromXML(String source) {
        XMLtoKnowledgeGraph.loadGraphFromXML(source, this);
    }
    
    public List<Fact> extractFacts() {
    	List<Fact> facts = new LinkedList<Fact>();

        try (Session session = this.driverNeo4j.session()) {
            String cypherQuery = "MATCH (n1)-[r]->(n2) RETURN r, n1, n2";
            Result result = session.run(cypherQuery);
	        while(result.hasNext()) {
	        
	        	  Record record = result.next();
	        	  String[] atoms = {record.get(1).get("id").asString(), record.get(2).get("id").asString() };
	        	  facts.add(new Fact(record.get(0).get("type").asString(), atoms));
	        }
        }
        return facts;
    }
    
    public void extractFacts(String fileName, String folder) {
       	
    	Writer writer = null;       	
    	ArrayList<String> literal_inserted = new ArrayList<String>();
        try (Session session = this.driverNeo4j.session()) {
            String cypherQuery = "MATCH (n1)-[r]->(n2) RETURN r, n1, n2";
            Result result = session.run(cypherQuery);
            
            System.out.println("writing file: "+ fileName);
        	File f = new File (folder,fileName+".pl");
        	f.createNewFile();
        	System.out.println("path file: "+ f.getAbsolutePath());
        	writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
            
	        while(result.hasNext()) {
	        
	        	  Record record = result.next();	        	  
	        	  
	          	  writer.write(new Fact(record.get(0).get("type").asString(), new String[] {record.get(1).get("id").asString(), record.get(2).get("id").asString() }).prologFacts());
	          	  writer.write("\n");
	          	  
	          	  if(!record.get(1).get("type").asString().contentEquals("o")) {			        	
	     		      if(!literal_inserted.contains(record.get(1).get("type").asString()+"_"+record.get(1).get("id").asString())) {
		        		  literal_inserted.add(record.get(1).get("type").asString()+"_"+record.get(1).get("id").asString());

			          	  writer.write(new Fact(record.get(1).get("type").asString(), new String[] {record.get(1).get("id").asString()}).prologFacts());
			          	  writer.write("\n");	    	  
	     		      }

	          	  }
	          	  
	          	  if(!record.get(2).get("type").asString().contentEquals("o")) {
	     		      if(!literal_inserted.contains(record.get(2).get("type").asString()+"_"+record.get(2).get("id").asString())) {
		        		  literal_inserted.add(record.get(2).get("type").asString()+"_"+record.get(2).get("id").asString());
	     		      
			          	  writer.write(new Fact(record.get(2).get("type").asString(), new String[] {record.get(2).get("id").asString()}).prologFacts());
			          	  writer.write("\n");	  
	     		      }
	          	  }
	          	  

	        	  if(!literal_inserted.contains(record.get(1).get("id").asString())) {
	        		  literal_inserted.add(record.get(1).get("id").asString());
		        	  writer.write( new Fact("literal_of", new String[] {record.get(1).get("id").asString(), record.get(1).get("name").asString()}).prologFacts());
		          	  writer.write("\n");
	        	  }

	        	  if(!literal_inserted.contains(record.get(2).get("id").asString())) {
	        		  literal_inserted.add(record.get(2).get("id").asString());
		        	  writer.write( new Fact("literal_of", new String[] {record.get(2).get("id").asString(), record.get(2).get("name").asString() }).prologFacts());
		          	  writer.write("\n");
	        	  }
	        }
	        
	        writer.write(":- ensure_loaded('\\\\dynamics_facts_rules.pl').\n");
        
    	} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			   try {writer.close();} catch (Exception ex) {/*ignore*/}
		    	System.out.println("file :"+fileName +".pl close ");

		}
        
    }
    
    
    public void addNode(String id, String name, String type) {
        if (!this.isNode(id)) {
            try (Session session = this.driverNeo4j.session()) {
                String cypherQuery = "CREATE (n: n_" + id + " {id: '" + id + "'," + "name: '" + name + "'," +"type: '" + type + "'" + " })";
                session.run(cypherQuery);
            }
        }
    }
    
    
    public void addNodeProperty(String id, String type) {
        if (this.isNode(id)) {
            try (Session session = this.driverNeo4j.session()) {
                String cypherQuery = "MATCH (n {id: '"+id+"'}) SET n.type = '"+type+"' 	RETURN n.type";
                session.run(cypherQuery);
            }
        }
    
    }

    
    public String getNodeId(String name) {
        String id = null;
    	try (Session session = this.driverNeo4j.session()) {
            String cypherQuery = "MATCH (n {name: '"+name+"'}) RETURN n";
            Result result = session.run(cypherQuery);
            while(result.hasNext())
            	id = result.next().get(0).get("id").asString();
        }
    	return id;
    }
    
    
    public void addEdge(String id1, String id2, String type) {

        if (this.isNode(id1) && this.isNode(id2) && !isEdge(id1, id2, type)) {
	        String cypherQuery = "MATCH (a),(b) "
	        		+ "WHERE a.id = '"+id1+"' AND b.id = '"+id2+"' "
	        		+ "CREATE (a)-[r: "+type+ " {start: '"+id1+"', type: '"+type+"', end: '"+id2+"'  }]->(b) "
	        		+ "RETURN type(r)";
	        
	        try (Session session = this.driverNeo4j.session()) {
	            session.run(cypherQuery);
	        }
        }
    }

    
    public Boolean isNode(String id) {

        String cypherQuery = "MATCH (n {id: '" + id + "'}) RETURN n";
        try (Session session = this.driverNeo4j.session()) {
            Result result = session.run(cypherQuery);
            if (result.hasNext()) {
                return true;
            }
        }
        return false;
    }

    public Boolean isEdge(String id1, String id2, String type) {

        String cypherQuery = "MATCH (n1 {id: '" + id1 + "'})-[r { type: '"+type+"' }]-> (n2 {id: '" + id2 + "'})  RETURN r";
        try (Session session = this.driverNeo4j.session()) {
            Result result = session.run(cypherQuery);
            if (result.hasNext()) {
                return true;
            }
        }
        return false;
    }


    public Boolean resetGraph() {
    	String cypherQuery = "MATCH (n1) detach delete  n1";
        try (Session session = this.driverNeo4j.session()) {
            Result result = session.run(cypherQuery);
            if (!result.hasNext()) {
                return true;
            }
        }
        return false;
    }
    
    
    public void close() throws Exception {
        this.driverNeo4j.close();
    }


   
}
