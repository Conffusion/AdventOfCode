package aoc2020.day14;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import aoc2020.common.MainMaster;

public class Main14 extends MainMaster {

	static class Mask {
		long andMask=0L;
		long orMask=0L;
		Mask (String input) {
			for(int i=0;i<input.length();i++)
			{
				char c=input.charAt(i);
				orMask=orMask*2;
				andMask=andMask*2;					
				if(c=='1') {
					orMask++;
					andMask++;
				} 
				if(c=='X') {
					andMask++;					
				}
				// if(c=='0') -> nothing to do
			}
		}
		public long apply(long value) {
			return (value | orMask)&andMask;
		}
	}
	Pattern maskPattern=Pattern.compile("mask = (.*)");
	Pattern memPattern=Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");
	
	// Oplossing: 17028179706934
	public void star1() {
		Map<Long,Long> mem=new HashMap<>();
		Mask mask=null;
		List<String> input=	loadInput(14, "input.txt");
		for(String l:input)
		{
			Matcher maskMatch=maskPattern.matcher(l);
			if(maskMatch.matches())
			{
				mask=new Mask(maskMatch.group(1));
				continue;
			} 
			Matcher memMatch=memPattern.matcher(l);
			if(memMatch.matches()) {
				long index=Long.parseLong(memMatch.group(1));
				long value=Long.parseLong(memMatch.group(2));
				mem.put(index,mask.apply(value));
			}
		}
		long sum=mem.values().stream().collect(Collectors.summingLong(Long::longValue));
		System.out.println("sum:"+sum);
	}
	
	public static void trycode() {
		long s=1;
		for(long l=0;l<35;l++)
			s=s*2+1;
	
		System.out.println(s);
		System.out.println(Long.toBinaryString(s));
		System.out.println("64="+Long.toBinaryString(64));
		System.out.println("11="+Long.toBinaryString(11));
		System.out.println(Long.toBinaryString((64|11)&(s-2)));

		Mask m=new Mask("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X");
		System.out.println(Long.toBinaryString(m.apply(11))+"="+m.apply(11));
		
	}
	public static void main(String[] args) {
		Main14 m=new Main14();
		m.star1();
	}

}
