package aoc2023.day16;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Queue;

import common.dim2.DimensionUtils;
import common.graph.Direction;
import common.graph.Vector;
import common.main.AbstractMainMaster;

public class Main16 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main16()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }

    Dimension dim;
    char[][] grid;
    Spot[][] spotGrid;
    
    @Override
    public void beforeEach() {
    	dim=testMode?new Dimension(10,10):new Dimension(110,110);
    	grid=loadInputInto2DArrayXY(dim.width, dim.height);
    }
    Queue<Vector> beamQueue=new LinkedList<>();
    
	long energized=0;
    // antwoord : 7067
    public Long star1() {
    	return countEnergized(new Vector(0,0,Direction.RIGHT));
    }

    // antwoord : 7324
    public Long star2() {
    	long max=0;
    	for (int x=0;x<dim.width;x++) {
    		max=Math.max(max, countEnergized(new Vector(x,0,Direction.DOWN)));
    		max=Math.max(max, countEnergized(new Vector(x,dim.height-1,Direction.UP)));
    	}
   		for (int y=0;y<dim.height;y++) {
    		max=Math.max(max, countEnergized(new Vector(0,y,Direction.RIGHT)));
    		max=Math.max(max, countEnergized(new Vector(dim.width-1,y,Direction.LEFT)));
   		}
        return max;
    }

    public Long countEnergized(Vector start) {
    	spotGrid=new Spot[dim.width][dim.height];
    	DimensionUtils.walk(grid, (c,p)->{spotGrid[p.x][p.y]=new Spot();});
    	energized=0;
    	beamQueue.add(start);
    	Vector current=null;
    	while((current=beamQueue.poll())!=null) {
    		beamLoop:
    		while(true) {
    			if(spotGrid[current.point.x][current.point.y].visitedFrom[current.dir.ordinal()]) {
    				// we zijn hier al geweest vanuit de richting van de beam
    				break beamLoop;
    			}
    			spotGrid[current.point.x][current.point.y].visitedFrom[current.dir.ordinal()]=true;
    			current=evaluateSpot(current);
    			current.forward();
    			if(!current.onMap(dim))
    				break beamLoop;
    		}
    	}
    	DimensionUtils.walk(grid, (s,p)->{
    		if(spotGrid[p.x][p.y].visitedCount()>0)
    			energized++;
    	});
    	
        return energized;
    	
    }
    
    /**
     * onderzoekt hoe de huidige positie de beam zal doen buigen afhankelijk
     * wat er op de huidige spot is
     * @param beam
     */
    public Vector evaluateSpot(Vector beam) {
		switch (grid[beam.point.x][beam.point.y]) {
		case '/': {
			if(beam.dir==Direction.UP ||beam.dir==Direction.DOWN)
				beam.turnRight();
			else
				beam.turnLeft();
			}
			break;
		case '\\': {
			if(beam.dir==Direction.UP ||beam.dir==Direction.DOWN)
				beam.turnLeft();
			else
				beam.turnRight();
			}
			break;
		case '-': if(beam.dir==Direction.UP||beam.dir==Direction.DOWN) {
			// split the beam
			Vector newbeam=new Vector(beam).turnLeft().forward();
			if(newbeam.onMap(dim))
				beamQueue.add(newbeam);
			beam.turnRight();
		}
		break;
		case '|': if(beam.dir==Direction.LEFT||beam.dir==Direction.RIGHT) {
			// split the beam
			Vector newbeam=new Vector(beam).turnLeft().forward();
			if(newbeam.onMap(dim))
				beamQueue.add(newbeam);
			beam.turnRight();
		}
		break;
		}
		return beam;
    	
    }
      
    public static class Spot {
    	// volgens ordinal value van Direction geeft dit aan of de spot al bezocht
    	// is door een straal uit die richting
    	boolean[]visitedFrom=new boolean[4];
    	public int visitedCount() {
			return (visitedFrom[0] ? 1 : 0) 
					+ (visitedFrom[1] ? 1 : 0) 
					+ (visitedFrom[2] ? 1 : 0)
					+ (visitedFrom[3] ? 1 : 0);
    	}
		@Override
		public String toString() {
			return "["+(visitedFrom[0] ? "#" : ".") 
					+ (visitedFrom[1] ? "#" : ".") 
					+ (visitedFrom[2] ? "#" : ".")
					+ (visitedFrom[3] ? "#" : ".")+"]";
		}
    	
    }
}