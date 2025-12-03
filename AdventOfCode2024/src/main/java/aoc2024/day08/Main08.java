package aoc2024.day08;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.dim2.CharTerrein;
import common.graph.Point;
import common.main.AbstractMainMaster;

/**
 * 2 antennes met zelfde teken, zorgen voor 2 'antinodes' in het verlengde van deze antennes in beide richtingen.
 * Dit kan ook een diagonale lijn zijn (niet noodzakelijk 45°)
 * Op de plaats van een antenne kan een antinode van een andere antenne staan.
 */
public class Main08 extends AbstractMainMaster<Integer> {
    public static void main(String[] args) {
        new Main08()
            //.testMode()
            .nolog()
           .start();
    }
    CharTerrein terrein;
    Set<Point> antinodes;
    // bevat van elk type antenne, alle locaties
    Map<Character,List<Point>> antennes;
    
    int dimx=50, dimy=50;
    
    @Override
	public void beforeAll() {
		if(testMode)
			dimx=dimy=12;
		super.beforeAll();
	}

	@Override
    public void beforeEach() {
    	terrein=new CharTerrein(loadInputInto2DArray(dimx, dimy));
    	antennes=new HashMap<>();
    	terrein.scan((a,p)->antennes.computeIfAbsent(a, _->new ArrayList<Point>()).add(p));
    	antennes.remove('.'); // dit is geen antenne
    	antinodes=new HashSet<>();	
    }

    // Hoeveel unieke antinodes zijn er
    // antwoord : 423
    public Integer star1() {
        antennes.values().forEach(plist->bepaalAntinodes(plist,false));
        return antinodes.size();
    }
    
    /**
     * Berekent alle antinodes voor een set van antennes van dezelfde soort
     * @param antenneptn
     * @param repeat als waar blijven we antinodes in verlengde toevoegen tot buiten terrein
     */
    private void bepaalAntinodes(List<Point> antenneptn,boolean repeat) {    	
    	for(Point p1:antenneptn) {
    		if(repeat) {
	        	// bij repeat zijn alle antennes ook antinodes
	    		antinodes.add(p1);
    		}
    		for(Point p2:antenneptn) {
    			if(p1.equals(p2))
    				continue;
    			// point in verlengde p1->p2->#
    			int deltax=p2.x-p1.x;
    			int deltay=p2.y-p1.y;
    			Point anti=new Point(p2.x+deltax,p2.y+deltay);
				while(terrein.isIn(anti)) {
					antinodes.add(anti);
	    			if(!repeat) 
	    				break;
					anti=new Point(anti.x+deltax,anti.y+deltay);
				}
    		}
    	}
    	logln("tussenstand:"+antinodes.size());
    }
    
    // antwoord : 1287
    public Integer star2() {
    	antennes.values().forEach(plist->bepaalAntinodes(plist,true));
        return antinodes.size();
    }
}