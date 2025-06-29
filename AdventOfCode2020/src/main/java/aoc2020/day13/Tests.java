package aoc2020.day13;

public class Tests {

	public static void main(String[] args) {
		System.out.println("3417%13:"+(3417%13));
		System.out.println("3417+2 %13:"+((3417+2)%13));
		long start=100000000000000L;
		start=start-start%23;
		System.out.println(start%23);
		long lcm=Main13.lcm(7, 13);
		System.out.println(Main13.lcm(7, 13)+"="+lcm);
		lcm=Main13.lcm(lcm, 19);
		System.out.println("lcm+19:"+lcm);
	}

}
