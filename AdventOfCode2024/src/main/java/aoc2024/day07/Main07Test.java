package aoc2024.day07;

import aoc2024.day07.Main07.Opgave;
import common.BasicUtils;

public class Main07Test {

	public static void main(String[] args) {
		Opgave op=new Opgave(7290,new long[] {6, 8,6, 15});
		System.out.println(new Main07().verifieer(op, "121".toCharArray()));
		System.out.println( BasicUtils.convertToBase(3L, 3));
	}

}
