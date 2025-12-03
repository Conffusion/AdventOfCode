package aoc2019.day01;

import java.util.List;

import common.main.AbstractMainMaster;

public class Main01 extends AbstractMainMaster<Long> {

    List<Long> inputs;
    
    public static void main(String[] args) {
		new Main01().start();
	}

    @Override
    public void beforeAll() {
        inputs=loadInputLongByLine();
    }

    /** antwoord: 3495189 */
    @Override
    public Long star1() {
        return inputs.stream()
        .mapToLong(l->l/3-2)
        .sum();
    }

    /**
     * antwoord: 5239910
     */
    @Override
    public Long star2() {
        return inputs.stream()
        .mapToLong(this::fuelNeed)
        .sum();
    }
    
    private Long fuelNeed(Long mass) {
        long tot=0;
        while (mass>0) {
            mass=mass/3-2;
            tot+=Math.max(mass,0L);
        }
        return tot;
    }
}
