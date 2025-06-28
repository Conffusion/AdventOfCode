package common.dim2;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

import common.graph.Point;

public class DimensionUtils {

	/**
	 * overloop alle elementen van de surface 2D array en voer de processor uit
	 * @param <T> datatype van de 2D array
	 * @param surface te overlopen 2D array
	 * @param processor wordt aangeroepen voor elk vakje
	 */
	public static <T> void walk(T[][] surface,BiFunction<T,Point,T> processor) {
		for (int y=0;y<surface.length;y++) {
			for(int x=0;x<surface[0].length;x++) {
				processor.apply(surface[y][x],new Point(x,y));
			}
		}
	}

	/**
	 * overloop alle elementen van de surface 2D array en voer de processor uit
	 * @param surface te overlopen 2D array
	 * @param processor wordt aangeroepen voor elk vakje
	 */
	public static void walk(char[][] surface,BiConsumer<Character,Point> processor) {
		for (int y=0;y<surface.length;y++) {
			for(int x=0;x<surface[0].length;x++) {
				processor.accept(surface[x][y],new Point(x,y));
			}
		}
	}
	/**
	 * overloop alle elementen van de surface 2D array en voer de processor uit
	 * @param surface te overlopen 2D array waarbij eerste dimensie Y is en tweede X
	 * @param processor wordt aangeroepen voor elk vakje
	 */
	public static void walkYX(char[][] surface,BiConsumer<Character,Point> processor) {
		for (int y=0;y<surface.length;y++) {
			for(int x=0;x<surface[0].length;x++) {
				processor.accept(surface[y][x],new Point(x,y));
			}
		}
	}
	/**
	 * overloop alle elementen van de surface 2D array en voer de processor uit
	 * @param surface te overlopen 2D array waarbij eerste dimensie Y is en tweede X
	 * @param processor wordt aangeroepen voor elk vakje
	 */
	public static void walkYX(int[][] surface,BiConsumer<Integer,Point> processor) {
		for (int y=0;y<surface.length;y++) {
			for(int x=0;x<surface[0].length;x++) {
				processor.accept(surface[y][x],new Point(x,y));
			}
		}
	}

	/**
	 * genereert een Point voor alle elementen in dimensie [dimX][dimY]
	 * Waarbij rij per rij wordt overlopen
	 * @param dimX
	 * @param dimY
	 * @param walker Consumer wordt aangeroepen voor elk gegenereert punt
	 */
	public static void walk(int dimX, int dimY, Consumer<Point> walker) {
		for (int y=0;y<dimY;y++) {
			for(int x=0;x<dimX;x++) {
				walker.accept(new Point(x,y));
			}
		}		
	}
	public static Long walkAndSum(int dimX, int dimY, Function<Point,Long> walker) {
		long som=0;
		for (int y=0;y<dimY;y++) {
			for(int x=0;x<dimX;x++) {
				som+=walker.apply(new Point(x,y));
			}
		}
		return som;
	}
	
	/**
	 * genereert een Point voor alle elementen in dimensie [0-dim.width[,[0-dim.height[
	 * Waarbij rij per rij wordt overlopen
	 * @param dimension
	 * @param walker
	 */
	public static void walk(Dimension dimension,Consumer<Point> walker) {
		walk(dimension.width,dimension.height,walker);
	}
	/**
	 * Doorloopt elk element van array en roept voor elk element check predicate aan.
	 * Als stopOn is bepaald stopt de iteratie vanaf de eerste predicate die deze waarde teruggeeft
	 * @param array input lijst door doorlopen wordt
	 * @param check Predicate met Character (waarde uit array) en index in array
	 * @param stopOn bij null wordt de volledige altijd doorlopen, anders maar tot eerste predicate
	 * die dezelfde waarde teruggeeft
	 * @return als stopOn is gedefinieerd geeft deze het antwoord van de laatste predicate terug
	 */
	public static Boolean testArray(char[] array,BiPredicate<Character,Integer> check,Boolean stopOn) {
		for(int i=0;i<array.length;i++) {
			if(Objects.equals(stopOn, check.test(array[i], i)))
				return stopOn;
		}
		return stopOn==null?null:!stopOn;
	}

	/**
	 * Doorloopt elk element van array en roept voor elk element check predicate aan.
	 * Als stopOn is bepaald stopt de iteratie vanaf de eerste predicate die deze waarde teruggeeft
	 * @param array input lijst door doorlopen wordt
	 * @param check Predicate met Character (waarde uit array) en index in array
	 * @param stopOn bij null wordt de volledige altijd doorlopen, anders maar tot eerste predicate
	 * die dezelfde waarde teruggeeft
	 * @return als stopOn is gedefinieerd geeft deze het antwoord van de laatste predicate terug
	 */
	public static Boolean testArrayRow(char[][] array,int rowNum, BiPredicate<Character,Integer> check,Boolean stopOn) {
		for(int x=0;x<array.length;x++) {
			if(Objects.equals(stopOn, check.test(array[x][rowNum], x)))
				return stopOn;
		}
		return stopOn==null?null:!stopOn;
	}

	public static void fill2Dim(char[][] space,char fill) {
		for(int x=0;x<space.length;x++) {
			Arrays.fill(space[x], fill);
		}
	}
	public static void fill2Dim(int[][] space, int fillValue) {
		for(int x=0;x<space.length;x++)
			Arrays.fill(space[x], fillValue);
	}
	public static void fill2Dim(long[][] space, int fillValue) {
		for(int x=0;x<space.length;x++)
			Arrays.fill(space[x], fillValue);
	}
	public static <T> void fill2Dim(T[] array,Function<Integer, T> builder) {
		for(int i=0;i<array.length;i++)
			array[i]=builder.apply(i);
	}
}
