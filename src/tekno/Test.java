  
package tekno;

public class Test {
    
    public static void main(String[] args) throws Exception {
    	
    	KnowledgeGraph kg = new KnowledgeGraph("bolt://localhost:7687","neo4j","corenlp");
    	HighLevelParsing hlp = new HighLevelParsing();
    	if(kg.resetGraph()) {
    		hlp.readFile("./source_files/sj.txt");
        	hlp.executeTeKnoPipeline();
        	hlp.generateGraphDB(kg);
    	}
    	
    	kg.extractFacts("sj", "prolog_files");
    	kg.close();
    	
    }

}