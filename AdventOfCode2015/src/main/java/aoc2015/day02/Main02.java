package aoc2015.day02;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import aoc2015.common.MainMaster;

public class Main02 extends MainMaster<Long> {

	public static void main(String[] args) {
		new Main02();
	}

	public Main02() {
		doStar1();
		doStar2();
	}
	
	List<Long[]> lines=super.loadInputByLine().stream().map(l->l.split("x"))
			.map(this::convert).collect(Collectors.toList());
	
	public Long star1() {
		return lines.stream()
		.map(this::opp)
		.collect(Collectors.summingLong(o->o));
	}
	private Long[] convert(String[]dim) {
		Long[] l= {Long.parseLong(dim[0]),Long.parseLong(dim[1]),Long.parseLong(dim[2])};
		return l;
	}
	private long opp(Long[] dim) {
		long l=dim[0], w=dim[1], h=dim[2];
		long side1=2*l*w, side2=2*l*h,side3=2*w*h;
		return side1+side2+side3+(Math.min(side1, Math.min(side2, side3))/2);
	}
	
	public Long star2() {
		return lines.stream()
		.map(dims->{Arrays.sort(dims);return dims;})
		.map(dims->(dims[0]+dims[1])*2+dims[0]*dims[1]*dims[2])
		.collect(Collectors.summingLong(o->o));
	}
}
