package aoc2022.day03;

import java.util.List;
import java.util.function.BiConsumer;

import common.main.AbstractMainMaster;


/**
 * Basis voor de Main
 * T : data type van antwoord
 */
public class Main03 extends AbstractMainMaster<Long> {

	List<String> lineInput;
	
	public static void main(String[] args) {
		new Main03()
			//.testMode()
			.start();
	}
	@Override
	public void beforeAll() {
		lineInput=loadInputByLine();
	}

	// antwoord: 7811
	@Override
	public Long star1() {
		return lineInput.stream()
			.map(this::toRuckSack)
			.map(RuckSack::commonItem)
			.mapToLong(this::calculate1)
			.sum();
	}

	private long calculate1(char c) {
		long prior=c>96?c-96:c-38;
		return prior;
	}

	// antwoord: 2639
	@Override
	public Long star2() {
		long som=0;
		for (int g=0;g<lineInput.size()-1;g=g+3) {
			char[] badge=commonItems(commonItems(lineInput.get(g).toCharArray(), lineInput.get(g+1).toCharArray()),lineInput.get(g+2).toCharArray());			
			som+=calculate1(badge[0]);
		}
		return som;
	}

	public RuckSack toRuckSack(String cont) {
		char[] in=cont.toCharArray();
		char[] comp1=new char[in.length/2];
		char[] comp2=new char[in.length/2];
		System.arraycopy(in, 0, comp1, 0, comp1.length);
		System.arraycopy(in, in.length/2, comp2, 0, comp2.length);
		return new RuckSack(comp1,comp2);
	}

	public class RuckSack {
		char[] compartment1,compartment2;

		public RuckSack(char[] compartment1,char[] compartment2) {
			this.compartment1 = compartment1;
			this.compartment2 = compartment2;
		}

		public char commonItem() {
			return commonItems(compartment1,compartment2)[0];
		}
	}

	public char[] commonItems(char[] compartment1,char[] compartment2) {
		StringBuffer sb=new StringBuffer();
		crossCompare(compartment1, compartment2, (c1,c2)-> {
				if (c1.charValue()==c2.charValue())
					sb.append(c1.charValue());
		});
		return sb.toString().toCharArray();
	}

	public void crossCompare(char[] arr1,char[] arr2,BiConsumer<Character,Character> consumer) {
		for(int i=0;i<arr1.length;i++) {
			for (int j=0;j<arr2.length;j++) {
				consumer.accept(arr1[i], arr2[j]);
			}
		}
		
	}
}
