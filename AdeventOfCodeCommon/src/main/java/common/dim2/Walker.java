package common.dim2;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import common.graph.Direction;
import common.graph.Point;

/**
 * Staat op een bepaald punt in een bepaalde richting
 */
public class Walker {
	Point pos;
	Direction dir;
	Dimension dimension;
	BiFunction<Point,Walker,Boolean> isAccessibleRule;
	boolean keepHistory=false;
	List<Point> path;
	
	public enum ForwardStatus {
		POSSIBLE, OBSTACLE, OUT_OF_BOUND;
	}
	
	public Walker() {
	}

	/**
	 * Start op een gegeven plaats pos in richting dir binnen een terrein met dimensies dimension.
	 * @param pos start positie
	 * @param dir start richting
	 * @param dimension dimensies van het beschikbare terrein
	 */
	public Walker(Point pos, Direction dir, Dimension dimension) {
		this.pos = pos;
		this.dir = dir;
		this.dimension=dimension;
	}

	public Walker withAccessibleRule(BiFunction<Point,Walker,Boolean> isAccessibleRule) {
		this.isAccessibleRule=isAccessibleRule;
		return this;
	}
	/**
	 * Elke call naar {@link #moveForward()} zal de nieuwe positie toevoegen aan het pad. 
	 * @return
	 */
	public Walker withHistory() {
		this.keepHistory=true;
		path=new ArrayList<>();
		return this;
	}
	public ForwardStatus canForward() {
		Point p=forward();
		if(p==null)
			return ForwardStatus.OUT_OF_BOUND;
		if (isAccessibleRule.apply(p, this))
			return ForwardStatus.POSSIBLE;
		else
			return ForwardStatus.OBSTACLE;
	}
	
	/**
	 * Geeft de volgende positie of null als deze buiten dimensies valt.
	 * De huidige positie wordt niet aangepast.
	 * @return
	 */
	public Point forward() {
		if(dir.canMove(pos, dimension))
			return dir.move(pos);
		else
			return null;
	}
	
	public Walker clone() {
		return new Walker(pos,dir,dimension).withAccessibleRule(isAccessibleRule);
	}
	public void copyInto(Walker copy) {
		copy.pos=this.pos;
		copy.dimension=this.dimension;
		copy.isAccessibleRule=this.isAccessibleRule;
		copy.dir=this.dir;
		if(this.keepHistory) {
			copy.withHistory();
			copy.path=new ArrayList<>(this.path.size());
			copy.path.addAll(this.path);
		}
	}
	/**
	 * Beweeg vooruit in de huidige richting
	 * @return null wanneer het niet mogelijk is. Gebruik eerst {@link #forward()} om te controleren of het kan.
	 */
	public Point moveForward() {
		Point next=forward();
		if(next!=null)
			pos=next;
		else
			return null;
		if(keepHistory)
			path.add(next);
		return pos;
	}
	/**
	 * Draai naar de nieuwe richting.
	 * @param newDir
	 */
	public void setDirection(Direction newDir) {
		dir=newDir;
	}
	
	public Point getPos() {
		return pos;
	}
	/**
	 * change the position. If keepHistory=true the new pos is added
	 * to the path
	 * @param pos
	 */
	public void setPos(Point pos) {
		this.pos = pos;
		if(keepHistory)
			path.add(pos);
	}

	public Direction getDir() {
		return dir;
	}

	public List<Point> getPath() {
		return path;
	}

	@Override
	public String toString() {
		return "Walker [" + pos + "," + dir + "]";
	}
}
