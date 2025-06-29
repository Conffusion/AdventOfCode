package aoc2023.day22;

/**
 * Representeert 1 plaats van een balk. Wanneer de balk verplaatst wordt zal deze BalkPos automatisch mee bewegen.
 */
public class BalkPos {
	private Balk balk;
	private int delta;
	
	public BalkPos(Balk balk, int delta) {
		this.balk = balk;
		this.delta = delta;
	}
	public int getX() {
		return balk.x+delta*balk.deltaX;
	}
	public int getY() {
		return balk.y+delta*balk.deltaY;
	}
	public int getZ() {
		return balk.z+delta*balk.deltaZ;
	}
	public Balk getBalk() {
		return balk;
	}
	@Override
	public String toString() {
		return "BalkPos(" + getX()+","+getY()+","+getZ() + ")";
	}
	
}
