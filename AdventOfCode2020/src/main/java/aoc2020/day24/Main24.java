package aoc2020.day24;

import java.awt.Point;

import common.main.AbstractMainMaster;

public class Main24 extends AbstractMainMaster<Long> {

	public static void main(String[] args) {
		new Main24().nolog().start2();
	}

	Hexagon tapijt = new Hexagon();

	public Long star1() {
		tapijt.log = super.log;
		char subdir = 0;
		for (String line : loadInputByLine()) {
			tapijt.gotoStart();
			for (char c : line.toCharArray()) {
				if (c == 's' || c == 'n') {
					subdir = c;
					continue;
				}
				tapijt.move("" + (subdir == 0 ? "" : subdir) + c);
				subdir = 0;
			}
			tapijt.flipCurrent();
		}
		return tapijt.count();
	}

	public Long star2() {
		star1();
		int days=100;
		if(log) tapijt.show();
		for (int day = 1; day <= days; day++) {
			Hexagon temp=tapijt.clone();
			for (int x = tapijt.minx - 1; x <= tapijt.maxx + 1; x++) {
				for (int y = tapijt.miny - 1; y <= tapijt.maxy + 1; y++) {
					Point p=new Point(x,y);
					boolean value=tapijt.getValue(p);
					int trues=tapijt.countTrueNeighbours(p);
					if((value &&(trues>2||trues==0))||(!value&&trues==2)) {
						if(log)logln("flipping:"+p+" with value "+value);
						temp.flipTile(p);
					}
				}
			}
			tapijt=temp;
			if(log) tapijt.show();
			if(log) logln("After "+day+" days, counted:"+tapijt.count());
		}
		return tapijt.count();
	}
}
