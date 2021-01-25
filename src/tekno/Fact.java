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
	
}
