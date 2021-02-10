package tekno;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class Relations {
	

	
	private Map<Integer, String> nodes = new HashMap<Integer, String>();
	private Map<Integer[], String> edges = new HashMap<Integer[], String>();
	private List<String> removePronouns = Arrays.asList("i","me","you","he", "she", "it", "him","her","his","we","us","they","them");

	public Relations() {
		
	}
	
	public void addRelation(String s, String r, String o) {
		int s_id = s.hashCode() & 0xfffffff;
		int o_id = o.hashCode() & 0xfffffff;
		
		
		
		
		if(removePronouns.contains(s) || removePronouns.contains(o))
			return ;
		
		if(nodes.isEmpty())
		{
			nodes.putIfAbsent(s_id, s.toLowerCase());
			nodes.putIfAbsent(o_id, o.toLowerCase());
	
		} else {
			Map<Integer, String> newNodes = Map.copyOf(nodes);
			
			for(Map.Entry<Integer, String> e: newNodes.entrySet() )
			{   
				System.out.println("nodes : "+nodes.toString());
				if(WikipediaIntegration.isSameWiki(e.getValue(), s))
					nodes.putIfAbsent(e.getKey(), s.toLowerCase());
				else
					nodes.putIfAbsent(s_id, s.toLowerCase());
				
				if(WikipediaIntegration.isSameWiki(e.getValue(), o))
					nodes.putIfAbsent(e.getKey(), o.toLowerCase());
				else
					nodes.putIfAbsent(o_id, o.toLowerCase());
			}
		}
		
		

		edges.put(new Integer[] {s_id, o_id}, r.replace(":", "_"));
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
