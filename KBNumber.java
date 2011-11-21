/*------------------------------------------------------------------------------
 * Max Guo
 * November 21, 2011
 * Kevin Bacon number
 *----------------------------------------------------------------------------*/

/*------------------------------------------------------------------------------
 * TODO: - speed up implementation
 *----------------------------------------------------------------------------*/

/*------------------------------------------------------------------------------
 * USAGE: - java KBNumber <args>
 *          
 *          if no args provided then actor defaults to "Bacon, Kevin"
 *              else it sets actor to the name provided
 *
 *          input args as "lastname, firstname"
 *
 *          program reads in a list of movies and actors from movies.txt
 *              outputs a list of actors and their corresponding number
 *              to the input actor obtained from args, stores output in file
 *              movies.out
 *----------------------------------------------------------------------------*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;

public class KBNumber {
	private Map<String, Set<String>> vertexGraph = new HashMap<String, Set<String>>();
	private Map<String, Boolean> visitedGraph = new HashMap<String, Boolean>();
	private Map<String, Integer> calcNum = new TreeMap<String, Integer>();

	public KBNumber(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String lineRead;
			while ((lineRead = br.readLine()) != null) {
				String[] parsedLine = lineRead.split("/");
				for (int i = 1; i < parsedLine.length - 1; i++) {
					for (int j = i + 1; j < parsedLine.length; j++) {
						this.newEdge(parsedLine[i], parsedLine[j]);
					}
				}
			}
			br.close();
			for (String s: vertexGraph.keySet()) {
				visitedGraph.put(s, false);
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	public void resetVisited() {
		for (String s: vertexGraph.keySet()) {
			visitedGraph.put(s, false);
		}
	}
	
	public void addToCalcNum(String s, int i) {
		this.calcNum.put(s, i);
	}
	
	public Set<String> getVertices() {
		return vertexGraph.keySet();
	}
	
	public Set<String> getEdges(String v) {
		return vertexGraph.get(v);
	}
	
	public boolean containsV(String v) {
		return vertexGraph.containsKey(v);
	}
	
	public void newEdge(String v, String w) {
		if (!containsV(v)) {
			vertexGraph.put(v, new HashSet<String>());
		}
		if (!containsV(w)) {
			vertexGraph.put(w, new HashSet<String>());
		}
		vertexGraph.get(v).add(w);
		vertexGraph.get(w).add(v);
	}
	
	public int calculateNumber(String name, String query) {
		if (!this.containsV(query) || !this.containsV(name)) {
			return -1;
		}
		if (query.equals(name)) {
			return 0;
		}
		int num = 0;
		List<String> vertices = new LinkedList<String>();
		List<String> temp;
		vertices.add(name);
		visitedGraph.put(name, true);
		while (vertices.size() != 0) {
			temp = vertices;
			num++;
			vertices = new LinkedList<String>();
			for (int i = 0; i < temp.size(); i++) {
				for (String s: this.getEdges(temp.get(i))) {
					if (s.equals(query)) {
						return num;
					}
					if (!visitedGraph.get(s)) {
						visitedGraph.put(s, true);
						if (!vertices.contains(s)) {
							vertices.add(s);
						}
					}
				}
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		String actor;
		int number;

		if (args.length == 0) {
			actor = "Bacon, Kevin";
		} else {
			actor = args[0];
		}
		
		KBNumber movies = new KBNumber("movies.txt");
		
		for (String query: movies.getVertices()) {
			movies.resetVisited();
			if (query.equals(actor)) {
				number = 0;
			} else {
				number = movies.calculateNumber(actor, query);
			}
			if (number >= 0) {
				movies.addToCalcNum(query, number);
			}
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("movies.out"));
			for (String s: movies.calcNum.keySet()) {
				bw.write(s + " " + movies.calcNum.get(s) + "\n");
			}
			bw.close();
		} catch (Exception e) {
			e.getMessage();
		}
	}
}

