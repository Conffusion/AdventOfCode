package aoc2015.day04;

import org.apache.commons.codec.digest.DigestUtils;

import aoc2015.common.MainMaster;

public class Main04 extends MainMaster<Long> {

	public static void main(String[] args) {
		new Main04();
	}

	public Main04() {
		log=false;
		doStar1();
		doStar2();
	}
		
	// answer: 117946
	@Override
	public Long star1() {
    	return min(-1l,"00000");
	}

	// answer: 3938038
	@Override
	public Long star2() {
		return min(-1l,"000000");
	}

	private long min(long suffix,String check) {
		while(!DigestUtils.md5Hex("ckczppom"+(++suffix)).startsWith("00000"));
    	return suffix;
	}

}
