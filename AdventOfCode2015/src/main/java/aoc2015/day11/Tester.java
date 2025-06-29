package aoc2015.day11;

public class Tester {

	public static void main(String[] args) {
		Main11 main=new Main11();
		main.currPassword=main.toNumbers("abcdffaa");
		System.out.println(main.isValid());
	}
}
