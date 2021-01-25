package corenlp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class Relations {
	

	
	private Map<Integer, String> nodes = new HashMap<Integer, String>();
	private Map<Integer[], String> edges = new HashMap<Integer[], String>();
	
	public Relations() {
		
	}
	
	public void addRelation(String s, String r, String o) {
		int s_id = s.hashCode() & 0xfffffff;
		int o_id = o.hashCode() & 0xfffffff;
		nodes.putIfAbsent(s_id, s);
		nodes.putIfAbsent(o_id, o);
		edges.put(new Integer[] {s_id, o_id}, r.replace("per:", "P_").replace(":", "_"));
	}


	public void printEdges() {
		edges.forEach((c, t) -> {
			System.out.println(c[0] + " - " + t + " -> "+c[1]);
		});
	}
	
	public void printNodes() {
		nodes.forEach((i, n) -> {
			System.out.println(i + " - " + n );
		});	
	}


	
	
	public Iterator<Entry<Integer[], String>> edgeIterator() {
		return this.edges.entrySet().iterator();
	}
	
	public Iterator<Entry<Integer, String>> nodeIterator() {
		return this.nodes.entrySet().iterator();
	}



}
