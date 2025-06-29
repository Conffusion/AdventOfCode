package aoc2024.day22;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.BasicUtils;
import common.main.AbstractMainMaster;

public class Main22 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main22()
            .testMode()
            //.nolog()
           .start();
    }

    int times=2000;

    // antwoord : 17262627539
    public Long star1() {
        return BasicUtils.streamSum(loadInputLongByLine().stream() , this::nextSecretNumber);
    }

    // antwoord : 1986
    public Long star2() {
        // bevat per combinatie (in string) de totale opbrengst
        Map<String,Long> comboWinst=new HashMap<>();
        List<Long> secrets=loadInputLongByLine();
        for(Long secret:secrets) {
        	long[] oneDigits= allOneDigits(secret);
        	Map<String,Long> changeMap=fillChangeMap(oneDigits);
        	for(Map.Entry<String, Long> entry:changeMap.entrySet()) {
        		comboWinst.merge(entry.getKey(),entry.getValue(),(v1,v2)->v1+v2);
        	}
        }
        return Collections.max(comboWinst.values());
    }
    
    private long nextSecretNumber(long secretNumber) {
    	long value;
    	for(int i=0;i<times;i++) {
    		value=secretNumber*64;
    		secretNumber=value^secretNumber; // mix: XOR
    		secretNumber=secretNumber%16777216L; // prune: module
    		value=(long) Math.floor(secretNumber/32);
    		secretNumber=value^secretNumber; // mix: XOR
    		secretNumber=secretNumber%16777216L; // prune: module
    		value=secretNumber*2048;
    		secretNumber=value^secretNumber; // mix: XOR
    		secretNumber=secretNumber%16777216L; // prune: module
    	}
    	return secretNumber;
    }
    
    private long[] allOneDigits(long secretNumber) {
    	long value;
    	long[] result=new long[times+1];
    	result[0]=(int)(secretNumber%10);
    	for(int i=1;i<=times;i++) {
    		value=secretNumber*64;
    		secretNumber=value^secretNumber; // mix: XOR
    		secretNumber=secretNumber%16777216L; // prune: module
    		value=(long) Math.floor(secretNumber/32);
    		secretNumber=value^secretNumber; // mix: XOR
    		secretNumber=secretNumber%16777216L; // prune: module
    		value=secretNumber*2048;
    		secretNumber=value^secretNumber; // mix: XOR
    		secretNumber=secretNumber%16777216L; // prune: module
    		result[i]=secretNumber%10;
    	}
    	
    	return result;
    }
    
    private Map<String,Long> fillChangeMap(long[] oneDigits) {
    	Map<String,Long> map=new HashMap<>();
    	for (int i=4;i<oneDigits.length;i++) {
    		String key=""+(oneDigits[i-3]-oneDigits[i-4])
    				+""+(oneDigits[i-2]-oneDigits[i-3])
    				+""+(oneDigits[i-1]-oneDigits[i-2])
    				+""+(oneDigits[i]-oneDigits[i-1]);
    		if(!map.containsKey(key))
    			map.put(key, oneDigits[i]);
    	}
    	return map;
    }
}