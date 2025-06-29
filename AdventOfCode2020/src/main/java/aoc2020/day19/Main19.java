package aoc2020.day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2020.common.MainMaster;

public class Main19 extends MainMaster {
	Map<Long,String> rules=new HashMap<>();
	List<String> patterns=new ArrayList<>();
	
	public void loadData(String filename)
	{
		Pattern cmdPatt=Pattern.compile("(\\d+):(.+)");
		List<String> data = loadInput(19, filename);
		for(String s:data) {
			if(s.length()>0) {
				Matcher m=cmdPatt.matcher(s);
				if(m.matches()) {
					rules.put(Long.parseLong(m.group(1)),m.group(2).trim());
				} else {
					patterns.add(s);
				}
			}
		}
	}

	static Pattern OrPattern=Pattern.compile("(.*)\\|(.*)");
	static Pattern CharPattern=Pattern.compile("\"([ab]+)\"");
	public String convert(String pattern)
	{
		Matcher mChar=CharPattern.matcher(pattern);
		if(mChar.matches())
			return mChar.group(1);
		Matcher mOr=OrPattern.matcher(pattern);
		if(mOr.matches())
		{
			return "("+convert(mOr.group(1))+"|"+convert(mOr.group(2))+")";
		}
		// hier enkel nog 1 of 2 nummers mogelijk
		String[] nrs=pattern.trim().split(" ");
		
		StringBuffer buf=new StringBuffer();
		if(nrs.length>1)
			buf.append("(");
		for (String nr:nrs)
		{
			buf.append(convert(rules.get(Long.parseLong(nr))));
		}
		if(nrs.length>1)
			buf.append(")");
		return buf.toString();
	}
	
	public void star1() {
		loadData("input.txt");
		
		Pattern patt=Pattern.compile(convert(rules.get(0l)));
		int counter=0;
		for(String s:patterns) {
			counter+=patt.matcher(s).matches()?1:0;
		}
		System.out.println("***STAR1:"+counter);
	}

	public void test1() {
		loadData("testinput.txt");
		String regex=convert(rules.get(0l));
		System.out.println("test:"+regex);
		System.out.println("ababbb:" + "ababbb".matches(regex));
		System.out.println("abbbab:" + "abbbab".matches(regex));
		System.out.println("bababa:" + "bababa".matches(regex));
		System.out.println("aaabbb:" + "aaabbb".matches(regex));
	}
	
	public void utest1() {
		String regex = "(a((((aa))|((bb))((ab))|((ba))))|((((ab))|((ba))((aa))|((bb))))b)";
		System.out.println("ababbb:" + "ababbb".matches(regex));
		System.out.println("bababa:" + "bababa".matches(regex));
	}

	public static void main(String[] args) {
		Main19 m = new Main19();
		m.log=false;
		m.timer(()-> m.star1());

	}

}
