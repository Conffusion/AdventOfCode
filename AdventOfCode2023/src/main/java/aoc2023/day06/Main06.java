package aoc2023.day06;

import java.util.List;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

/**
 * Niet geimplementeerd. Gewoon met de hand berekend
 */
public class Main06 extends AbstractMainMaster<Long> {
	public static void main(String[] args) {
		new Main06()
				.testMode()
				//.nolog()
				.start();
	}
	List<Long> raceTimes;
	List<Long> raceDistances;
	
	@Override
	public void beforeAll() {
		RegexTool numLine =new RegexTool(RegexTool.NUMBER_GROUP_PATTERN);
		List<String> lines=loadInputByLine();
		raceTimes=numLine.groupAllLong(lines.get(0));
		raceDistances=numLine.groupAllLong(lines.get(1));
	}
		
	// antwoord: 
	@Override
	public Long star1() {
		
		return 0L;
	}
}