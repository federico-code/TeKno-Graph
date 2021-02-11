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
	private Map<String, Integer> wikiMatches = new HashMap<String, Integer>();
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
				if(matchPageId(e.getValue(), s))
					nodes.putIfAbsent(e.getKey(), s.toLowerCase());
				else
					nodes.putIfAbsent(s_id, s.toLowerCase());
				
				if(matchPageId(e.getValue(), o))
					nodes.putIfAbsent(e.getKey(), o.toLowerCase());
				else
					nodes.putIfAbsent(o_id, o.toLowerCase());
				
			}
		}

		edges.put(new Integer[] {s_id, o_id}, r.replace(":", "_"));
	}
	
	
	
	private boolean matchPageId(String s1, String s2) {
		//System.out.println(wikiMatches);
		int id1 = -1, id2 = -1;
		if(wikiMatches.containsKey(s1)) id1 = wikiMatches.get(s1);
		else {
			id1 = WikipediaIntegration.getWikiID(s1);
			wikiMatches.putIfAbsent(s1, id1);
		}
		if(wikiMatches.containsKey(s2)) id1 = wikiMatches.get(s2);
		else {
			id2 = WikipediaIntegration.getWikiID(s2);
			wikiMatches.putIfAbsent(s2, id2);
		}
		return id1==id2;
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
