package tekno;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 *This is a static class which contains some utilities for importing a graph from an XML file to the neo4J database.
 */
public class XMLtoKnowledgeGraph {

	

	/**
	 * this method is used to load a XML formatted graph (the location of the file is given in input with the String source parameter) into the neo4J knowledge graph passed in input. Once the file is loaded, if  found, it instantiates a DocumentBuilder that gives access to methods for reading tags. It then extracts all the “node” and the “edge” tags from the file and loades them into the graph with the addNode and addEdge methods respectively.

	 * @param source the XML file location, containing a graph to be imported
	 * @param kg a KnowledgeGraph active instance
	 */
	public static void loadGraphFromXML (String source, KnowledgeGraph kg) {
		File xml_file = new File(source);
		
    	if(xml_file.canRead())
    	{
    		System.out.println("Creating Graph from XML file");
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xml_file);
		        doc.getDocumentElement().normalize();
		        NodeList nodes = doc.getDocumentElement().getElementsByTagName("node");
		        NodeList edges = doc.getDocumentElement().getElementsByTagName("edge");
		        addNode(nodes, kg);
		        addEdge(edges, kg);
	    		System.out.println("Done");

			} catch (ParserConfigurationException  | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}

	}
	
	
    /**
     *  this method takes in input a NodeList (of “edge” XML tags) and a knowledge graph instance. Cycling through the list, it extracts the starting and target node ids and the edge tag, then it inserts them into the database calling the addEdge method.

     * @param nodeList a list containing the edges of the graph (XML nodes) to be imported
     * @param knowledge_graph a KnowledgeGraph active instance
     */
    private static  void addEdge(NodeList nodeList, KnowledgeGraph knowledge_graph) {
	    	
	    	System.out.println("adding edges");
	        for (int count = 0; count < nodeList.getLength(); count++) {
	
	        Node tempNode = nodeList.item(count);
	
	        NamedNodeMap nodeMap = tempNode.getAttributes();
	        String id1 = nodeMap.item(0).getNodeValue();
	        String id2 = nodeMap.item(1).getNodeValue();
	        String type = tempNode.getTextContent();
	        String format_type = type.replace("\t", "").strip();
	        knowledge_graph.addEdge(id1, id2, format_type);


        }

      }
    
    
    /**
     * @param nodeList a list containing the nodes of the graph (XML nodes) to be imported
     * @param knowledge_graph a KnowledgeGraph active instance
     */
    private static  void addNode(NodeList nodeList, KnowledgeGraph knowledge_graph) {
	    	int len = nodeList.getLength();
	    	System.out.println("adding nodes");
	
	        for (int count = 0; count < len ; count++) {
	        	//System.out.print(""+(count+1)+"/"+len+"\r");
	        	Node tempNode = nodeList.item(count);
	        	NamedNodeMap nodeMap = tempNode.getAttributes();
	        	String id = nodeMap.item(0).getNodeValue();
	
	            NodeList data = tempNode.getChildNodes();
	            
	            String name = data.item(1).getTextContent();
	            String type = data.item(3).getTextContent();
	
	            knowledge_graph.addNode(id, name, type);
        
       }

        
    }
}
