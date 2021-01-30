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
    	

    	KnowledgeGraph kg = new KnowledgeGraph("bolt://localhost:7687","neo4j","pass");
    	HighLevelParsing hlp = new HighLevelParsing();
    	if(kg.resetGraph()) {
    		hlp.readFile("./source_files/sj.txt");
        	hlp.executeTeKnoPipeline();
        	hlp.generateGraphDB(kg);
    	}
    	kg.close();
    	
    }

}
