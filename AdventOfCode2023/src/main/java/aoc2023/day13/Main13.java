package aoc2023.day13;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import common.dim2.CharTerrein;
import common.main.AbstractMainMaster;

public class Main13 extends AbstractMainMaster<Long> {
	
    public static void main(String[] args) {
        new Main13()
            //.testMode()
            //.nolog()
           .start();
    }
    List<String> lines;
    List<CharTerrein> terreinen;
    
    @Override
    public void beforeEach() {
    	terreinen=new ArrayList<>();
    	lines=loadInputByLine();
    	List<String> terr=new ArrayList<>();
    	for(String line:lines) {
    		if(line.isBlank()) {
    			// einde van een terrein
    			CharTerrein ct=new CharTerrein(new Dimension(terr.get(0).length(),terr.size()));
    			ct.load(terr);
    			terreinen.add(ct);
    			terr=new ArrayList<>();
    		} else {
    			terr.add(line);
    		}
    	}
    	// laatste terrein
    	CharTerrein ct=new CharTerrein(new Dimension(terr.get(0).length(),terr.size()));
    	ct.load(terr);
    	terreinen.add(ct);
    }
    
    // antwoord : 34993
    public Long star1() {
    	return (long)terreinen.stream().map(this::zoekSpiegel).reduce(0, Integer::sum);
    }

    // antwoord : 
    public Long star2() {
    	return 0L;
    }
    
    private int zoekSpiegel(CharTerrein terrein) {
    	// horizontale spiegel zoeken
    	int[] asnumberH=new int[terrein.getDim().height];
    	terrein.scan((c,p)->{asnumberH[p.y]+=(int)(c=='#'?1:0)*Math.pow(2,p.x);});
    	int maxHor=-1;
    	for(int a=0;a<asnumberH.length-1;a++) {
    		if(asnumberH[a]==asnumberH[a+1]) {
    			int b=a;
    			int c=a+1;
    			while(true) {
    				b--; c++;
    				if(b<0||c>=asnumberH.length) {
    					maxHor=a+1;
    					break;
    				}
    				if(asnumberH[b]!=asnumberH[c])
    					break;
    			}
    		}
    	}
    	if(maxHor>0)
    		return maxHor*100;
    	//vertikaal zoeken
    	int[] asnumberV=new int[terrein.getDim().width];
    	terrein.scan((c,p)->{asnumberV[p.x]+=(int)(c=='#'?1:0)*Math.pow(2,p.y);});
    	int maxVer=-1;
    	for(int a=0;a<asnumberV.length-1;a++) {
    		if(asnumberV[a]==asnumberV[a+1]) {
    			int b=a;
    			int c=a+1;
    			while(true) {
    				b--; c++;
    				if(b<0||c>=asnumberV.length) {
    					maxVer=a+1; break;}
    				if(asnumberV[b]!=asnumberV[c])
    					break;
    			}
    		}
    	}
    	if(maxVer>0)
    		return maxVer;
    	
    	return 0;
    }
}