package aoc2022.day14;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import common.dim2.DimensionUtils;
import common.graph.Point;
import common.main.AbstractMainMaster;

/**
 * zand valt in een ruimte 
 * @author walter
 *
 */
public class Main14 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main14()
            //.testMode()
            //.nolog()
           .start();
    }
    // [X][Y]
    // X coord gaat van links naar rechts
    // Y coord gaat van boven naar onder. y=0 is bovenste rij van space
    char[][] space;
    int minX=500, maxX=0;
    int maxY=0;
    List<Shape> shapes;
    
    @Override
	public void beforeAll() {
    	shapes=streamInput(this::parseShape).collect(Collectors.toList());
    	logln("space dimensies minX="+minX+", maxX="+maxX+", maxY="+maxY);
	}

	@Override
    public void beforeEach() {    	
    	maxY++;
    	minX=500-maxY;
    	maxX=501+maxY;
    	space=new char[maxX-minX+1][maxY+1];
    	DimensionUtils.fill2Dim(space, '.');
    	if(onStar==2) {
    		shapes.add(new Shape().setStart(new Point(minX,maxY)).addVector(new Vector(DIR.RIGHT,new Point(maxX,maxY))));
    	}
    	shapes.forEach(Shape::drawShape);
    	printSpace();
    }

    public Long star1() {
    	long teller=0L;
    	while(dropSand(new Point(500,0)))
    		teller++;
    	printSpace();
        return teller;
    }

    // laat zandkorrel vallen. Opgelet p wordt gewijzigd
    // true: de zandkorrel is ergens stilgevallen
    // false: ze zandkorrel is in het oneindige gevallen
    private boolean dropSand(Point p) {
    	while (true) {
    		if(p.x<minX||p.x>maxX||p.y>maxY-1) {
    			if(onStar==2)
    				throw new RuntimeException("Mag niet kunnen in star2:"+p);
    			return false;
    		} 
    		if(space[p.x-minX][p.y+1]=='.')
    			; // recht naar beneden vallen, x verandert niet
    		else if (p.x-minX-1<0) {
    			if(onStar==2)
    				throw new RuntimeException("Mag niet kunnen in star2:"+p);
    			return false; // langs links eraf gevallen
    		} else if (space[p.x-minX-1][p.y+1]=='.')
    			p.x--; // korrel kan naar links
    		else if(p.x-minX+1>maxX) {
    			if(onStar==2)
    				throw new RuntimeException("Mag niet kunnen in star2:"+p);
    			return false; // langs rechts eraf gevallen
    		} else if(space[p.x-minX+1][p.y+1]=='.')
    			p.x++; // korrel kan naar rechts
    		else {
    			// korrel kan niet meer verder dus blijft liggen
    			space[p.x-minX][p.y]='o';
    			if(onStar==2 &&p.x==500&&p.y==0)
    				return false;
    			return true;
    		}
    		p.y++; // we zakken altijd
    		if(p.y>maxY)
    			return false; // we ijn er door gevallen
    	}
    }
    
    // 31706
    public Long star2() {
    	long teller=0L;
    	while(dropSand(new Point(500,0)))
    		teller++;
    	teller++;
    	printSpace();
        return teller;
    }
    
    private void printSpace() {
    	logln("");
    	for (int y=0;y<=maxY;y++) {
    		for(int x=0;x<=maxX-minX;x++)
    			log(""+space[x][y]);
    		logln(" "+y);
    	}
    }
    /**
     * format x1,y1 -> x2,y2 -> x3,y3
     * @param line
     * @return
     */
    public Shape parseShape(String line) {
    	Shape sh=new Shape();
    	String[] points=line.split(" -> ");
    	Point prevPoint=parsePoint(points[0]);
    	sh.setStart(prevPoint);
    	for (int i=1;i<points.length;i++) {
    		Point newp=parsePoint(points[i]);
    		sh.addVector(new Vector(calcDIR(prevPoint, newp),newp));
    		prevPoint=newp;
    	}
    	return sh;
    }
    
    // format x,y
    private Point parsePoint(String in) {
    	String[] coords=in.split(",");
    	return new Point(Integer.parseInt(coords[0]),Integer.parseInt(coords[1]));
    }
    private DIR calcDIR(Point pFrom, Point pTo) {
    	if (pFrom.x==pTo.x) {
    		// vertical
    		return pFrom.y>pTo.y?DIR.UP:DIR.DOWN;
    	} else
    		return pFrom.x>pTo.x?DIR.LEFT:DIR.RIGHT;
    }
    public class Shape {
    	Point start;
    	List<Vector> vectors=new ArrayList<>();
    	public Shape setStart(Point p) {
    		this.start=p;
    		maxY=Math.max(maxY, p.y);
    		minX=Math.min(minX, p.x);
    		maxX=Math.max(maxX, p.x);
    		return this;
    	}
    	public Shape addVector(Vector v)
    	{
    		maxY=Math.max(maxY, v.target.y);
    		minX=Math.min(minX, v.target.x);
    		maxX=Math.max(maxX, v.target.x);
    		vectors.add(v);
    		return this;
    	}
    	public void drawShape() {
    		space[start.x-minX][start.y]='#';
    		Point prev=start;
    		for(Vector v:vectors) {
    			switch(v.dir) {
    			case LEFT:
    				for (int i=prev.x;i>=v.target.x;i--)
    					space[i-minX][prev.y]='#';
    				break;
    			case RIGHT:
    				for (int i=prev.x;i<=v.target.x;i++)
    					space[i-minX][prev.y]='#';
    				break;
    			case UP:
    				for (int i=prev.y;i>=v.target.y;i--)
    					space[prev.x-minX][i]='#';
    				break;
    			case DOWN:
    				for(int i=prev.y;i<=v.target.y;i++)
    					space[prev.x-minX][i]='#';
    			}
    			prev=v.target;
    		}
    	}
    }
    enum DIR {LEFT,RIGHT,UP,DOWN}
    
    public class Vector {
    	Point target;
    	DIR dir;
		public Vector(DIR dir,Point target) {
			super();
			this.target = target;
			this.dir = dir;
		}
		@Override
		public String toString() {
			return "[" + target + ", " + dir + "]";
		}
		
    }
}