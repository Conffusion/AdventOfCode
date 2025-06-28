package common.dim3;

public class Plaats<T> {
	int x, y, z;
	T inhoud;
	
	public Plaats(int x, int y, int z, T inhoud) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.inhoud = inhoud;
	}
	
}
