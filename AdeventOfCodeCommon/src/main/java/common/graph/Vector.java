package common.graph;

import java.awt.Dimension;

/**
 * Een vector heeft een positie (point) en een richting (dir)
 */
public class Vector {
	public Point point;
	public Direction dir;
	
	public Vector(int x, int y, Direction dir) {
		point=new Point(x,y);
		this.dir=dir;
	}
	public Vector(Direction dir,Point point) {
		this.point = point;
		this.dir = dir;
	}
	public Vector(Vector toclone) {
		this.dir=toclone.dir;
		this.point=new Point(toclone.point);
	}
	/**
	 * verplaatst de huidige locatie volgens de richting.
	 * dit past dus de locatie aan van de huidige instance !
	 */
	public Vector forward() {
		point=dir.move(point);
		return this;
	}
	public Vector turnLeft() {
		dir=dir.turnLeft();
		return this;
	}
	public Vector turnRight() {
		dir=dir.turnRight();
		return this;
	}
	public boolean onMap(Dimension dim) {
		return point.isInbound(dim);
	}
	/**
	 * geeft de volgende positie aan
	 * @return nieuwe positie vector in dezelfde richting als deze
	 */
	public Vector next() {
		return new Vector(dir,dir.move(point));
	}
	
	@Override
	public String toString() {
		return "[" + point + ", " + dir + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector other = (Vector) obj;
		if (dir != other.dir)
			return false;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		return true;
	}
}
