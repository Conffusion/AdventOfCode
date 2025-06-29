package aoc2020.day03;

import java.util.List;

import common.main.AbstractMainMaster;

public class Main03 extends AbstractMainMaster<Long> {

	private List<String> bos;
	private int cols;

	public static void main(String[] args) throws Exception{
		new Main03()
		.nolog()
		.start();
	}
	
	@Override
	public void beforeEach() {
		bos=loadInputByLine();
		cols=bos.get(0).length();
		logln("row length:"+cols);
	}
	
	@Override
	public Long star1() {
		return slope(3,1);
	}
	// antwoord: 1592662500
	public Long star2() {
		if (log) info("product:"+(slope(1,1)*slope(3,1)*slope(5,1)*slope(7,1)*slope(1,2)));
		return slope(1,1)*slope(3,1)*slope(5,1)*slope(7,1)*slope(1,2);
	}

	// antwoord: 250
	public Long slope(int right, int down) {
		int col=-right; 
		long trees=0;
		for(int r=0;r<bos.size();r+=down) {
			String row=bos.get(r);
			col=(col+right)%cols;
			trees+=row.charAt(col)=='#'?1:0;
			if(log) logln("check ("+row+","+r+","+ col+"), bos:"+row.charAt(col)+"; trees:"+trees );
		};
		if(log) info("trees: ("+right+","+down+"):"+trees);
		return trees;
	}
}
