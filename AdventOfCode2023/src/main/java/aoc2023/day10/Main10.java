package aoc2023.day10;

import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

import common.dim2.DimensionUtils;
import common.graph.Point;
import common.main.AbstractMainMaster;

public class Main10 extends AbstractMainMaster<Long> {
	public static void main(String[] args) {
		new Main10()
			//.testMode()
			// .withFileOutput()
			// .nolog()
			.start();
	}

	private static final int DIM_TEST = 5;
	private static final int DIM_REAL = 140;

	char[][] map; // [x][y]
	long[][] distance; // afstand van elke plaats tov start S, S=0 [x][y]
	int dimx,dimy;
	Point start;

	@Override
	public String getResourceName() {
		if (onStar==2 && testMode) {
			return "testinput2.txt";
		}
		return super.getResourceName();
	}

	@Override
	public void beforeEach() {
		dimx = testMode ? DIM_TEST : DIM_REAL;
		dimy = testMode ? DIM_TEST : DIM_REAL;
		if (onStar==2 && testMode) {
			dimx=20; dimy=10; }
		map = loadInputInto2DArrayXY(dimx, dimy);
		distance = new long[dimx][dimy];
		DimensionUtils.fill2Dim(distance, -1);
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (map[x][y] == 'S')
					start = new Point(x, y);
			}
		}
		distance[start.x][start.y] = 0;
	}

	enum Dir{N,O,W,S}
	
	// antwoord :6640
	public Long star1() {
		// zoek 2 pijpen vertrekken van S
		Pair<Point,Dir> p1 = null, p2 = null;
		check: {
			// links van start
			if (start.x > 0 && "-LF".indexOf(map[start.x-1][start.y]) >= 0) {
				p1 = Pair.of(new Point(start.x-1, start.y),Dir.O);
			}
			// boven start
			if (start.y > 0 && "|7F".indexOf(map[start.x][start.y - 1]) >= 0)
				if (p1 == null) {
					p1 = Pair.of(new Point(start.x, start.y - 1),Dir.S);
				} else {
					p2 = Pair.of(new Point(start.x, start.y - 1),Dir.S);
					break check;
				}
			// rechts van start
			if(start.x<dimx-1 && "-J7".indexOf(map[start.x+1][start.y])>=0)
				if(p1==null) {
					p1=Pair.of(new Point(start.x+1,start.y),Dir.W);
				} else {
					p2=Pair.of(new Point(start.x+1,start.y),Dir.W);
					break check;
				}
			// onder start
			if(start.y< dimy-1 && "|LJ".indexOf(map[start.x][start.y+1])>=0) {
				p2=Pair.of(new Point(start.x,start.y+1),Dir.N);
			}
		}
		long steps=1;
		distance[p1.getLeft().x][p1.getLeft().y]=steps;
		distance[p2.getLeft().x][p2.getLeft().y]=steps;
		while(!Objects.equals(p1.getLeft(), p2.getLeft())) {
			p1=nextPoint(p1.getLeft(),p1.getRight());
			p2=nextPoint(p2.getLeft(),p2.getRight());
			steps++;
			distance[p1.getLeft().x][p1.getLeft().y]=steps;
			distance[p2.getLeft().x][p2.getLeft().y]=steps;
		}
		return steps;
	}

	public Pair<Point,Dir> nextPoint(Point current, Dir previous) {
		
		// links van current
		if (previous !=Dir.W &&current.x > 0 && "-7J".indexOf(map[current.x][current.y])>=0 && "-LF".indexOf(map[current.x-1][current.y]) >= 0)
			return Pair.of(new Point(current.x-1, current.y),Dir.O);
		// boven current
		if (previous !=Dir.N && current.y > 0 && "|JL".indexOf(map[current.x][current.y])>=0&& "|7F".indexOf(map[current.x][current.y - 1]) >= 0)
			return Pair.of(new Point(current.x, current.y - 1),Dir.S);
		// rechts van current
		if(previous !=Dir.O && current.x<dimx-1 && "-FL".indexOf(map[current.x][current.y])>=0&& "-J7".indexOf(map[current.x+1][current.y])>=0)
			return Pair.of(new Point(current.x+1,current.y),Dir.W);
		// onder current
		if(previous !=Dir.S && current.y< dimy-1 && "|7F".indexOf(map[current.x][current.y])>=0&& "|LJ".indexOf(map[current.x][current.y+1])>=0)
			return Pair.of(new Point(current.x,current.y+1),Dir.N);
		throw new IllegalArgumentException("Er is geen vervolg op "+current+" na "+previous);
	}
	
	// antwoord :411
	public Long star2() {
		star1(); // om distance te vullen
		distance[start.x][start.y] = 0;
		map[start.x][start.y]='|';
		long teller=0;
		for (int y=0;y<dimy;y++) {
			boolean inLoop=false;
			for (int x=0;x<dimx;x++) {
				if(distance[x][y]==-1) {
					if(inLoop) {
						teller++;
						map[x][y]='*';
					} else
						map[x][y]=' ';
				} else {
					if("|JL".indexOf(map[x][y])>=0)
						inLoop=!inLoop;
				}
			}
		}
		DimensionUtils.walk(dimx, dimy, this::printVak);
		return teller;
	}
	private void printVak(Point p) {
		if(p.x==0)logln("");
		log(""+map[p.x][p.y]);
	}
}