package aoc2020.day05;
import java.util.List;

import aoc2020.common.MainMaster;
public class Main05 extends MainMaster {
	
	List<String> data;
	
	public Main05() {
		data=loadInput(5, "input.txt");
	}
	public int seatId(String in)
	{
		return Integer.parseInt(in.replaceAll("[B,R]", "1").replaceAll("[F,L]", "0"), 2);
	}
	public void star1() throws Exception {
		info("star1:"+data.stream().mapToInt(s->seatId(s)).max().getAsInt());
	}
	public void star2() throws Exception {
		final int[] all=new int[884];
		data.stream().mapToInt(s->seatId(s)).forEach(i->all[i]=1);
		for (int i=7;i<883;i++)
			if(all[i-1]==1&&all[i+1]==1&&all[i]==0)
				info("star2:"+i);
	}
	public static void main(String[] args) throws Exception{
		Main05 m=new Main05();
		m.timer(()->m.star1());
		m.timer(()->m.star2());
	}
}