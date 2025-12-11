package common.dim2;

import java.awt.Dimension;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import common.graph.Direction;
import common.graph.Point;

/**
 * Terrein bevat een 2 dimensionale wereld met elementen van Type E.
 * Eerste coordinaat is x, 2e is y.
 * 
 */
public class Terrein<E> {
	public enum OutOfBoundAction {
		IGNORE,
		THROW_ERROR
	}
	Class<E> clazz;
	E[][] terrein;
	Dimension dim;
	OutOfBoundAction onOutOfBound=OutOfBoundAction.IGNORE;
	Field pointField;
	
	/**
	 * constructor
	 * @param dimx breedte van het terrein
	 * @param dimy lengte van het terrein
	 * @param clazz data type van elke element in het terrein.
	 * @param pointFieldName Naam van de property die de positie bevat van het element in het terrein. 
	 * 	Wordt automatisch ingevuld bij {@link #setField(Point, Object)}
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	public Terrein(int dimx, int dimy, Class<E> clazz, String pointFieldName) {
		this.clazz=clazz;
		try {
			pointField=clazz.getDeclaredField(pointFieldName);
		} catch (NoSuchFieldException noField) {
			try {
				// we proberen 1 niveau hoger
				pointField=clazz.getSuperclass().getDeclaredField(pointFieldName);
			} catch (NoSuchFieldException noField2) {
				throw new RuntimeException("Geen Point veld met naam "+pointFieldName+" gevonden in "+clazz.getName());
			}
		}
		this.terrein = (E[][])Array.newInstance(clazz, dimx, dimy);
		this.dim=new Dimension(dimx,dimy);
	}
	
	public Terrein<E> withOnOutOfBound(OutOfBoundAction onOutOfBound) {
		this.onOutOfBound = onOutOfBound;
		return this;
	}

	public Dimension getDim() {
		return dim;
	}

	/**
	 * Zet value op plaats p
	 * @param p positie. Moet binnen terrein vallen.
	 * @param value nieuwe waarde voor deze positie. De Point in value wordt overschreven.
	 * @return vorige instance op die positie. Kan null zijn bij initialisatie.
	 */
	public E setField(Point p,E value) {
		if(validPoint(p)) {
			E old=terrein[p.x][p.y];
			terrein[p.x][p.y]=value;
			try {
				pointField.set(value, p);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return old;
		}
		return null;
	}
	/**
	 * Geeft de waarde op positie p.
	 * Als p buiten het terrein valt hangt het antwoord af van onOutOfBound waarde:
	 * <li>THROW_ERROR -> exception
	 * <li>IGNORE -> null
	 * @param p
	 * @return
	 */
	public E getField(Point p) {
		if(validPoint(p))
			return terrein[p.x][p.y];
		return null;
	}
	
	/**
	 * controleert of punt binnen terrein valt en reageert afhankelijk van onOutOfBound 
	 * @param p the valideren Point. Mag niet null zijn
	 * @return als onOutOfBound=THROW_ERROR dan ArrayIndexOutOfBoundsException
	 *  anders true als p binnen terrein valt, anders false
	 */
	public boolean validPoint(Point p) {
		if(0>p.x|| 0>p.y||p.x>=dim.width||p.y>=dim.height) {
			if(onOutOfBound==OutOfBoundAction.THROW_ERROR)
				throw new ArrayIndexOutOfBoundsException("Punt "+p+" valt buiten terrein ("+dim.width+","+dim.height+")");
			return false;
		}
		return true;
	}
	public void walk(Consumer<E> consumer) {
		for(int x=0;x<dim.width;x++)
			for(int y=0;y<dim.height;y++)
				consumer.accept(terrein[x][y]);
	}
	
	public Stream<E> walkingStream(Traverser iterable) {
		Spliterator<E> spitr = Spliterators 
              .spliteratorUnknownSize(iterable, Spliterator.NONNULL); 
	    // Convert spliterator into a sequential stream 
	    return StreamSupport.stream(spitr, false);
    }
	
	public Traverser walker() {
		return new Traverser();
	}
	/**
	 * Iterator die over de elementen gaat vertrekkende van (startX,startY).
	 * Eerst volgens firstDir tot aan de rand van terrein, 
	 * Dan 1 verder volgens secDir.
	 * vb: start(0,0), first=RIGHT, sec=DOWN : zoals we een blad lezen van links bovenaan
	 * start (dimx,dimy),first=LEFT,sec=UP : blad lezen van achter naar voor
	 * 
	 */
	public class Traverser implements Iterator<E> {
		boolean isFirst=true;
		Point current;
		// berekend door hasNext, null als er geen is
		Point next;
		Direction firstDir, secDir;
		public Traverser withStart(int startX, int startY) {
			current=new Point(startX,startY);
			return this;
		}
		public Traverser withFirstDirection(Direction firstDir) {
			this.firstDir=firstDir;
			return this;
		}
		public Traverser withSecondDirection(Direction secDir) {
			this.secDir=secDir;
			return this;
		}
		public Stream<E> toStream() {
			Spliterator<E> spitr = Spliterators 
	              .spliteratorUnknownSize(this, Spliterator.NONNULL); 
		    // Convert spliterator into a sequential stream 
		    return StreamSupport.stream(spitr, false);
	    }
		@Override
		public boolean hasNext() {
			if(isFirst) {
				return true;
			}
			next=firstDir.move(current);
			if(validPoint(next))
				return true;
			switch(firstDir) {
				case RIGHT: next.x=0; break;
				case LEFT: next.x=dim.width-1; break;
				case DOWN: next.y=0; break;
				case UP: next.y=dim.height-1; break;
			}
			next=secDir.move(next);
			return validPoint(next);
		}

		@Override
		public E next() {
			if(isFirst) {
				isFirst=false;
			} else {
				current=next;
			}
			return getField(current);
		}		
	}
	
	/**
	 * Geeft element wanneer je verplaatst van current 1 stap in richting dir.
	 * @param current huidige vak binnen terrein
	 * @param dir richting waarin we willen bewegen
	 * @return vak op volgende plaats of null wanneer we aan de rand zijn
	 */
	public E next(E current, Direction dir) {
		Point p=getPointValue(current);
		if(dir.canMove(p, dim)) {
			p=dir.move(p);
			return terrein[p.x][p.y];
		}
		return null;
	}
	private Point getPointValue(E element) {
		try {
			return (Point)pointField.get(element);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("point field not accessible in "+element);
		}
	}	
}
