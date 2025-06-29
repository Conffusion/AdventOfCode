package aoc2024.day05;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import common.graph.PlantUMLGenerator;
import common.main.AbstractMainMaster;

public class Main05 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main05()
            //.testMode()
            //.withFileOutput()
            .nolog()
           .start();
    }
    private Map<Integer,Set<Integer>> pageOrder=new HashMap<>();
    private List<int[]> updates=new ArrayList<>();
    
    @Override
    public void beforeEach() {
    	parseInput( this::processLine);
    	File outfile=new File(currentFolder(),(testMode?"test":"")+"manuals.puml");
    	try (PlantUMLGenerator umlGen=new PlantUMLGenerator(outfile)) {
    		umlGen.addNode("start");
			for(Entry<Integer,Set<Integer>> e:pageOrder.entrySet()) {
				for(Integer dest:e.getValue()) {
					umlGen.addConnection(""+e.getKey(), ""+dest);
				}
			}
		} catch (IOException iox) {
		}
    }
    Pattern pattern1=Pattern.compile("([0-9]+)\\|([0-9]+)");
    Pattern pattern2=Pattern.compile("^[0-9,]+$");
    
    private int processLine(String line) {
    	Matcher m1=pattern1.matcher(line);
    	if(m1.matches()) {
    		pageOrder.computeIfAbsent(Integer.parseInt(m1.group(1)),v-> new HashSet<Integer>())
    			.add(Integer.parseInt(m1.group(2)));
    	} else {
    		Matcher m2=pattern2.matcher(line);
    		if(m2.matches()) {
    			updates.add(parseNumbersLine(line, ","));
    		}
    	}
    	return 0;
    }
    
    // antwoord : 5639
    public Long star1() {
    	long som=0;
    	for(int[] pagelist:updates) {
    		if(checkOrder(pagelist)) {
    			logln("is valid "+pagelist+" -> "+pagelist[pagelist.length/2]);
    			som+=pagelist[pagelist.length/2];
    		} else {
    			logln("is not valid "+pagelist);
    		}
    	}
        return som;
    }
    private boolean checkOrder(int[] pagelist) {
		for(int t1=0;t1<pagelist.length-1;t1++) {
			for(int t2=t1+1;t2<pagelist.length;t2++) {
				if(!pageOrder.containsKey(pagelist[t1])) {
					return false;
				} else if(!pageOrder.get(pagelist[t1]).contains(pagelist[t2]))
					return false;
			}
		}
		return true;
    }
    
    // antwoord : 5273
    public Long star2() {
    	long som=0;
		// sorteer pagelist volgens pageOrder rules
    	Comparator<Integer> comp=new Comparator<>() {
		    public int compare(Integer o1, Integer o2) {
		    	if(pageOrder.containsKey(o1)&&pageOrder.get(o1).contains(o2))
		    		return -1;
		    	else return 1;
		    }
		};
    	for(int[] pagelist:updates) {
    		if(!checkOrder(pagelist)) {
    			final Integer[] sorted = ArrayUtils.toObject(pagelist);
    			Arrays.sort(sorted, comp);
    			som+=sorted[pagelist.length/2];
    		}
    	}
        return som;
    }
}