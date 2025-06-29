package aoc2023.day21;

import common.QueueHelper;
import common.dim2.Terrein;
import common.graph.Direction;
import common.graph.Point;
import common.main.AbstractMainMaster;

/**
 * In een tuin met gras en rotsen. Op hoeveel plaatsen kan de Elf staan na 64 zetten.
 * Er mag alleen Links,Rechts,Boven of Onder bewogen worden en enkel via gras.
 * Er mag wel teruggekeerd worden. 
 */
public class Main21 extends AbstractMainMaster<Long> {
	
    public static void main(String[] args) {
        new Main21()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }
    int dimx, dimy;
    Terrein<Vak> tuin;
    int startX,startY;
    int maxDist;
    QueueHelper<Vak> queue=new QueueHelper<>();
    
    @Override
    public void beforeEach() {
    	if(testMode) {
    		dimx=11; dimy=11;
    		maxDist=6;
    	} else {
    		dimx=131; dimy=131;
    		maxDist=64;
    	}
    	try {
			tuin=new Terrein<Vak>(dimx,dimy,Vak.class,"point");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Class Vak heeft geen veld 'point'");
		}
    	loadInputInto2DTArray(tuin,new InputConverter());
    }
    class InputConverter implements InputCharConverter<Vak> {
		@Override
		public Vak convert(Character c, int x, int y) {
			if(c=='S') {
				startX=x;startY=y;
			}
			return new Vak(c!='#');
		}    	
    }
    class QueueElement {
    	Vak vak;
    	int dist;
		public QueueElement(Vak vak, int dist) {
			this.vak = vak;
			this.dist = dist;
		}    	
    }

    private long star1Teller=0;
    // antwoord : 3773
    public Long star1() {
    	Vak startVak=tuin.getField(new Point(startX,startY));
    	startVak.afstand=0;
    	queue.add(startVak);
    	queue.consume(this::process1);
    	tuin.walk(this::star1Teller);
        return star1Teller;
    }
    void star1Teller(Vak vak) {
    	if(vak.afstand<=maxDist && vak.afstand%2==0)
    		star1Teller+=1;
    }
    
    void process1(Vak vak) {
    	for(Direction dir:Direction.values()) {
	    	Vak vl=tuin.next(vak, dir);
	    	if(vl!=null && vl.isGras &&vl.afstand>(vak.afstand+1)) {
	    		// we hebben een korter traject gevonden
	    		vl.afstand=vak.afstand+1;
	    		queue.add(vl);
	    	}
    	}
    }
    
    // antwoord : 
    public Long star2() {
        return null;

    }
}