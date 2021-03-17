package tekno;

import java.util.Arrays;



/**
 * 
 * This class represents a single prolog fact, consisting of a predicate and atoms.
 */
public class Fact {
	
	/**
	 * this private attribute represents the predicate, or the name, of a Prolog fact (e.g. title or date_of_birth, and so on).
	 */
	private String predicate;
	/**
	 *  this list contains the atoms of the corresponding Prolog predicate (e.g. for the predicate date_of_birth the predicates can be ‘bill gates’ and ‘October 28, 1955’). This list is usually composed of one or two atoms as for the considered knowledge base, but there is no specific limit on this.
	 */
	private String[] atoms;
	

	public Fact() {
		super();
		this.predicate = null;
		this.atoms = null;
	}	
	
	/**
	 * this method is the constructor for this class and it just sets the attributes of the class to the ones passed in input.
	 * @param predicate the predicate, or the name, of a Prolog fact
	 * @param atoms atoms of the corresponding Prolog predicate (usually a list of two Strings)
	 */
	public Fact(String predicate, String[] atoms) {
		super();
		this.predicate = predicate;
		this.atoms = atoms;
	}

	/**
	 * get method for the predicate attribute
	 * @return the predicate String.
	 */
	public String getPredicate() {
		return predicate;
	}
	
	/**
	 * set method for the predicate attribute
	 * @param predicate the predicate, or the name, of a Prolog fact
	 */
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	/**
	 *  get method for the atoms attribute
	 * @return the list of atom Strings.
	 */
	public String[] getAtoms() {
		return atoms;
	}

	/**
	 * set method for the atoms attribute
	 * @param atoms atoms of a corresponding Prolog predicate (usually a list of two Strings)
	 */
	public void setAtoms(String[] atoms) {
		this.atoms = atoms;
	}


	@Override
	public String toString() {
		return "Fact [predicate=" + predicate + ", atoms=" + Arrays.toString(atoms) + "]";
	}
	
	/**
	 * this method creates a String in the format of a Prolog fact, from the class attributes. For example, given predicate=’date_of_birth’ and atoms=[‘bill gates’, ‘October 28, 1955’]
	 * @return the string date_of_birth(‘bill gates’, ‘October 28, 1955’). If an atom is composed only by numbers, it gets converted from a String to an integer (e.g. for a year ‘1995’ to 1995). 
	 */
	public String prologFacts() {
		String atomsString = "";
		int nAtoms = atoms.length;
		int i=0;
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
	
	
	/**
	 * this method checks if the String given in input is a number, if it is, returns true, false otherwise.
	 * @param strNum the String to be checked
	 * @return true if the input represents a number, false otherwise
	 */
	private static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
}
