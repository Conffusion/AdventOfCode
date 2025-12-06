package aoc2025.day05;

import java.util.ArrayList;
import java.util.List;

import common.io.Loader;
import common.main2.AoCLogger;
import common.main2.AoCRunner;
import common.main2.AoCSolver;

public class Solver05 extends AoCSolver<Long> {
	static AoCLogger logger=new AoCLogger();
	static boolean testMode=false;
	Loader loader=Loader.forMain(this).withTestMode(testMode).build();
	
	public static void main (String[] args) {
		//logger.nolog(true);
		AoCRunner<Long> runner=new AoCRunner<>(new Solver05());
		logger.nolog(true);
		if(testMode)
			runner.testMode();
		runner.start();
	}
	List<Range> freshRanges;
	List<Long> freshIds;
	
    @Override
    public void beforeEach() {
    	freshRanges=new ArrayList<>();
    	freshIds=new ArrayList<>();
    	loader.loadInputByLine().forEach(this::parseLine);
    	logger.logln("ranges:"+freshRanges.size());
    	logger.logln("ids:"+freshIds.size());
    }
    
    private void parseLine(String line) {
    	if(line.length()==0)
    		return;
    	if(line.contains("-")) {
    		freshRanges.add(new Range(Long.parseLong(line.split("-")[0]),Long.parseLong(line.split("-")[1])));
    	} else {
    		freshIds.add(Long.parseLong(line));
    	}
    }
    // antwoord : 758
    public Long star1() {
        return freshIds.parallelStream().filter(this::idInRange).count();
    }
    private boolean idInRange(Long id) {
    	boolean result= freshRanges.stream().filter(r->r.from()<=id&&r.to()>=id).findAny().isPresent();
    	if(result)
    		logger.logln(id);
    	return result;
    }
    // antwoord : 343143696885053
    public Long star2() {
        Long result=0L;
        freshRanges.sort((r1,r2)->Long.compare(r1.from, r2.from));
        Long lastTo=0L;
        for(Range r:freshRanges) {
        	Long newFrom=lastTo;
        	if(r.from>newFrom) {
        		newFrom=r.from();
        		result++;
        	}
        	if(r.to>newFrom) {
        		result+=(r.to-newFrom);
        		newFrom=r.to;
        	}
        	lastTo=newFrom;
        }
        return result;
    }
    record Range(long from, long to) {}
}

