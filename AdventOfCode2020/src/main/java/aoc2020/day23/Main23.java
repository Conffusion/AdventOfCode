package aoc2020.day23;

import aoc2020.day23.Ring.RingElement;
import common.main.AbstractMainMaster;

public class Main23 extends AbstractMainMaster<Long> {

	String input="315679824";
	
	public static void main(String[] args) {
		new Main23()
			.nolog()
			.start();
	}
	
	@Override
	public void beforeAll() {
		if(testMode)
			input="389125467";
	}
	long result=0;
	
	public Long star1() {
		Ring ring=new Ring();
		
		for(char c:input.toCharArray())
		{
			ring.add(c-'0');
		}
		ring.closeRing();
		for(int i=0;i<=100;i++)
		{
			Ring.RingElement part=ring.cut(3);
			ring.newPaste(part);
		}
		ring.goTo(1).next();
		ring.walk(this::concatResult);
		return result;
	}
	private void concatResult(RingElement elem) {
		if(elem.value!=-1)
			result=result*10+elem.value;
	}
	
	public Long star2() {
		Ring ring=new Ring();
		int max=0;
		for(char c:input.toCharArray())
		{
			ring.add(c-'0');
			max=(c-'0')>max?c-'0':max;
		}
		if(log) logln("completing ring starting from "+max);
		for(int i=max+1;i<=1000000;i++) {
			ring.add(i);
			
		}
		ring.closeRing();
		if(log) logln("ring finished and closed. size:"+ring.size());
		RingElement re=ring.elementOf(1);
		for(int i=0;i<=10000000;i++)
		{
			if (log) logln("iteratie "+i+":"+ring);
			if(log && i%100000==0)
				logln("*");
			Ring.RingElement part=ring.cut(3);
			ring.newPaste(part);
			if(log && i>9999997) {
				logln("\niteratie "+i+":"+re.value+","+re.next.value+","+re.next.next.value);				
			}
				
		}
//		RingElement re=ring.elementOf(1);
		if(log)logln("search 934001:"+ring.elementOf(934001).value+" - "+ring.elementOf(934001).next.value);
		long result=re.next.value;
		result=result*re.next.next.value;
		if(log)logln("***Star2:"+re.value+","+re.next.value+" x "+re.next.next.value+"="+result);
		return result;
	}
}
