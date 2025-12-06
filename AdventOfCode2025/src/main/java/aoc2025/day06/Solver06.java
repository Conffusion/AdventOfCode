package aoc2025.day06;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.MathUtils;
import common.io.Loader;
import common.main2.AoCLogger;
import common.main2.AoCRunner;
import common.main2.AoCSolver;

public class Solver06 extends AoCSolver<Long> {
	static AoCLogger logger=new AoCLogger();
	static boolean testMode=false;
	Loader loader=Loader.forMain(this).withTestMode(testMode).build();
	
	public static void main (String[] args) {
		//logger.nolog(true);
		AoCRunner<Long> runner=new AoCRunner<>(new Solver06());
		if(testMode)
			runner.testMode();
		runner.start();
	}
	
	class Problem {
		List<Long> numbers=new ArrayList<>();
		String operator;
		Long calculate() {
			if(operator.equals("+"))
				return MathUtils.sumOf(numbers);
			else
				return MathUtils.multiplyOf(numbers);
		}
	}

    // antwoord : 4387670995909
    public Long star1() {
    	Map<Integer,Problem> problems=new HashMap<>();
    	int row=0;
    	for(String line: loader.loadInputByLine()) {
    		String[] elements=line.trim().split("\\s+");
    		if(row==0) {
    			for(int i=0;i<elements.length;i++) {
    				problems.put(i, new Problem());
    			}
    		}
    		if(elements[0].equals("+")||elements[0].equals("*")) {
    			for(int i=0;i<elements.length;i++) {
    				problems.get(i).operator=elements[i];
    			}
    		} else {
    			for(int i=0;i<elements.length;i++)
    				problems.get(i).numbers.add(Long.parseLong(elements[i]));
    		}
    		row++;
    	}
        return problems.values().parallelStream().mapToLong(Problem::calculate).sum();
    }

    // antwoord : 9625320374409
    public Long star2() {
    	List<char[]> input=new ArrayList<>();
    	for(String line: loader.loadInputByLine())
    		input.add(line.toCharArray());
        Long result=0L;
        int colsize=input.get(0).length;
        int operatorRow=input.size()-1;
    	List<Long> numbers=new ArrayList<>();
        for(int col=colsize-1;col>=0;col--) {
        	long l=0L;
        	for (int r=0;r<input.size()-1;r++) {
            	if(input.get(r)[col]==' ')
            		continue;
            	l=l*10+(input.get(r)[col]-'0');
        	}
        	numbers.add(l);
        	switch (input.get(operatorRow)[col]) {
        	case '+':
        		result+=MathUtils.sumOf(numbers);
        		numbers=new ArrayList<>();
        		col--; // skip empty column
        		break;
        	case '*':
        		result+=MathUtils.multiplyOf(numbers);
        		numbers=new ArrayList<>();
        		col--; // skip empty column        		
        		break;
        	default:
        	}
        }
        return result;
    }
}