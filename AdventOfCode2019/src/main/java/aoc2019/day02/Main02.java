package aoc2019.day02;

import common.main.AbstractMainMaster;

public class Main02 extends AbstractMainMaster<Integer> {

    int[] input=null;

    public static void main(String[] args) {
		new Main02()
        //.testMode()
        .start();
	}
    @Override
    public void beforeEach() {
        input=parseNumbersLine(loadInputToString(), ",");
    }

    /** antwoord: 5866714 */
    @Override
    public Integer star1() {
        input[1]=12;
        input[2]=2;
        input= new IntcodeComputer().run(input);

        return input[0];
    }

    /**
     * antwoord: 5208
     */
    @Override
    public Integer star2() {
        IntcodeComputer icc=new IntcodeComputer();
        for(int verb=0;verb<=99;verb++)
        	for(int noun=0;noun<=99;noun++) {
                int[] tempinput=new int[input.length];
                System.arraycopy(input, 0, tempinput, 0, input.length);
                tempinput[1]=noun;
                tempinput[2]=verb;
                icc.run(tempinput);
                if(tempinput[0]==19690720)
                	return noun*100+verb;
        	}
        return 0;
    }
    
}
