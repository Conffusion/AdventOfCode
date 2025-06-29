package aoc2015.day08;

import java.util.stream.Collectors;

import aoc2015.common.MainMaster;

public class Main08 extends MainMaster<Long> {
	
	@Override
	public Long star1() {
		return parseInput(this::lineProcessor).stream().collect(Collectors.summarizingInt(pr->pr.getValue())).getSum();
	}

	class ProcessResult {
		int charlength;
		int inputlength;
		public int getValue() {
			return inputlength-charlength;
		}
	}
	ProcessResult lineProcessor(String line) {
		ProcessResult pr=new ProcessResult();
		pr.inputlength=line.length();
		String parsed=line.substring(1, line.length()-1)
			.replaceAll("\\\\\"", "\"")
			.replaceAll("\\\\x[0-9a-f]{2}", "R");
		pr.charlength=parsed.length();
		System.out.println(""+pr.getValue()+":"+line +"  -> "+parsed);
		return pr;
	}
	
	@Override
	public Long star2() {
		return 0L;
	}

	public static void main(String[] args) {
		new Main08()
		//.withTestMode()
		.start();
	}
}
