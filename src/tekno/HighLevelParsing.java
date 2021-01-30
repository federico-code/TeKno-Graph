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

import java.util.LinkedList;


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


public class HighLevelParsing {
	

	private CoreDocument doc;
	private StanfordCoreNLP pipeline;
	private Map<String, String> ner = new HashMap<String, String>();
	private Relations rel = new Relations();

	
	public HighLevelParsing() {
		String pipeline = "tokenize, ssplit, pos, lemma, ner, parse, coref, kbp, entitymentions";
		System.out.println("creating pipeline: " + pipeline);
	   	Properties props = new Properties();
    	props.setProperty("annotators", pipeline);
       	this.setPipeline(props);
	}
	
	public HighLevelParsing(String annotators) {
	   	Properties props = new Properties();
    	props.setProperty("annotators", annotators);
       	this.setPipeline(props);
	}
	
	
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


	private String formatString(String inStr) {
		return inStr.
				replace("\n", " ").
				replace("\r", " ").
				replace(".", ". ").
				replace(",", ", ").
				replace(" ,", ",").
				replace(" .", ".").
				replace("  ", " ").
				replace("\'", "").
				replaceAll("\\[[^\\]]*\\]", "");
	}
	
	
	
	public void setPipeline(Properties props) {
		this.pipeline = new StanfordCoreNLP (props);
	}
	
	
	public void annotateDocument () {
        System.out.print("Document annotation ...");
		this.pipeline.annotate(this.doc);
        System.out.print(" done.\n");
	}
	
	
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
	

	
	public void namedEntityRecognition() {
        System.out.print("Named Entity Recognition ...");

		List<CoreEntityMention> mentions = this.doc.entityMentions();
		System.out.println("mentions :"+mentions);
		if(mentions==null || mentions.isEmpty()) {
			System.out.print("no entities found.\n");
			return;
		}
		mentions.forEach(em -> {
			this.ner.put(em.text(), em.entityType());
		});
        System.out.print(" done.\n");

	}
	
	
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
    	this.extractRelations();
    	this.wikipediaTripleExtraction();

	}
	
	
	public void wikipediaTripleExtraction() {
		List<String> exclude_types = new ArrayList<String>();
		exclude_types.add("TITLE");
		exclude_types.add("DATE");
		exclude_types.add("ORDINAL");
		exclude_types.add("MONEY");
		exclude_types.add("NUMBER");
		exclude_types.add("DURATION");
		this.ner.forEach( (n,t) -> {
			System.out.println(n + " " + t);

			if(exclude_types.contains(t)) return;
			String wikiExtract = WI.getText(n);
	        this.doc = new CoreDocument(this.formatString(wikiExtract));
	        annotateDocument();
	    	this.corefResolution();
	    	this.extractRelations();
		});
		
	}
	
	
	
	public void generateGraphXML (String folder, String file_name) {
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
			StreamResult streamResult = new StreamResult(new File(folder+"/"+file_name));
			transformer.transform(domSource, streamResult);

			System.out.println("Done creating XML File");
			
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
	
	
	public void generateGraphDB (KnowledgeGraph knowledge_graph) {
		this.rel.nodeIterator().forEachRemaining(n -> {
	        knowledge_graph.addNode(n.getKey().toString(), n.getValue(), "o");
		});
		this.rel.edgeIterator().forEachRemaining(e -> {
			knowledge_graph.addEdge(e.getKey()[0].toString(), e.getKey()[1].toString(), e.getValue());
		});
	}

	
	
}
