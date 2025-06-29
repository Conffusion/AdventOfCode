package aoc2020.day17;

import java.util.List;

import aoc2020.common.MainMaster;

public class Main17Star2 extends MainMaster {
	// x,y,z
	boolean universe[][][][];
	int dimx, dimy, dimz, dimw;
	int iteraties;
	
	public void loadData(String filename) {
		List<String> all = loadInput(17, filename);
		dimx = dimy = all.get(0).length() + 2 * (iteraties + 1);
		dimz = dimw = 2 * (iteraties + 1) + 1;

		universe = new boolean[dimx][dimy][dimz][dimw];
		int startx = iteraties + 1;
		int starty = iteraties + 1;
		int startz = iteraties + 1;
		int startw = iteraties + 1;
		int y = starty;
		for (String s : all) {
			int x = startx;
			for (char c : s.toCharArray()) {
				if (c == '#') {
					if (log)
						logln("# found at x=" + x + ",y=" + y + ",z=" + startz);
					universe[x][y][startz][startw] = true;
				}
				x++;
			}
			y++;
		}
		if (log)
			logln("Data loaded from " + filename);
	}

	public boolean[][][][] clone(boolean[][][][] in,int it) {
		boolean[][][][] out = new boolean[dimx][dimy][dimz][dimw];
		for (int x = 0; x < dimx; x++)
			for (int y = 0; y < dimy; y++)
				for (int z = 0; z < dimz; z++)
					System.arraycopy(in[x][y][z], 0, out[x][y][z], 0, dimw);
		return out;
	}

	public int countSurroundings(int x, int y, int z, int w, boolean value) {
		int result = 0;
		for (int dx = x - 1; dx <= x + 1; dx++) {
			for (int dy = y - 1; dy <= y + 1; dy++) {
				for (int dz = z - 1; dz <= z + 1; dz++) {
					for (int dw = w - 1; dw <= w + 1; dw++) {
						if (dx == x && dy == y && dz == z && dw == w)
							continue; // skip eigen blok
						if(universe[dx][dy][dz][dw] == value) 
						{
							result ++;
							// optimalisatie aangezien we enkel willen weten of 3 of meer
							if (result >= 4) {
								if (log)
									logln("counted " + x + "," + y + "," + z + "," + w + ": " + result + ":");
								return result;
							}
						}
					}
				}
			}
		}
		if (log)
			logln("counted " + x + "," + y + "," + z + "," + w + ": " + result);
		return result;
	}

	public int countActive(boolean[][][][] all) {
		int count = 0;

		for (int dx = 0; dx < dimx; dx++)
			for (int dy = 0; dy < dimy; dy++)
				for (int dz = 0; dz < dimz; dz++)
					for (int dw = 0; dw < dimw; dw++)
						count += universe[dx][dy][dz][dw] ? 1 : 0;
		return count;
	}

	public void star2() {
		boolean[][][][] temp;
		for (int it = 0; it < iteraties; it++) {
			temp = clone(universe,it);
			if (log)
				logln("iteration " + it + ", active:" + countActive(temp));
			if (log)
				logln("xrange=" + (iteraties - it) + "-" + (dimx - iteraties - 1 + it));
			for (int x = iteraties - it; x <= dimx - iteraties - 1 + it; x++) {
				for (int y = iteraties - it; y <= dimy - iteraties - 1 + it; y++) {
					if (log)
						logln("Iteration " + it + " x=" + x + " y=" + y);
					for (int z = iteraties - it; z <= dimz - iteraties - 1 + it; z++) {
						for (int w = iteraties - it; w <= dimw - iteraties - 1 + it; w++) {
							int count = countSurroundings(x, y, z, w, true);
							if (universe[x][y][z][w]) {
								temp[x][y][z][w] = count == 2 || count == 3;
							} else {
								temp[x][y][z][w] = count == 3;
							}
						}
					}
				}
			}
			universe = temp;
			if (log)
				logln("iteration " + it + ", active:" + countActive(universe));
		}
		System.out.println("***STAR1: " + countActive(universe));
	}

	public static void main (String[] args) {
		Main17Star2 m=new Main17Star2();
		m.log=false;
		m.iteraties=6;
		m.loadData("input.txt");
		m.timer(()-> {m.star2();});
	}
}
