package aoc2024.day23;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import common.graph.PlantUMLGenerator;
import common.main.AbstractMainMaster;
import common.regex.RegexMatchBuilder;
/**
 * zoek het grootste subnetwerk van computers binnen een network die een verbinding hebben met elke andere computer van dit subnetwork. 
 * Algorithme: BronKerbosch
 * bron: https://www.geeksforgeeks.org/maximal-clique-problem-recursive-solution/
 * 
 */
public class Main23 extends AbstractMainMaster<Integer> {
    public static void main(String[] args) {
        new Main23()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }
    Map<String,Node> network;
	PlantUMLGenerator mapGen=new PlantUMLGenerator("aoc2024/day23","lan.puml");
    
    @Override
    public void beforeAll() {
    	network=new HashMap<>();
    	RegexMatchBuilder matchBuilder=new RegexMatchBuilder("([a-z]{2})-([a-z]{2})");
    	streamInput(matchBuilder).forEach(rm->buildGraph(rm.group(1),rm.group(2)));
    	network.keySet().forEach(s->mapGen.addNode(s));
    	network.values().forEach(n->{for (Node n2:n.connections) mapGen.addConnection(n.name, n2.name);});
    	IOUtils.closeQuietly(mapGen);
    	System.out.println(""+network.keySet().size());
    }
    Set<String> trios;
    /**
     * Zoek alle driehoeken met minstens 1 punt met naam ~'t?'
     */
    // antwoord : 1043
    public Integer star1() {
        trios=new HashSet<>();
        // we overlopen alle nodes met naam matching t?
        // voor elke 2 gelinkte nodes controleren we of er ook een link tussen deze 2 is, zo ja-> een driehoek!
        network.values().stream().filter(n->n.name.startsWith("t")).forEach(this::checkDriehoek);
        return trios.size();
    }

    /**
     * zoek de grootste groep van computers die aan elkaar hangen (elke computer moet connectie hebben met elke andere)
     */
    // antwoord : 
    public Integer star2() {
        Map<String, Set<String> > graph=new HashMap<>();
        for(Node node:network.values()) {
        	graph.computeIfAbsent(node.name,name-> new HashSet<String>())
        		.addAll(node.connections.stream().map(n->n.name).toList());
        }
        Set<Set<String> > allCliques
        = bronKerbosch(new HashSet<>(), network.keySet(),
                       new HashSet<>(), graph);
		int maxCliqueSize = allCliques.stream()
		                        .mapToInt(Set::size)
		                        .max()
		                        .orElse(-1);
		List<String> bigClique=new ArrayList<>();
		bigClique.addAll(allCliques.stream().filter(l->l.size()==maxCliqueSize).findFirst().get());
		bigClique.sort(Comparator.naturalOrder());
		System.out.println("Antwoord:"+StringUtils.join(bigClique,","));
        return maxCliqueSize;
    }
    
    private void buildGraph(String name1,String name2) {
    	Node node1=network.computeIfAbsent(name1, n->new Node(n));
    	Node node2=network.computeIfAbsent(name2, n->new Node(n));
    	node1.addNode(node2);
    	node2.addNode(node1);
    }
    
    public void checkDriehoek(Node tnode) {
    	for(int i=0;i<tnode.connections.size()-1;i++) {
    		for(int j=i+1;j<tnode.connections.size();j++) {
    			if(network.get(tnode.connections.get(i).name).connections.contains(tnode.connections.get(j))) {
    				trios.add(buildTrio(tnode,tnode.connections.get(i),tnode.connections.get(j)));
    			}
    		}
    	}
    }
    private String buildTrio(Node node1, Node node2, Node node3) {
    	List<String> nodenames=new ArrayList<>();
    	nodenames.addAll(List.of(node1.name,node2.name,node3.name));
    	nodenames.sort(Comparator.naturalOrder());
    	return StringUtils.join(nodenames,"-");
    }
    
    static class Node {
    	String name;
    	List<Node> connections=new ArrayList<>();
		public Node(String name) {
			this.name = name;
		}
    	Node addNode(Node conn) {
    		connections.add(conn);
    		return this;
    	}
		@Override
		public int hashCode() {
			return Objects.hash(name);
		}
		@Override
		public boolean equals(Object obj) {
			return Objects.equals(name, ((Node) obj).name);
		}   	
    }

    /**
     * code gecopieerd, begrijp het nog niet helemaal
     * @param R
     * @param P
     * @param X
     * @param graph
     * @return
     */
	public static Set<Set<String>> bronKerbosch(Set<String> R, Set<String> P, Set<String> X,
			Map<String, Set<String>> graph) {
		Set<Set<String>> cliques = new HashSet<>();
		if (P.isEmpty() && X.isEmpty()) {
			cliques.add(new HashSet<>(R));
		}
		while (!P.isEmpty()) {
			String v = P.iterator().next();
			Set<String> newR = new HashSet<>(R);
			newR.add(v);
			Set<String> newP = new HashSet<>(P);
			newP.retainAll(graph.get(v));
			Set<String> newX = new HashSet<>(X);
			newX.retainAll(graph.get(v));
			cliques.addAll(bronKerbosch(newR, newP, newX, graph));
			P.remove(v);
			X.add(v);
		}
		return cliques;
	}
}