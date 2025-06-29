package aoc2023.day22;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

/**
 * Input zijn een reeks vallende rotsen in een 3D omgeving.
 * Elk rotsblok is een rechte lijn die dus horizontaal (volgens X of Y) loopt of verticaal.
 * In de input kunnen sommige blokken zweven (nog aan het vallen).
 * Eerst moeten deze allemaal verticaal zakken zodat ze op elkaar liggen. 
 * Ze kunnen niet kantelen dus vanaf er 1 blok een onderliggende raakt, stopt de valbeweging.
 * 
 * Geldt voor volledige input = z1 <= z2, dus deltaZ is altijd >=0
 */
public class Main22 extends AbstractMainMaster<Long> {
    private static int BALK_ID_SEQ=1;
	
    public static void main(String[] args) {
        new Main22()
            .testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }
    
	int maxX, maxY,maxZ;
	SteunBalk[][][] ruimte;
	List<SteunBalk> balken;
	int bodemLevel=1; // bodem is niet op pos 0 maar op 1. Er kan geen balk op niveau null
	
    @Override
    public void beforeEach() {
    	BALK_ID_SEQ=1;
    	// de gehele input doorlopen om de dimensies te kennen
    	balken=parseInput(this::parseBalk);
    	ruimte=new SteunBalk[maxX+1][maxY+1][maxZ+1];
    	// plaats de Balken in de ruimte
    	balken.stream().forEachOrdered(this::vulRuimte);
    }
    
    private static final RegexTool inputParser=new RegexTool("(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)");
    
    /**
     * Creeer Balk instanties en bereken dimensies (max*)
     * @param input
     * @return
     */
    private SteunBalk parseBalk(String input) {
    	if(inputParser.evaluate(input)) {
    		maxX=Math.max(maxX,Math.max(inputParser.intGroup(1),inputParser.intGroup(4)));
    		maxY=Math.max(maxY,Math.max(inputParser.intGroup(2),inputParser.intGroup(5)));
    		maxZ=Math.max(maxZ,Math.max(inputParser.intGroup(3),inputParser.intGroup(6)));
    		return new SteunBalk(""+BALK_ID_SEQ++,inputParser.intGroup(1),inputParser.intGroup(2),inputParser.intGroup(3),
    				inputParser.intGroup(4),inputParser.intGroup(5),inputParser.intGroup(6));
    	}
    	return null;
    }
    
    /**
     * Plaats elke balk in de ruimte. Elk vak verwijst naar de Balk die door het vak gaat.
     * @param balk
     */
    private void vulRuimte(SteunBalk balk) {
    	balk.getPosities().forEach(bp->ruimte[bp.getX()][bp.getY()][bp.getZ()]=balk);
    }
    
    /**
     * Welke blokken kunnen weggenomen worden zonder dat een ander blok valt. Dit betekent niet dat er geen blok mag op rusten !
     * vb:
     *   AAA DDD 
     *   B CCC
     * Blok B of C kan weggenomen worden want A wordt dan nog altijd gedragen door het andere (respectievelijk C en B)
     * Hoeveel blokken kunnen er weggenomen worden. A, B zijn het goede antwoord.
     * 
     * Design: een blok mag weg wanneer: elke blok die er op steunt ook nog op een ander blok steunt.
     * We berekenen eerst per blok wie er op steunt en waarop het steunt.
     */
    // antwoord : 465
    public Long star1() {
    	valBeweging();
        return jenga();
    }
    /**
     * Laat alle balken zakken tot ze niet meer kunnen.
     */
    private void valBeweging() {
    	// zo lang we een balk hebben kunnen laten zakken testen we voort
    	boolean balkGezakt=false;
    	do {
    		balkGezakt=false;
    		balkenLoop:
        	for(SteunBalk b:balken) {
        		if(b.z>1) {
        			for (BalkPos bp:b.grondVlak) {
        				if(ruimte[bp.getX()][bp.getY()][bp.getZ()-1]!=null) {
        					// bezet vak onder balk, kan dus niet meer zakken...
        					continue balkenLoop;
        				}
        			}
        			// als we hier geraken kan de balk zakken
        			for (BalkPos bp:b.balkPoss)
        				ruimte[bp.getX()][bp.getY()][bp.getZ()]=null;
        			b.z--;
        			for (BalkPos bp:b.balkPoss)
        				ruimte[bp.getX()][bp.getY()][bp.getZ()]=b;
        			balkGezakt=true;
        		}
        	}    		
    	} while(balkGezakt==true);
    }

    /**
     * Telt hoeveel balken kunnen weggenomen worden zonder dat andere vallen
     * @return
     */
    public long jenga() {
    	// eerst alle balken overlopen en per balk bepalen wie op hen steunt en waarop ze steunen.
    	balkLoop:
    	for(SteunBalk balk:balken) {
    		for(BalkPos pos:balk.grondVlak) {
    			if(pos.getZ()==1)
    				continue balkLoop; // balk ligt onderaan
    			SteunBalk onder=ruimte[pos.getX()][pos.getY()][pos.getZ()-1];
    			if(onder!=null) {
    				onder.ondersteunt(balk);
    				balk.steuntOp(onder);
    			}
    		}
    	}
    	Set<SteunBalk> kanWeg=new HashSet<>();
    	balkLoop:
    	for(SteunBalk balk:balken) {
    		for(SteunBalk boven:balk.ondersteunt()) {
    			if(boven.steuntOp().size()==1) {
    				// niet goed. boven steunt enkel op huidige balk dus balk mag niet weg
    				balk.setDragend(true);
    				continue balkLoop;
    			}
    		}
    		// alle balken die steunen op balk, steunen ook nog op iets anders, dus deze kan weggenomen worden
    		kanWeg.add(balk);
    		logln("Kan Weg:"+balk);
    	}
    	return kanWeg.size();
    }
    /**
     * Wanneer we telkens een dragende balk zouden wegnemen hoeveel andere balken vallen dant telkens mee ?
     * Antwoord is de som van het aantal vallende balken. 
     * !! werkt niet !!
     */
    // antwoord : 
    public Long star2() {
    	// in star1 hebben we de dragende balken geidentificeerd. enkel die moeten nu
    	// geevalueerd worden.
    	star1();
    	
    	Comparator<SteunBalk> sorter=Comparator.comparing(b->b.z);
    	for(SteunBalk balk:balken.stream().sorted(sorter.reversed()).toList()) {
    		if(!balk.isDragend())
    			continue;
    		Set<SteunBalk> cumul=new HashSet<>();
    		for(SteunBalk boven:balk.ondersteunt()) {
    			cumul.add(boven);
    			if(boven.getOndersteuntCumul()!=null)
    				cumul.addAll(boven.getOndersteuntCumul());
    			else if(boven.isDragend()) {
    				logln("OPGELET: bovenliggende dragende balk niet berekend!:"+boven);
    			}
    		}
    		balk.setOndersteuntCumul(cumul);
    	}
    	long teller=0;
    	for(SteunBalk balk:balken) {
    		if(!balk.isDragend())
    			continue;
    		teller+=balk.getOndersteuntCumul().size();
    	}
        return teller;
    }
}