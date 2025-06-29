package aoc2023.day07;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import common.main.AbstractMainMaster;
import common.regex.RegexMatchBuilder;

/**
 * Evalueer een lijst van telkens 5 speelkaarten door pokervormen te detecteren.
 * Sorteer de kaarten volgens hun score
 */
public class Main07 extends AbstractMainMaster<Long> {
	static final String INPUT_PATTERN="([2-9TJQKA]{5}) ([0-9]+)";
	static RegexMatchBuilder linePattern=new RegexMatchBuilder(INPUT_PATTERN);
	
	public static void main(String[] args) {
		new Main07()
				//.testMode()
				.nolog()
				.withFileOutput()
				.start();
	}
	
	List<Hand> hands;

	@Override
	public void beforeEach() {
		hands=streamInput(linePattern::evaluate).map(m->new Hand(m.group(1),m.longGroup(2))).collect(Collectors.toList());
	}

	// antwoord: 251106089
	@Override
	public Long star1() {
		// de positie van het character in de lijst bepaalt zijn waarde
		String CARD_VALUES="23456789TJQKA";
		hands.forEach(h->h.calculateStar1(CARD_VALUES));
		return sortAndCount(hands);
	}

	/**
	 * J=joker en kan een andere waarde aannemen om een beter combo te maken
	 * maar als kaart is ze het minste waard
	 */
	// antwoord: 249620106
	@Override
	public Long star2() {
		// de positie van het character in de lijst bepaalt zijn waarde
		String CARD_VALUES="J23456789TQKA";
		hands.forEach(h->h.calculateStar2(CARD_VALUES));
		return sortAndCount(hands);
	}
	
	Comparator<Hand> comboComparator=Comparator.comparing(h->h.comboValue);
	Comparator<Hand> handComparator=comboComparator.thenComparing(h->h.cardsValue);

	private Long sortAndCount(List<Hand> hands) {
		// sorteert op hand.value van laag naar hoog
		hands.sort(handComparator);
		long result=0L;
		for(int i=0;i<hands.size();i++) {
			Hand hand=hands.get(i);
			logln(hands);
			writeToFile(hand.hand,""+hand.comboValue,""+hand.cardsValue,hand.bid+"\n");
			result+=(1L+i)*hand.bid;
		}
		return result;		
	}	
}