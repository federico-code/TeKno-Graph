package tekno;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.CorefChain.CorefMention;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * This is the central class in our project. It is concerned with executing the complete pipeline, from reading the input file, to generating the graph (both from the analyzed text or from an XML file).
 *
 */
public class HighLevelParsing {
	

	private CoreDocument doc;
	private StanfordCoreNLP pipeline;
	private Map<String, String> ner = new HashMap<String, String>();
	private Relations rel = new Relations();
	
	/**
	 *  this is the constructor for this class, it initializes the pipeline attribute with the following arguments: tokenize, ssplit, pos, lemma, ner, parse, coref, kbp, entitymentions.

	 */
	public HighLevelParsing() {
		String pipeline = "tokenize, ssplit, pos, lemma, ner, parse, coref, kbp, entitymentions";
		System.out.println("creating pipeline: " + pipeline);
	   	Properties props = new Properties();
    	props.setProperty("annotators", pipeline);
       	this.setPipeline(props);
	}
	
	/**
	 * this is another constructor for this class, and initializes the pipeline attribute with the argument passed in input.
	 * @param annotators a string that specifies which annotators to be used in Stanford NLP pipeline
	 */
	public HighLevelParsing(String annotators) {
	   	Properties props = new Properties();
    	props.setProperty("annotators", annotators);
       	this.setPipeline(props);
	}
	
	
	/**
	 * this method, given a file location, analyzes the text in the specified file. If the String source_file is correct, the attribute doc is initialized by creating a new CoreDocument with the text extracted from the file, after some formatting with the method formatString.

	 * @param source_file the location of the file to be read
	 */
	public void readFile(String source_file) {
		System.out.println("Reading file: " + source_file);

	    try  {
	    	String data = "";
	        File file = new File(source_file);
	        Scanner reader = new Scanner(file);
	        while (reader.hasNextLine()) {
		         data += reader.nextLine();
	        }
	        
	        this.doc = new CoreDocument(this.formatString(data));
	        reader.close();
        } catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
        }
        System.out.println("Document created.");

	}
	
	

	public Map<String, String> getNer() {
		return this.ner;
	}
	

	public void printDocumentText() {
		this.doc.sentences().forEach(s-> {System.out.println(s);});
	}
	

	public void printRelations() {
		this.rel.printNodes();
		this.rel.printEdges();
	}


	/**
	 * this method allows to do some pre-formatting of the text to analyze. For example it removes special characters like \n or \r.
	 * @param inStr the string to be formatted
	 * @return the formatted string
	 */
	private String formatString(String inStr) {
		return inStr.
				replace("\n", " ").
				replace("\r", " ").
				replace(".", ". ").
				replace(",", ", ").
				replace(" ,", ",").
				replace(" .", ".").
				replace("  ", " ").
				replace("'", "\'").
				replace(" the", "").
				replace("The", "").
				replaceAll("\\[[^\\]]*\\]", "");
	}
	
	
	
	/**
	 * this method initializes the pipeline attribute with the input given properties.
	 * @param props a Property object which should be initialized as such: 
	 * 	Properties props = new Properties();
    	props.setProperty("annotators", pipeline);
	 */
	public void setPipeline(Properties props) {
		this.pipeline = new StanfordCoreNLP (props);
	}
	
	
	/**
	 * this method is a wrapper for the annotate method of the pipeline attribute. It gets executed on the current document, instantiated into the doc attribute. 

	 */
	public void annotateDocument () {
        System.out.print("Document annotation ...");
		this.pipeline.annotate(this.doc);
        System.out.print(" done.\n");
	}
	
	
	/**
	 * this method searches for the coreference chains in the text, with Stanford’s library method, then, cycling through the tokens in the text, replaces the terms with the alias found in the chain. E.g. in a text about “Bill Gates”, when it references him with a pronoun like “He”, it replaces it with the original name: “He” to “Bill Gates” (if present in a coreference chain). When it is done,  the method replaces the current document (attribute doc) with the new resolved text; finally it re-annotates the new document
	 */
	public void corefResolution() {
        System.out.print("Resolving coreferences ...");

		Map<Integer, CorefChain> corefs = this.doc.corefChains();
        List<CoreSentence> sentences = this.doc.sentences();
        List<String> resolved = new ArrayList<String>();

        for (CoreSentence sentence : sentences) {
            List<CoreLabel> tokens = sentence.tokens();

            for (CoreLabel token : tokens) {
                Integer corefClustId= token.get(CorefCoreAnnotations.CorefClusterIdAnnotation.class);
                CorefChain chain = corefs.get(corefClustId);
                if(chain==null || chain.getMentionsInTextualOrder().size() == 1)
                    resolved.add(token.word());
                else
                {
                    int sentINdx = chain.getRepresentativeMention().sentNum -1;
                    CoreSentence corefSentence = sentences.get(sentINdx);
                    List<CoreLabel> corefSentenceTokens = corefSentence.tokens();
                    String newwords = "";
                    CorefMention reprMent = chain.getRepresentativeMention();
                    if (token.index() <= reprMent.startIndex || token.index() >= reprMent.endIndex) {
                            for (int i = reprMent.startIndex; i < reprMent.endIndex; i++) {
                                CoreLabel matchedLabel = corefSentenceTokens.get(i - 1); 
                                resolved.add(matchedLabel.word().replace("'s", ""));
                                newwords += matchedLabel.word() + " ";

                            }
                    }
                    else resolved.add(token.word());
                }
            }
        }

        String resolvedStr ="";
        for (String str : resolved) {
            resolvedStr+=str+" ";
        }
        System.out.print(" done.\n");

        this.doc = new CoreDocument(this.formatString(resolvedStr));
        annotateDocument();
	}
	

	
	/**
	 * this method searches for named entities in the text, with the Stanford NLP library method, then puts the results in the ner attribute, as a pair (entity_text, entity_type).
	 */
	public void namedEntityRecognition() {
        System.out.print("Named Entity Recognition ...");

		List<CoreEntityMention> mentions = this.doc.entityMentions();
		if(mentions==null || mentions.isEmpty()) {
			System.out.print("no entities found.\n");
			return;
		}
		mentions.forEach(em -> {
			this.ner.put(em.text(), em.entityType());
		});
        System.out.print(" done.\n");

	}
	
	
	/**
	 * this method extracts SRO triples from the document, adding them into the rel attribute with the method addRelation (Relations class). This creates a graph abstraction, which can be exploited to populate the database.

	 */
	public void extractRelations() { 
        System.out.print("Extracting relations ...");

		this.doc.sentences().forEach(s -> {
			s.relations().forEach(r -> {
				this.rel.addRelation(r.subjectGloss(), r.relationGloss(), r.objectGloss());
			});
		});
		

        System.out.print(" done.\n");

	}
	
	

	public void executeTeKnoPipeline() {

    	this.annotateDocument();
    	this.corefResolution();
    	this.namedEntityRecognition();
    	//ner 
    	this.extractRelations();

    	this.wikipediaTripleExtraction();


	}
	
	/**
	 * this method executes the pipeline, by running the previous methods. It takes a boolean in input which specifies if the program should search for extracts on Wikipedia; this is done because the execution of  the wikipediaTripleExtraction method can take a long time.
	 * @param wiki boolean value that specifies if the wikipedia search should be used (true) or not (false)
	 */
	public void executeTeKnoPipeline(boolean wiki) {

    	this.annotateDocument();
    	this.corefResolution();
    	this.namedEntityRecognition();

    	this.extractRelations();
    	
    	if(wiki)
    		this.wikipediaTripleExtraction();

	}
	
	
	/**
	 *  this method searches for text extracts on Wikipedia (with the class WikipediaIntegration), about the named entities found in the input text. From this search, some categories are excluded e.g., Date entities, Number entities and so on. For each Wikipedia extract found,  we instantiate the document attribute (doc), annotate it, resolve coreference and finally extract the relations which will be added to the previous found ones.

	 */
	public void wikipediaTripleExtraction() {
		List<String> exclude_types = new ArrayList<String>();
		exclude_types.add("TITLE");
		exclude_types.add("DATE");
		exclude_types.add("ORDINAL");
		exclude_types.add("MONEY");
		exclude_types.add("NUMBER");
		exclude_types.add("DURATION");
		this.ner.forEach( (n,t) -> {
			System.out.println("wikipedia search for: " + n + " (" + t + ")");

			if(exclude_types.contains(t)) return;
			String wikiExtract = WikipediaIntegration.getText(n);
	        this.doc = new CoreDocument(this.formatString(wikiExtract));
	        annotateDocument();
	    	this.corefResolution();
	    	this.extractRelations();
		});
		
	}
	
	
	
	/**
	 * this method generates an XML file from the database contents, in order to export it.
	 * @param file where the generated XML file should be saved
	 */
	public void generateGraphXML ( String file) {
		System.out.println("Generating XML file from Graph");
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element root = document.createElement("graphml");
			document.appendChild(root);
			
			Element graph = document.createElement("graph");
			root.appendChild(graph);
			
			this.rel.nodeIterator().forEachRemaining(n -> {
				Element node = document.createElement("node");
				node.setAttribute("id", n.getKey().toString());
				graph.appendChild(node);
				Element data = document.createElement("data");
				data.setAttribute("key", "name");
				data.setTextContent(n.getValue());
				node.appendChild(data);
				Element type = document.createElement("data");
				type.setAttribute("key", "type");
				type.setTextContent("o");
				node.appendChild(type);
			});
			
			this.rel.edgeIterator().forEachRemaining(e -> {
				Element edge = document.createElement("edge");
				edge.setAttribute("source", e.getKey()[0].toString());
				edge.setAttribute("target", e.getKey()[1].toString());
				graph.appendChild(edge);
				Element data = document.createElement("data");
				data.setAttribute("key", "type");
				data.setTextContent(e.getValue());
				edge.appendChild(data);
			});
		

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(file));
			transformer.transform(domSource, streamResult);

			System.out.println("Done creating XML File");
			
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
	
	
	/**
	 * this method populates the database, starting from the rel attribute; it takes in input a KnowledgeGraph database instance. First, it adds the nodes (with a generic “o” label which symbolizes an object), then adds the edges; if a relation references a person or a organization (starts with “per_” or “org_”) the label of that node is updated from “o” to either “person” or “organization” respectively. 
The graph is then created, but a final step is done to update all nodes labels with the entity type, if found. 

	 * @param knowledge_graph an active instance of the class KnowledgeGraph
	 */
	public void generateGraphDB (KnowledgeGraph knowledge_graph) {	
		
		// adding all the nodes of the triples
		this.rel.nodeIterator().forEachRemaining(n -> {
		    knowledge_graph.addNode(n.getKey().toString(), n.getValue().toLowerCase(), "o");
		});
		
		// adding the relations between the nodes inserted before 
		this.rel.edgeIterator().forEachRemaining(e -> {
			String id1 = e.getKey()[0].toString();
			String id2 = e.getKey()[1].toString();
			String r = e.getValue().toLowerCase();
			if(r.startsWith("per_")) {
				knowledge_graph.addEdge(id1, id2, r.replace("per_", ""));
				knowledge_graph.addNodeProperty(id1, "person");
			}
			else if(r.startsWith("org_")) {
				knowledge_graph.addEdge(id1, id2, r.replace("org_", ""));
				knowledge_graph.addNodeProperty(id1, "organization");
			}
			else knowledge_graph.addEdge(id1, id2, r);
			
		});
		this.ner.forEach((k, t) -> {
			System.out.println(k + " " + t);
			String id = knowledge_graph.getNodeId(k.toLowerCase());
			System.out.println(k + " match " + id);

			if(id != null)
				knowledge_graph.addNodeProperty(id, t.toLowerCase());
		});
	}

	
	
}
