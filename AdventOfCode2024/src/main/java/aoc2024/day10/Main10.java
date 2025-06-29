package aoc2024.day10;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import common.QueueHelper;
import common.dim2.IntTerrein;
import common.graph.Direction;
import common.graph.Point;
import common.main.AbstractMainMaster;

/**
 * Mogelijke paden vinden van 0 naar 9.
 * Deel 1: som van mogelijke 9 die bereikbaar van 0.
 * Deel 2: aantal mogelijke paden
 */
public class Main10 extends AbstractMainMaster<Integer> {
    public static void main(String[] args) {
        new Main10()
            //.testMode()
            //.withFileOutput()
            .nolog()
           .start();
    }
    IntTerrein map;
    Set<Pad> trails=new HashSet<>();
    int padCounter=0;
    
    QueueHelper<Pad> padQueue=new QueueHelper<>();
    int dimx=48, dimy=48;
    
    @Override
	public void beforeAll() {
    	if(testMode)
    		dimx=dimy=8;
    	map=new IntTerrein(loadInputInto2DIntArrayYX(dimx, dimy));
    	searchTrails();
    }

    // antwoord : 550 
    public Integer star1() {
        return trails.size();
    }
    
    // antwoord : 1255
    public Integer star2() {
        return padCounter;
    }
    
    /**
     * dit berekent het antwoord op star1 en star2:
     * star1: mogelijke combo's van 0 tot 9
     * star2: mogelijke paden van 0 tot 9
     */
    private void searchTrails() {
    	map.scan((v,p)->{
        	if(v==0) {
        		if(log) logln("startpunt: "+p);
        		padQueue.add(new Pad(p,p,0));
        	}
        });
       	padQueue.consume(this::stapPad);
    }
    
    private void stapPad(Pad pad) {
    	if(log) logln("evaluate:"+pad);
    	for(Direction richt:Direction.values()) {
    		Point newp=richt.move(pad.currPos);
    		if(map.at(newp,Integer.MIN_VALUE)==pad.value+1) {
    			Pad newpad=new Pad(pad.startPos,newp,pad.value+1);
				if(log) logln("->"+richt+":"+newpad);
    			if(newpad.value==9) {
    				trails.add(newpad);
    				padCounter++;
    				if(log) logln(newpad);
    			} else
    				padQueue.add(newpad);
    		}
    	}
    }
        
    private static class Pad {
    	Point startPos;
    	Point currPos;
    	int value;
		public Pad(Point startPos, Point currPos, int value) {
			this.startPos = startPos;
			this.currPos = currPos;
			this.value = value;
		}
		@Override
		public String toString() {
			return "Pad [startPos=" + startPos + ", currPos=" + currPos + ", value=" + value + "]";
		}
		@Override
		public int hashCode() {
			return Objects.hash(currPos, startPos);
		}
		@Override
		public boolean equals(Object obj) {
			Pad other = (Pad) obj;
			return Objects.equals(currPos, other.currPos) && Objects.equals(startPos, other.startPos);
		}
		
    }
}