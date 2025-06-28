package common.dim2;

import java.awt.Dimension;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import org.apache.commons.lang3.ArrayUtils;

import common.graph.Point;

/**
 * Terrein voor een 2-dim array van chars.
 * eerste dimensie is y, tweede x
 */
public class CharTerrein {
	private char[][] terrein;
	private Dimension dim;
	public CharTerrein(char[][] terrein) {
		this.terrein=terrein;
		dim=new Dimension(terrein[0].length,terrein.length);
	}
	
	public CharTerrein(Dimension dim) {
		this.dim = dim;
		terrein=new char[dim.height][dim.width];
	}
	/**
	 * Instantieer een nieuw terrein en elk element met initchar
	 * @param dim
	 * @param initchar
	 */
	public CharTerrein(Dimension dim,char initchar) {
		this(dim);
		DimensionUtils.walkYX(terrein, (c,p)->setValue(p, initchar));
	}
	
	public char at(Point point) {
		return terrein[point.y][point.x];
	}
	/**
	 * Verander de waarde naar value op positie point
	 * @param point
	 * @param value
	 */
	public void setValue(Point point, char value) {
		terrein[point.y][point.x]=value;
	}
	/**
	 * Overloop alle punten van het terrein en roep de processor aan
	 * @param processor
	 */
	public void scan(BiConsumer<Character,Point> processor) {
		DimensionUtils.walkYX(terrein, processor);
	}
	
	public Long scanAndSum(BiFunction<Character,Point,Long> evalueer) {
		return DimensionUtils.walkAndSum(dim.width, dim.height, p->evalueer.apply(at(p), p));
	}
	public Long scanAndCount(BiPredicate<Character,Point> test) {
		return DimensionUtils.walkAndSum(dim.width, dim.height, p->test.test(at(p), p)?1L:0L);
	}
	public CharTerrein clone() {
		CharTerrein cloned=new CharTerrein(dim);
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
	/**
	 * Leest alle lijnen van de lijst en zet ze in het huidige terrein.
	 * De dimensies van de lijst moeten overeenkomen met de dimensies van dit terrein. 
	 * @param lines
	 * @return
	 */
	public CharTerrein load(List<String> lines) {
		for(int y=0;y<lines.size();y++) {
			String line=lines.get(y);
			for(int x=0;x<line.length();x++)
			terrein[y][x]=line.charAt(x);
		}
		return this;
	}

	public Dimension getDim() {
		return dim;
	}
}
