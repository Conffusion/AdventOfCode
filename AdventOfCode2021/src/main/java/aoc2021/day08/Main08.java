package aoc2021.day08;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import common.main.AbstractMainMaster;

public class Main08 extends AbstractMainMaster<Long> {

	List<Puzzle> puzzles;

	Pattern linePattern=Pattern.compile("([a-g]+) ([a-g]+) ([a-g]+) ([a-g]+) ([a-g]+) ([a-g]+) ([a-g]+) ([a-g]+) ([a-g]+) ([a-g]+) \\| ([a-g]+) ([a-g]+) ([a-g]+) ([a-g]+)");

	private class Puzzle {
		Map<Character,Integer> charOccurances=new HashMap<>();
		String[] signals=new String[10];
		int[] digits=new int[4];
		
		public Puzzle(String line) {
			Matcher matcher = linePattern.matcher(line);
			if(matcher.matches()) {
				buildSignals(matcher);
				buildDigits(matcher);
			}
		}
		private void buildSignals(Matcher matcher) {
			@SuppressWarnings("unchecked")
			List<String>[] imageBySize=new ArrayList[8];
			for(int i=2;i<=7;i++)
				imageBySize[i]=new ArrayList<>();
			for(int i=1;i<=10;i++)
			{
				String im=sortString(matcher.group(i));
				imageBySize[im.length()].add(im);
				for(char c:im.toCharArray()) {
					charOccurances.put(c,charOccurances.getOrDefault(c, 0)+1);
				}
			}
			Character c4=occurs(4).get(0);
			Character c6=occurs(6).get(0);
			signals[1]=imageBySize[2].get(0);
			signals[2]=imageBySize[5].stream().filter(s->in(c4,s)).findFirst().get();
			signals[4]=imageBySize[4].get(0);
			signals[5]=imageBySize[5].stream().filter(s->in(c6,s)).findFirst().get();
			signals[7]=imageBySize[3].get(0);
			Character cc=occurs(8).stream().filter(c->in(c,signals[1])).findFirst().get();
			signals[6]=imageBySize[6].stream().filter(s->!in(cc,s)).findFirst().get();
			signals[8]=imageBySize[7].get(0);
			signals[9]=imageBySize[6].stream().filter(s->!in(c4,s)).findFirst().get();
			signals[3]=imageBySize[5].stream().filter(s->!s.equals(signals[2])&&!s.equals(signals[5])).findFirst().get();
			signals[0]=imageBySize[6].stream().filter(s->!s.equals(signals[6])&&!s.equals(signals[9])).findFirst().get();
			
		}
		public String sortString(String in) {
			char[] chars=in.toCharArray();
			Arrays.parallelSort(chars);
			return new String(chars);
		}
		
		private void buildDigits(Matcher matcher) {
			int digit=0;
			digitLoop:
			for (int i=11;i<=14;i++) {
				String in=matcher.group(i);
				in=sortString(in);
				for(int s=0;s<signals.length;s++) {
					if (signals[s].equals(in))
					{
						digits[digit++]=s;
						continue digitLoop;
					}
				}
				logln("ERROR: digit "+in+" not found in "+logArray(signals));
			}
		}
		
		private List<Character> occurs(int number) {
			return charOccurances.entrySet().stream().filter(e->e.getValue()==number).map(e->e.getKey()).collect(Collectors.toList());
		}
		private boolean in (char c, String s)
		{
			return s.indexOf(c)>-1;
		}
		public int countForStar1() {
			int counter=0;
			for(int i=0;i<digits.length;i++)
				if(digits[i]==1||digits[i]==4||digits[i]==7||digits[i]==8)
					counter++;
			return counter;
		}
		public long countForStar2() {
			return digits[0]*1000+digits[1]*100+digits[2]*10+digits[3];
		}
	}
	@Override
	public Long star1() {
		return (long)streamInput(s->new Puzzle(s)).map(p->p.countForStar1()).collect(Collectors.summingInt(Integer::intValue));
	}

	@Override
	public Long star2() {
		return (long)streamInput(s->new Puzzle(s)).map(p->p.countForStar2()).collect(Collectors.summingLong(Long::longValue));
	}

	public static void main(String[] args) {
		new Main08()
		//.testMode()
		.start();
	}

}
