package aoc2022.day10;

import java.util.Arrays;
import java.util.List;

import common.swing.MainMasterGUI;

public class Main10 extends MainMasterGUI<Long> {
    public static void main(String[] args) {
        Main10 m=new Main10();
            //.testMode()
            //.nolog()
        m.launch();
    }

    int cycle;
    int regX=1;
    char[] crt;
    
    @Override
	public void beforeAll() {
	}

	@Override
    public void beforeEach() {
    	regX=1;
    	cycle=1;
    }

	List<Integer> signals;
	
	// answer: 11820
    public Long star1() {
    	signals=Arrays.asList(20,60,100,140,180,220);
		return streamInput(s->new Command(s)).mapToLong(this::execute1).sum();
    }
    
    public long execute1(Command com) {
    	long strength=0L;
    	switch(com.instr) {
    	case noop:
    		strength=evaluateCycle();
    		cycle++;
    		break;
    	case addx:
    		strength=evaluateCycle();
    		cycle++;
    		strength+=evaluateCycle();
    		cycle++;
    		regX+=com.param;
    	}    	
    	return strength;
    }

    private long evaluateCycle() {
    	long strength=0L;
		if (signals.contains(cycle))
			strength=cycle*regX;
		logln(""+cycle+": regX="+regX+(strength!=0L?" -signalstrength="+strength:""));
		return strength;
    }
    
    public Long star2() {
    	crt=new char[241];
    	cycle=0;
    	Arrays.fill(crt, '.');
		streamInput(s->new Command(s)).forEachOrdered(this::execute2);
		for(int row=0;row<6;row++)
			System.out.println(new String(crt,row*40,40));
		return 0L;
    }
    
    public void execute2(Command com) {
    	switch(com.instr) {
    	case noop:
    		runCycle();
    		break;
    	case addx:
    		runCycle();
    		runCycle();
    		regX+=com.param;
    	}   	
    }
    private void runCycle() {
		if(Math.abs(regX-cycle%40)<=1)
			crt[cycle]='#';
		cycle++;
		logln(""+cycle+": regX="+regX+" - "+crt[cycle-1]);
    }
    
    enum Instruction {
    	addx, noop;
    }
    
    public class Command {
    	Instruction instr;
    	int param=0;
    	
    	public Command(String cmd) {
    		if(cmd.equals("noop")) {
    			instr=Instruction.noop;
    		} else if (cmd.startsWith("addx")) {
    			instr=Instruction.addx;
    			param=Integer.parseInt(cmd.substring(5));
    		} else {
    			throw new IllegalArgumentException("snap ik niet:"+cmd);
    		}
    	}

		@Override
		public String toString() {
			return instr + " " + param;
		}
    	
    }

}