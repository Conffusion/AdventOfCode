package aoc2020.day14;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import aoc2020.common.MainMaster;

public class Main14Star2 extends MainMaster {

	// Voor deel 2 maakt deze de start set waarbij X-> 0
	static class ValueMask {
		long andMask=0L;
		long orMask=0L;
		ValueMask (String input) {
			for(int i=0;i<input.length();i++)
			{
				char c=input.charAt(i);
				orMask=orMask*2;
				andMask=andMask*2;					
				if(c=='1') {
					orMask++;
				} 
				if(c!='X') {
					andMask++;					
				}
			}
		}
		public long apply(long value) {
			return (value | orMask)&andMask;
		}
	}
	static class FloatingMask {
		String mask;
		ValueMask vmask;
		FloatingMask (String mask) {
			this.mask=mask;
			this.vmask=new ValueMask(mask);
		}
		
		public Set<Long> apply(long value) {
			Set<Long> result=new HashSet<>();
			
			long start=vmask.apply(value);
			result.add(start);
			for(int i=0;i<36;i++)
			{
				if(mask.charAt(i)=='X')
				{
					Set<Long> tmp=new HashSet<>();
					
					long macht=(long)Math.pow(2,35-i);
					// loop over all combinaties en verdubbel ze
					for(long l:result) {
						tmp.add(l); // 0
						tmp.add(l+macht); // 1
					}
					result=tmp;
				}
			}
			
			return result;
		}
	}
	Pattern maskPattern=Pattern.compile("mask = (.*)");
	Pattern memPattern=Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");
	
	// answer: 3683236147222
	public void star2() {
		Map<Long,Long> mem=new HashMap<>();
		FloatingMask mask=null;
		List<String> input=	loadInput(14, "input.txt");
		for(String line:input)
		{
			Matcher maskMatch=maskPattern.matcher(line);
			if(maskMatch.matches())
			{
				mask=new FloatingMask(maskMatch.group(1));
				continue;
			} 
			Matcher memMatch=memPattern.matcher(line);
			if(memMatch.matches()) {
				long index=Long.parseLong(memMatch.group(1));
				long value=Long.parseLong(memMatch.group(2));
				for(long l:mask.apply(index))
					mem.put(l,value);
			}
		}
		long sum=mem.values().stream().collect(Collectors.summingLong(Long::longValue));
		System.out.println("sum:"+sum);
	}
	
	public static void trycode() {

		ValueMask m=new ValueMask("000000000000000000000000000000X1001X");
		System.out.println("42="+Long.toBinaryString(42));
		System.out.println(Long.toBinaryString(m.apply(42))+"="+m.apply(42));
		double macht=Math.pow(2,3);
		System.out.println((long)macht);
	}
	public static void main(String[] args) {
		Main14Star2 m=new Main14Star2();
		m.timer(()-> m.star2());
		//m.trycode();
	}

}
