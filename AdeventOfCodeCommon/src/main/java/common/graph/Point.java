package common.graph;

import java.awt.Dimension;

public class Point {
    public int x, y;

    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    public Point(Point toclone) {
        this.x=toclone.x;
        this.y=toclone.y;
    }

    public String toString() {
        return "("+x+","+y+")";
    }

	/**
	 * Controleert of huidig punt binnen dimensie valt ([0-dim.width[ , [0-dim.height[)
	 * @param dim dimensie van de 2D wereld
	 * @return
	 */
	public boolean isInbound(Dimension dim) {
		return x>=0 && x<dim.width && y>=0 && y<dim.height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
