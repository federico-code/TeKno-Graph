package tekno;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

public class PrologReasoning {
	
	
	
	public PrologReasoning(String file){
		
		Query q1 =  new Query( "consult", new Term[] {new Atom(file)} );
		//System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed"));
	}
	
	/*
    public static void main(String[] args) {
    
    	PrologReasoning pro = new PrologReasoning("./prolog_files/refined_facts.pl");

    }
    
    */
    
    public static void main(String[] args) throws Exception {
    	
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "swipl");
        System.out.println( "flavie mannalaaa ");

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
    }

	
}
