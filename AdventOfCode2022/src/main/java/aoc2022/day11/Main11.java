package aoc2022.day11;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

public class Main11 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main11()
            //.testMode()
            //.nolog()
           .start2();
    }

    Monkey[] monkeys=new Monkey[8];
    int nrOfMonkeys=8;
    
    @Override
	public void beforeAllTest() {
    	nrOfMonkeys=4;
    }

	@Override
    public void beforeAll() {
        monkeys=new Monkey[nrOfMonkeys];
    	List<String> input=loadInputByLine();
    	for (int m=0;m<nrOfMonkeys;m++) {
    		Monkey mon=new Monkey();
    		mon.name="Monkey "+m;
    		System.out.println(input.get(m*7+1).substring(18));
    		Arrays.stream(input.get(m*7+1).substring(18).split(", "))
    			.mapToLong(Long::parseLong)
    			.forEachOrdered(mon.items::add);
    		mon.operation=new Operation(input.get(m*7+2));
    		lastNumber.evaluate(input.get(m*7+3));
    		mon.divisionTest=lastNumber.intGroup(1);
    		lastNumber.evaluate(input.get(m*7+4));
    		mon.trueMonkey=lastNumber.intGroup(1);
    		lastNumber.evaluate(input.get(m*7+5));
    		mon.falseMonkey=lastNumber.intGroup(1);
    		monkeys[m]=mon;
    	}
    }

	// answer: 99840
    public Long star1() {
    	for(int r=1;r<=20;r++)
    		performRound(r);
    	Arrays.sort(monkeys);
        return monkeys[0].inspectCount*monkeys[1].inspectCount;
    }

    public void performRound(int round) {
    	for (int m=0;m<nrOfMonkeys;m++) {
    		Long item=null;
    		Monkey monk=monkeys[m];
    		while((item=monk.items.poll())!=null) {
    			long worry=monk.operation.apply(item)/3;
    			int targetMonkey=0;
        		monk.inspectCount++;
        		if(worry%monk.divisionTest==0)
        			targetMonkey=monk.trueMonkey;
        		else
        			targetMonkey=monk.falseMonkey;
        		worry=worry%monkeys[targetMonkey].divisionTest;
    			monkeys[targetMonkey].items.offer(worry);

    		}
    	}
    	if(round%100==0) {
	    	logln("-- After round "+round+" :");
	    	for (int m=0;m<nrOfMonkeys;m++) {
	    		logln(monkeys[m]);
	    	}
    	}
    }
    long verkleiner=1;
    public Long star2() {
    	for(Monkey m:monkeys)
    		verkleiner*=m.divisionTest;
    	for(int r=1;r<=10000;r++)
    		performRound2(r);
    	Arrays.sort(monkeys);
        return monkeys[0].inspectCount*monkeys[1].inspectCount;
    }
    
    public void performRound2(int round) {
    	for (int m=0;m<nrOfMonkeys;m++) {
    		Long item=null;
    		Monkey monk=monkeys[m];
    		while((item=monk.items.poll())!=null) {
    			long worry=monk.operation.apply(item);
    			int targetMonkey=0;
        		monk.inspectCount++;
        		if(worry%monk.divisionTest==0)
        			targetMonkey=monk.trueMonkey;
        		else
        			targetMonkey=monk.falseMonkey;
        		worry=worry%verkleiner;
    			monkeys[targetMonkey].catchItem(worry);

    		}
    	}
    	if(round%1000==0) {
	    	logln("-- After round "+round+" :");
	    	for (int m=0;m<nrOfMonkeys;m++) {
	    		logln(monkeys[m]);
	    	}
    	}
    }
    
    class Monkey implements Comparable<Monkey>{
    	String name;
    	Queue<Long> items=new LinkedList<>();
    	Operation operation;
    	int divisionTest;
    	int trueMonkey;
    	int falseMonkey;
    	long inspectCount=0;
    	
    	public int throwToMonkey(int worry) {
    		inspectCount++;
    		if(worry%divisionTest==0)
    			return trueMonkey;
    		else
    			return falseMonkey;
    	}
    	public void catchItem(long worry) {
    		items.offer(worry);
    	}
    	public String toString() {
    		StringBuffer buf=new StringBuffer(name+"[")
    		.append(inspectCount).append("] (");
    		items.forEach(i->buf.append(" ").append(i));
    		buf.append(")");
    		return buf.toString();
    	}

    	/**
    	 * sort descending by itemcount
    	 */
		@Override
		public int compareTo(Monkey moth) {
			return Long.compare(moth.inspectCount,inspectCount);
		}
    }
    
    RegexTool operationPattern=new RegexTool("  Operation: new = old ([*+]) (\\d+)");
    RegexTool lastNumber=new RegexTool(".* (\\d+)");
    
    class Operation {
    	String operation;
    	long factor;
    	public Operation(String formule) {
    		if(formule.equals("  Operation: new = old * old")) {
    			operation="^";    			
    		} else {
	    		operationPattern.evaluate(formule);
	    		operation=operationPattern.group(1);
    			factor=Integer.parseInt(operationPattern.group(2));
    		}
    	}
    	
    	public long apply(long value) {
    		if(operation.equals("*"))
    			return value*factor;
    		if(operation.equals("+"))
    			return value+factor;
    		if(operation.equals("^"))
    			return value*value;
    		return value;
    	}
    }
}