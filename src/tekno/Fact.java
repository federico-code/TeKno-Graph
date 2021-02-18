package tekno;

import java.util.Arrays;


public class Fact {
	private String predicate;
	private String[] atoms;
	
	public Fact() {
		super();
		this.predicate = null;
		this.atoms = null;
	}	
	
	public Fact(String predicate, String[] atoms) {
		super();
		this.predicate = predicate;
		this.atoms = atoms;
	}

	public String getPredicate() {
		return predicate;
	}
	
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String[] getAtoms() {
		return atoms;
	}

	public void setAtoms(String[] atoms) {
		this.atoms = atoms;
	}

	@Override
	public String toString() {
		return "Fact [predicate=" + predicate + ", atoms=" + Arrays.toString(atoms) + "]";
	}
	
	public String prologFacts() {
		String atomsString = "";
		int nAtoms = atoms.length;
		int i=0;
		String returnString="";
		for(String atom :atoms)
		{	
			if(isNumeric(atom)) { // if the string is a number 
				atomsString+=atom; //put the string without quotes
			}else {
				atomsString+= "'"+atom +"'";
			}
			
			if(i!=(nAtoms-1)) // if it's not the last element put the comma
				atomsString+=",";
		
			i=i+1;
		}

		return predicate.toLowerCase()+"("+atomsString+").";
	}
	
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
}
