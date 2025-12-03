package aoc2025.day02;

public class TestCode {

	public static void main(String[] args) {
		double in=10210.;
		double length=Math.ceil(Math.log10(in));
		if(length%2==0) {
			double halflength=Math.pow(10, length/2);
			double leftPart=Math.floor(in/halflength);
			double rightPart=in%halflength;
			System.out.println(leftPart==rightPart);
		}
	}

}
