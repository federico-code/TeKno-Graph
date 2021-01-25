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

public class XMLtoKnowledgeGraph {

	

	public static void loadGraphFromXML (String source, KnowledgeGraph kg) {
		File xml_file = new File(source);
		
    	if(xml_file.canRead())
    	{
    		System.out.println("file found");
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
			} catch (ParserConfigurationException  | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

	}
	
	
    private static  void addEdge(NodeList nodeList, KnowledgeGraph knowledge_graph) {
	    	int len = nodeList.getLength();
	    	System.out.println("adding edges");
	        for (int count = 0; count < nodeList.getLength(); count++) {
	        System.out.print(""+(count+1)+"/"+len+"\r");
	
	        Node tempNode = nodeList.item(count);
	
	        NamedNodeMap nodeMap = tempNode.getAttributes();
	        String id1 = nodeMap.item(0).getNodeValue();
	        String id2 = nodeMap.item(1).getNodeValue();
	        String type = tempNode.getTextContent();
	        String format_type = type.replace("\t", "").strip();
	        knowledge_graph.addEdge(id1, id2, format_type);


        }

      }
    
    
    private static  void addNode(NodeList nodeList, KnowledgeGraph knowledge_graph) {
	    	int len = nodeList.getLength();
	    	System.out.println("adding nodes");
	
	        for (int count = 0; count < len ; count++) {
	        	System.out.print(""+(count+1)+"/"+len+"\r");
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
