package tekno;


import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

public class PrologReasoning {
	
	
	
	public PrologReasoning(String file){
		
		Query q1 =  new Query( "consult", new Term[] {new Atom(file)} );
		//System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed"));
	}
	
    public static void main(String[] args) {
    
    	PrologReasoning pro = new PrologReasoning("./prolog_files/facts.pl");

    }
    

	
}
