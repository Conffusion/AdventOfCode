package aoc2023.day21;

import common.graph.Point;

public class Vak {
	public Point point;
	boolean isGras;
	int afstand;
	
	public Vak(boolean isGras) {
		this.isGras=isGras;
		afstand=Integer.MAX_VALUE;
	}

	@Override
	public String toString() {
		return "Vak [point=" + point + ", isGras=" + isGras + ", afstand=" + afstand + "]";
	}
	
}
