package aoc2024.day03;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.main.AbstractMainMaster;

public class Main03 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main03()
           // .testMode()
           .start();
    }

    String content;
    
    @Override
    public void beforeAll() {
		content=loadInputToString();
    }

    // antwoord : 183669043
    public Long star1() {
		Pattern pattern = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)");
		Matcher matcher = pattern.matcher(content);
		long som = 0L;
		while (matcher.find()) {
			long fac1 = Long.parseLong(matcher.group(1));
			long fac2 = Long.parseLong(matcher.group(2));
			som += fac1 * fac2;
		} 
        return som;
    }

    // antwoord : 59097164
    public Long star2() {
    	Pattern pattern=Pattern.compile("(mul)\\(([0-9]+),([0-9]+)\\)|(do)\\(\\)|(don\\'t)\\(\\)");
    	Matcher matcher = pattern.matcher(content);
		long som = 0L;
		boolean inDo=true;
		while (matcher.find()) {
			if(inDo && Objects.equals(matcher.group(1),"mul")) {
				som += Long.parseLong(matcher.group(2)) * Long.parseLong(matcher.group(3));
			}
			if(Objects.equals(matcher.group(4),"do"))
				inDo=true;
			if(Objects.equals(matcher.group(5),"don't"))
				inDo=false;
		} 
		return som;
    }
}