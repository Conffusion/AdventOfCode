package aoc2015.day12;

import aoc2015.common.MainMaster;

public class Main12 extends MainMaster<Long> {

	public static void main(String[] args) {
		new Main12()
		//.withTestMode()
		.start();
	}
	
	// antwoord: 191164
	@Override
	public Long star1() {
		String input=loadInputToString();
		input=input.replaceAll("\\{\\}","");
		input=input.replaceAll("[\\{\\}\\[\\],\\:\"]+",",");
		input=input.replaceAll("[a-z]+", ",");
		input=input.replaceAll("[,]+", ",");
		input=input.substring(1, input.length()-1);
		String[] values=input.split(",");
		long som=0L;
		for(String s:values) {
			som+=Integer.parseInt(s);
		}
		System.out.println(input);
		return som;
	}

	@Override
	public Long star2() {
		
		return 0L;
	}

}
