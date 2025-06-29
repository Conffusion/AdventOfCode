package aoc2023.day08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import common.BasicUtils;
import common.main.AbstractMainMaster;
import common.regex.RegexMatch;
import common.regex.RegexMatchBuilder;

public class Main08 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main08()
            //.testMode()
            .nolog()
           .start2(100);
    }

    char[] instructions;
    Map<String,Pair<String,String>> network;
    RegexMatchBuilder linePattern=new RegexMatchBuilder("([A-Z0-9]+) = \\(([A-Z0-9]+), ([A-Z0-9]+)\\)",true);
    
    @Override
    public void beforeEach() {
    	List<String> lines=null;
    	if(testMode && onStar==2)
    		lines=loadInputByLine("testinput2.txt");
    	else
    		lines=loadInputByLine();
    	instructions=lines.get(0).toCharArray();
    	lines.remove(0); // instruction line
    	lines.remove(0); // blank line
    	network=new HashMap<>();
    	lines.forEach(this::parseLine);
    }

    public void parseLine(String line) {
    	RegexMatch match=linePattern.evaluate(line);
    	network.put(match.group(1), Pair.of(match.group(2),match.group(3)));
    }
    
    // antwoord : 16271
    public Long star1() {
    	int instrIndex=0;
    	long steps=0;
    	String pos="AAA";
    	while(true) {
    		pos=instructions[instrIndex]=='L'?network.get(pos).getLeft():network.get(pos).getRight();
    		logln("nextpos="+pos);
    		steps++;
    		if(pos.equals("ZZZ"))
    			break;
    		instrIndex=(instrIndex+1)%instructions.length;
    	}
        return steps;
    }

    /**
     * Dit werkt maar gaat van een veronderstelling uit dit niet waar hoeft te zijn.
     */
    // antwoord: 14265111103729
    public Long star2() {
    	// verzamel alle punten die eindigen op 'A'
    	String[] pos=BasicUtils.toArray(network.keySet().stream().filter(s->s.endsWith("A")).collect(Collectors.toList()));
    	// houdt voor elk startpunt '..A' bij hoeveel steps tot aan de '..Z'
    	long[] stepPerPos=new long[pos.length];
    	int pathsDone=0;
    	logln("Aantal startposities:"+pos.length);
    	int instrIndex=0;
    	long steps=0;
    	while(true) {
    		for (int i=0;i<pos.length;i++) {
    			if(stepPerPos[i]==0) {
    				pos[i]=instructions[instrIndex]=='L'?network.get(pos[i]).getLeft():network.get(pos[i]).getRight();
    				if(pos[i].endsWith("Z")) { 
    					stepPerPos[i]=steps+1;
    					pathsDone++;
    				}
    			}
    		}
    		steps++;
    		if(pathsDone==pos.length)
    			break;
    		instrIndex=(instrIndex+1)%instructions.length;
    		if(steps%100000000==0) logln("instructionloop begin at step "+steps);
    	}
    	// nu bevat pathsDone de lengte van elk path tot aan de Z
    	// KGV nemen
    	List<Map<Long, Long>> primeFactors=new ArrayList<>();
    	for(int p=0;p<stepPerPos.length;p++) {
    		logln("Path "+p+" steps: #"+ stepPerPos[p]);
    		primeFactors.add(BasicUtils.getPrimeFactors(stepPerPos[p]));
    	}
    	return BasicUtils.kgmByFactors(primeFactors);
    }

	/**
	 * Dit is eigenlijk een juist implementatie maar het duurt te lang
	 * @return
	 */
    public Long star2correct() {
    	// een element voor elk pad met naam op ..A
    	String[] pos=BasicUtils.toArray(network.keySet().stream().filter(s->s.endsWith("A")).collect(Collectors.toList()));
    	logln("Aantal startposities:"+pos.length);
    	int instrIndex=0;
    	long steps=0;
    	while(true) {
    		boolean allZ=true;
    		for (int i=0;i<pos.length;i++) {
    			pos[i]=instructions[instrIndex]=='L'?network.get(pos[i]).getLeft():network.get(pos[i]).getRight();
    			allZ=allZ&&pos[i].endsWith("Z");
    		}
    		steps++;
    		if(allZ)
    			break;
    		instrIndex=(instrIndex+1)%instructions.length;
    		if(steps%100000000==0) logln("instructionloop begin at step "+steps);
    	}
        return steps;
    }
}