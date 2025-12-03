package aoc2025.day01;

import java.util.List;

import common.io.Loader;
import common.main2.AoCLogger;
import common.main2.AoCRunner;
import common.main2.AoCSolver;

public class Solver01 extends AoCSolver<Long> {
	static AoCLogger logger=new AoCLogger();
	Loader loader=Loader.forMain(this).withTestMode(false).build();
	List<Integer> instructions;
	
	public static void main (String[] args) {
		//logger.nolog(true);
		new AoCRunner<>(new Solver01()).start();
	}
	  
    @Override
    public void beforeEach() {
    	instructions=loader.streamInput(this::toInstruct).toList();
    }

    // antwoord : 1034
    public Long star1() {
        Long result=0L;
        int value=50;
        for(Integer inst:instructions) {
        	value=(value+inst)%100;
        	if(value==0)
        		result++;
        }
        return result;
    }

    // antwoord : 6166
    public Long star2() {
        Long result=0L;
        int value=50;
        int time=0;
        for(Integer inst:instructions) {
        	int oldValue=value;
    		value+=inst;
    		time=0;
    		if(value<=0) {
        		if(oldValue==0)
        			result--;
    			time=1+(-value)/100;
    			value=(value+100*time)%100;
        	} else if(value>=100){
        		time=value/100;
        	}
    		result+=time;
    		value=value%100;        	
        }
        return result;
    }
    
    private Integer toInstruct(String line) {
    	return (line.charAt(0)=='L'?-1:1)*Integer.parseInt(line.substring(1));
    }    
}