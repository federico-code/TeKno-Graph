package tekno;

public class Main {
    
    public static void main(String[] args) throws Exception {
//    	FileHelper fh = new FileHelper("./source_files/");
//    	
//    	KnowledgeGraph kg = new KnowledgeGraph();
//    	HighLevelParsing hlp = new HighLevelParsing();
//    	if(kg.resetGraph()) {
//	    	fh.listSourceFiles().forEach(file_string-> {
//	        	hlp.readFile(file_string);
//	        	hlp.executeTeKnoPipeline();
//	        	hlp.generateGraphDB(kg);
//	    	});
//    	}
//      kg.close();
    	
    	
    	//String base_URL = "https://en.wikipedia.org/wiki";
		//String topic = "Steve_Jobs";
		//String folder = "./source_files/";

    	//WikipediaIntegration wiki = new WikipediaIntegration(base_URL);
    	//wiki.getAllPage(topic,folder);

    	
    	KnowledgeGraph kg = new KnowledgeGraph("bolt://localhost:7687","neo4j","corenlp");
    	HighLevelParsing hlp = new HighLevelParsing();
    	if(kg.resetGraph()) {
    		hlp.readFile("./source_files/bg.txt");
        	hlp.executeTeKnoPipeline();
        	hlp.generateGraphDB(kg);
    	}
    	kg.close();
    	
    }

}
