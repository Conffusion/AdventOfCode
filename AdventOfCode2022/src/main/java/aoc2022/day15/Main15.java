package aoc2022.day15;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.graph.Point;
import common.main.AbstractMainMaster;
import common.regex.RegexTool;

/**
 * Landschap van sensors en bakens. Waar is de plek die niet door een sensor wordt gelezen?
 * @author walter
 *
 */
public class Main15 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main15()
            //.testMode()
            //.nolog()
           .start();
    }

    List<Sensor> sensors=new ArrayList<>();
    Set<Point> beacons=new HashSet<>();
    int xMin,xMax, yMin, yMax;
    int targetRow=2000000;
    int maxDistress=4000000;
    RegexTool inputPhrase=new RegexTool("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)",true);
    
    @Override
	public void beforeAll() {
    	streamInput().forEach(this::parseLine);
    	System.out.println("Dimensies: ("+xMin+","+yMin+") * ("+xMax+","+yMax+")");
    }

    private void parseLine(String line) {
    	inputPhrase.evaluate(line);
    	Point pSensor=new Point(inputPhrase.intGroup(1),inputPhrase.intGroup(2));
    	Point pBeacon=new Point(inputPhrase.intGroup(3),inputPhrase.intGroup(4));
    	Sensor s=new Sensor(pSensor,pBeacon);
    	xMin=Math.min(xMin, pSensor.x-s.distance);
    	xMax=Math.max(xMax, pSensor.x+s.distance);
    	yMin=Math.min(yMin, pSensor.y-s.distance);
    	yMax=Math.max(yMax, pSensor.y+s.distance);
    	sensors.add(s);
    	beacons.add(pBeacon);
    }
    
	@Override
    public void beforeEach() {
		if(testMode) {
			targetRow=10;
			maxDistress=20;
		}
    }

	// answer: 4811413
    public Long star1() {
    	long counter=0;
    	for (int x=xMin;x<xMax;x++) {
			Point xpoint=new Point(x,targetRow);
			if(sensors.stream().anyMatch(s->s.inReach(xpoint)))
				counter++;
    	}
    	logln("beacons op rij "+targetRow+":"+beacons.stream().filter(b->b.y==targetRow).count());
        return counter-beacons.stream().filter(b->b.y==targetRow).count();
    }

    public Long star2() {
    	int x=0,y=0;
		boolean reach=false;
    	while(y<=maxDistress) {
    		if(y%100==0)
    			log("*");
    		if(y%10000==0)
    			logln(":"+y);
    		x=0;
			int newx=0;
    		while (x<=maxDistress) {
    			Point p=new Point(x,y);
    			reach=false;
    			newx=x;
    			for(Sensor s:sensors) {
    				if(s.inReach(p)) {
    					reach=true;
       					newx=Math.max(newx, s.distance-Math.abs(y-s.position.y)+s.position.x);
    				}
    			}
    			if(reach) {
    				if(newx>x)
    					x=newx;
    				else
    					x++;
    			} else
    				return 4000000L*x+y;
    		}
    		y++;
    	}
    	return 0L;
    }
    
    class Sensor {
    	Point position;
    	Point beaconPos;
    	int distance;
		public Sensor(Point position, Point beaconPos) {
			this.position = position;
			this.beaconPos = beaconPos;
			distance=distance(position,beaconPos);
		}
		boolean inReach(Point p) {
			return distance(position,p)<=distance;
		}
    }
    
    // Manhattan distance
    int distance(Point p1, Point p2) {
    	return Math.abs(p1.x-p2.x)+Math.abs(p1.y-p2.y);
    }
}