package corenlp;

public class Main {
    
    public static void main(String[] args) throws Exception {
    	

 
    	HighLevelParsing hlp = new HighLevelParsing("./sj.txt");
    	hlp.executeBasicPipeline();
    	hlp.generateGraphXML("./", "new_xml.xml");
    	
    	KnowledgeGraph kg = new KnowledgeGraph();
    	if(kg.resetGraph())
    		{
    			kg.loadGraphFromXML("./new_xml.xml");
            	System.out.println(kg.extractFacts());
    		}
        kg.close();
    }

}
