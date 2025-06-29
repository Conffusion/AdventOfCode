package aoc2024.day18;
import java.awt.Dimension;
import java.util.List;
import java.util.function.BiFunction;

import common.QueueHelper;
import common.dim2.CharTerrein;
import common.dim2.IntTerrein;
import common.dim2.Walker;
import common.dim2.Walker.ForwardStatus;
import common.graph.Direction;
import common.graph.Point;
import common.main.AbstractMainMaster;
import common.regex.RegexMatchBuilder;

/**
 * Een doolhof maar waarbij er steeds nieuwe obstakels bijkomen en de vraag is:
 * vanaf welk obstakel is er geen pad meer mogelijk van start (links bovenaan)
 * naar de uitgang (rechts onderaan)
 * Optimalisatie: we berekenen het beste pad. Zolang er geen nieuwe obstakels op ons 
 * pad vallen blijft ons pad een geldige oplossing. 
 * Wanneer een obstakel wel op ons pad valt, berekenen we opnieuw een beste pad.
 * Als dit niet lukt hebben we het antwoord op deel 2.
 */
public class Main18 extends AbstractMainMaster<Long> {
	Dimension dim=new Dimension(71,71);
	/**
	 * Dit aantal obstakels heeft gegarandeerd nog een geldig pad.
	 */
	int startIndexPart2=1024;
	/**
	 * bevat voor elke punt de kortste weg vanaf startPoint 
	 */
	IntTerrein costTerrein;
	/**
	 * definitie van doolhof: . is gras, # is onbereikbaar
	 */
	CharTerrein terrein;
	QueueHelper<MazeWalker> queue;
    BiFunction<Point,Walker,Boolean> accessRule=(p,w)->terrein.at(p)!='#';
    /**
     * Beste pad van start naar endPoint
     */
    MazeWalker bestPath;
    Point startPoint=new Point(0,0);
    Point endPoint;

    public static void main(String[] args) {
        new Main18()
            //.testMode()
            //.withFileOutput()
            .nolog()
           .start();
    }

	@Override
	public String getResourceName() {
		if(onStar==1 && !testMode)
			return "inputpart1.txt";
		if(onStar==1 && testMode)
			return "testinputpart1.txt";
		if(onStar==2 && testMode)
			return "testinputpart2.txt";
		
		return super.getResourceName();
	}

	@Override
    public void beforeEach() {
    	if(testMode) {
    		dim=new Dimension(7,7);
    		startIndexPart2=12;
    	}
    	endPoint=new Point(dim.width-1,dim.height-1); 
    }
    
	// antwoord : 408
    public Long star1() {
        queue=new QueueHelper<MazeWalker>();
    	terrein=new CharTerrein(dim,'.');
    	costTerrein=new IntTerrein(dim,Integer.MAX_VALUE);
    	RegexMatchBuilder builder=new RegexMatchBuilder("([0-9]+),([0-9]+)");
    	streamInput(builder::evaluate).forEach(match->terrein.setValue(new Point(match.intGroup(1),match.intGroup(2)), '#'));
    	MazeWalker start=new MazeWalker(new Point(0,0),Direction.RIGHT,0);
    	start.withAccessibleRule(accessRule);
        queue.add(start);
        queue.consume(this::walkStar);
        return (long)costTerrein.at(endPoint);
    }

    // antwoord : 
    public Long star2() {
        queue=new QueueHelper<MazeWalker>();
    	terrein=new CharTerrein(dim,'.');
    	costTerrein=new IntTerrein(dim,Integer.MAX_VALUE);
    	RegexMatchBuilder builder=new RegexMatchBuilder("([0-9]+),([0-9]+)");
    	List<Point>input =parseInput(builder::evaluate, match->new Point(match.intGroup(1),match.intGroup(2)));    	
    	for(int i=0;i<startIndexPart2;i++) {
    		terrein.setValue(input.get(i), '#');
    	}
    	// eerst zoeken we het kortste pad
    	findPath();
    	if(log) logln("beste pad: "+bestPath);
        for (int b=startIndexPart2;b<input.size();b++) {
        	Point newbyte=input.get(b);
        	terrein.setValue(newbyte, '#');
        	if(log) logln("nieuwe byte "+newbyte);
        	if(bestPath.getPath().contains(newbyte)) {
        		// nieuwe byte valt op ons pad dus we moeten het herberekenen
        		costTerrein=new IntTerrein(dim,Integer.MAX_VALUE);
        		if(!findPath()) {
        			System.out.println("De uitweg is afgesloten door byte "+newbyte);
        			return 0L;
        		}
            	if(log) logln("beste pad: "+bestPath.getPath());
        	} else {
        		// deze byte ligt niet op ons pad en is dus geen probleem
        	}
        }
        return (long)costTerrein.at(new Point(dim.width-1,dim.height-1));
    }

    private boolean findPath() {
    	MazeWalker walker1=new MazeWalker(new Point(0,0),Direction.RIGHT,0);
    	walker1.withAccessibleRule(accessRule).withHistory();
    	MazeWalker walker2=new MazeWalker(new Point(0,0),Direction.DOWN,0);
    	walker2.withAccessibleRule(accessRule).withHistory();
        queue.add(walker1);
        queue.add(walker2);
        queue.consume(this::walkStar);
        if(costTerrein.at(new Point(dim.width-1,dim.height-1))==Integer.MAX_VALUE) {
        	// we geraken niet aan de onderkant
        	return false;
        }
        return true;
    }
    private void walkStar(MazeWalker walker) {
		Point dest=walker.moveForward();
		walker.score++;
		if(costTerrein.at(dest)>walker.score) {
			costTerrein.setValue(dest, walker.score);
			if(dest.equals(endPoint)) {
				bestPath=walker;
				return;
			}
		} else
			return;
		if(walker.canForward()==ForwardStatus.POSSIBLE)
			queue.add(walker);
		MazeWalker leftWalker=walker.copy();
		leftWalker.setDirection(walker.getDir().turnLeft());
		if(leftWalker.canForward()==ForwardStatus.POSSIBLE)
			queue.add(leftWalker);
		MazeWalker rightWalker=walker.copy();
		rightWalker.setDirection(walker.getDir().turnRight());
		if(rightWalker.canForward()==ForwardStatus.POSSIBLE)
			queue.add(rightWalker);    		    		
    }
    class MazeWalker extends Walker {
    	int score=0;
    	
    	public MazeWalker() {
			super();
		}
		public MazeWalker(Point pos, Direction dir,int score) {
			super(pos, dir, dim);
			this.score=score;
		}
    	public MazeWalker copy() {
    		MazeWalker clone=new MazeWalker();
    		copyInto(clone);
    		clone.score=this.score;
    		return clone;
    	}
		@Override
		public String toString() {
			return "MazeWalker ["+super.toString()+", score=" + score + "]";
		}
    	
    }
}