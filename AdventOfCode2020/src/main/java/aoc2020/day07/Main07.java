package aoc2020.day07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2020.common.MainMaster;

public class Main07 extends MainMaster {
	List<String> data;
	static Pattern fullPattern=Pattern.compile("(.+) bags contain (.+)");
	static Pattern bagPattern=Pattern.compile("(\\d+)? ([a-z ]+) bags?[,.]");
	
	public Main07() {
		data=loadInput(7, "input.txt");
	}

	/**
	 * recursieve functie om aantal containing bags te tellen
	 * @param bag
	 * @return
	 */
	public int bagCounter(String bag,Map<String,List<String>> star2Bagtree) {
		int result=0;
		if(star2Bagtree.containsKey(bag))
		{
			result=star2Bagtree.get(bag).stream().mapToInt(b->bagCounter(b,star2Bagtree)).sum();
		} else
			System.err.println("no info found for "+bag);
		return result+1; // input bag zelf niet vergeten
	}


	public void star1() {
		Map<String,Set<String>> bagtree=new HashMap<>();
		data.stream().map(line->fullPattern.matcher(line))
		.filter(m->m.matches())
		.forEach(m->
			{
				Matcher containsMatch=bagPattern.matcher(m.group(2));
				while(containsMatch.find())
					// ignore group 1
					bagtree.computeIfAbsent(containsMatch.group(2),c->new HashSet<>()).add(m.group(1));
		});
		// let's count
		Set<String> result=new HashSet<>();
		result.add("shiny gold");
		int oldCount=0;
		do {
			oldCount=result.size();
			Set<String> newBags=new HashSet<>();
			for(String bag:result)
				if(bagtree.get(bag)!=null)
					newBags.addAll(bagtree.get(bag));
			result.addAll(newBags);
			logln("bag count:"+newBags.size()+"->"+result.size());
		} while (oldCount!=result.size());
		result.remove("shiny gold");
		info("*** ANSWER star 1:"+result.size());
	}

	public void star2() {
		Map<String,List<String>> star2Bagtree=new HashMap<>();
		for(String line:data) {
			Matcher fullMatch=fullPattern.matcher(line);
			if(fullMatch.matches())
			{
				String container=fullMatch.group(1);
				String contains=fullMatch.group(2);
				Matcher containsMatch=bagPattern.matcher(contains);
				List<String> containList=new ArrayList<>();
				//logln("add container:"+container);
				while(containsMatch.find())
				{
					String contain=containsMatch.group(2);
					if(contain.equals("other"))
						break;
					for(int i=0;i<Integer.parseInt(containsMatch.group(1));i++)
						containList.add(contain);
				}
				star2Bagtree.put(container,containList);
			} else {
				System.err.println("no match: "+line);
			}
		}
		info("*** ANSWER Star 2:"+(bagCounter("shiny gold",star2Bagtree)-1));		
	}

	public static void main(String[] args) {
		Main07 m=new Main07();
		m.log=false;
		m.timer(()->m.star1());
		m.timer(()->m.star2());
	}

}
