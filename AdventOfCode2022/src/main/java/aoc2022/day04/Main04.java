package aoc2022.day04;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import common.main.AbstractMainMaster;


public class Main04 extends AbstractMainMaster<Long>{
    public static void main(String[] args) {
		new Main04()
        //.testMode()
        .start();
	}

    List<Pair<Range,Range>> pairInput;
    
    public static class Range {
        int from, to;
        /**
         * create Range from input format "nn-mm"
         * @param inp
         */
        public Range(String inp) {
            String[] bounds=inp.split("-");
            from=Integer.parseInt(bounds[0]);
            to=Integer.parseInt(bounds[1]);
        }
    }

    public Pair<Range,Range> parse(String line) {
        String[] elf=line.split(",");
        return Pair.of(new Range(elf[0]), new Range(elf[1]));
    }

    @Override
    public void beforeAll() {
        pairInput=streamInput(this::parse).collect(Collectors.toList());
    }

    // 477
    @Override
    public Long star1() {
        return pairInput.stream().filter(this::contained).count();
    }

    // controleert of een range volledig binnen de andere zit
    public boolean contained(Pair<Range,Range> pair) {
        return pair.getLeft().from<=pair.getRight().from && pair.getLeft().to>=pair.getRight().to
        || pair.getRight().from<=pair.getLeft().from && pair.getRight().to>=pair.getLeft().to;
    }

    // 830
    @Override
    public Long star2() {
        return pairInput.stream().filter(this::overlaps).count();
    }

    // controleert of er overlapping is
    public boolean overlaps(Pair<Range,Range> pair) {
        return pair.getLeft().to>=pair.getRight().from && pair.getLeft().from<=pair.getRight().to
            || pair.getRight().to>=pair.getLeft().from && pair.getRight().from<=pair.getLeft().to;
    }
}