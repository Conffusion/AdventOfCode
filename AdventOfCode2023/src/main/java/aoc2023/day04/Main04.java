package aoc2023.day04;

import java.util.List;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

/**
 * laad input in 2 DIM schema met 1e dim=rij, 2e dim=kolom zodat schema[0] de
 * eerste rij voorstelt.
 */
public class Main04 extends AbstractMainMaster<Long> {
	public static void main(String[] args) {
		new Main04()
				//.testMode()
				.nolog()
				.start();
	}

	List<Card> inputCards;
	
	@Override
	public void beforeAll() {
		inputCards=parseInput(this::parse);
	}

	// antwoord:21558
	@Override
	public Long star1() {
		long som=0L;
		for(Card card:inputCards) {
			som+=card.countWinning>0?(long) Math.pow(2,card.countWinning-1):0L;
		}
		return som;
	}

	// antwoord: 10425665
	@Override
	public Long star2() {
		long winningCardsCount=0;
		Card[] cards=inputCards.toArray(new Card[0]);
		for(int i=0;i<cards.length;i++) {
			Card card=cards[i];
			winningCardsCount+=card.copies;
			for (int w=1;w<=card.countWinning;w++) {
				cards[i+w].copies+=card.copies;
			}
		}
		return winningCardsCount;
	}
	
	static RegexTool cardPattern=new RegexTool("Card +([0-9]+): ([0-9 ]*) \\| ([0-9 ]*)",true);
	static RegexTool numbersPattern=new RegexTool(RegexTool.NUMBER_GROUP_PATTERN);
	
	private Card parse(String line) {
		cardPattern.evaluate(line);
		logln(""+cardPattern.intGroup(1)+": "+cardPattern.group(2)+" - "+cardPattern.group(3));
		return new Card(cardPattern.intGroup(1), numbersPattern.groupAllInt(cardPattern.group(2)),
				numbersPattern.groupAllInt(cardPattern.group(3)));
	}
}