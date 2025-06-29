package aoc2024.day12;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.ToLongFunction;

import common.BasicUtils;
import common.QueueHelper;
import common.dim2.CharTerrein;
import common.graph.Direction;
import common.graph.Point;
import common.main.AbstractMainMaster;

public class Main12 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main12()
            //.testMode()
            //.withFileOutput()
            .nolog()
           .start();
    }
    int dimx=140, dimy=140;
    CharTerrein garden;
    Set<Point> allFields;
    Set<Regio> allRegios;
    
    @Override
	public void beforeAll() {
    	if(testMode)
    	{
    		dimx=10; dimy=10;
    	}
    	garden=new CharTerrein(loadInputInto2DArray(dimx, dimy));
    	allRegios=new HashSet<>();
    	allFields=new HashSet<>();
        garden.scan(this::buildRegion);
	}

    ToLongFunction<Regio> calcCost1=r->r.part1Fences*r.fields.size();
    ToLongFunction<Regio> calcCost2=r->r.part2Fences*r.fields.size();
    
    // antwoord : 1344578
    public Long star1() {
        return BasicUtils.streamSum(allRegios.stream(),calcCost1);
    }

    // antwoord : 814302
    public Long star2() {
        return BasicUtils.streamSum(allRegios.stream(),calcCost2);
    }

    QueueHelper<QueueElem> queue;
    
    /**
     * vanuit het startpunt zoeken we naar alle buren met dezelfde letter.
     * @param name
     * @param startPoint
     */
    private void buildRegion(Character name,Point startPoint) {
    	if(allFields.contains(startPoint))
    		return;
    	Regio regio=new Regio(name);
    	queue=new QueueHelper<>();
    	queue.add(new QueueElem(regio,startPoint));
    	queue.consume(this::handleQueueElem);
    	if(log) logln("regio:"+regio);
    	allRegios.add(regio);
    }
    
    private void handleQueueElem(QueueElem elem) {
    	elem.regio.fields.add(elem.point);
    	if(!allFields.add(elem.point))
    		return;
    	for(Direction dir:Direction.values()) {
    		Point next=dir.move(elem.point);
    		if(elem.regio.fields.contains(next)) 
    			continue;
    		if(!garden.isIn(next)) {
    			elem.regio.part1Fences++;
    			addNewFencePart2(elem.regio,new Fence(elem.point,dir));
    			continue;
    		}
    		if(garden.at(next)==elem.regio.name)
    			queue.add(new QueueElem(elem.regio,next));
    		else {
    			elem.regio.part1Fences++;
    			addNewFencePart2(elem.regio,new Fence(elem.point,dir));
    		}
    	}    	
    }
    /**
     * voor deel 2 mogen alle fences die in elkaars verlengde liggen maar als 1 geteld worden
     * @param regio
     * @param newFence
     */
    private void addNewFencePart2(Regio regio,Fence newFence) {
    	boolean fenceExists=false;
    	switch (newFence.dir) {
    	case UP: if (regio.part2FenceSet.contains(new Fence(new Point(newFence.point.x-1,newFence.point.y),Direction.UP))
    			|| regio.part2FenceSet.contains(new Fence(new Point(newFence.point.x+1,newFence.point.y),Direction.UP)))
    		fenceExists=true; break;
    	case LEFT: if (regio.part2FenceSet.contains(new Fence(new Point(newFence.point.x,newFence.point.y-1),Direction.LEFT))
    			|| regio.part2FenceSet.contains(new Fence(new Point(newFence.point.x,newFence.point.y+1),Direction.LEFT)))
    		fenceExists=true; break;
    	case RIGHT: if (regio.part2FenceSet.contains(new Fence(new Point(newFence.point.x,newFence.point.y-1),Direction.RIGHT))
    			|| regio.part2FenceSet.contains(new Fence(new Point(newFence.point.x,newFence.point.y+1),Direction.RIGHT)))
    		fenceExists=true; break;
    	case DOWN: if (regio.part2FenceSet.contains(new Fence(new Point(newFence.point.x-1,newFence.point.y),Direction.DOWN))
    			|| regio.part2FenceSet.contains(new Fence(new Point(newFence.point.x+1,newFence.point.y),Direction.DOWN)))
    		fenceExists=true; break;
    	}
    	regio.part2FenceSet.add(newFence);
    	if(!fenceExists)
    		regio.part2Fences++;
    }
    
    static class Regio {
    	char name;
    	Set<Point> fields=new HashSet<>();
    	int part1Fences=0;
    	int part2Fences=0;
    	Set<Fence> part2FenceSet=new HashSet<>();
		public Regio(char name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return "Regio [" + name + ", " + fields + ", part1Fences=" + part1Fences + ", part2Fences="
					+ part2Fences + "]";
		}

    }
    
    static class QueueElem {
    	Regio regio;
    	Point point;
		public QueueElem(Regio regio, Point point) {
			this.regio = regio;
			this.point = point;
		}
		@Override
		public String toString() {
			return "QueueElem [" + regio + ", " + point + "]";
		}
    }
    
    /**
     * Omheining relatief tot punt
     */
    static class Fence {
    	Point point;
    	Direction dir;

    	public Fence(Point point, Direction dir) {
			this.point = point;
			this.dir = dir;
		}

		@Override
		public String toString() {
			return "Fence [" + point + ", " + dir + "]";
		}

		@Override
		public int hashCode() {
			return Objects.hash(dir, point);
		}

		@Override
		public boolean equals(Object obj) {
			Fence other = (Fence) obj;
			return dir == other.dir && Objects.equals(point, other.point);
		}
    }
}