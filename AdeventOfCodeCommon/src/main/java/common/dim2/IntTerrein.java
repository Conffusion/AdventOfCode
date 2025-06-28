package common.dim2;

import java.awt.Dimension;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import org.apache.commons.lang3.ArrayUtils;

import common.graph.Point;

/**
 * Terrein voor een 2-dim array van chars.
 * eerste dimensie is y, tweede x
 */
public class IntTerrein {
	private int[][] terrein;
	private Dimension dim;
	public IntTerrein(int[][] terrein) {
		this.terrein=terrein;
		dim=new Dimension(terrein[0].length,terrein.length);
	}
	
	public IntTerrein(Dimension dim) {
		this.dim = dim;
		terrein=new int[dim.height][dim.width];
	}
	public IntTerrein(Dimension dim, int initValue) {
		this(dim);
		DimensionUtils.walkYX(terrein, (c,p)->setValue(p, initValue));
	}
	public int at(Point point) {
		return terrein[point.y][point.x];
	}
	/**
	 * Als point buiten ligt, geef dan defaultValue
	 * @param point
	 * @param defaultValue
	 * @return waarde in terrein op positie point of defaultValue
	 */
	public int at(Point point, int defaultValue) {
		return isIn(point)? terrein[point.y][point.x]:defaultValue;
	}

	/**
	 * Verander de waarde naar value op positie point
	 * @param point
	 * @param value
	 */
	public void setValue(Point point, int value) {
		terrein[point.y][point.x]=value;
	}
	/**
	 * Overloop alle punten van het terrein en roep de processor aan
	 * @param processor
	 */
	public void scan(BiConsumer<Integer,Point> processor) {
		DimensionUtils.walkYX(terrein, processor);
	}
	
	public Long scanAndSum(BiFunction<Integer,Point,Long> evalueer) {
		return DimensionUtils.walkAndSum(dim.width, dim.height, p->evalueer.apply(at(p), p));
	}
	public Long scanAndCount(BiPredicate<Integer,Point> test) {
		return DimensionUtils.walkAndSum(dim.width, dim.height, p->test.test(at(p), p)?1L:0L);
	}
	public IntTerrein clone() {
		IntTerrein cloned=new IntTerrein(dim);
		for(int y=0;y<dim.height;y++) {
			cloned.terrein[y]=ArrayUtils.clone(terrein[y]);
		}
		return cloned;
	}
	/**
	 * Controleert of punt p binnen terrein valt
	 * @param p
	 * @return
	 */
	public boolean isIn(Point p) {
		return p.x>=0&&p.x<dim.width&&p.y>=0&&p.y<dim.height;
	}
}
