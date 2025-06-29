package aoc2021.day18;

import java.util.List;

import common.main.AbstractMainMaster;

public class Main18 extends AbstractMainMaster<Long> {
    private List<SFNumber> sfnumbers;

    public static void main(String[] args) {
        new Main18()
        .testMode()
        .start();
    }

    @Override
    public void beforeEach() {
        sfnumbers=parseInput(String::toCharArray, this::parseSFNumber);
    }

    @Override
    public Long star1() {
        SFNumber sum=sfnumbers.get(0);
        for (int i=1;i<sfnumbers.size();i++) {
            sum=sum.add(sfnumbers.get(i));
        }
        return sum.magnitude();
    } 
    
    private SFNumber parseSFNumber(char[] line) {
        return new SFNumberParser(line).parse();
    }
}
