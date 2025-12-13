package aoc2025.day09;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalLong;

import common.graph.LPoint;
import common.io.Loader;
import common.main2.AoCLogger;
import common.main2.AoCRunner;
import common.main2.AoCSolver;

public class Solver09 extends AoCSolver<Long> {
	static AoCLogger logger=new AoCLogger();
	static boolean testMode=false;
	
	public static void main (String[] args) {
		//logger.nolog(true);
		AoCRunner<Long> runner=new AoCRunner<>(new Solver09());
		logger.nolog(false);
		if(testMode)
			runner.testMode();
		runner.start();
	}
	Comparator<OptionalLong> customComparator = Comparator.comparing(ol -> ol.getAsLong());
	
    /**
     * Bereken de grootste rechthoekige oppervlakte met 2 punten als overliggende hoekpunten.
     */
    // antwoord : 4763509452
    public Long star1() {
    	Loader loader=Loader.forMain(this).withTestMode(testMode).build();
    	List<LPoint> input=loader.parseInput(l->new LPoint(Long.parseLong(l.split(",")[0]),Long.parseLong(l.split(",")[1])));
        return input.stream().map(p1->
        	input.stream()
        		.filter(p2->!p2.equals(p1))
        		.mapToLong(p2->(Math.abs(p1.x-p2.x)+1)*(Math.abs(p1.y-p2.y)+1))
        		.max()).mapToLong(ol->ol.getAsLong()).max().getAsLong();
    }

    /**
     * Vorm een gesloten veelhoek met de gegeven punten.
     * Welke grootste rechthoek past volledig binnen de gecreeerde vorm.
     */
    // antwoord : 1516897893
    public Long star2() {
    	Loader loader=Loader.forMain(this).withTestMode(testMode).build();
   	 	List<Point> input=loader.parseInput(l->new Point(Integer.parseInt(l.split(",")[0]),Integer.parseInt(l.split(",")[1])));
        Polygon polygon=new Polygon();
        for(Point p:input) {
        	polygon.addPoint(p.x, p.y);
        }
        return input.stream().map(p1->
    	input.stream()
    		.filter(p2->!p2.equals(p1))
    		.map(p2->toRectangle(p1,p2))
    		.filter(rect->polygon.contains(rect))
    		// terug originele grootte van rechthoek gebruiken om de oppervlakte te berekenen
    		.mapToLong(rect->(long)(rect.getWidth()+2)*(long)(rect.getHeight()+2))
    		.max()).mapToLong(ol->ol.getAsLong()).max().getAsLong();
    }
    
    private Rectangle2D toRectangle(Point p1, Point p2) {
    	Rectangle2D rect=new Rectangle2D.Double();
	// spijtig: Polygon.contains beschouwt de eigen randen niet als deel van de polygoon
	// we maken onze Rectangle dan 1 punt kleiner in alle richtingen en controleren dan de contains()
		rect.setRect(Math.min(p1.x, p2.x)+1, Math.min(p1.y, p2.y)+1, Math.abs(p1.x-p2.x)-1, Math.abs(p1.y-p2.y)-1);
		return rect;
	}    	
}
