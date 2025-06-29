package aoc2015.day10;

import aoc2015.common.MainMaster;

public class Main10 extends MainMaster<Integer> {

	public static void main(String[] args) {
		new Main10()
		//.withTestMode()
		.start();
	}

	@Override
	public Integer star1() {
		String input="1113122113";
		for(int n=0;n<40;n++)
			input=process(input);
		return input.length();
	}

	String process(String inString) {
		StringBuffer out=new StringBuffer();
		char[] in=inString.toCharArray();
		char last=in[0];
		int teller=1;
		for(int i=1;i<in.length;i++) {
			if(in[i]!=last) {
				out.append(teller);
				out.append(last);
				teller=1;
				last=in[i];
			} else {
				teller++;
			}
		}
		out.append(teller);
		out.append(last);
		return out.toString();
	}
	@Override
	public Integer star2() {
		String input="1113122113";
		for(int n=0;n<50;n++)
			input=process(input);
		return input.length();
	}

}
