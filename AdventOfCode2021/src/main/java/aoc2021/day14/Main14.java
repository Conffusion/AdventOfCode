package aoc2021.day14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.main.AbstractMainMaster;

public class Main14 extends AbstractMainMaster<Long> {

	class Rule {
		String name;
		Rule r1,r2;
		public Rule(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return "Rule [name=" + name + ", r1=" + (r1==null?"null":r1.name) + ", r2=" + (r2==null?"null":r2.name) + "]";
		}
	}
	
	List<Rule> startRules;
	Map<Rule,Long> occurances;
	int maxIterations;
	String template;
	
	
	@Override
	public void beforeEach() {
		Pattern rulePattern=Pattern.compile("([A-Z]{2}) -> ([A-Z])");
		Map<String,Rule> rules=new HashMap<>();
		List<String> lines=loadInputByLine();

		startRules=new ArrayList<>();
		occurances=new HashMap<>();

		for(String line:lines) {
			Matcher m=rulePattern.matcher(line);
			if(m.matches())
			{
				Rule r=build(m.group(1),rules);
				rules.put(r.name,r);
				char[] chars=m.group(1).toCharArray();
				r.r1=build(Character.toString(chars[0])+m.group(2),rules);
				r.r2=build(m.group(2)+ Character.toString(chars[1]),rules);
			}
		}
		for(Rule r:rules.values()) {
			if(r.r1==null)
				logln("rule.r1 is null for "+r);
			if(r.r2==null)
				logln("rule.r2 is null for "+r);
		}
		template=lines.get(0);
		for(int i=0;i<template.length()-1;i++)
		{
			String rulename=template.substring(i, i+2);
			Rule r=rules.get(rulename);
			logln("startrule:"+r);
			long v=occurances.getOrDefault(r, 0l);
			occurances.put(r,v+1);
			startRules.add(r);
		}
	}

	private Rule build(String key,Map<String,Rule> rules) {
		if(rules.containsKey(key))
			return rules.get(key);
		Rule r=new Rule(key);
		rules.put(key,r);
		return r;
	}

	public Map<Rule,Long> loop() {
		Map<Rule,Long> newRules=new HashMap<>();
		for(Map.Entry<Rule,Long> entry:occurances.entrySet()) {
			Long occ=newRules.getOrDefault(entry.getKey().r1,0l);
			newRules.put(entry.getKey().r1,occ+entry.getValue());
			occ=newRules.getOrDefault(entry.getKey().r2,0l);
			newRules.put(entry.getKey().r2,occ+entry.getValue());
		}
		return newRules;
	}

	private Long count() {
		Map<String,Long> counters=new HashMap<>();
		counters.put(template.substring(template.length()-1,template.length()),1l);
		long totalLength=1;
		for(Map.Entry<Rule,Long> entry:occurances.entrySet()) 
		{
			logln("rule:%s",entry.getKey());
			String key=entry.getKey().name.substring(0, 1);
			totalLength+=entry.getValue();
			counters.merge(key,entry.getValue(),(o,n)->o+n);
		}
		logln("total length:%d",totalLength);
		long max=0,min=Long.MAX_VALUE;
		for(Long counter:counters.values())
		{
			max=max<counter?counter:max;
			min=min>counter?counter:min;
		}
		return max-min;
	}
	
	@Override
	public Long star1() {
		maxIterations=10;
		for(int i=1;i<=maxIterations;i++) {
			occurances=loop();
		}
		return count();
	}

	@Override
	public Long star2() {
		maxIterations=40;
		for(int i=1;i<=maxIterations;i++) {
			occurances=loop();
		}
		return count();
	}

	public static void main(String[] args) {
		new Main14()
		.nolog()
		//.testMode()
		.start();
	}

}
