package common.dim2;

import common.graph.Point;

public class Velocity {
	int deltax, deltay;
	
	
	public Velocity(int deltax, int deltay) {
		this.deltax = deltax;
		this.deltay = deltay;
	}

	/**
	 * Moves the point p using the current velocity
	 * @param p Point to move
	 * @return new point after applying the velocity once
	 */
	public Point move(Point p) {
		return new Point(p.x+deltax,p.y+deltay);
	}
	
	/**
	 * Moves the point p x times using the current velocity
	 * @param p Point to move
	 * @param times how many times the current velocity must be applied
	 * @return new point after applying the velocity x times
	 */
	public Point move(Point p, int times) {
		return new Point(p.x+deltax*times,p.y+deltay*times);
	}

	@Override
	public String toString() {
		return "V(dx=" + deltax + ", dy=" + deltay + ")";
	}
}
