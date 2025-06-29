package aoc2015.day05;

import java.util.List;
import java.util.regex.Pattern;

import aoc2015.common.MainMaster;

public class Main05 extends MainMaster<Long> {

	public static void main(String args[]) {
		new Main05();
	}
	
	public Main05() {
		testMode=false;
		doStar1();
	}

	List<String> lines;
	
	private boolean multipleChars(String line) {
		char last='*';
		for(char c:line.toCharArray())
		{
			if(c==last)
				return true;
			last=c;
		}
		return false;
	}
	Pattern vowelPattern=Pattern.compile("[aeiou]");
	
	@Override
	public Long star1() {
		lines=loadInputByLine();
		return lines.parallelStream()
				.filter(l->!l.contains("ab")&&!l.contains("cd")&&!l.contains("or")&&!l.contains("xy"))
				.filter(l->vowelPattern.matcher(l).results().count()>=3)
				.filter(this::multipleChars)
				.map(this::logln)
				.count();
	}

	@Override
	public Long star2() {
		// TODO Auto-generated method stub
		return null;
	}

}
