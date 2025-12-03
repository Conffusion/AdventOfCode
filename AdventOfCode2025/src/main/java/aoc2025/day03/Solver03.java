package aoc2025.day03;

import java.util.List;
import java.util.stream.Collectors;

import common.io.Loader;
import common.main2.AoCLogger;
import common.main2.AoCRunner;
import common.main2.AoCSolver;

public class Solver03 extends AoCSolver<Long> {
	static AoCLogger logger=new AoCLogger();
	Loader loader=Loader.forMain(this).withTestMode(false).build();
	
	public static void main (String[] args) {
		new AoCRunner<>(new Solver03()).start();
	}
	List<Bank> banks;
	
    @Override
    public void beforeEach() {
    	banks=loader.streamInput(l->toBank(loader.parseDigitsLine(l))).toList();
    }
    private Bank toBank(int[] values) {
    	Battery[] batteries=new Battery[values.length];
    	for(int i=0;i<values.length;i++) {
    		batteries[i]=new Battery(values[i],i);
    	}
    	return new Bank(batteries);
    }
    
    // antwoord : 17144
    public Long star1() {
        return banks.parallelStream().collect(Collectors.summarizingLong(b->maxJoltage(b,2))).getSum();
    }

    private Battery highestInRange(Bank bank, int fromIndex, int toIndex) {
    	Battery highest=bank.batteries[fromIndex];
    	for(int i=fromIndex+1;i<toIndex;i++) {
    		if(bank.batteries[i].value>highest.value)
    			highest=bank.batteries[i];
    	}
    	return highest;
    }

    private long maxJoltage(Bank bank, int nrOfBatteries) {
    	long totValue=0;
    	int fromIndex=0;
    	int toIndex=bank.batteries.length-nrOfBatteries+1;
    	for (int batti=0;batti<nrOfBatteries;batti++) {
    		Battery toSwitchOn=highestInRange(bank,fromIndex,toIndex);
    		fromIndex=toSwitchOn.pos+1;
    		toIndex++;
    		totValue=totValue*10+toSwitchOn.value;
    	}
    	return totValue;
    }
    // antwoord : 170371185255900
    public Long star2() {
        return banks.parallelStream().map(b->maxJoltage(b,12)).collect(Collectors.summarizingLong(l->l)).getSum();
    }
    record Battery(int value, int pos) {};
    record Bank(Battery[]batteries) {};
}

