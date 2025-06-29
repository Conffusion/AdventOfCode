package aoc2020.day10;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import aoc2020.common.MainMaster;

public class Main10 extends MainMaster {
	List<Integer> data;
	
	public Main10() {
		data=loadInput(10,"input.txt").stream()
				.map(s -> Integer.parseInt(s)).collect(Collectors.toList());
		Collections.sort(data);
	}
	public void star1() {
		int[] turf=new int[3];
		int jolt=0;
		for(int x:data) {
			int i=x-jolt;
			turf[i-1]++;
			jolt+=i;
//			System.out.println("x:"+x+",i="+i+"turf:"+turf[0]+","+turf[1]+","+turf[2]);
		}
		info("***STAR1:"+(turf[0]*(turf[2]+1)));
	}

	public void star2() {
		int[] array = data.stream().mapToInt(i->i).toArray();
		int[] diffs=new int[array.length-1];
		for(int i=0;i<array.length-1;i++) {
			diffs[i]=array[i+1]-array[i];
		}
		long total=1;
		int ones=1;
		for (int i=0;i<diffs.length-1;i++)
		{
			// komt blijkbaar niet voor:
			// total*=diffs[i]+diffs[i+1]==3?2:1;
			if(diffs[i]==1)
				ones++;
			else {
				if(ones<=1);
				else if(ones==2) total*=2;
				else if(ones==3) total*=4;
				else if(ones==4) total*=7;
				else System.err.println("missing ones:"+ones);
				ones=0;
			}
//			System.out.println("x="+diffs[i]+", ones="+ones+", total:"+total);
		}
		ones ++;
		if(ones<=1);
		else if(ones==2) total*=2;
		else if(ones==3) total*=4;
		else if(ones==4) total*=7;
		else System.err.println("missing ones:"+ones);
		
		info("***STAR2:"+total);
	}
	
	public static void main(String[] args) throws Exception{
		Main10 m =new Main10();
		m.timer(()->m.star1());
		m.timer(()->m.star2());
	}

}
