package aoc2020.day22;

import java.util.List;

import aoc2020.common.MainMaster;

public class Main22 extends MainMaster {

	Player player1,player2;
	public void loadData(String filename) {
		List<String> data=super.loadInput(22, filename);
		Player current=null;
		for (String s:data) {
			if(s.startsWith("Player"))
			{
				String playerid=s.substring(7, 8);
				if (playerid.equals("1")) {
					player1=new Player();
					current=player1;
				} else {
					player2=new Player();
					current=player2;
				}
				current.name=s;
			}
			else if (s.length()==0) {
				
			} else {
				// card
				current.addCard(Integer.parseInt(s));
			}
		}
		System.out.println("Player1 has "+player1.nrOfCards()+" cards");
		System.out.println("Player2 has "+player2.nrOfCards()+" cards");
	}
	
	public void star1() {
		loadData("input.txt");
		while(player1.nrOfCards()>0 && player2.nrOfCards()>0)
		{
			int card1=player1.playCard();
			int card2=player2.playCard();
			if(card1>card2) {
				player1.addCard(card1);
				player1.addCard(card2);
				System.out.println("Player1 wins: ("+card1+","+card2+"). P1("+player1.nrOfCards()+")P2("+player2.nrOfCards()+")");
			} else {
				player2.addCard(card2);
				player2.addCard(card1);
				System.out.println("Player2 wins: ("+card1+","+card2+")");
			}
		}
		Player winner=(player1.nrOfCards()==0?player2:player1);
		System.out.println("Winner is:"+winner.name);
		System.out.println("Cards value:"+winner.cardsValue());
	}
	public static void main(String[] args) {
		Main22 m=new Main22();
		m.star1();
	}

}
