package aoc2021.day06;

import common.main.AbstractMainMaster;

public class Main06 extends AbstractMainMaster<Long> {

	long[]input;
	
	@Override
	public void beforeEach() {
		input=new long[9];
		for(String s:loadInputToString().split(","))
		{
			input[Integer.parseInt(s)]++;
		}
	}

	private Long evolution(int days, long[]startbuf) {
		long[] buf1=startbuf, buf2=new long[9];
		for (int day=1;day<=days;day++)
		{
			System.arraycopy(buf1, 1, buf2, 0, 8);
			buf2[8]=buf1[0];
			buf2[6]+=buf1[0];
			System.arraycopy(buf2, 0, buf1, 0, buf2.length);
		}
		return sumArray(buf1);
	}
	private long sumArray(long[] array) {
		long sum=0l;
		for(int i=0;i<9;i++)
			sum+=array[i];
		return sum;
	}

	// antwoord: 362666
	@Override
	public Long star1() {
		return evolution(80,input);
	}

	// antwoord: 1640526601595
	@Override
	public Long star2() {
		return evolution(256,input);
	}

	public static void main(String[] args) {
		new Main06()
		//.testMode()
		.nolog()
		.start();
	}

}
