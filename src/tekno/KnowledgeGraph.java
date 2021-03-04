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

/**
 * Takes care of the connection with the Neo4J DBMS, handling data inserts and exports from the graph
 *
 */
public class KnowledgeGraph implements AutoCloseable {
    
	private Driver driverNeo4j; // it is the driver used for the connection with the neo4J DBMS.
    private String URI_NEO4J; // must be in the form of  bolt://<HOST>:<PORT> 
    private String USR_NEO4J;
    private String PSW_NEO4J; 



    /**
     * Constructor of the class.  
     * It takes no arguments,and if called asks for the parameters, namely URI, user and password on the console log.
     * After inserting the parameters, it instantiates the connection on the private attribute driverNeo4J.
     */
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
    
    /**
     * Constructor of the class. No request will be asked to the user.
     * Automatically create the connection with the database
     * 
     * @param uri connection string for the database
     * @param usr user name of the database
     * @param pwd password of the database
     */
    public KnowledgeGraph(String uri, String usr, String pwd) {
    	this.URI_NEO4J = uri;
    	this.USR_NEO4J = usr;
    	this.PSW_NEO4J = pwd;
        this.driverNeo4j = GraphDatabase.driver(this.URI_NEO4J, AuthTokens.basic(this.USR_NEO4J, this.PSW_NEO4J));
    }
    
    /**
     * Constructor of the class. No request will be asked to the user.
     * Automatically create the connection with the database, and automatically loads data from an XML file passed in input as String xml_source. 
     * This constructor calls another static class called XMLtoKnowledgeGraph.
     *  
     * @param uri connection string for the database
     * @param usr user name of the neo4j database
     * @param pwd password of the neo4j database
     * @param xml_source the file from witch the db will be created
     */
    public KnowledgeGraph(String uri, String usr, String pwd, String xml_source) {
    	this.URI_NEO4J = uri;
    	this.USR_NEO4J = usr;
    	this.PSW_NEO4J = pwd;
        this.driverNeo4j = GraphDatabase.driver(this.URI_NEO4J, AuthTokens.basic(this.USR_NEO4J, this.PSW_NEO4J));
        XMLtoKnowledgeGraph.loadGraphFromXML(xml_source, this);
    }
 
    /**
     *  load the graph starting from the xml file

     * @param source xml file for the load of the graph
     */
    public void loadGraphFromXML(String source) {
        XMLtoKnowledgeGraph.loadGraphFromXML(source, this);
    }
    
    /**
     * 
     * @return
     */
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
    
    /**
     *  extracts prolog facts from the current knowledge base stored in the neo4j database.
     * @param fileName name of the output file where the facts will be stored
     * @param folder  folder location to which the file will be saved.
     */
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
    
    
    /**
     * adds a new node to the graph, if not already present. if the node is already present with the isNode method, 
     * then after establishing a new Session it performs a query to add a new object to the database, with the specified id, name and type.
     * @param id 
     * @param name
     * @param type
     */
    public void addNode(String id, String name, String type) {
        if (!this.isNode(id)) {
            try (Session session = this.driverNeo4j.session()) {
                String cypherQuery = "CREATE (n: n_" + id + " {id: '" + id + "'," + "name: '" + name + "'," +"type: '" + type + "'" + " })";
                session.run(cypherQuery);
            }
        }
    }
    
    
    /**
     * @param id
     * @param type
     */
    public void addNodeProperty(String id, String type) {
        if (this.isNode(id)) {
            try (Session session = this.driverNeo4j.session()) {
                String cypherQuery = "MATCH (n {id: '"+id+"'}) SET n.type = '"+type+"' 	RETURN n.type";
                session.run(cypherQuery);
            }
        }
    
    }

    
    /**
     * @param name
     * @return
     */
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
    
    
    /**
     * @param id1
     * @param id2
     * @param type
     */
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

    
    /**
     *   this method checks, with a query, if the node with the id specified in input, 
     *   is already present in the knowledge base. It returns true if it exists, false otherwise.
     * @param id id of the node to check 
     * @return
     */
    private Boolean isNode(String id) {

        String cypherQuery = "MATCH (n {id: '" + id + "'}) RETURN n";
        try (Session session = this.driverNeo4j.session()) {
            Result result = session.run(cypherQuery);
            if (result.hasNext()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param id1
     * @param id2
     * @param type
     * @return
     */
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


    /**
     * @return
     */
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
    
    
    /**
     *
     */
    public void close() throws Exception {
        this.driverNeo4j.close();
    }


   
}
