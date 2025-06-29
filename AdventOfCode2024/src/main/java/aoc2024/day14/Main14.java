package aoc2024.day14;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import common.dim2.DimensionUtils;
import common.dim2.Velocity;
import common.graph.Point;
import common.main.AbstractMainMaster;
import common.regex.RegexMatchBuilder;

public class Main14 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main14()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }
    int robotSeq=0;
    int dimx=101;
    int dimy=103;
    List<Robot> robots;
    Map<Point,Set<Robot>> robotLoc;
    
    @Override
	public void beforeAll() {
		if(testMode) {
			dimx=11;
			dimy=7;
		}		
	}
    
	@Override
    public void beforeEach() {
		robotSeq=0;
		robotLoc=new HashMap<>();
		robots=parseInput(new RegexMatchBuilder("p=([0-9]+),([0-9]+) v=([-0-9]+),([-0-9]+)"), 
				match->new Robot(new Point(match.intGroup(1),match.intGroup(2)),
						new Velocity(match.intGroup(3),match.intGroup(4))));
    }
	
    // antwoord : 209409792
    public Long star1() {
    	int times=100;
        long[] quadrant=new long[4];
        for(Robot r:robots) {
        	r.pos=r.velo.move(r.pos, times);
            r.pos=new Point(r.pos.x%dimx,r.pos.y%dimy);
            if(r.pos.x<0)
            	r.pos.x+=dimx;
            if(r.pos.y<0)
            	r.pos.y+=dimy;
            if(r.pos.x<dimx/2) {
            	if(r.pos.y<dimy/2)
            		quadrant[0]++;
            	else if(r.pos.y>dimy/2)
            		quadrant[2]++;
            }else if(r.pos.x>dimx/2) {
            	if(r.pos.y<dimy/2)
            		quadrant[1]++;
            	else if(r.pos.y>dimy/2)
            		quadrant[3]++;
            }
        }        
        return quadrant[0]*quadrant[1]*quadrant[2]*quadrant[3];
    }

    // antwoord : 
    public Long star2() {
    	int baseLength=8; // verwacht aantal robots op 1 rij
        int times=100000;
        long it=0;
        while(true) {
        	if(it++>=times)
        		break;
	        for(Robot r:robots) {
	        	Point newpos=r.velo.move(r.pos);
	        	newpos=new Point(newpos.x%dimx,newpos.y%dimy);
	            if(newpos.x<0)
	            	newpos.x+=dimx;
	            if(newpos.y<0)
	            	newpos.y+=dimy;
	            r.moveTo(newpos);
	        }
	        // check op kerstboom
	        robotLoop:
	        for(Robot r:robots) {
	        	Point p=new Point(r.pos);
	        	for (int i=0;i<baseLength;i++) {
	        		if(!hasRobotAtPos(p))
	        			continue robotLoop;
       				p.x++;
       				if(p.x>=dimx)
       					continue robotLoop;
	        	}
	        	// als we hier geraken hebben we genoeg robots op 1 rij gevonden
	        	printMap();
	        	return it;
	        }
        }
        return -1L;

    }
    private void printMap() {
    	DimensionUtils.walk(new Dimension(dimx,dimy), p->{
    		if(p.x==0)
    			System.out.println();
    		System.out.print(hasRobotAtPos(p)?"#":".");
    	});
    }
    
    private class Robot {
    	int id;
    	private Point pos;
    	Velocity velo;
		public Robot(Point pos, Velocity velo) {
			this.pos = pos;
			this.velo = velo;
			this.id=++robotSeq;
			robotLoc.computeIfAbsent(pos, p->new HashSet<Robot>()).add(this);
		}
		
		public void moveTo(Point pos) {
			Point old=this.pos;
			this.pos = pos;
			robotMoved(this,old,pos);
		}
		@Override
		public String toString() {
			return "Robot-"+id+" [" + pos + ", velo=" + velo + "]";
		}
		@Override
		public int hashCode() {
			return 31+Objects.hash(id);
		}
		@Override
		public boolean equals(Object obj) {
			Robot other = (Robot) obj;
			return id == other.id;
		}
    }
    private void robotMoved(Robot r, Point oldPos, Point newPos) {
    	robotLoc.computeIfAbsent(oldPos, p->new HashSet<Robot>()).remove(r);
    	robotLoc.computeIfAbsent(newPos, p->new HashSet<Robot>()).add(r);
    }
    private boolean hasRobotAtPos(Point pos) {
    	Set<Robot> list=robotLoc.get(pos);
    	return CollectionUtils.isNotEmpty(list);
    }
}