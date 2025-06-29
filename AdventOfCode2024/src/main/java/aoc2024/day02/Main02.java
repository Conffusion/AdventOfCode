package aoc2024.day02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.main.AbstractMainMaster;

public class Main02 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main02()
            //.testMode()
           .start();
    }
    
    private List<Integer> parseLine(String line) {
    	return Arrays.stream(line.split(" ")).map(s->Integer.parseInt(s)).toList();
    }
    
    // antwoord : 559
    public Long star1() {
    	return streamInput(this::parseLine).filter(this::isSafe).count();
    }
    /**
     * Alle getallen van een rij moeten ofwel oplopend of aflopend zijn 
     * en verschil tussen 2 opeenvolgende getallen mag max 3 zijn.
     */
    private boolean isSafe(List<Integer> list) {
    	int diff=list.get(1)-list.get(0);
    	if(diff==0||Math.abs(diff)>3)
    		return false;
    	for(int i=2;i<list.size();i++) {
    		int idiff=list.get(i)-list.get(i-1);
    		if((diff>0 && (idiff<=0 || idiff >3))
    				||(diff<0 && (idiff>=0 || idiff<-3)))
    			return false;
    	}
    	return true;
    }
    
    // antwoord : 
    public Long star2() {
    	return streamInput(this::parseLine).filter(this::isValidReport2).count();
    }
    public boolean isValidReport2(List<Integer> list ) {
    	return isSafe(list) || isSafeSingleBad(list);
    }
    /**
     * Alle getallen van een rij moeten ofwel oplopend of aflopend zijn 
     * en verschil tussen 2 opeenvolgende getallen mag max 3 zijn.
     * 1 getal mag fout zijn
     * methode: we verwijderen 1 voor 1 een element en controleren de nieuwe rij.
     * return true van zodra een rij goed is
     */
    private boolean isSafeSingleBad(List<Integer> list) {
    	for(int i=0;i<list.size();i++) {
    		List<Integer> reduced=new ArrayList<>();
    		reduced.addAll(list);
    		reduced.remove(i);
    		if(isSafe(reduced))
    			return true;
    	}
    	return false;
    }
}