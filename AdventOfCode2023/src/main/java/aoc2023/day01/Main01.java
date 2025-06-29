package aoc2023.day01;

import java.util.HashMap;
import java.util.Map;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

public class Main01 extends AbstractMainMaster<Long>{
    public static void main(String[] args) {
		new Main01()
        //.testMode()
        .start();
	}
    
    @Override
    public void beforeEach() {
    }
    
    // antwoord: 55477
    @Override
    public Long star1() {
        RegexTool firstNum=new RegexTool("[a-z]*([0-9]).*");
        RegexTool lastNum=new RegexTool(".*([0-9])[a-z]*");
        return streamInput()
        	.sequential()
        	.filter(line->firstNum.evaluate(line)&&lastNum.evaluate(line))
        	.map(l->Long.parseLong(firstNum.group(1)+lastNum.group(1)))
        	.reduce(0L, Long::sum);
    }

    enum Numbers {
    	zero,one,two,three,four,five,six,seven,eight,nine;
    }
    
    // 54431
    @Override
    public Long star2() {
    	Map<String,Long> numbers=new HashMap<>();
    	numbers.put("one",1L);
    	numbers.put("two", 2L);
    	numbers.put("three", 3L);
    	numbers.put("four", 4L);
    	numbers.put("five",5L);
    	numbers.put("six", 6L);
    	numbers.put("seven", 7L);
    	numbers.put("eight", 8L);
    	numbers.put("nine", 9L);
        RegexTool firstNumber=new RegexTool(".*?(one|two|three|four|five|six|seven|eight|nine|[0-9]).*",true);
        RegexTool lastNumber=new RegexTool(".*(one|two|three|four|five|six|seven|eight|nine|[0-9]).*?",true);
        return streamInput().sequential()
        	.filter(line->firstNumber.evaluate(line)&&lastNumber.evaluate(line))
        	.map(line->numbers.computeIfAbsent(firstNumber.group(1), Long::parseLong)*10
    			+numbers.computeIfAbsent(lastNumber.group(1), Long::parseLong))
        	.reduce(Long::sum).get();
    }
}
