package aoc2024.day11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.BasicUtils;
import common.main.AbstractMainMaster;

public class Main11 extends AbstractMainMaster<Long> {

	public String input = "872027 227 18 9760 0 4 67716 9245696";
	public String testInput = "125 17";

	public static void main(String[] args) {
		new Main11()
				// .testMode()
				// .withFileOutput()
				.nolog()
				.start();
	}

	List<Long> stoneList;

	@Override
	public void beforeEach() {
		String stones = input;
		if (testMode)
			stones = testInput;
		stoneList=Arrays.stream(stones.split(" ")).map(s -> Long.parseLong(s)).toList(); //.forEach(l -> stoneList.add(l));
	}

	// antwoord : 199946
	public Long star1() {
		return blinkerMap(25);
	}

	// antwoord : 237994815702032
	public Long star2() {
		return blinkerMap(75);
	}

	private Long blinkerMap(int blinkCount) {
		long digitCnt = 0;
		// key= waarde van de steen ; value=aantal stenen met deze waarde
		Map<Long,Long> cache=new HashMap<>();
		for(Long l:stoneList)
			cache.put(l, 1L);
		for (int i = 0; i < blinkCount; i++) {
			if(log) logln("iteration:" + i);
			Map<Long,Long> newcache=new HashMap<>();
			for(Map.Entry<Long, Long> entry:cache.entrySet()) {
				Long stone=entry.getKey();
				if (stone == 0L) {
					addValue(newcache,1L,entry.getValue());
				} else if ((digitCnt = (int) Math.floor(Math.log10(stone) + 1)) % 2 == 0) {
					// even number of digits
					int halfdigit = (int) Math.pow(10, digitCnt / 2);
					addValue(newcache,stone / halfdigit,entry.getValue());
					addValue(newcache,stone % halfdigit,entry.getValue());
				} else {
					addValue(newcache,stone *2024,entry.getValue());
				}
			}
			cache=newcache;
		}
		// tel alle values op om het totaal aantal stenen te kennen
		return BasicUtils.streamSum(cache.values().stream(),v->v);
	}

	private void addValue(Map<Long,Long> map,Long key, Long value) {
		Long oldValue=map.getOrDefault(key, 0L);
		map.put(key, oldValue+value);
	}
}