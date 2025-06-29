package aoc2015.day13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import aoc2015.common.MainMaster;
import common.graph.PlantUMLGenerator;
import common.regex.RegexMatch;
import common.regex.RegexMatchBuilder;

public class Main13 extends MainMaster<Integer> {

	public static void main(String[] args) {
		new Main13()
		//.withTestMode()
		.start();
	}
	
	/**
	 * key1=persoon 1
	 * key2=persoon 2
	 * value= gelukswaarde
	 * voor elk paar (key1,key2) is ook (key2,key1) in de map met dezelfde waarde
	 */
	Map<String,Map<String,Integer>> allPairs;
	Map<String,Map<String,Integer>> summedPairs;
	List<String> allNames;
	private static final String linePattern="([A-Za-z]+) would ([losegain]+) ([0-9]+) happiness units by sitting next to ([A-Za-z]+).";
	
	@Override
	public void beforeAll() {
		allPairs=new HashMap<>();
		streamInput(new RegexMatchBuilder(linePattern)).forEach(this::fillAllPairs);
		allNames=new ArrayList<>();
		allNames.addAll(allPairs.keySet());
		try (PlantUMLGenerator umlGen=new PlantUMLGenerator("aoc2015/day13",(testMode?"test":"")+"tafel.puml")) {
			for(String dep:allNames)
				umlGen.addNode(dep);
			for(String dep:allNames) {
				for(Map.Entry<String,Integer> dest:allPairs.get(dep).entrySet())
					umlGen.addConnection(dep, dest.getKey(),""+dest.getValue());
			}
		} catch (IOException iox) {
		}
	}


	@Override
	public Integer star1() {
		summedPairs=new HashMap<>();
		fillSummedPairs();
		// start bij de hoogste waarde
		String eerste=null, name2=null;
		int startScore=0;
		for(String n1:allNames) {
			for(String n2:allNames) {
				if(!Objects.equals(n1, n2) && summedPairs.get(n1).get(n2)>startScore) {
					eerste=n1;
					name2=n2;
					startScore=summedPairs.get(n1).get(n2);
				}
			}
		}
		int score=startScore;
		Set<String> geplaatst=new HashSet<>();
		geplaatst.add(eerste);
		geplaatst.add(name2);
		String currentNaam=name2;
		logln("naam:"+eerste);
		logln("-- "+score+" -- "+name2);
		String laatste=null;
		while(geplaatst.size()<allNames.size()) {
			int maxvalue=Integer.MIN_VALUE;
			laatste=null;
			for(Map.Entry<String, Integer> entry: summedPairs.get(currentNaam).entrySet()) {
				if(!geplaatst.contains(entry.getKey())&& entry.getValue()>maxvalue) {
					maxvalue=entry.getValue();
					laatste=entry.getKey();
				}
			}
			logln("-- "+maxvalue+" -- "+laatste);
			// buur bevat nu de beste buur
			geplaatst.add(laatste);
			currentNaam=laatste;
			score+=maxvalue;
		}
		logln("-- "+summedPairs.get(laatste).get(eerste)+" -- "+eerste);
		score+=summedPairs.get(laatste).get(eerste);
		
		return score;
	}
	private void fillAllPairs(RegexMatch match) {
		allPairs
		.computeIfAbsent(match.group(1), name->new HashMap<String,Integer>())
		.computeIfAbsent(match.group(4), name2->match.intGroup(3)*(match.group(2).equals("gain")?1:-1));
	}
	private void fillSummedPairs() {
		for(String name1:allPairs.keySet()) {
			for(String name2:allPairs.keySet()) {
				if(!Objects.equals(name1, name2)) {
					int value=allPairs.get(name1).get(name2)+allPairs.get(name2).get(name1);
					summedPairs.computeIfAbsent(name1, n->new HashMap<String,Integer>()).put(name2, value);
				}
			}
		}
	}
	@Override
	public Integer star2() {
		return 0;
	}

}
