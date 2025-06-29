package aoc2023.day07;

import java.util.HashMap;
import java.util.Map;

public class Hand {

	// waarde van de combo
	String hand;
	long comboValue;
	// waarde van de some van de kaarten in hun volgord
	long cardsValue;
	long bid;
	
	public Hand(String hand, long bid) {
		this.hand = hand;
		this.bid = bid;
	}

	public Hand calculateStar1(String valueDefinition) {
		cardsValue=calculateCardsValue(valueDefinition);
		comboValue=calculateComboValue();
		return this;
	}
	public Hand calculateStar2(String valueDefinition) {
		cardsValue=calculateCardsValue(valueDefinition);
		comboValue=calculateComboValue2();
		return this;
	}
	
	public long calculateComboValue() {
		Map<Character,Integer> occurances=new HashMap<>();
		for(char card:hand.toCharArray()) {
			occurances.put(card, occurances.getOrDefault(card, 0)+1);
		}
		if(occurances.values().contains(5))
			return 7; // Five of a kind
		else if (occurances.values().contains(4))
			return 6; // Four of a kind
		else if (occurances.values().contains(3)&&occurances.values().contains(2))
			return 5; // Full house
		else if (occurances.values().contains(3))
			return 4; // Three of a kind
		else if (occurances.values().contains(2)&&occurances.size()==3)
			return 3;
		else if (occurances.values().contains(2))
			return 2;
		else
			return 1;
	}
	public long calculateComboValue2() {
		Map<Character,Integer> occurances=new HashMap<>();
		for(char card:hand.toCharArray()) {
			occurances.put(card, occurances.getOrDefault(card, 0)+1);
		}
		int jokers=occurances.getOrDefault('J',0);
		if(occurances.values().contains(5))
			return 7; // Five of a kind
		else if (occurances.values().contains(4)) {
			if(jokers>0)
				return 7; // 4J+X of 4X+J maken Five of a kind
			return 6;
		} else if (occurances.values().contains(3)&&occurances.values().contains(2)) {
			if(jokers==2||jokers==3)
				return 7; // 3X+2J of 3J+2X maken Five of a kind
			return 5; // Full house
		} else if (occurances.values().contains(3)) { // 3+1+1
			if(jokers>0)
				return 6; // 3X+J+Y of 3J+X+Y maken Four of a kind
			return 4; // Three of a kind
		} else if (occurances.values().contains(2)&&occurances.size()==3) {
			if (jokers==2)
				return 6; // 2X+2J+Y maken Four of a kind
			if(jokers==1)
				return 5; // 2X+J+2Y maken Full House
			return 3; // Double pair
		} else if (occurances.values().contains(2)) {
			if(jokers>0)
				return 4; // X+2J of 2X+J maken Three of a kind
			return 2; // Pair
		} else {
			if(jokers==1)
				return 2; // X+J
			return 1; // 5 verschillende
		}
	}
	
	private long calculateCardsValue(String valueDefinition) {
		long value=0;
		for(char c:hand.toCharArray()) {
			value=value*100+valueDefinition.indexOf(c)+1;
		}
		return value;
	}

	@Override
	public String toString() {
		return "Hand [hand=" + hand + ", comboValue=" + comboValue + ", cardsValue=" + cardsValue + ", bid=" + bid
				+ "]";
	}
	
}