package aoc2021.day15;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import common.main.AbstractMainMaster;

/**
 * Deel 2 Werkt niet :(
 * Wel met de testdata maar niet met de puzzel input.
 * 
 * @author walter
 *
 */
public class Main15 extends AbstractMainMaster<Long> {

	int[][]map, // initiele opgave met kost van elke node
	  cost; // kleinste kost om hier te geraken
	Queue<Point> todo; // welke nodes moeten nog nagekeken worden ?
	Set<Point> todoSet;
	int xdim,ydim; // dimensies van de kaart
	
	@Override
	public void beforeEach() {
		List<String> lines=loadInputByLine();
		xdim=lines.get(0).length();
		ydim=lines.size();
		map=new int[xdim][ydim];
		cost=new int[xdim][ydim];
		todo=new LinkedList<>();
		todoSet=new HashSet<>();
		int y=-1;
		for(String line:lines) {
			y++;
			for(int x=0;x<xdim;x++) {
				map[x][y]=line.charAt(x)-'0';
				if(x!=0||y!=0)
					cost[x][y]=Integer.MAX_VALUE;
			}
		}
		todo.add(new Point(0,0));
	}

	private void updateCost(int x,int y, int pcost) {
		if (cost[x][y]>pcost+map[x][y])
		{
			cost[x][y]=pcost+map[x][y];
			Point p=new Point(x,y);
			if(todoSet.add(p))
				todo.add(p);
		}
	}
	private void evaluate(Point p) {
		int pcost=cost[p.x][p.y];
		if (p.x>0)
			updateCost(p.x-1,p.y,pcost);
		if (p.x<xdim-1)
			updateCost(p.x+1,p.y,pcost);
		if(p.y>0)
			updateCost(p.x,p.y-1,pcost);
		if(p.y<ydim-1)
			updateCost(p.x,p.y+1,pcost);
	}
	
	@Override
	public Long star1() {
		long loops=0;
		while(!todo.isEmpty()) {
			loops++;
			Point p=todo.remove();
			todoSet.remove(p);
			evaluate(p);
			if(loops%100==0)
				logln("queue size:"+todo.size());
		}
		return (long)cost[xdim-1][ydim-1];
	}

	int[][]map2, // initiele opgave met kost van elke node
	  cost2; // kleinste kost om hier te geraken
	int xdim2,ydim2;
	
	private void copy(int shiftx,int shifty,int inc) {
		for (int y=0;y<ydim;y++)
			for (int x=0;x<xdim;x++) {
				map2[x+shiftx][y+shifty]=map[x][y]+inc>9?map[x][y]+inc-9:map[x][y]+inc;
			}
				
	}
	
	@Override
	public Long star2() {
		xdim2=xdim*5;
		ydim2=ydim*5;
		logln("dimensies:"+xdim2+","+ydim2);
		map2=new int[xdim2][ydim2];
		cost2=new int[xdim2][ydim2];
		for (int x=0;x<5;x++)
			for (int y=0;y<5;y++)
				copy(x*xdim,y*ydim,x+y);
		for(int x=0;x<xdim2;x++)
			for(int y=0;y<ydim2;y++)
				if(x!=0||y!=0)
					cost2[x][y]=Integer.MAX_VALUE;
		cost=cost2;
		map=map2;
		xdim=xdim2;
		ydim=ydim2;
		long loops=0;
		while(!todo.isEmpty()) {
			loops++;
			Point p=todo.remove();
			evaluate(p);
			if(loops%5000==0)
				logln("queue size:%d",todo.size());
		}
		
		return (long)cost2[xdim2-1][ydim2-1];
	}

	public static void main(String[] args) {
		new Main15()
		.nolog()
//		.testMode()
		.start2();
	}

}
