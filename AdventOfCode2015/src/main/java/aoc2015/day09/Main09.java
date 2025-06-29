package aoc2015.day09;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2015.common.MainMaster;
import common.graph.PlantUMLGenerator;

/**
 * Salesman travel probleem: Bereken de kortste route die alle plaatsen maar 1 keer aandoet.
 * Eenderd welke plaats mag vertrekpunt zijn en het maakt ook niet uit waar we uitkomen.
 * Vereenvoudiging: de input bevat de afstanden tussen elk ander punt
 * Oplossing: voor elke plaats, ga telkens naar de dichtsbijzijnde plaats waar we nog niet geweest zijn
 * Neem dan de kortste van al deze routes 
 */
public class Main09 extends MainMaster<Integer> {
	
	public static void main(String[] args) {
		new Main09()
		//.withTestMode()
		.start();
	}

	Map<String,Map<String,Integer>> allTrajects;
		
	@Override
	public void beforeAll() {
		allTrajects=new HashMap<>();
		parseInput(this::parseLine);
		try (PlantUMLGenerator umlGen=new PlantUMLGenerator("aoc2015/day09",(testMode?"test":"")+"kaart.puml")) {
			for(String dep:allTrajects.keySet()) {
				for(Map.Entry<String,Integer> dest:allTrajects.get(dep).entrySet())
					umlGen.addConnection(dep, dest.getKey(),""+dest.getValue());
			}
		} catch (IOException iox) {
		}
	}
	Pattern linePattern=Pattern.compile("([a-zA-Z]+) to ([a-zA-Z]+) = ([0-9]+)");
	
	String parseLine(String line) {
		Matcher m=linePattern.matcher(line);
		if(m.matches()) {
			allTrajects.computeIfAbsent(m.group(1), from->new HashMap<String,Integer>())
			.put(m.group(2), Integer.parseInt(m.group(3)));
			allTrajects.computeIfAbsent(m.group(2), from->new HashMap<String,Integer>())
			.put(m.group(1), Integer.parseInt(m.group(3)));
			return m.group(1);
		}
		return null;
	}
	
	// antwoord: 251
	@Override
	public Integer star1() {
		return findSingleRoute(Integer.MAX_VALUE,(d1,d2)->d1<d2).distance;
	}
	// antwoord: 898
	@Override
	public Integer star2() {
		return findSingleRoute(0,(d1,d2)->d1>d2).distance;
	}
	
	Route findSingleRoute(int initDist, BiFunction<Integer, Integer, Boolean> comparator) {
		Route shortestRoute=null;
		for(String from:allTrajects.keySet()) {
			Route r=new Route(from);
			String current=from;
			while(true) {
				int bestDist=initDist;
				String bestPlace=null;
				for(Map.Entry<String,Integer> step: allTrajects.get(current).entrySet()) {
					if(!r.hasVisited(step.getKey())&& comparator.apply(step.getValue(),bestDist)) {
						bestDist=step.getValue();
						bestPlace=step.getKey();
					}
				}
				if(bestPlace==null)
					break;
				r.addNode(bestPlace, bestDist);
				current=bestPlace;
			}
			if(shortestRoute==null || comparator.apply(r.distance,shortestRoute.distance)) {
				// new shorter route found
				shortestRoute=r;
			}
		}
		return shortestRoute;	
	}
}
