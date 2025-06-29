package aoc2021.day09;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.main.AbstractMainMaster;

public class Main09 extends AbstractMainMaster<Long> {

	int[][] map;
	int xdimension=100;
	int ydimension=100;
	int [][]bassins;
	Map<Point,Integer> lowest=new HashMap<>();
	
	@Override
	public void beforeAll() {
		if(testMode) {
			xdimension=10;
			ydimension=5;
		}
		map=new int[xdimension][ydimension];
		bassins=new int[xdimension][ydimension];
		List<String> input=loadInputByLine();
		int y=-1;
		for(String line:input) {
			y++;
			int x=-1;
			for(char c: line.toCharArray()) {
				x++;
				map[x][y]=c-'0';
			}
		}
	}

	public int isLowest(int x, int y) {
		int curr=map[x][y];
		if(x>0 && map[x-1][y]<=curr)
			return 0;
		if(x<xdimension-1 && map[x+1][y]<=curr)
			return 0;
		if(y>0 && map[x][y-1]<=curr)
			return 0;
		if(y<ydimension-1 && map[x][y+1]<=curr)
			return 0;
//		logln("lowpoint:"+x+","+y+": "+curr);
		return curr+1;
	}
	
	@Override
	public Long star1() {
		int count=0;
		for (int x=0;x<xdimension;x++)
			for (int y=0;y<ydimension;y++)
			{
				int v=isLowest(x,y);
				count+=v;
				if(v>0)
					lowest.put(new Point(x,y),0);
			}
		return (long)count;
	}
	
	/**
	 * als de waarde in (x,y) = 9 -> 0
	 * als het punt (x,y) al tot een bassin behoort -> 0
	 * anders: markeer (x,y) met bassinnr en roep buren aan -> 1 + som(antwoorden van buren) 
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int checkBassin(int x,int y,int bassinnr) {
		if (map[x][y]==9 || bassins[x][y]>0)
			return 0;
		bassins[x][y]=bassinnr;
		return 1 + (x>0?checkBassin(x-1,y,bassinnr):0)+ (x<xdimension-1?checkBassin(x+1,y,bassinnr):0)+
				(y>0?checkBassin(x,y-1,bassinnr):0)+ (y<ydimension-1?checkBassin(x,y+1,bassinnr):0);		
	}
	
	public void logBassin() {
		for(int y=0;y<ydimension;y++) {
			for(int x=0;x<xdimension;x++) {
				log(" "+bassins[x][y]);
			}
			logln("");
		}
	}
	
	/**
	 * antwoord: 1038240
	 */
	@Override
	public Long star2() {
		int bassinNr=0;
		int top1=0,top2=0,top3=0;
		for(Point p: lowest.keySet()) {
			int size=checkBassin(p.x,p.y,++bassinNr);
			if(size>top1) {
				top3=top2;
				top2=top1;
				top1=size;
			} else if (size>top2){
				top3=top2;
				top2=size;
			} else if (size> top3) {
				top3=size;
			}
		}
		logBassin();
		return (long)top1*top2*top3;
	}

	public static void main(String[] args) {
		new Main09()
		//.testMode()
		.start();
	}

}
