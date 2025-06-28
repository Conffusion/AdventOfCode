package common.graph;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import common.dim2.Velocity;

public enum Direction {
	RIGHT(1,0), DOWN(0,1), LEFT(-1,0), UP(0,-1);
	Velocity delta;
	
	private Direction(int xdelta, int ydelta) {
		this.delta=new Velocity(xdelta, ydelta);
	}

	public Direction turnLeft() {
		return values()[(ordinal()+3)%4];
	}
	
	public Direction turnRight() {
		return values()[(ordinal()+1)%4];
	}
	
	/**
	 * Geeft een nieuw Point met de volgende locatie volgens de huidige richting.
	 * We gaan er van uit dat de (0,0) links bovenaan is
	 * @param p
	 * @return nieuw punt in huidige richting. Opgelet: resultaat kan buiten bereik vallen
	 */
	public Point move(Point p) {
		return delta.move(p);
	}
	/**
	 * Kunnen we punt p verplaatsen in huidige richting zonder buiten de dimensies te vallen
	 * @param p te onderzoeken punt
	 * @param dim dimensie van de 2D wereld
	 * @return
	 */
	public boolean canMove(Point p,Dimension dim) {
		Point newp=delta.move(p);
		return newp.x>=0 && newp.x<dim.width && newp.y>=0&&newp.y<dim.height;
	}
	
	public boolean isOpposite(Direction dir) {
		return switch(dir) {
		case DOWN->this==Direction.UP;
		case UP->this==Direction.DOWN;
		case LEFT->this==Direction.RIGHT;
		case RIGHT->this==Direction.LEFT;
		};
	}
    public Direction opposite() {
    	switch(this) {
    	case DOWN: return Direction.UP;
    	case UP: return Direction.DOWN;
    	case LEFT: return Direction.RIGHT;
    	case RIGHT: return Direction.LEFT;
    	}
    	return null;
    }
    /**
     * Returns all directions except the opposite of foreward
     * @param foreward
     * @return
     */
    public List<Direction> allButOpposite() {
    	return Arrays.stream(values()).filter(d->!d.isOpposite(this)).toList();
    }
}