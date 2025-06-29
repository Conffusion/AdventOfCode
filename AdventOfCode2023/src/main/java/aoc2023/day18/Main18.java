package aoc2023.day18;

import java.awt.Dimension;
import java.util.List;

import common.QueueHelper;
import common.dim2.DimensionUtils;
import common.graph.Direction;
import common.graph.Point;
import common.main.AbstractMainMaster;
import common.regex.RegexMatch;
import common.regex.RegexMatchBuilder;

public class Main18 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main18()
            //.testMode()
            .withFileOutput()
            //.nolog()
           .start();
    }

    static class DigInstruction {
    	Direction dir;
    	long distance;
		public DigInstruction(Direction dir, long distance) {
			this.dir = dir;
			this.distance = distance;
		}
    }
    
    private DigInstruction parseLine1(RegexMatch match) {
    	Direction dir=switch(match.group(1)) {
    	case "R"->Direction.RIGHT;
    	case "U"->Direction.UP;
    	case "L"->Direction.LEFT;
    	case "D"->Direction.DOWN;
    	default->null;
    	};
    	return new DigInstruction(dir,match.intGroup(2));
    }
    
    List<DigInstruction> instructions;
    Dimension dim;
    Point start;
    
    static final RegexMatchBuilder builder1=new RegexMatchBuilder("([RLDU]) ([0-9]+) \\(#([a-z0-9]{6})\\)", true);
    static final RegexMatchBuilder builder2=new RegexMatchBuilder("[RLDU] [0-9]+ \\(#([a-z0-9]{5})([0-9])\\)", true);
    
    class Hole {
    	boolean digged=false;
    	public Hole dig() {
    		if(!digged) {
    			digged=true;
    			digCounter++;
    		}
    		return this;
    	}
		@Override
		public String toString() {
			return digged?"#":".";
		}
    	
    }
    Hole[][] map;
    long digCounter=0;
    QueueHelper<Point> queue=new QueueHelper<>();

    // antwoord : 61865
    public Long star1() {
    	instructions= streamInput(builder1::evaluate)
    	.map(this::parseLine1)
    	.toList();
    	// calculate dimensions
    	int xmin=0,xmax=0,ymin=0,ymax=0;
    	int xcurr=0,ycurr=0;
    	for(DigInstruction ins:instructions) {
    		switch (ins.dir) {
    		case RIGHT: xcurr+=ins.distance; break;
    		case DOWN: ycurr+=ins.distance; break;
    		case LEFT: xcurr-=ins.distance; break;
    		case UP: ycurr-=ins.distance;break;
    		}
    		xmin=Math.min(xmin, xcurr);
    		xmax=Math.max(xmax, xcurr);
    		ymin=Math.min(ymin, ycurr);
    		ymax=Math.max(ymax, ycurr);
    	}
    	dim=new Dimension(xmax-xmin+1,ymax-ymin+1);
    	System.out.println("Nodige dimensies:x["+xmin+","+xmax+"]-y["+ymin+","+ymax+"]");
    	start=new Point(-xmin,-ymin);
    	
    	map=new Hole[dim.height][dim.width];
    	DimensionUtils.walk(map, (h,p)->map[p.y][p.x]=new Hole());
    	digCounter=0;
    	Point curr=new Point(start);
    	for(DigInstruction ins:instructions) {
    		for (int i=0;i<=ins.distance;i++) {
    			if (i>0)
    				curr=ins.dir.move(curr);
	    		switch (ins.dir) {
		    		case RIGHT:
		    		case LEFT: map[curr.y][curr.x].dig(); break;
		    		case DOWN:
		    		case UP: map[curr.y][curr.x].dig(); break;
	    		}
    		}
    	}
    	printMap();
    	logln("\n------------------------");
    	xcurr=0;
    	ycurr=dim.height/2;
    	while(!map[ycurr][xcurr].digged) xcurr++;
    	while(map[ycurr][xcurr].digged)xcurr++;
    	System.out.println("Starting point for filling: "+xcurr+","+ycurr);
    	queue.add(new Point(xcurr,ycurr));
    	queue.consume(this::fillInterior);
    	printMap();
        return digCounter;
    }
    
    void fillInterior(Point p) {
    	if(map[p.y][p.x].digged)
    		return;
    	map[p.y][p.x].dig();
    	if(p.x>0)
    		queue.add(new Point(p.x-1,p.y));
    	if(p.y<dim.height-1)
    		queue.add(new Point(p.x,p.y+1));
    	if(p.y>0)
    		queue.add(new Point(p.x,p.y-1));
    	if(p.x<dim.width-1)
    		queue.add(new Point(p.x+1,p.y));
    }
 
    private DigInstruction parseLine2(RegexMatch match) {
    	Direction dir=switch(match.group(2)) {
    	case "0"->Direction.RIGHT;
    	case "3"->Direction.UP;
    	case "2"->Direction.LEFT;
    	case "1"->Direction.DOWN;
    	default->null;
    	};
    	return new DigInstruction(dir,Long.parseLong(match.group(1),16));
    }

    // antwoord : 
    public Long star2() {
    	instructions= streamInput(builder2::evaluate)
    	.map(this::parseLine2)
    	.toList();
    	// calculate dimensions
    	int xmin=0,xmax=0,ymin=0,ymax=0;
    	int xcurr=0,ycurr=0;
    	for(DigInstruction ins:instructions) {
    		switch (ins.dir) {
    		case RIGHT: xcurr+=ins.distance; break;
    		case DOWN: ycurr+=ins.distance; break;
    		case LEFT: xcurr-=ins.distance; break;
    		case UP: ycurr-=ins.distance;break;
    		}
    		xmin=Math.min(xmin, xcurr);
    		xmax=Math.max(xmax, xcurr);
    		ymin=Math.min(ymin, ycurr);
    		ymax=Math.max(ymax, ycurr);
    	}
    	dim=new Dimension(xmax-xmin+1,ymax-ymin+1);
    	System.out.println("Nodige dimensies:x["+xmin+","+xmax+"]-y["+ymin+","+ymax+"]:"+dim);
    	start=new Point(-xmin,-ymin);
    	
    	map=new Hole[dim.height][dim.width];
    	DimensionUtils.walk(map, (h,p)->map[p.y][p.x]=new Hole());
    	digCounter=0;
    	Point curr=new Point(start);
    	for(DigInstruction ins:instructions) {
    		for (int i=0;i<=ins.distance;i++) {
    			if (i>0)
    				curr=ins.dir.move(curr);
	    		switch (ins.dir) {
		    		case RIGHT:
		    		case LEFT: map[curr.y][curr.x].dig(); break;
		    		case DOWN:
		    		case UP: map[curr.y][curr.x].dig(); break;
	    		}
    		}
    	}
    	printMap();
    	logln("\n------------------------");
    	xcurr=0;
    	ycurr=dim.height/2;
    	while(!map[ycurr][xcurr].digged) xcurr++;
    	while(map[ycurr][xcurr].digged)xcurr++;
    	System.out.println("Starting point for filling: "+xcurr+","+ycurr);
    	queue.add(new Point(xcurr,ycurr));
    	queue.consume(this::fillInterior);
    	printMap();
        return digCounter;
    }
    
    private void printMap() {
    	DimensionUtils.walk(map, (h,p)-> {if(p.x==0) writeToFile("\n"); writeToFile(""+map[p.y][p.x]);return h;});
    }
}