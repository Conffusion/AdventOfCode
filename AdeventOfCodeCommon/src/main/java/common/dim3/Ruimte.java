package common.dim3;

public class Ruimte<T> {
	int[][][] grid;
	
	public Ruimte(int dimX, int dimY, int dimZ) {
		grid=new int[dimX][dimY][dimZ];
	}
	
}
