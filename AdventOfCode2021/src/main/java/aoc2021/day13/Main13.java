package aoc2021.day13;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.main.AbstractMainMaster;

public class Main13 extends AbstractMainMaster<Integer> {

	Set<Point> points;
	List<Fold> folds;
	int maxX,maxY;
	
	class Fold {
		int axe, pos;

		public Fold(String axe, int pos) {
			this.axe = axe.charAt(0);
			this.pos = pos;
		}
	}

	Pattern pointPattern = Pattern.compile("([0-9]+),([0-9]+)");
	Pattern foldPattern = Pattern.compile("fold along ([a-z])=([0-9]+)");

	@Override
	public void beforeEach() {
		points = new HashSet<>();
		folds = new ArrayList<>();
		List<String> lines = loadInputByLine();
		for (String line : lines) {
			Matcher m = pointPattern.matcher(line);
			if (m.matches()) {
				int x=Integer.parseInt(m.group(1));
				int y=Integer.parseInt(m.group(2));
				points.add(new Point(x, y));
				maxX=maxX<x?x:maxX;
				maxY=maxY<y?y:maxY;
			} else {
				m = foldPattern.matcher(line);
				if (m.matches())
					folds.add(new Fold(m.group(1), Integer.parseInt(m.group(2))));
			}
		}
		super.beforeEach();
	}

	private Set<Point> foldX(int pos, Set<Point> points) {
		int x0 = pos * 2;
		Set<Point> out = new HashSet<>();
		for (Point p : points) {
			if (p.x < pos)
				out.add(p);
			else if (p.x > pos)
				out.add( new Point(x0 - p.x, p.y));
			// ignore points with p.x = pos
		}
		maxX=maxX/2;
		return out;
	}

	private Set<Point> foldY(int pos, Set<Point> points) {
		int y0 = pos * 2;
		Set<Point> out = new HashSet<>();
		for (Point p : points) {
			if (p.y < pos)
				out.add(p);
			else if (p.y > pos)
				out.add(new Point(p.x, y0 - p.y));
			// ignore points with p.y = pos
		}
		maxY=maxY/2;
		return out;
	}

	@Override
	public Integer star1() {
		Fold fold = folds.get(0);
		if (fold.axe == 'x')
			points = foldX(fold.pos, points);
		if (fold.axe == 'y')
			points = foldY(fold.pos, points);
		return points.size();
	}

	@Override
	public Integer star2() {
		for (Fold fold : folds) {
			if (fold.axe == 'x')
				points = foldX(fold.pos, points);
			if (fold.axe == 'y')
				points = foldY(fold.pos, points);
		}
		logln("size:"+maxX+" x "+maxY);
		char[][] paper=new  char[maxX+1][maxY+1];
		for(Point p:points)
		{
			paper[p.x][p.y]='#';
		}
		for(int y=0;y<maxY;y++)
		{
			for(int x=0;x<maxX;x++)
				System.out.print(paper[x][y]=='#'?'#':' ');
			System.out.println();
		}
		return points.size();
	}

	public static void main(String[] args) {
		new Main13()
//		.testMode()
				.start();
	}

}
