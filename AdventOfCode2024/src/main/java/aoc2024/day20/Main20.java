package aoc2024.day20;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import common.QueueHelper;
import common.dim2.CharTerrein;
import common.dim2.IntTerrein;
import common.dim2.Walker;
import common.dim2.Walker.ForwardStatus;
import common.graph.Direction;
import common.graph.Point;
import common.main.AbstractMainMaster;

/**
 * Doolhof met door muur wandelen mogelijkheid
 */
public class Main20 extends AbstractMainMaster<Long> {
	Dimension dim=new Dimension(141,141);
	CharTerrein terrein;
	IntTerrein scoreTerrein;
	QueueHelper<MazeWalker> queue;
	Point startPoint;
	Point endPoint;
    BiFunction<Point,Walker,Boolean> accessRule=(p,w)->terrein.at(p)!='#';
    /**
     * Beste pad van start naar endPoint
     */
    MazeWalker bestPath;
    Map<Point,Integer> bestPathMap;
    int requiredReduction=100;
	
    public static void main(String[] args) {
        new Main20()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }

    @Override
    public void beforeEach() {
    	if(testMode) {
    		dim=new Dimension(15,15);
    		requiredReduction=20;
    	}
    	terrein=new CharTerrein(loadInputInto2DArray(dim.width, dim.height));
    	terrein.scan((c,p)->{
    		if(c=='S') startPoint=p; 
    		if(c=='E') endPoint=p;
    		});
    	scoreTerrein=new IntTerrein(dim,Integer.MAX_VALUE);
    	queue=new QueueHelper<MazeWalker>();
    }

    /**
     * Op hoeveel plaatsen kunnen we 100 stappen korter zijn van S naar E
     * wanneer we 1 keer door een muur mogen ?
     * Algorithme: bereken kortste route en dan vanaf elk punt op de route:
     * <pre>
     *    | 12 | # | 120 | #
     *    | 13 | # | 119 | #
     * </pre>
     * als we van 12 naar 120 gaan door de muur winnen we 107 stappen=120-12+1    
     */
    // antwoord :
    public Long star1() {
    	searchBestPath();
        // plaatsen waar we met door de muur te gaan, de route voldoende inkorten (>=requiredReduction)
        Set<Point> breaks=new HashSet<>();
        for(Point pos:bestPath.getPath()) {
        	int curval=scoreTerrein.at(pos);
        	// controleer in de 4 richtingen
        	for(Direction dir:Direction.values()) {
        		Point muur=dir.move(pos);
        		if(terrein.isIn(muur)&& terrein.at(muur)=='#') {
        			for(Direction dir2:dir.allButOpposite()) {
        				Point check2=dir2.move(muur);
        				if(terrein.isIn(check2) 
        						&& bestPathMap.containsKey(check2)
        						&& bestPathMap.get(check2)>curval+requiredReduction+1) {
        					breaks.add(muur);
        				}
        			}
        		}
        	}
        }
        logln(breaks);
        return (long)breaks.size();
    }

    // antwoord : 
    public Long star2() {
    	searchBestPath();
        // plaatsen waar we met door de muur te gaan, de route voldoende inkorten (>=requiredReduction)
        Set<Point> breaks=new HashSet<>();

        return (long)breaks.size();
    }
    private void searchBestPath() {
    	MazeWalker start=new MazeWalker(startPoint,Direction.UP,0);
    	start.withAccessibleRule(accessRule).withHistory();
    	for(Direction dir:Direction.values()) {
    		start.setDirection(dir);
    		if(start.canForward()==ForwardStatus.POSSIBLE)
    			queue.add(start.copy());
    	}
        queue.consume(this::walkStar);
        bestPathMap=new HashMap<>();
        // nu is bestPath bepaald
        for(Point p:bestPath.getPath()) {
        	bestPathMap.put(p, scoreTerrein.at(p));
        }
    	
    }
    private void walkStar(MazeWalker walker) {
		Point dest=walker.moveForward();
		walker.score++;
		if(scoreTerrein.at(dest)>walker.score) {
			scoreTerrein.setValue(dest, walker.score);
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