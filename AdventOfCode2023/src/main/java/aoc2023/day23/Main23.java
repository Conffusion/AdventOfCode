package aoc2023.day23;

import aoc2023.day23.Vak23.VakType;
import common.QueueHelper;
import common.dim2.Pad;
import common.dim2.Terrein;
import common.graph.Direction;
import common.graph.Point;
import common.main.AbstractMainMaster;

public class Main23 extends AbstractMainMaster<Long> {
	int dimx, dimy;
    Terrein<Vak23> bos;
    Point startPos, eindPos;
    boolean steilteCheck=true;
    
    QueueHelper<Pad<Vak23>> queue=new QueueHelper<>();
    public static void main(String[] args) {
        new Main23()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }
    
    @Override
    public void beforeEach() {
	    if(testMode) {
			dimx=23; dimy=23;
		} else {
			dimx=141; dimy=141;
		}
		try {
			bos=new Terrein<Vak23>(dimx,dimy,Vak23.class,"point");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Class Vak heeft geen veld 'point'");
		}
		loadInputInto2DTArray(bos,new InputConverter());
	}
    
	class InputConverter implements InputCharConverter<Vak23> {
		@Override
		public Vak23 convert(Character c, int x, int y) {
			VakType vt=switch(c) {
				case '#'-> VakType.BOS;
				case '.'-> VakType.WEG;
				case '>'-> VakType.STEIL_O;
				case 'v'-> VakType.STEIL_Z;
				default-> throw new IllegalArgumentException("onverwachte input:"+c);
			};
			return new Vak23(vt,new Point(x,y));
		}    	
	}

    // antwoord : 2402
    public Long star1() {
    	steilteCheck=true;
    	startPos=new Point(1,0);
    	eindPos=new Point(dimx-2, dimy-1);
    	Vak23 startVak=bos.getField(startPos);
    	queue.add(new Pad<>(startVak));
    	queue.consume(this::process1);
        return bos.getField(eindPos).afstand;
    }
    
    /**
     * Vanaf de huidige positie gaan we alle richtingen controleren.
     * Als we er naar toe kunnen (geen rots of steilte) en we zijn er nog niet geweest
     * met het huidige pad en de afstand van dit pad is langer, dan proberen we dit pad.
     * Elke verdere stap is een clone van het huidige pad met het nieuwe vak toegevoegd.
     * @param pad
     */
    void process1(Pad<Vak23> pad) {
    	for(Direction dir:Direction.values()) {
	    	Vak23 vl=bos.next(pad.getHuidigePos(), dir);
	    	if(vl!=null && vl.kanPasseren(dir,steilteCheck) 
	    			&& vl.afstand<(pad.getHuidigePos().afstand+1) // deze weg is langer
	    			&& !pad.alGeweest(vl)) // dit pad is hier nog niet geweest 
	    	{
	    		Pad<Vak23> newp=pad.clone();
	    		// we hebben een langer traject gevonden
	    		vl.afstand=pad.getHuidigePos().afstand+1;
	    		newp.setHuidigePos(vl);
	    		queue.add(newp);
	    	}
    	}
    }

    // antwoord : 
    public Long star2() {
    	// geen rekening houden met steiltes ('v','>')
    	steilteCheck=false;
    	startPos=new Point(1,0);
    	eindPos=new Point(dimx-2, dimy-1);
    	Vak23 startVak=bos.getField(startPos);
    	queue.add(new Pad<>(startVak));
    	queue.consume(this::process1);
        return bos.getField(eindPos).afstand;
    }
}