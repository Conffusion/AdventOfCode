package aoc2024.day07;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import common.BasicUtils;
import common.main.AbstractMainMaster;

public class Main07 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main07()
            //.testMode()
            .nolog()
           .start();
    }
    List<Opgave> opgaven;
    int maxOperands=0;
    String operandsBase;
    
    static class Opgave {
    	long resultaat;
    	long[] waarden;
		public Opgave(long resultaat, long[] waarden) {
			super();
			this.resultaat = resultaat;
			this.waarden = waarden;
		}
		@Override
		public String toString() {
			return "Opgave [resultaat=" + resultaat + ", waarden=" + Arrays.toString(waarden) + "]";
		}
    }
    
    @Override
    public void beforeAll() {
    	 opgaven=parseInput(this::parseOpgave);
    	 operandsBase=StringUtils.repeat("0", maxOperands);
    	 logln("operandsBase:"+operandsBase);
    }
    
    private Opgave parseOpgave(String lijn) {
    	String[] delen=lijn.split(":");
    	maxOperands=Math.max(maxOperands, delen[1].trim().split(" ").length-1);
    	return new Opgave(Long.parseLong(delen[0]), Stream.of(delen[1].trim().split(" ")).mapToLong(Long::parseLong).toArray());
    }

    // antwoord : 1038838357795
    public Long star1() {
    	return opgaven.stream().mapToLong(o->evalueer(o,2)).sum();
    }
    /**
     * berekent voor alle mogelijke operatoren (+,*,||) of de opgave kan kloppen.
     * @param opgave
     * @param operatorCount als 2, enkel +,*, als 3: +,*,||
     * @return
     */
    private Long evalueer(Opgave opgave, int operatorCount) {
    	int maxValue=(int) Math.pow(operatorCount, opgave.waarden.length-1);
    	for(int i=0;i<maxValue;i++) {
    		char[] ops=BasicUtils.prefill(BasicUtils.convertToBase(i, operatorCount), '0', opgave.waarden.length-1).toCharArray();
    		if(verifieer(opgave,ops))
    			return opgave.resultaat;
    	}
    	return 0L;
    }
        
    // antwoord : 254136560217241
    public Long star2() {
    	return opgaven.stream().mapToLong(o->evalueer(o,3)).sum();
    }
        
    public boolean verifieer(Opgave opgave,char[] operators) {
    	long waarde=opgave.waarden[0];
    	for(int i=1;i<opgave.waarden.length;i++) {
    		switch (operators[i-1]) {
	    		case '0': waarde+=opgave.waarden[i]; break;
	    		case '1': waarde*=opgave.waarden[i]; break;
	    		case '2': waarde=Long.parseLong(""+waarde+""+opgave.waarden[i]); break;
    		}
    	}
    	if(waarde==opgave.resultaat) {
    		logln(opgave+" correct met operators "+Arrays.toString(operators));
    	} 
    	return waarde==opgave.resultaat;
    }
}