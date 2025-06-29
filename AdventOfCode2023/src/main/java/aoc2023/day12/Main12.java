package aoc2023.day12;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

public class Main12 extends AbstractMainMaster<Long> {
	
    public static void main(String[] args) {
        new Main12()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }

    static class Opgave {
    	String springs;
    	String[] springParts;
    	int[] groups;
    }
    
	List<Opgave>input;
	int maxPatternDelta=0;
	
    @Override
    public void beforeEach() {
    	input=streamInput(this::parseLine).toList();
    	logln("Max PatternDelta="+maxPatternDelta);
    }
    
    private Opgave parseLine(String line) {
    	Opgave opgave=new Opgave();
    	String[] splitted=line.split(" ");
    	opgave.springs=splitted[0];
    	String[] groups=splitted[1].split(",");
    	opgave.groups=new int[groups.length];
    	int patternLength=0;
    	for(int i=0;i<groups.length;i++) {
    		opgave.groups[i]=Integer.parseInt(groups[i]);
    		patternLength+=opgave.groups[i];
    	}
    	patternLength+=opgave.groups.length-1;
    	maxPatternDelta=Math.max(maxPatternDelta, opgave.springs.length()-patternLength);
    	return opgave;
    }
    
    // antwoord : 
    public Long star1() {
    	long combos=0;
    	for(Opgave o:input) {
			boolean allMatched=true;
    		// vervang alle .+ door . en split de blokken
    		// .??.?##....##.  -> ??,?##,##
    		o.springParts= o.springs.replaceAll("\\.+", " ")
    				.replace("^\\.","")
    				.replace("\\.$","")
    				.trim().split(" ");
    		if(o.groups.length==o.springParts.length) {
    			// 1-op-1 mapping van blokken
    			long gcombo=1;
    			for(int b=0;b<o.groups.length;b++) {
    				long bcombos=matchingCombos(o.springParts[b],o.groups[b]);
    				if (bcombos>0) {
    		    		logln ("  "+o.springParts[b]+" "+o.groups[b]+":"+bcombos);
    					gcombo*=bcombos;
    				} else allMatched=false;
    			}
    			combos+=gcombo;
    		} else
    			allMatched=false;
    		if(!allMatched)
    			logln(o.springs+" -> "+StringUtils.join(o.springParts,","));
    	}
        return combos;
    }
    RegexTool QHQ=new RegexTool("(\\?*)(#+)(\\?*)");
    RegexTool QQQ=new RegexTool("(\\?+)");
    public long matchingCombos(String part,int size) {
    	if(part.length()==size ||part.startsWith("#")||part.endsWith("#"))
    		return 1;
    	if(QHQ.evaluate(part)) {
    		long c=1+Math.min(size-QHQ.group(2).length(),Math.min(QHQ.group(1).length(), QHQ.group(3).length()));
    		return c;
    	}
    	if(QQQ.evaluate(part))
    		return 1+part.length()-size;
    	return -1;
    }

    // antwoord : 
    public Long star2() {
        return null;

    }
}