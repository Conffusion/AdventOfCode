package aoc2025.day07;

import common.dim2.Terrein;
import common.graph.Direction;
import common.graph.Point;
import common.io.Loader;
import common.main2.AoCLogger;
import common.main2.AoCRunner;
import common.main2.AoCSolver;

public class Solver07 extends AoCSolver<Long> {
	static AoCLogger logger=new AoCLogger();
	static boolean testMode=false;
	Loader loader=Loader.forMain(this).withTestMode(testMode).build();
	
	public static void main (String[] args) {
		//logger.nolog(true);
		AoCRunner<Long> runner=new AoCRunner<>(new Solver07());
		logger.nolog(false);
		if(testMode)
			runner.testMode();
		runner.start();
	}
	
	Terrein<Field> terrein;
	Field start;
    long[] beams;
    Long splitCounter;
    	
    @Override
    public void beforeEach() {
    	terrein=new Terrein<>(testMode?15:141,testMode?16:142,Field.class,"p");
    	loader.loadInputInto2DTArray(terrein, this::convert);
        start=terrein.walkingStream(terrein.walker().withStart(0, 0).withFirstDirection(Direction.RIGHT).withSecondDirection(Direction.DOWN))
            	.filter(f->f.sign=='S')
            	.findAny().get();
    	beams=new long[(int)terrein.getDim().getWidth()];
    }
    
    public Field convert(Character c,int x, int y) {
    	return new Field(c,new Point(x,y));
    }
    
    /**
     * Bij elke splitsing ^ splijt de straal aan beide kanten maar op elke locatie kan maar 
     * 1 straal zijn : 
     * <pre>
     *   ..S..
     *   ..^..
     *   .^.^.
     * </pre>
     * Totaal aantal stralen onderaan is 3
     */
    // antwoord: 1656
    public Long star1() {
    	splitCounter=0L;
        beams[start.p.x]=1;
        for(int y=0;y<terrein.getDim().height;y=y+2) {
        	for(int x=0;x<beams.length;x++) {
        		long currBeam=beams[x];
        		if(currBeam==0)
        			continue; // no beam here
        		Field field=terrein.getField(new Point(x,y));
        		if(field.sign=='^') {
        			splitCounter++;
    				beams[x-1]=currBeam;
    				beams[x+1]=currBeam;
        			beams[x]=0;
        		}
        	}
        }
        return splitCounter;
    }
    
    /**
     * Hoeveel mogelijke paden zijn er voor de stralen
     * <pre>
     *   ..S..
     *   ..^..
     *   .^.^.
     * </pre>
     * Totaal aantal mogelijke stralen onderaan is 4
     */
    // antwoord : 76624086587804
    public Long star2() {
        beams[start.p.x]=1;
        for(int y=0;y<terrein.getDim().height;y=y+2) {
        	for(int x=0;x<beams.length;x++) {
        		long currBeam=beams[x];
        		if(currBeam==0) {
        			// no beam here
        			continue;
        		}
        		Field field=terrein.getField(new Point(x,y));
        		if(field.sign=='^') {
        			beams[x-1]+=currBeam;
        			beams[x+1]+=currBeam;
        			beams[x]=0;
        		}
        	}
        }
        Long result=0L;
        for(int i=0;i<beams.length;i++)
        	result+=beams[i];
        return result;
    }
    
    public static class Field {
    	char sign; 
    	public Point p;
    	Field(char sign,Point p){
    		this.sign=sign;
    		this.p=p;
    	}
		@Override
		public String toString() {
			return "Field [" + sign + "," + p + "]";
		}
    }
}
