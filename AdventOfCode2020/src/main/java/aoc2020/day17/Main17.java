package aoc2020.day17;

import java.util.List;

import aoc2020.common.MainMaster;

public class Main17 extends MainMaster {
	// x,y,z
	boolean universe[][][];
	int dimx,dimy,dimz;
	
	public void loadData(String filename, int iteraties) {
		List<String> all = loadInput(17, filename);
		dimx=dimy=all.get(0).length()+2*(iteraties+1);
		dimz=2*(iteraties+1)+1;
		universe=new boolean[dimx][dimy][dimz];
		int startx=iteraties+1;
		int starty=iteraties+1;
		int startz=iteraties+1;
		int y=starty;
		for(String s:all) {
			int x=startx;
			for (char c:s.toCharArray())
			{
				if(c=='#') {
					if(log)logln("# found at x="+x+",y="+y+",z="+startz);
					universe[x][y][startz]=true;
				}
				x++;
			}
			y++;
		}
		if(log)logln("Data loaded from "+filename);
	}

	public boolean[][][] clone(boolean[][][] in) {
		boolean[][][] out = new boolean[dimx][dimy][dimz];
		for (int x=0;x<dimx;x++)
			for(int y=0;y<dimy;y++) {
				System.arraycopy(in[x][y], 0, out[x][y], 0, dimz);
			}
		
		return out;
	}
	
	public int countSurroundings(int x, int y, int z, boolean value) {
		int result=0;
		StringBuffer buf=new StringBuffer();
		for (int dx=x-1;dx<=x+1;dx++) {
			for (int dy=y-1;dy<=y+1;dy++) {
				for (int dz=z-1;dz<=z+1;dz++) {
					if(dx==x&&dy==y&&dz==z)
						continue; // skip eigen blok
					if(log&&universe[dx][dy][dz]==value)buf.append("("+dx+","+dy+","+dz+")");
					result+=universe[dx][dy][dz]==value?1:0;
					// optimalisatie aangezien we enkel willen weten of 3 of meer
					if(result>=4) {
						if(log)logln("counted "+x+","+y+","+z+": "+result+":"+buf.toString());
						return result;
					}
				}
			}
		}
		if(log)logln("counted "+x+","+y+","+z+": "+result+":"+buf.toString());
		return result;
	}
	
	public int countActive(boolean[][][] all) {
		int count = 0;
		
		for (int dx=0;dx<dimx;dx++) {
			for (int dy=0;dy<dimy;dy++) {
				for (int dz=0;dz<dimz;dz++) {
					count+=universe[dx][dy][dz]?1:0;
				}
			}
		}
		return count;
	}

	public void star1(int iteraties) {
		boolean[][][]temp;
		for(int it=0;it<iteraties;it++)
		{
			temp=clone(universe);
			if (log) logln("iteration " + it + ", active:" + countActive(temp));
			if (log) logln("xrange="+(iteraties-it)+"-"+(dimx-iteraties-1+it));
			for (int x=iteraties-it;x<=dimx-iteraties-1+it;x++)
			{
				for (int y=iteraties-it;y<=dimy-iteraties-1+it;y++)
				{
					if(log)logln("Iteration "+it+" x="+x+" y="+y);
					for (int z=iteraties-it;z<=dimz-iteraties-1+it;z++)
					{
						int count=countSurroundings(x, y, z, true);
						if(universe[x][y][z])
						{
							temp[x][y][z]=count==2||count==3;
						} else {
							temp[x][y][z]=count==3;
						}
					}
				}
			}	
			universe=temp;
			if (log)
				logln("iteration " + it + ", active:" + countActive(universe));
		}
		System.out.println("***STAR1: "+countActive(universe));
	}
	
	public static void main (String[] args) {
		Main17 m=new Main17();
		m.log=false;
		int iteraties=6;
		m.loadData("input.txt", iteraties);
		m.star1(iteraties);
	}
}
