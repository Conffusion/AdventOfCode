package aoc2015.day03;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import aoc2015.common.MainMaster;

public class Main03 extends MainMaster<Integer> {

	public static void main(String[] args) {
		new Main03();
	}

	public Main03() {
		doStar1();
		doStar2();
	}


	char[] input=loadInputToString().toCharArray();
	
	// answer: 2572
	@Override
	public Integer star1() {
		Set<Integer> world=new HashSet<>();
		Point santa=new Point(0,0);
		for (char c:input)
		{
			switch(c) {
			case '>':santa.x++; break;
			case '<':santa.x--; break;
			case '^':santa.y--; break;
			case 'v':santa.y++; break;
			default:
				System.err.println("Onbekend character "+c);
			}
			world.add(santa.x*100000+santa.y);
		}
		return world.size();
	}

	// answer: 2631
	@Override
	public Integer star2() {
		Set<Integer> world=new HashSet<>();
		// 0=Santa , 1= Robo-Santa
		Point[]santas=new Point[] {new Point(0,0),new Point(0,0)};
		int mover=0;
		for (char c:input)
		{
			Point santa=santas[mover];
			switch(c) {
			case '>': santa.x++; break;
			case '<': santa.x--; break;
			case '^': santa.y--;break;
			case 'v': santa.y++;break;
			default:
				System.err.println("Onbekend character "+c);
			}
			world.add(santa.x*100000+santa.y);
			mover=(mover+1)%2;
		}
		return world.size();
	}
	
}
