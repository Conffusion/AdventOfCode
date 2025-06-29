package aoc2020.day18;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2020.common.MainMaster;

public class Main18 extends MainMaster {
	List<String> data;

	public void loadData(String filename) {
		data = super.loadInput(18, filename);
	}

	public long calculatePart(String line) {
		long result = 0;
		line = line.replaceAll(" ", "").replaceAll("[()]", "");
		long currNum = 0;
		char lastOper='+';
		int pos=0;
		for (char c : line.toCharArray()) {
			if(c>='0' && c<='9')
				currNum=currNum*10+(c-'0');
			else
				break;
			pos++;
		}
		for (char c : line.substring(pos).toCharArray()) {
			if (c == '+' || c == '*') {
				if (lastOper == '+') {
					result += currNum;
					currNum = 0;
				} else {
					// *
					result *= currNum;
					currNum = 0;
				}
				lastOper=c;
			} else {
				// digit1-9
				currNum=currNum*10+(c-'0');				
			}
		}
		if (lastOper == '+') {
			result += currNum;
		} else {
			// *
			result *= currNum;
		}
		return result;
	}
	static final String PATTERN="(\\([^()]+\\))";
	static Pattern pattern = Pattern.compile(PATTERN);

	public long calculate(String line) {
		Matcher matcher;
		line = line.replaceAll(" ", "");
		while((matcher = pattern.matcher(line)).find())
		{
			long sub = calculatePart(matcher.group(1));
			line=line.substring(0, matcher.start())+sub+line.substring(matcher.end());
		}
		return calculatePart(line);
	}

	public void star1() {
		long som = 0;
		for (String line : data) {
			som += calculate(line);
		}
		System.out.println("***STAR1:"+som);
		
	}

	public static void main(String[] args) {
		Main18 m = new Main18();
		m.loadData("input.txt");
		m.star1();
		//System.out.println(""+m.calculate("9 * 6 * 2 * ((4 + 7 * 6 + 7 * 3 + 9) * 4 + 5 * 8) * 2 + 5"));
	}

}
