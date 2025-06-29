package aoc2020.day09;

import java.util.List;
import java.util.stream.Collectors;

import aoc2020.common.MainMaster;

public class Main09 extends MainMaster {
	List<Long> data;

	public Main09() {
		data=loadInput(9, "input.txt").stream()
				.map(s -> Long.parseLong(s)).collect(Collectors.toList());
	}
	
	// juiste antwoord: min+max = 76096372
	public long star2() {
		long sol = data.get(620);
		logln("zoeken voor " + sol);
		// i=beginindex
		for (int i = 0; i < 620; i++) {
			long sum = data.get(i);
			long min = sum;
			long max = sum;
			int j = i + 1;
			for (; sum < sol; j++) {
				long v=data.get(j);
				sum +=v;
				min = min>v?v:min;
				max = max<v?v:max;
			}
			if (sum == sol)
				return min + max;
		}
		System.out.println("niets gevonden");
		return 0;
	}

	public long detectError(int preamble) {
		long newnum;
		main_loop: for (int i = 0; i < data.size() - preamble; i++) {
			logln(i+preamble);
			List<Long> nums = data.subList(i, i + preamble);
			newnum = data.get(i + preamble);
			for (int j = 0; j < nums.size() - 1; j++)
				for (int k = j + 1; k < nums.size(); k++)
					if (nums.get(j) + nums.get(k) == newnum) {
						continue main_loop;
					}
			// no match found
			return newnum;
		}
		return -1;
	}

	public void star1(int preamble) {
		info("STAR1:" + detectError(25));
	}

	public static void main(String[] args) throws Exception {
		Main09 m=new Main09();
		m.log=false;
		m.timer(()->m.info("STAR1:" + m.detectError(25)));
		m.timer(()->m.info("STAR2:"+m.star2()));
	}

}
