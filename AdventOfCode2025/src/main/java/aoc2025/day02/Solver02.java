package aoc2025.day02;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import common.io.Loader;
import common.main2.AoCLogger;
import common.main2.AoCRunner;
import common.main2.AoCSolver;

public class Solver02 extends AoCSolver<Long> {
	static AoCLogger logger=new AoCLogger();
	Loader loader=Loader.forMain(this).withTestMode(false).build();
	
	public static void main (String[] args) {
		//logger.nolog(true);
		new AoCRunner<>(new Solver02()).start();
	}
	List<Range> ranges;
	
    @Override
    public void beforeEach() {
    	ranges=Arrays.stream(loader.loadInputToString().split(","))
    	.map(s->s.split("-"))
    	.map(Solver02::toRange)
    	.toList();
    }
    static Range toRange(String[] boundaries) {
    	return new Range(Long.parseLong(boundaries[0]),Long.parseLong(boundaries[1]));
    }
    
    record Range(long from, long to) {}

    // antwoord : 52316131093
    // time: avg(100)=20ms
    public Long star1() {
    	return ranges.parallelStream().collect(Collectors.summingLong(this::processRangeStar1));
    }

    private long processRangeStar1(Range range) {
    	long result=0;
    	for(long id=range.from;id<=range.to;id++) {
    		double length=Math.ceil(Math.log10(id));
    		if(length%2==0) {
    			double halflength=Math.pow(10, length/2);
    			// controleer of linkerhelft = rechterhelft
    			if(Math.floor(id/halflength)==id%halflength)
    				result+=id;
    		}
    	}
    	return result;
    }
    // antwoord : 69564213293
    public Long star2() {
        return ranges.parallelStream().collect(Collectors.summingLong(this::processRangeStar2));
    }

    private long processRangeStar2(Range range) {
    	long result=0;
    	idLoop:
    	for(long id=range.from;id<=range.to;id++) {
    		double length=Math.ceil(Math.log10(id)); // aantal cijfers in id
    		subLoop:
    		for (int sublength=1;sublength<=length/2;sublength++) {
    			if(length%sublength==0) {
    				//length is een veelvoud van sublength dus we kunnen id opsplitsen in gelijke delen
    				String ids=Long.toString(id);
    				for(int si=sublength;si<length;si+=sublength) {
    					if(!isEqual(ids,0,sublength,ids,si))
    						continue subLoop;
    				}
    				result+=id;
    				continue idLoop; // om te vermijden dat we eenzelfde id 2 keer tellen
    			}
    		}
    	}
    	return result;
    }
    
    /**
     * Vergelijkt een substring van first in een deel van second zonder nieuwe substring objecten te maken.
     * Om performance reden is er geen controle op de lengte van de input strings.
     * @param first 
     * @param firstStartPos start positie in first voor de te vergelijken characters
     * @param length aantal te vergelijken characters
     * @param second tweede string
     * @param secStartPos start positie in second voor de te vergelijken characters
     * @return
     */
    private boolean isEqual(String first, int firstStartPos, int length, String second,int secStartPos) {
    	for (int i=0;i<length;i++) {
    		if(first.charAt(firstStartPos+i)!=second.charAt(secStartPos+i))
    			return false;
    	}
    	return true;
    }
}

