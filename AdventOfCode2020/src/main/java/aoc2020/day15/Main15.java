package aoc2020.day15;

import java.util.HashMap;
import java.util.Map;

import aoc2020.common.MainMaster;


public class Main15 extends MainMaster {
	public Map<Integer,Integer> memory=new HashMap<>();

	public int addNumber(int num, int index) {
		
		int tmp=memory.getOrDefault(num, 0);
		if(log)log("*{"+num+"="+tmp+"->"+(index-1)+"}");
		memory.put(num,index-1);
		return tmp==0?0:index-1-tmp;
	}

	public void star(String input,long wanted) {
		memory=new HashMap<>();
		int i=1, last=0;
		int result=0;
		for(String s:input.split(",")) {
			i++;
			int n=Integer.parseInt(s);
			addNumber(n,i);
			if(log)logln("it "+i+"="+n);
			last=n;
		}
		i--;
		int out=0;
		while(i<wanted) {
			i++;
			out=last;
			if(log)log("it "+i+", in="+out+"->");
			last=addNumber(last,i);
			if(log)logln(""+last);
			if(i==wanted)result=last;
			if(i%1000000==0)
				System.out.println(i);
		}
		System.out.println("***STAR:"+wanted+"th:"+result);
	}
	
	public static void main(String[] args) {
		Main15 m=new Main15();
		m.log=false;
		// STAR 1: 2020th
		m.timer(()->m.star("1,20,11,6,12,0",2020));
		// STAR2: 30000000th. Answer=10652
		m.timer(()->m.star("1,20,11,6,12,0",30000000));
	}
}
