package aoc2015.day01;

import aoc2015.common.MainMaster;

/**
 * ASCII '(' = 40
 * ASCII ')' = 41
 * @author walter
 *
 */
public class Main01 extends MainMaster<Integer> {

	public static void main(String[] args) {
		new Main01();
	}

	public Main01() {
		doStar1();
		doStar2();
	}

	String input=loadInputToString("input.txt");

	// answer: 138
	public Integer star1() {
		return input.chars().map(c->c==40?1:-1).sum();
	}
	
	// answer: 1771
	public Integer star2() {
		int floor=0;
		int pos=0;
		for(char c:input.toCharArray()) {
			floor+= c==40?1:-1;
			pos++;
			if(floor<0)
				return pos;
		}
		return pos;
	}

}
