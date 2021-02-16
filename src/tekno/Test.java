  
package tekno;

public class Test {
    
    public static void main(String[] args) throws Exception {
    	
    	KnowledgeGraph kg = new KnowledgeGraph("bolt://localhost:7687","neo4j","corenlp");
    	HighLevelParsing hlp = new HighLevelParsing();
    	if(kg.resetGraph()) {
    		hlp.readFile("./source_files/bg.txt");
        	hlp.executeTeKnoPipeline(false);
        	hlp.generateGraphDB(kg);
    	}
    	
    	kg.extractFacts("ID", "prolog_files");
    	kg.close();
    	
    }

}