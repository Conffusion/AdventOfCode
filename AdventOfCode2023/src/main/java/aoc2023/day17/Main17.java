package aoc2023.day17;

import java.awt.Dimension;
import java.util.LinkedList;

import common.dim2.DimensionUtils;
import common.graph.Direction;
import common.graph.Point;
import common.graph.Vector;
import common.main.AbstractMainMaster;

/**
 * Dijkstra !!
 * Door een map lopen op zoek naar pad met laagste kost(=waarde van elke cel).
 * 
 */
public class Main17 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main17()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }

    Dimension dim;
    int[][] inputmap;
    CityBlock[][] city;
    LinkedList<Car> todoQueue=new LinkedList<>();
    
    @Override
    public void beforeEach() {
    	dim=testMode?new Dimension(13,13):new Dimension(141,141);
    	inputmap=loadInputInto2DIntArrayXY(dim.width, dim.height);
    	city=new CityBlock[dim.width][dim.height];
    	DimensionUtils.walk(dim, p->city[p.x][p.y]=new CityBlock(inputmap[p.x][p.y]));
    }

    // antwoord : 
    public Long star1() {
    	todoQueue.offer(new Car(Direction.RIGHT,new Point(0,0),0,0));
    	todoQueue.offer(new Car(Direction.DOWN,new Point(0,0),0,0));
    	processQueue();
        return city[dim.width-1][dim.height-1].lowest;
    }

    // antwoord : 
    public Long star2() {
        return null;

    }
    
    private void processQueue() {
    	Car car;
    	while((car=todoQueue.pollFirst())!=null) {
    		if(car.dir.canMove(car.point, dim)) {
    			car.forward();
    			CityBlock block=cityBlockAt(car.point);
    			if(car.value+block.heatloss<block.lowest) {
    				car.value+=block.heatloss;
    				block.lowest=car.value;
    				for(Direction dir:Direction.values()) {
    					if(!dir.isOpposite(car.dir) && dir.canMove(car.point, dim))
    						if(car.dir!=dir||car.straight<3)
    							todoQueue.offer(new Car(dir,car.point,car.value,car.dir==dir?car.straight+1:1));
    				}
    			}
    		}
    	}
    }
    private CityBlock cityBlockAt(Point p) {
    	return city[p.x][p.y];
    }
    
    static class CityBlock {
    	int heatloss;
    	long lowest=Long.MAX_VALUE;
		public CityBlock(int heatloss) {
			this.heatloss = heatloss;
		}
		@Override
		public String toString() {
			return "CB[heatloss=" + heatloss + ", lowest=" + lowest + "]";
		}
		
    }
    
    static class Car extends Vector {
    	// heatloss som tot waar de Car is
    	long value;
    	// aantal keer we al in dezelfde richting hebben gereden
    	int straight;
		public Car(Direction dir, Point p, long value,int straight) {
			super(dir,p);
			this.value = value;
			this.straight=straight;
		}
		@Override
		public String toString() {
			return "Car {"+super.toString()+", value=" + value + ", straight=" + straight + "}";
		}
    	
    }
}