package aoc2020.day05;

public class Test {

	public static void test(String in, int expected)
	{
		int seat=new Main05().seatId(in);
		if(seat==expected)
			System.out.println(in+":"+seat);
		else
			System.err.println(in+":"+seat);
	}
	public static void main(String[] args) {
		test("BFFFBBFRRR",567);
		System.out.println("BFFFBBFRRR".replaceAll("[B,R]", "1").replaceAll("[F,L]", "0"));
		System.out.println(Integer.parseInt("BFFFBBFRRR".replaceAll("[B,R]", "1").replaceAll("[F,L]", "0"), 2));
	}

}
