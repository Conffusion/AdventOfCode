package aoc2022.day22;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import common.dim2.DimensionUtils;
import common.graph.Direction;
import common.graph.Point;
import common.graph.Vector;
import common.main.AbstractMainMaster;

public class Main22 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        
    	new Main22()
            //.testMode()
            //.nolog()
           .start();
    }
    
    char[][] space;
    int dimX, dimY;
    String instructions[];
    
    @Override
    public void beforeEach() {
    	List<String> lines=loadInputByLine();
    	dimX=lines.stream().mapToInt(String::length).max().getAsInt();
    	dimY=lines.size();
    	space=new char[dimX][dimY];
    	DimensionUtils.fill2Dim(space, ' ');
    	int row=0;
    	for(String line:lines) {
    		parseLine(line,row++);
    	}
    	instructions=(testMode?loadInputToString("testinstructions.txt"):    		
    	loadInputToString("instructions.txt"))
    	.replace("R", ",R,").replace("L", ",L,").split(",");
    }

    private void parseLine(String line,int rowNum) {
    	char[] chars=line.toCharArray();
    	for(int x=0;x<chars.length;x++) {
    		space[x][rowNum]=chars[x];
    	}
    }
    
    public Long star1() {
    	Vector currentPos;
    	int x=0;
    	while(space[x][0]==' ') x++;
    	currentPos=new Vector(Direction.RIGHT,new Point(x,0));
    	boolean readDistance=true;
    	logln("start "+currentPos);
    	for(String instr:instructions) {
    		if(readDistance) {
    			moveForward1(currentPos,Integer.parseInt(instr));
    		} else {
    			if(instr.equals("R"))
    				turnRight(currentPos);
    			else if (instr.equals("L"))
    				turnLeft(currentPos);
    		}
    		logln(instr+"  :  "+currentPos);
    		readDistance=!readDistance;    			
    	}
        return 1000L * (currentPos.point.y+1)+4*(currentPos.point.x+1)+dirToValue(currentPos.dir);
    }

    private int dirToValue(Direction dir) {
    	switch (dir) {
    	case RIGHT:
    		return 0;
    	case DOWN:
    		return 1;
    	case LEFT:
    		return 2;
    	case UP:
    		return 3;
    	}
    	return 0;
    }
    private void moveForward1(Vector current,int distance) {
    	Point newPos=current.point;
    	for (int i=0;i<distance;i++) {
    		switch(current.dir) {
    		case RIGHT:
    			if(newPos.x==dimX-1||space[newPos.x+1][newPos.y]==' ')
    			{
    				// we moeten van links af op zoek naar de eerste plaats binnen doolhof
    				int x=0;
    				while (space[x][newPos.y]==' ') x++;
    				// kunnen we staan op deze eerste plaats ?
    				if(space[x][newPos.y]=='.')
    					newPos.x=x;
    			} else if(space[newPos.x+1][newPos.y]=='.')
    				newPos.x++;
    			break;
    		case LEFT:
    			if(newPos.x==0||space[newPos.x-1][newPos.y]==' ')
    			{
    				// we moeten van rechts af op zoek naar de eerste plaats binnen doolhof
    				int x=space.length-1;
    				while (space[x][newPos.y]==' ') x--;
    				// kunnen we staan op deze eerste plaats ?
    				if(space[x][newPos.y]=='.')
    					newPos.x=x;
    			} else if (space[newPos.x-1][newPos.y]=='.')
    				newPos.x--;
    			break;
    		case DOWN:
    			if(newPos.y==dimY-1||space[newPos.x][newPos.y+1]==' ') {
    				int y=0;
    				while(space[newPos.x][y]==' ') y++;
    				if(space[newPos.x][y]=='.')
    					newPos.y=y;
    			} else if(space[newPos.x][newPos.y+1]=='.')
    				newPos.y++;
    			break;
    		case UP:
    			if(newPos.y==0||space[newPos.x][newPos.y-1]==' ') {
    				int y=space[newPos.x].length-1;
    				while(space[newPos.x][y]==' ') y--;
    				if(space[newPos.x][y]=='.')
    					newPos.y=y;
    			} else if(space[newPos.x][newPos.y-1]=='.')
    				newPos.y--;
    			break;
    		}
    	}
    }
    
    private void turnRight(Vector curr) {
    	switch(curr.dir) {
    	case DOWN:
    		curr.dir=Direction.LEFT;
    		break;
    	case UP:
    		curr.dir=Direction.RIGHT;
    		break;
    	case LEFT:
    		curr.dir=Direction.UP;
    		break;
    	case RIGHT:
    		curr.dir=Direction.DOWN;
    	}
    }
    private void turnLeft(Vector curr) {
    	switch(curr.dir) {
    	case DOWN:
    		curr.dir=Direction.RIGHT;
    		break;
    	case UP:
    		curr.dir=Direction.LEFT;
    		break;
    	case LEFT:
    		curr.dir=Direction.DOWN;
    		break;
    	case RIGHT:
    		curr.dir=Direction.UP;
    	}
    }
    // antwoord: 95316
    public Long star2() {
    	fillBorderMap();
    	Vector currentPos;
    	int x=0;
    	while(space[x][0]==' ') x++;
    	currentPos=new Vector(Direction.RIGHT,new Point(x,0));
    	boolean readDistance=true;
    	logln("start "+currentPos);
    	for(String instr:instructions) {
    		if(readDistance) {
    			currentPos=moveForward2(currentPos,Integer.parseInt(instr));
    		} else {
    			if(instr.equals("R"))
    				turnRight(currentPos);
    			else if (instr.equals("L"))
    				turnLeft(currentPos);
    		}
    		logln(instr+"  :  "+currentPos);
    		readDistance=!readDistance;    			
    	}
        return 1000L * (currentPos.point.y+1)+4*(currentPos.point.x+1)+dirToValue(currentPos.dir);
    }
    /**
     * Voor ster2 moet de map gezien worden als een ontwikkelde kubus.
     * Als we aan een rand komen gebruiken we borderMap om te weten waar de map terug binnenkomen
     * @param current
     * @param distance
     */
    private Vector moveForward2(Vector current,int distance) {
    	Point newPos=current.point;
    	for (int i=0;i<distance;i++) {
    		switch(current.dir) {
    		case RIGHT:
    			if(newPos.x==dimX-1||space[newPos.x+1][newPos.y]==' ')
    			{
    				// we zijn aan de rand van een vlak
    				Vector next=fetchCorner(current);
    				if (space[next.point.x][next.point.y]=='#')
    					// het eerste vak op het nieuwe vlak is een muur, dus stoppen
    					return current;
    				// current overschrijven want dir kan veranderd zijn
    				current=new Vector(next);
    				newPos=current.point;
    			} else if(space[newPos.x+1][newPos.y]=='.')
    				newPos.x++;
    			break;
    		case LEFT:
    			if(newPos.x==0||space[newPos.x-1][newPos.y]==' ')
    			{
    				// we zijn aan de rand van een vlak
    				Vector next=fetchCorner(current);
    				if (space[next.point.x][next.point.y]=='#')
    					// het eerste vak op het nieuwe vlak is een muur, dus stoppen
    					return current;
    				// current overschrijven want dir kan veranderd zijn
    				current=new Vector(next);
    				newPos=current.point;
    			} else if (space[newPos.x-1][newPos.y]=='.')
    				newPos.x--;
    			break;
    		case DOWN:
    			if(newPos.y==dimY-1||space[newPos.x][newPos.y+1]==' ') {
    				// we zijn aan de rand van een vlak
    				Vector next=fetchCorner(current);
    				if (space[next.point.x][next.point.y]=='#')
    					// het eerste vak op het nieuwe vlak is een muur, dus stoppen
    					return current;
    				// current overschrijven want dir kan veranderd zijn
    				current=new Vector(next);
    				newPos=current.point;
    			} else if(space[newPos.x][newPos.y+1]=='.')
    				newPos.y++;
    			break;
    		case UP:
    			if(newPos.y==0||space[newPos.x][newPos.y-1]==' ') {
    				// we zijn aan de rand van een vlak
    				Vector next=fetchCorner(current);
    				if (space[next.point.x][next.point.y]=='#')
    					// het eerste vak op het nieuwe vlak is een muur, dus stoppen
    					return current;
    				// current overschrijven want dir kan veranderd zijn
    				current=new Vector(next);
    				newPos=current.point;
    			} else if(space[newPos.x][newPos.y-1]=='.')
    				newPos.y--;
    			break;
    		}
    	}
    	return current;
    }

    
    Function<Point,Point> yPlus1=p->new Point(p.x,p.y+1);
    Function<Point,Point> yMin1=p->new Point(p.x,p.y-1);
    Function<Point,Point> xPlus1=p->new Point(p.x+1,p.y);
    Function<Point,Point> xMin1=p->new Point(p.x-1,p.y);
    
    Map<Vector,Vector> borderMap=new HashMap<>();

    private Vector fetchCorner(Vector from) {
    	Vector other=borderMap.get(from);
    	if(other==null)
    		throw new IllegalArgumentException("Positie "+from+" is niet op een rand");
    	return other;
    }
    
    private void fillBorderMap() {
    	addBorder(new Vector(Direction.LEFT,new Point(50,0)),new Vector(Direction.RIGHT,new Point(0,149)),yPlus1,yMin1); //1
    	addBorder(new Vector(Direction.UP,new Point(50,0)),new Vector(Direction.RIGHT,new Point(0,150)),xPlus1,yPlus1); //2
    	addBorder(new Vector(Direction.UP,new Point(100,0)),new Vector(Direction.UP,new Point(0,199)),xPlus1,xPlus1); //3
    	addBorder(new Vector(Direction.RIGHT,new Point(149,0)),new Vector(Direction.LEFT,new Point(99,149)),yPlus1,yMin1); //4
    	addBorder(new Vector(Direction.DOWN,new Point(100,49)),new Vector(Direction.LEFT,new Point(99,50)),xPlus1,yPlus1); //5
    	addBorder(new Vector(Direction.DOWN,new Point(50,149)),new Vector(Direction.LEFT,new Point(49,150)),xPlus1,yPlus1); //6
    	addBorder(new Vector(Direction.LEFT,new Point(50,50)),new Vector(Direction.DOWN,new Point(0,100)),yPlus1,xPlus1); //7
    }
    
    private void addBorder(Vector from, Vector to, Function<Point,Point> walkFrom,Function<Point,Point> walkTo) {
    	// from -> to
    	Vector f=from;
    	Vector t=to;
    	for (int i=0;i<50;i++) {
    		borderMap.put(f, t);
    		f=new Vector(f.dir,walkFrom.apply(f.point));
    		t=new Vector(t.dir,walkTo.apply(t.point));
    	}
    	// to -> from
    	f=new Vector(to.dir.opposite(),to.point);
    	t=new Vector(from.dir.opposite(),from.point);
    	for (int i=0;i<50;i++) {
    		borderMap.put(f, t);
    		f=new Vector(f.dir,walkTo.apply(f.point));
    		t=new Vector(t.dir,walkFrom.apply(t.point));
    	}    	
    }
}