package aoc2023.day04;

import java.util.List;

public class Card {
	int number;
	int copies=1;
	long countWinning=0;
	
	public Card(int number, List<Integer> winning, List<Integer> allnumbers) {
		this.number = number;
		countWinning=winning.stream()
				.filter(i->allnumbers.contains(i))
				.count();
	}
}
