package aoc2020.day18;

import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2020.common.MainMaster;

public class Main18Star2 extends MainMaster {
	List<String> data;

	public void loadData(String filename) {
		data = super.loadInput(18, filename);
	}
	static Pattern somPattern=Pattern.compile("((\\d+)\\+(\\d+))");
	static Pattern maalPattern=Pattern.compile("((\\d+)\\*(\\d+))");
	
	public String calculate(String line,Pattern pattern,BiFunction<Long,Long,Long> func)
	{
		Matcher matcher;
		while((matcher = pattern.matcher(line)).find())
		{
			long sub = func.apply(Long.parseLong(matcher.group(2)),Long.parseLong(matcher.group(3)));
			line=line.substring(0, matcher.start())+sub+line.substring(matcher.end());
		}
		return line;		
		
	}
	public long calculatePart(String line) {
		line = line.replaceAll("[()]", "");
		
		line=calculate(line,somPattern,(l1,l2)->l1+l2);
		line=calculate(line,maalPattern,(l1,l2)->l1*l2);
		return Long.parseLong(line);
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

	public void star2() {
		long som = 0;
		for (String line : data) {
			som += calculate(line);
		}
		System.out.println("***STAR2:"+som);
		
	}

	// answer: 328920644404583  (75ms)
	public static void main(String[] args) {
		Main18Star2 m = new Main18Star2();
		m.loadData("input.txt");
		m.timer(()-> m.star2());
		System.out.println("expected:"+328920644404583l);
		//System.out.println(""+m.calculate("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"));
	}

}
