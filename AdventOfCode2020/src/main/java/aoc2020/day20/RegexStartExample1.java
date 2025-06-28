package aoc2020.day20;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexStartExample1 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pattern p = Pattern.compile("bab");
		String input="aabbababbaaa";
		Matcher m = p.matcher(input);
		int index=0;
		while (m.find())
		{
			index+=m.start();
			System.out.println("Start position :" + (index) + ", End position : " + (index));
			System.out.println(p.matcher(input.substring(index,index+3)).find()?"yes":"no");
			
			m=p.matcher(input.substring(++index));
		}
	}
}
