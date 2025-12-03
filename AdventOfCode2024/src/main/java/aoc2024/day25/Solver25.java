package aoc2024.day25;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.io.Loader;
import common.main2.AoCLogger;
import common.main2.AoCRunner;
import common.main2.AoCSolver;

public class Solver25 extends AoCSolver<Long> {
	static AoCLogger logger=new AoCLogger();
	Loader loader=Loader.forMain(this).withTestMode(false).build();
	
	public static void main (String[] args) {
		//logger.nolog(true);
		new AoCRunner<>(new Solver25()).start();
	}
	
    List<Code> keys;
    List<Code> locks;
    
    @Override
    public void beforeEach() {
    	keys=new ArrayList<>();
    	locks=new ArrayList<>();
    	List<String>input=loader.loadInputByLine();
    	int r=-1;
    	Code currCode=null;
    	// elke 8 rijen vormen 1 set (KEY of LOCK)
    	for (String line:input) {
    		r++;
    		if(r==0) { 
    			currCode=new Code();
    			currCode.value=new int[] {0,0,0,0,0};
    			if(line.equals(".....")) {
        			currCode.type=CodeType.KEY;
        			keys.add(currCode);
    			}
	    		if(r==0 && line.equals("#####")) {
	    			currCode.type=CodeType.LOCK;
        			locks.add(currCode);
	    		}
	    	} else if(r==6) {}
    		else if(r==7) {
    			r=-1;
    		} else {
    			for(int c=0;c<5;c++) {
    				if (line.charAt(c)=='#') {
    					currCode.value[c]++;
    				}
    			}
    		}
    	}
    }

    // antwoord : 3291
    public Long star1() {
        Long result=0L;
        for(Code key:keys) {
        	for(Code lock:locks) {
        		if(fit(key,lock)) {
        			logger.logln(()->"fit: "+key+","+lock);
        			result++;
        		}
        	}
        }
        return result;
    }

    // antwoord : 
    public Long star2() {
        Long result=0L;

        return result;

    }
    enum CodeType {KEY,LOCK};
    
    class Code {
    	CodeType type;
    	int[] value;
		@Override
		public String toString() {
			return "" + type + "[" + Arrays.toString(value) + "]";
		}
    	
    }
    private boolean fit(Code key, Code lock) {
    	for(int c=0;c<5;c++)
    		if(key.value[c]+lock.value[c]>5)
    			return false;
    	return true;
    }
}
