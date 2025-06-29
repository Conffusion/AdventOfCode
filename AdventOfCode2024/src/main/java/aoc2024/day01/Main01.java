package aoc2024.day01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.main.AbstractMainMaster;
import common.regex.RegexMatchBuilder;

public class Main01 extends AbstractMainMaster<Long>{
    public static void main(String[] args) {
		new Main01()
        //.testMode()
        .start();
	}
    
	List<Integer> col1=new ArrayList<>(), col2=new ArrayList<>();

	@Override
    public void beforeAll() {
        streamInput(new RegexMatchBuilder("([0-9]+)   ([0-9]+)"))
        		.forEach(rm->{
        			col1.add(rm.intGroup(1));
        			col2.add(rm.intGroup(2));
        		});
    }
    
    // antwoord: 2430334
    @Override
    public Long star1() {
        Collections.sort(col1);
        Collections.sort(col2);
        long diff=0;
        for(int i=0;i<col1.size();i++) {
        	diff+=Math.abs(col1.get(i)-col2.get(i));
        }
        return diff;
    }

    // antwoord: 28786472
    @Override
    public Long star2() {
        long diff=0;
        for(int i=0;i<col1.size();i++) {
        	long lval=col1.get(i);
        	long rcnt=col2.stream().filter(v->v==lval).count();
        	diff+=col1.get(i)*rcnt;
        }
        return diff;
    }
}
