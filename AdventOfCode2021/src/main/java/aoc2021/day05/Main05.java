package aoc2021.day05;

import java.util.List;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

public class Main05 extends AbstractMainMaster<Long> {
	private static int DIMENSION = 1000;

	class Line {
		int x1, x2, y1, y2;
		int xdir,ydir;
		public Line(int x1, int y1, int x2, int y2) {
			super();
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
			if(x1<x2)
				xdir=1;
			else if (x1>x2)
				xdir=-1;
			else 
				xdir=0;
			if(y1<y2)
				ydir=1;
			else if (y1>y2)
				ydir=-1;
			else 
				ydir=0;
		}

		@Override
		public String toString() {
			return "" + x1 + "," + y1 + "->" + x2 + "," + y2;
		}

	}

	RegexTool regex=new RegexTool("([0-9]+),([0-9]+) -> ([0-9]+),([0-9]+)");
	List<Line> lines;
	int[][] map = new int[DIMENSION][DIMENSION];
	int teller=0;

	@Override
	public void beforeAll() {
		lines=parseInput(this::parseLine);
		if(testMode) {
			DIMENSION=10;
		}
	}

	@Override
	public void beforeEach() {
		map = new int[DIMENSION][DIMENSION];
		teller=0;
	}

	public Line parseLine(String line) {
		if (regex.evaluate(line)) {
			return new Line(regex.intGroup(1), regex.intGroup(2), regex.intGroup(3),
			regex.intGroup(4));
		}
		return null;
	}

	private void drawLine3(Line line) {
		int x = line.x1;
		int y = line.y1;
		if(++map[y][x]==2)
			teller++;
		while (x != line.x2 ||  y != line.y2) {
			x+=line.xdir;
			y+=line.ydir;
			if(++map[y][x]==2)
				teller++;
		} 
	}

	// antwoord: 4728
	@Override
	public Long star1() {
		lines.stream().filter(l -> l.x1 == l.x2 || l.y1 == l.y2).forEach(this::drawLine3);
		return (long) teller;
	}

	// antwoord: 17717
	@Override
	public Long star2() {
		lines.stream().forEach(this::drawLine3);
		return (long) teller;
	}

	public static void main(String[] args) {
		new Main05().nolog().start();
	}
}
