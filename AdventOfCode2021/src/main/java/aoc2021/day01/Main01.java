package aoc2021.day01;

import java.util.LinkedList;
import java.util.List;

import common.main.AbstractMainMaster;


public class Main01 extends AbstractMainMaster<Long> {

	List<Long> input;
	LinkedList<Long> window;

	@Override
	public void beforeAll() {
		input=loadInputLongByLine();
	}
	
	@Override
	public void beforeEach() {
		window=new LinkedList<>();
		prev=-1;
	}

	// antwoord=1266
	@Override
	public Long star1() {
		return input
		.stream().filter(this::increase)
		.count();
	}

	private long prev;

	private boolean increase(long in) {
		boolean resp=prev!=-1 && in>prev;
		prev=in;
		return resp;
	}

	// antwoord 1217
	@Override
	public Long star2() {
		return input
		.stream().filter(this::increaseWindow)
		.count();		
	}
	
	private boolean increaseWindow(long in) {
		if (window.size()<3) {
			window.add(in);
			return false;
		}
		Long old=window.removeFirst();
		window.add(in);
		return old<in;
	}

	public static void main(String[] args) {
		new Main01().start();
	}
}
