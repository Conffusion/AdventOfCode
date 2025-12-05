package aoc2025.day04;

import common.BasicUtils;
import common.dim2.Terrein;
import common.graph.Direction;
import common.graph.Point;
import common.io.Loader;
import common.main2.AoCLogger;
import common.main2.AoCRunner;
import common.main2.AoCSolver;

/**
 * 2D magazijn: elk element is een rol papier. 
 */
public class Solver04 extends AoCSolver<Long> {
	static AoCLogger logger=new AoCLogger();
	static boolean testMode=false; 
	Loader loader=Loader.forMain(this).withTestMode(testMode).build();
	
	public static void main (String[] args) {
		//logger.nolog(true);
		AoCRunner<Long> runner=new AoCRunner<>(new Solver04());
		if(testMode)
			runner.testMode();
		runner.start();
	}
	Terrein<Rol> magazijn;
	
    @Override
    public void beforeEach() {
    	int dim=context.isTestMode()?10:140;
    	magazijn=new Terrein<Rol>(dim,dim,Rol.class,"p");
    	loader.loadInputInto2DTArray(magazijn,this::convert);
    }
	public Rol convert(Character c, int x, int y) {
		return new Rol(c=='@'?1:0);
	}
    
	// Hoeveel rollen hebben max 4 rollen op de 8 plaatsen rond zich.
    // antwoord : 1523
    public Long star1() {
       return magazijn
    		   .walkingStream(magazijn.walker().withStart(0, 0).withFirstDirection(Direction.RIGHT).withSecondDirection(Direction.DOWN))
    		   .filter(r->r.value==1) // enkel tellen als er een rol is
    		   .filter(this::solve1)
    		   .count();
    }

    private boolean solve1(Rol rol) {
    	boolean result= BasicUtils.onNotNull(magazijn.getField(new Point(rol.p.x-1,rol.p.y-1)), r->r.value, 0)
    	+BasicUtils.onNotNull(magazijn.getField(new Point(rol.p.x,rol.p.y-1)), r->r.value, 0)
    	+BasicUtils.onNotNull(magazijn.getField(new Point(rol.p.x+1,rol.p.y-1)), r->r.value, 0)
    	+BasicUtils.onNotNull(magazijn.getField(new Point(rol.p.x-1,rol.p.y)), r->r.value, 0)
    	+BasicUtils.onNotNull(magazijn.getField(new Point(rol.p.x+1,rol.p.y)), r->r.value, 0)
    	+BasicUtils.onNotNull(magazijn.getField(new Point(rol.p.x-1,rol.p.y+1)), r->r.value, 0)
    	+BasicUtils.onNotNull(magazijn.getField(new Point(rol.p.x,rol.p.y+1)), r->r.value, 0)
    	+BasicUtils.onNotNull(magazijn.getField(new Point(rol.p.x+1,rol.p.y+1)), r->r.value, 0)<4;
    	return result;
    }
    
    // Verwijder de rollen met max 4 rollen op de 8 plaatsen rond zich. Blijf dit herhalen tot er geen rollen meer weg kunnen.
    // Hoeveel rollen zijn weggenomen ?
    // antwoord : 
    public Long star2() {
        Long result=0L;
        long subresult=1;
        int iter=0;
        while(subresult>0) {
        	subresult= magazijn
     		   .walkingStream(magazijn.walker().withStart(0, 0).withFirstDirection(Direction.RIGHT).withSecondDirection(Direction.DOWN))
     		   .filter(r->r.value==1) // enkel tellen als er een rol is
     		   .filter(this::solve2).count();
        	System.out.println("Iteratie "+(++iter)+" verwijdert "+subresult+" rollen");
        	result+=subresult;
        }
        return result;
    }
    private boolean solve2 (Rol rol) {
    	boolean result=solve1(rol);
    	if(result) {
    		// rol verwijderen. Dit heeft dan al invloed in deze iteratie voor de andere rollen in de buurt 
    		// maar dat is geen probleem: het versnelt zelfs het proces.
    		rol.value=0; 
    	}
    	return result;    		
    }
    
    /**
     * value: 1= papierrol, 0=leeg
     */
    public static class Rol{
    	int value; 
    	public Point p;
		public Rol(int value) {
			this.value = value;
		}
    }
}