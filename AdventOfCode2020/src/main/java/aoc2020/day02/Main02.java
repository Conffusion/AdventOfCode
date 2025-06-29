package aoc2020.day02;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2020.common.MainMaster;

public class Main02 extends MainMaster {
	static Pattern linePattern = Pattern.compile("([\\d]*)-([\\d]*) (.?): ([a-zA-Z]*)");

	public void star1() throws Exception{
		long valid =loadInput(2, "input.txt").stream().filter((l)-> {
				Matcher m = linePattern.matcher(l);
				if (m.matches()) {
					int min = Integer.parseInt(m.group(1));
					int max = Integer.parseInt(m.group(2));
					String cs = m.group(3);
					String password = m.group(4);
					char charact=cs.charAt(0);
					long count = password.chars().filter(ch -> ch == charact).count();
					if (min <= count && count <= max)
					{
						log(l);
						return true;
					}
						
				}
				return false;
			}).count();
		System.out.println("star1:"+valid);
	}
	
	public void star2() throws Exception {
		long valid =loadInput(2, "input.txt").stream().filter((l)-> {
				Matcher m = linePattern.matcher(l);
				if (m.matches()) {
					int min = Integer.parseInt(m.group(1));
					int max = Integer.parseInt(m.group(2));
					char c = m.group(3).charAt(0);
					String password = m.group(4);
					return (password.length()>=max && 
							(password.charAt(min-1)==c ^ password.charAt(max-1)==c));
				}
				return false;
			}).count();
		System.out.println("star2:"+valid);
	}

	public static void main(String[] args) throws Exception {
		Main02 m=new Main02();
		m.log=false;
		m.timer(()-> m.star1());
		m.timer(()-> m.star2());
	}

}
