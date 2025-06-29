package aoc2023.day03;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.main.AbstractMainMaster;

/**
 * laad input in 2 DIM schema met 1e dim=rij, 2e dim=kolom zodat schema[0] de
 * eerste rij voorstelt.
 */
public class Main03 extends AbstractMainMaster<Long> {
	private static int DIM_TEST = 10;
	private static int DIM_REAL = 140;
	private static Pattern numPattern = Pattern.compile("([0-9]+)");

	public static void main(String[] args) {
		new Main03()
				// .testMode()
				.start();
	}

	private char[][] schema; // volledige input, vorm is vierkant
	private int DIM = 0; // dimensie afhankelijk van testMode of real

	@Override
	public void beforeEach() {
		super.beforeEach();
		if (testMode) {
			DIM = DIM_TEST;
		} else {
			DIM = DIM_REAL;
		}
		schema = loadInputInto2DArray(DIM, DIM);
	}

	// antwoord: 532428
	@Override
	public Long star1() {
		long som = 0L;
		for (int line = 0; line < DIM; line++) {
			Matcher numMatch = numPattern.matcher(String.valueOf(schema[line]));
			//System.out.println("line " + line + ":" + new String(schema[line]));
			numLoop: while (numMatch.find()) {
				long goodNumber = Long.parseLong(numMatch.group());
				int fromPos = numMatch.start();
				int toPos = numMatch.end();
				if (fromPos > 0 && !Character.isDigit(schema[line][fromPos - 1]) && schema[line][fromPos - 1] != '.') {
					som += goodNumber;
					continue numLoop; 
				}
				if (toPos < DIM - 1 && !Character.isDigit(schema[line][toPos]) && schema[line][toPos] != '.') {
					som += goodNumber;
					continue numLoop; 
				}
				int leftPos = fromPos > 0 ? fromPos - 1 : fromPos;
				int rightPos = toPos < DIM - 1 ? toPos : toPos - 1;
				if (line > 0) {
					char[] around = new char[rightPos - leftPos + 1];
					System.arraycopy(schema[line - 1], leftPos, around, 0, rightPos - leftPos + 1);
					for (int c : around)
						if (c != '.' && !Character.isDigit(c)) {
							som += goodNumber;
							continue numLoop;
						}
				}
				if (line < DIM - 1) {
					char[] around = new char[rightPos - leftPos + 1];
					System.arraycopy(schema[line + 1], leftPos, around, 0, rightPos - leftPos + 1);
					for (int c : around)
						if (c != '.' && !Character.isDigit(c)) {
							som += goodNumber;
							continue numLoop;
						}
				}
			}
		}
		return som;
	}

	// key: positie van * rij*DIM+kol
	// value: lijst van nummers die er aan grenzen
	private Map<Integer, List<Long>> hits = new HashMap<>();

	/**
	 * zoek elk nummer paar die aan eenzelfde '*' grenzen antwoord: 84051670
	 */
	@Override
	public Long star2() {
		for (int line = 0; line < DIM; line++) {
			Matcher numMatch = numPattern.matcher(String.valueOf(schema[line]));
			while (numMatch.find()) {
				long goodNumber = Long.parseLong(numMatch.group());
				int fromPos = numMatch.start();
				int toPos = numMatch.end();
				if (fromPos > 0 && schema[line][fromPos - 1] == '*') {
					addHit(line, fromPos - 1, goodNumber);
				}
				if (toPos < DIM - 1 && schema[line][toPos] == '*') {
					addHit(line, toPos, goodNumber);
				}
				// voor de rij boven en onder het getal moet er ook schuin worden gekeken (vb 456,
				// behalve als het getal aan de rand ligt (vb 123 en 789):
				// aaaa..bbbbb..cccc
				// 123a..b456b..c789
				// aaaa..bbbbb..cccc
				int leftPos = Math.max(0, fromPos - 1);
				int rightPos = Math.min(toPos,DIM - 1);
				for (int p = leftPos; p <= rightPos; p++) {
					if (line > 0 && schema[line - 1][p] == '*')
						addHit(line - 1, p, goodNumber);
					if (line < DIM - 1 && schema[line + 1][p] == '*')
						addHit(line + 1, p, goodNumber);
				}
			}
		}
		return hits.values().stream().filter(l -> l.size() == 2).map(l -> l.get(0) * l.get(1)).reduce(0L, Long::sum);
	}

	private void addHit(int line, int col, Long number) {
		hits.computeIfAbsent(line * DIM + col, i -> new ArrayList<>()).add(number);
	}
}