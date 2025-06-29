package aoc2021.day11;

import common.main.AbstractMainMaster;

public class Main11 extends AbstractMainMaster<Long> {

	int[][] map;
	int[][] flashed;
	long flashCount;
	int RDIM=10;
	int CDIM=10;
	int syncFlash=0;
	
	@Override
	public void beforeEach() {
		map=new int[10][10];
		flashCount=0;
		int r=-1,c=0;
		for(String line:loadInputByLine()) {
			r++;
			c=0;
			for(char v:line.toCharArray()) {
				map[r][c++]=v-'0';
			}
		}		
	}

	private void printMap() {
		for(int r=0;r<map.length;r++) {
			for (int c=0;c<map[0].length;c++)
				log(" "+map[r][c]);
			logln("");
		}
	}
	// verhoog een octopus omdat een buur geflashed heeft
	public void inc(int c, int r) {
		if(c<0|| c>=CDIM || r<0||r>=RDIM)
			return;
		map[r][c]++;
		if(map[r][c]>9)
		{
			flashed(c,r);
		}
	}
	public void flashed(int c,int r) {
		if(flashed[r][c]==1)
			return;
		flashed[r][c]=1;
		for(int dc=-1;dc<=1;dc++)
			for(int dr=-1;dr<=1;dr++)
				if(dc!=0||dr!=0)
					inc(c+dc,r+dr);
	}
	public boolean step() {
		flashed=new int[10][10];
		// all energy+1
		for(int r=0;r<map.length;r++) {
			for (int c=0;c<map[0].length;c++)
				map[r][c]++;
		}
		for(int r=0;r<RDIM;r++) {
			for (int c=0;c<CDIM;c++)
				if(map[r][c]>9 && flashed[r][c]==0)
				{
					flashed(c,r);
				}
		}
		// tel aantal flashes van deze step
		int stepflashes=0;
		for(int r=0;r<RDIM;r++)
			for(int c=0;c<CDIM;c++)
			{
				if(flashed[r][c]==1) {
					stepflashes+=flashed[r][c];
					map[r][c]=0;
				}
			}		
		flashCount+=stepflashes;
		return stepflashes==RDIM*CDIM;
	}
	
	@Override
	public Long star1() {
		printMap();
		for (int step=1;step<=100;step++)
		{
			step();
			logln("after step "+step);
			//printMap();
		}
		return flashCount;
	}

	@Override
	public Long star2() {
		long stepcount=1;
		while (!step()) {
			stepcount++;
		}	
		return stepcount;
	}

	public static void main(String[] args) {
		new Main11()
		.nolog()
		//.testMode()
		.start();
	}

}
