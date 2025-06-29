package aoc2021.day07;

import java.util.Arrays;

import common.main.AbstractMainMaster;

public class Main07 extends AbstractMainMaster<Long> {

	int[] input;
	
	@Override
	public void beforeAll() {
		String[] splitted=loadInputToString().split(",");
		logln("array size:"+splitted.length);
		input=new int[splitted.length];
		for(int i=0;i<splitted.length;i++)
		{
			input[i]=Integer.parseInt(splitted[i]);
		}
	}

	@Override
	public Long star1() {
		long sum=0;
		long mediaan=0;
		Arrays.parallelSort(input);
		
		if(input.length%2==1)
			mediaan=input[input.length/2];
		else
			mediaan=input[(input.length+1)/2];
		for(int i=0;i<input.length;i++)
		{
			sum+=Math.abs(input[i]-mediaan);
		}
		return sum;
	}

	@Override
	public Long star2() {
		long mediaan=0;
		Arrays.parallelSort(input);
		if(input.length%2==1)
			mediaan=input[input.length/2];
		else
			mediaan=input[(input.length+1)/2];
		long medSum=star2Sum(mediaan);
		while(true) {
			logln("voor midden "+mediaan+":"+medSum);
			long newsum=star2Sum(mediaan+1);
			if(newsum>=medSum)
				break;
			mediaan++;
			medSum=newsum;
		}
		return medSum;
	}
	
	public long star2Sum(long mediaan) {
		long sum=0;
		for(int i=0;i<input.length;i++)
		{
			long delta=Math.abs(input[i]-mediaan);
			sum+=delta*(delta+1)/2;
		}
		return sum;
		
	}
	public static void main(String[] args) {
		new Main07()
		//.testMode()
		.start();
	}
}
