package aoc2020.day22;

import java.util.LinkedList;
import java.util.Queue;

public class Player {
	String name;
	Queue<Integer> queue=new LinkedList<>();
	
	public void addCard(int card) {
		queue.add(card);
	}
	public int playCard() {
		return queue.poll();
	}
	public int nrOfCards() {
		return queue.size();
	}
	public int cardsValue() {
		int counter=nrOfCards();
		Integer card=0;
		int score=0;
		while((card=queue.poll())!=null) {
			score+=card*(counter--);
		}
		return score;
	}
}
