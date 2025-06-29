package aoc2023.day05;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

/**
 * laad input in 2 DIM schema met 1e dim=rij, 2e dim=kolom zodat schema[0] de
 * eerste rij voorstelt.
 */
public class Main05 extends AbstractMainMaster<Long> {
	public static void main(String[] args) {
		new Main05()
				//.testMode()
				//.nolog()
				.start();
	}
	
	List<Long> seeds=null;
	// key.value1= source range start
	// key.value2 = range length
	// value = destination delta
	List<NavigableMap<Long,Range>> maps=new ArrayList<NavigableMap<Long,Range>>();

	@Override
	public void beforeAll() {
		int mapIndex=-1;
		List<String> input=loadInputByLine();
		// first line: seeds
		RegexTool mapTitleLine=new RegexTool("([a-z]+)-to-([a-z]+) map:");
		RegexTool mapLine=new RegexTool("([0-9]+) ([0-9]+) ([0-9]+)");
		RegexTool seedsLine =new RegexTool(RegexTool.NUMBER_GROUP_PATTERN);
		seeds= seedsLine.groupAllLong(input.get(0).split(":")[1].trim());
		for(String line:input) {
			if(line.startsWith("seeds:"))
				continue;
			if(StringUtils.isBlank(line))
				continue;
			if (mapTitleLine.evaluate(line))
			{
				mapIndex++;
				logln("nieuwe map ["+mapIndex+"]:"+line);
				maps.add(new TreeMap<>());
				continue;
			}
			if(mapLine.evaluate(line))
			{
				// index1=destination ; index2=source ; index3=length
				maps.get(mapIndex).put(mapLine.longGroup(2),new Range(mapLine.longGroup(2), mapLine.longGroup(3),mapLine.longGroup(1)-mapLine.longGroup(2)));
			}
		}
		
	}
		
	// antwoord: 650599855
	@Override
	public Long star1() {
		long lowestSeedValue=Long.MAX_VALUE;
		for(Long seed:seeds) {
			logln("seed:"+seed);
			Long number=seed;
			for(NavigableMap<Long,Range> map:maps) {
				Entry<Long,Range> entry=map.floorEntry(number);
				if(entry!=null&& entry.getValue().isInRange(number)) {
					number+=entry.getValue().destination;
				}
			}
			if(number<lowestSeedValue) {
				lowestSeedValue=number;
			}
		}
		return lowestSeedValue;
	}

	// antwoord: 1240035
	@Override
	public Long star2() {
		long lowestSeedValue=Long.MAX_VALUE;
		Long number=0L;
		int seedIndex=0;
		long lowestSeed=0;
		while(seedIndex+1<seeds.size()) {
			for(long seed=seeds.get(seedIndex);seed<seeds.get(seedIndex)+seeds.get(seedIndex+1);seed++) {
				number=seed;
				for(NavigableMap<Long,Range> map:maps) {
					Entry<Long,Range> entry=map.floorEntry(number);
					if(entry!=null&& entry.getValue().isInRange(number)) {
						number+=entry.getValue().destination;
					}
				}
				if(number<lowestSeedValue) {
					lowestSeedValue=number;
					lowestSeed=seed;
				}
			}
			logln("after seed "+(seeds.get(seedIndex)+seeds.get(seedIndex+1)-1)+":"+lowestSeed+"/"+lowestSeedValue);
			seedIndex+=2;
		}
		return lowestSeedValue;
	}
}