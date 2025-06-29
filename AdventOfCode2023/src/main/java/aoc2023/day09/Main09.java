package aoc2023.day09;

import java.util.List;

import common.BasicUtils;
import common.main.AbstractMainMaster;
import common.regex.RegexTool;

public class Main09 extends AbstractMainMaster<Integer> {
    public static void main(String[] args) {
        new Main09()
            //.testMode()
            .nolog()
            .start();
    }

    List<int[]> input;
    
    @Override
    public void beforeAll() {
    	input=streamInput(this::parseLine).toList();
    }
    RegexTool linePattern=new RegexTool(RegexTool.NUMBER_NEG_GROUP_PATTERN);
    
    private int[] parseLine(String line) {
    	return BasicUtils.toIntArray(linePattern.groupAllInt(line));
    }
    
    // antwoord : 1980437560
    public Integer star1() {
    	return input.stream().map(this::volgendGetal).map(s->logln(s)).reduce(0, Integer::sum);
    }
    
    private int volgendGetal(int[] rij) {
    	return rij[rij.length-1]+innerVolgendGetal(rij);
    }
    
    private int innerVolgendGetal(int[] rij) {
    	int[] nextRij=new int[rij.length-1];
    	boolean allZero=true;
    	for(int i=1;i<rij.length;i++) {
    		allZero=(nextRij[i-1]=rij[i]-rij[i-1])==0 && allZero;
    	}
    	if(allZero)
    		return 0;
    	int nieuw= innerVolgendGetal(nextRij);
    	return nieuw+nextRij[nextRij.length-1];
    }
    
    // antwoord : 977
    public Integer star2() {
    	return input.stream().map(this::vorigGetal).map(s->logln(s)).reduce(0, Integer::sum);
    }
    
    private int vorigGetal(int[] rij) {
    	return rij[0]-innerVorigGetal(rij);
    }
    
    private int innerVorigGetal(int[] rij) {
    	int[] nextRij=new int[rij.length-1];
    	boolean allZero=true;
    	for(int i=1;i<rij.length;i++) {
    		allZero=(nextRij[i-1]=rij[i]-rij[i-1])==0&&allZero;
    	}
    	if(allZero)
    		return 0;
    	int nieuw=innerVorigGetal(nextRij);
    	return nextRij[0]-nieuw;
    }
}