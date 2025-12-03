package aoc2024.day06;

import java.awt.Dimension;

import common.dim2.CharTerrein;
import common.dim2.Walker;
import common.graph.Direction;
import common.graph.Point;
import common.main.AbstractMainMaster;

public class Main06 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
    	new Main06()
            //.testMode()
            //.withFileOutput()
            //.nolog()
        	.start();
    }
    static final char ROTS='#';
    static final char GRAS='.';
    static final char PAD='X'; // gras waar op gewandeld is
    static final char GUARD='^'; // enkel in opgave, we vervangen het door X bij aanvang
    int dimx = 131, dimy=131;
    CharTerrein inputTerrein;
    Walker inputGuard;
    CharTerrein terrein;
    Walker guard;
    
	@Override
    public void beforeEach() {
    	if(testMode) {
    		dimx=dimy=10;
    	}
    	inputTerrein=new CharTerrein(loadInputInto2DArray(dimx, dimy));
    	// zoek de guard
    	inputTerrein.scan((c,p)->{
    		if(c==GUARD) 
    			inputGuard=new Walker(p,Direction.UP,new Dimension(dimx,dimy));
    	});
    	inputTerrein.setValue(inputGuard.getPos(),PAD);
    	inputGuard.withAccessibleRule(this::checkAccess);
    }
	public Boolean checkAccess(Point p, Walker w) {
		return terrein.at(p)!=ROTS;
	}

    // antwoord : 4973
    public Long star1() {
    	terrein=inputTerrein;
    	guard=inputGuard;
    	while(true) {
    		switch(guard.canForward()) {
    		case OUT_OF_BOUND: return terrein.scanAndSum((c,_)->c==PAD?1L:0L);
    		case OBSTACLE:
    			guard.setDirection(guard.getDir().turnRight()); break;
    		case POSSIBLE:
    			terrein.setValue(guard.moveForward(), PAD);
    		}
    	}
        
    }
    /**
     * Als ons pad zo lang is betekent het dat we in een loop aan het wandelen zijn en mogen we stoppen.
     */
    long maxPadSize;
    
    /**
     * Waar kunnen we 1 obstakel toevoegen zodat de guard in een loop blijft lopen en nooit de rand raakt ?
     * Hoeveel van die punten zijn er?
     */
    // antwoord : 1482
    public Long star2() {
    	maxPadSize=dimx*dimy;
    	return inputTerrein.scanAndSum((_,p)->zetOpstakelEnTest(p));
    }
    /**
     * Geeft 1 terug als ons pad langer wordt dan maxPadSize (en we dus in een loop lopen), anders 0.
     * @param p
     * @return
     */
    public long zetOpstakelEnTest(Point p) {
    	terrein=inputTerrein.clone();
    	guard=inputGuard.clone();
    	terrein.setValue(p, ROTS);
    	long padTeller=0;
    	while(true) {
    		switch(guard.canForward()) {
    		case OUT_OF_BOUND: return 0;
    		case OBSTACLE:
    			guard.setDirection(guard.getDir().turnRight()); break;
    		case POSSIBLE:
    			terrein.setValue(guard.moveForward(), PAD);
    			padTeller++;
        		if(padTeller>maxPadSize)
        			return 1;
    		}
    	}    	
    }
}