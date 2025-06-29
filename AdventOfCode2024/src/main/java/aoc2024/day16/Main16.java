package aoc2024.day16;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import common.QueueHelper;
import common.dim2.CharTerrein;
import common.dim2.Vak;
import common.dim2.Walker;
import common.dim2.Walker.ForwardStatus;
import common.graph.Direction;
import common.graph.Point;
import common.main.AbstractMainMaster;

/**
 * Doolhof !!
 * Dijkstra: pad met laagste waarde
 * <li>1 stap vooruit: 1 punt
 * <li>1 bocht van 90°: 1000 punten
 */
public class Main16 extends AbstractMainMaster<Long> {
	Dimension dim=new Dimension(141,141);
	CharTerrein terrein;
	QueueHelper<MazeWalker> queue;
	MazeVak[][] vakken;
	List<MazeWalker> bestWalkers=new ArrayList<>();
	long bestScore=Long.MAX_VALUE;
	
	public static void main(String[] args) {
        new Main16()
            .testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }

    @Override
	public String getResourceName() {
		if(testMode)return "testinput2.txt";
		return super.getResourceName();
	}

	@Override
    public void beforeEach() {
		bestWalkers=new ArrayList<>();
    	queue=new QueueHelper<MazeWalker>();
		switch (getResourceName()) {
		case "testinput.txt":  dim=new Dimension(15,15); break;
		case "testinput2.txt": dim=new Dimension(17,17); break;
		}
    	terrein=new CharTerrein(loadInputInto2DArray(dim.width, dim.height));
    	vakken=new MazeVak[dim.height][dim.width];
    }

    BiFunction<Point,Walker,Boolean> accessRule=(p,w)->terrein.at(p)!='#';
    
    // antwoord : 
    public Long star1() {
    	Point endPos=new Point(0,0);
    	MazeWalker start=new MazeWalker(new Point(0,0),Direction.UP,1000L);
    	start.withAccessibleRule(accessRule).withHistory();
    	terrein.scan((c,p)->{if(c=='S') start.setPos(p); if(c=='E') {endPos.x=p.x; endPos.y=p.y;}});
    	if(log)logln("Startpositie:"+start);
        queue.add(start);
        queue.consume(this::walkStar);
        return vakken[endPos.y][endPos.x].score;
    }

    // antwoord : 
    public Long star2() {
    	Point endPos=new Point(0,0);
    	MazeWalker start=new MazeWalker(new Point(0,0),Direction.UP,1000L);
    	start.withAccessibleRule(accessRule).withHistory();
    	terrein.scan((c,p)->{if(c=='S') start.setPos(p); if(c=='E') {endPos.x=p.x; endPos.y=p.y;}});
    	if(log)logln("Startpositie:"+start);
        queue.add(start);
        queue.consume(this::walkStar);
        Set<Point> allGood=new HashSet<>();
        bestWalkers.stream().forEach(mw->allGood.addAll(mw.getPath()));
        return (long)allGood.size()+2;
    }
    
    private void walkStar(MazeWalker walker) {
		Point dest=walker.moveForward();
		walker.score++;
		MazeVak vak=vakken[dest.y][dest.x];
		if(vak==null)
			vak=(vakken[dest.y][dest.x]=new MazeVak(dest));
		if(vak.score>walker.score) {
			vak.score=walker.score;
		} else if(vak.score<walker.score)
			return;
		if(terrein.at(dest)=='E') {
			if (bestScore>walker.score) {
				bestScore=walker.score;
				bestWalkers.clear();
				bestWalkers.add(walker);
			} else if(bestScore==walker.score) {
				bestWalkers.add(walker);
			}
			return;
		}
		if(walker.canForward()==ForwardStatus.POSSIBLE)
			queue.add(walker);
		MazeWalker leftWalker=walker.copy();
		leftWalker.setDirection(walker.getDir().turnLeft());
		leftWalker.score=walker.score+1000L;
		if(leftWalker.canForward()==ForwardStatus.POSSIBLE)
			queue.add(leftWalker);
		MazeWalker rightWalker=walker.copy();
		rightWalker.setDirection(walker.getDir().turnRight());
		rightWalker.score=walker.score+1000L;
		if(rightWalker.canForward()==ForwardStatus.POSSIBLE)
			queue.add(rightWalker);    		    		
    }
    
    class MazeVak extends Vak {
    	long score=Long.MAX_VALUE;
    	public MazeVak(Point p) {
    		super(p);
    	}
		@Override
		public String toString() {
			return "[score=" + score + "]";
		}    	
    }
    
    class MazeWalker extends Walker {
    	long score=0;
    	
    	public MazeWalker() {
			super();
		}
		public MazeWalker(Point pos, Direction dir,long score) {
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