package aoc2024.day13;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import common.BasicUtils;
import common.main.AbstractMainMaster;
import common.regex.RegexMatch;
import common.regex.RegexMatchBuilder;

/**
 * Wiskunde probleem:
 * knop A beweegt X+xa en Y+ya
 * knop B beweegt X+xb en Y+yb
 * Hoeveel keer moeten we A en B klikken om op positie (tx,ty) te komen ?
 * Oplossing: omzetten in 2 vergelijkingen en uitrekenen met input getallen
 */
public class Main13 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main13()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }
    RegexMatchBuilder BUTTON_PATTERN=new RegexMatchBuilder("Button .: X\\+([0-9]+), Y\\+([0-9]+)");
    RegexMatchBuilder PRIZE_PATTERN=new RegexMatchBuilder("Prize: X=([0-9]+), Y=([0-9]+)");
    
    List<String> lines;
    List<ClawMachine> machines;
    
    @Override
    public void beforeEach() {
    	lines=loadInputByLine();
    	machines=new ArrayList<ClawMachine>();
    }

    // antwoord : 33209
    public Long star1() {
        int linecount=0;
        while(linecount<lines.size()) {
        	RegexMatch aButton=BUTTON_PATTERN.apply(lines.get(linecount++));
        	RegexMatch bButton=BUTTON_PATTERN.apply(lines.get(linecount++));
        	RegexMatch prize=PRIZE_PATTERN.apply(lines.get(linecount++));
        	linecount++; // lege lijn
        	ClawMachine cm=new ClawMachine();
        	cm.xa=aButton.longGroup(1);
        	cm.ya=aButton.longGroup(2);
        	cm.xb=bButton.longGroup(1);
        	cm.yb=bButton.longGroup(2);
        	cm.tx=prize.longGroup(1);
        	cm.ty=prize.longGroup(2);
        	machines.add(cm);
        }
        return BasicUtils.streamSum(machines.stream().map(this::resolve1), ClawMachine::cost);
    }

    // antwoord : 83102355665474
    public Long star2() {
        int linecount=0;
        while(linecount<lines.size()) {
        	RegexMatch aButton=BUTTON_PATTERN.apply(lines.get(linecount++));
        	RegexMatch bButton=BUTTON_PATTERN.apply(lines.get(linecount++));
        	RegexMatch prize=PRIZE_PATTERN.apply(lines.get(linecount++));
        	linecount++; // lege lijn
        	ClawMachine cm=new ClawMachine();
        	cm.xa=aButton.longGroup(1);
        	cm.ya=aButton.longGroup(2);
        	cm.xb=bButton.longGroup(1);
        	cm.yb=bButton.longGroup(2);
        	cm.tx=prize.longGroup(1)+10000000000000L;
        	cm.ty=prize.longGroup(2)+10000000000000L;
        	machines.add(cm);
        }
        return machines.stream().map(this::resolve1).collect(Collectors.summarizingLong(ClawMachine::cost)).getSum();
    }
    private ClawMachine resolve1(ClawMachine mach) {
    	double b=(mach.ya*mach.tx-mach.xa*mach.ty)/(mach.ya*mach.xb - mach.xa*mach.yb);
    	double a=(mach.tx-mach.xb*b)/mach.xa;
    	if(a==Math.floor(a)&&b==Math.floor(b)) {
    		mach.apush=(long)a;
    		mach.bpush=(long)b;
    	}
    	return mach;
    }
    
    static class ClawMachine {
    	double xa, xb, ya, yb, tx, ty;
    	long apush, bpush;
    	
    	long cost() {
    		return apush*3+bpush;}
    }
}