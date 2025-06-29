package aoc2022.day01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.main.AbstractMainMaster;


public class Main01 extends AbstractMainMaster<Long>{
    public static void main(String[] args) {
		new Main01()
        //.testMode()
        .start();
	}
    
    @Override
    public void beforeEach() {
    }

    @Override
    public Long star1() {
        long max=0;
        long current=0;
        List<String> lines=loadInputByLine();
        for(String line:lines) {
            if(line.length()==0) {
                max=Math.max(current,max);
                current=0L;
                continue;
            }
            current+=Long.parseLong(line);
        }
        max=Math.max(current,max);

        return max;
    }

    @Override
    public Long star2() {
        List<Long> elfs=new ArrayList<>();
        long current=0;
        List<String> lines=loadInputByLine();
        for(String line:lines) {
            if(line.length()==0) {
                elfs.add(current);
                current=0L;
                continue;
            }
            current+=Long.parseLong(line);
        }
        int last=elfs.size()-1;
        Collections.sort((elfs));
        return elfs.get(last)+elfs.get(last-1)+elfs.get(last-2);
    }
}
