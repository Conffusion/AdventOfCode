package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class BasicUtils {

	/**
	 * Converteert een array van type T naar een List<T>
	 * @param <T>
	 * @param values
	 * @return
	 */
	public static <T> List<T> toList(T[] values) {
		List<T> result=new ArrayList<>();
		for(T v:values)
			result.add(v);
		return result;
	}
	public static String[] toArray(List<String> values) {
		String[] result=new String[values.size()];
		values.toArray(result);
		return result;
	}
	public static long[] toLongArray(List<Long> values) {
		return values.stream().mapToLong(Long::longValue).toArray();
	}
	
	public static int[] toIntArray(List<Integer> values) {
		return values.stream().mapToInt(Integer::intValue).toArray();
	}
	
	public static long kgm (List<Long> values) {
		return kgmByFactors(values.stream().map(BasicUtils::getPrimeFactors).toList());
	}

	/**
	 * Kleinst gemeen veelvoud van een lijst van getallen waarbij elk getal al is ontbonden in priemfactoren
	 * @param primeFactors
	 * @return
	 */
    public static long kgmByFactors(List<Map<Long, Long>> primeFactors) {
    	Set<Long> primeFactorsUnionSet = new HashSet<>();
    	for(Map<Long, Long> oneSet:primeFactors) {
    		primeFactorsUnionSet.addAll(oneSet.keySet());
    	}
    	long lcm=1;
	    for (Long primeFactor : primeFactorsUnionSet) {
	    	long power=0;
	    	for(Map<Long, Long> oneSet:primeFactors) {
	    		power=Math.max(power, oneSet.getOrDefault(primeFactor, 0L));
	    	}
	    	lcm *= Math.pow(primeFactor,power);
	    }
	    return lcm;
    }

    /**
     * Ontbind een getal in priemfactoren
     * @param number
     * @return Map met key=priemgetal, value=aantal keer
     */
	public static Map<Long, Long> getPrimeFactors(long number) {
	    long absNumber = Math.abs(number);

	    Map<Long, Long> primeFactorsMap = new HashMap<>();

	    for (long factor = 2; factor <= absNumber; factor++) {
	        while (absNumber % factor == 0) {
	        	Long power = primeFactorsMap.get(factor);
	            if (power == null) {
	                power = 0L;
	            }
	            primeFactorsMap.put(factor, power + 1);
	            absNumber /= factor;
	        }
	    }
	    return primeFactorsMap;
	}
	/**
	 * Converteert een getal in text vorm uitgedrukt in base fromBase, naar een getal in base toBase
	 * vb: str="33", fromBase=10, toBase=3 -> 100
	 * @param str getal in tekst
	 * @param fromBase base waarin getal is uitgedrukt
	 * @param toBase base naar waar moet geconverteert worden
	 * @return String met getal in toBase
	 */
	public static String convertFromBaseToBase(String str, int fromBase, int toBase) {
	    return Integer.toString(Integer.parseInt(str, fromBase), toBase);
	}
	/**
	 * Converteert value (in base 10) naar String in toBase
	 * @param value waarde
	 * @param toBase deol base
	 * @return
	 */
	public static String convertToBase(Long value, int toBase) {
	    return Long.toString(value, toBase);
	}
	/**
	 * Converteert value (in base 10) naar String in toBase
	 * @param value waarde
	 * @param toBase deol base
	 * @return
	 */
	public static String convertToBase(Integer value, int toBase) {
	    return Integer.toString(value, toBase);
	}
	/**
	 * voegt vooraan fillChar toe tot resultaat lengte totLength heeft.
	 * Als input lengte >= totLength wordt input teruggegeven.
	 * @param input
	 * @param fillChar
	 * @param totLength
	 * @return
	 */
	public static String prefill(String input, char fillChar, int totLength) {
		if(input.length()>=totLength)
			return input;
		return StringUtils.repeat(fillChar, totLength-input.length())+input;
	}
	
	public static <T> Long streamSum(Stream<T> stream,ToLongFunction<T> converter) {
		return stream.collect(Collectors.summarizingLong(converter)).getSum();
	}
	/**
	 * 
	 * @param <T> type van te evalueren object
	 * @param <V> type van return waarde
	 * @param i input object
	 * @param converter functie om V waarde uit i te halen. Wordt enkel aangeroepen wanneer i niet null is
	 * @param defaultValue waarde wordt teruggegeven wanneer i null is
	 * @return resultaat van converter of defaultValue;
	 */
	public static <T,V> V onNotNull(T i, Function<T,V> converter,V defaultValue) {
		if(i!=null)
			return converter.apply(i);
		else
			return defaultValue;
	}
}
