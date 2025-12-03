package common.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tool om input lijnen te parsen met een regex expressie.
 * Instantieer een object 1 keer met de regex expressie met groupen (gebruik van ronde haakjes).
 * Parse dan elke lijn via de {@link #evaluate(String)} methode.
 * Gebruik dan de verschillende methodes om group values op te vragen.
 */
public class RegexTool {
    Pattern patt;
    Matcher matcher;
    boolean throwUnmatched=false;
    
    public RegexTool(String pattern) {
        this(pattern,false);
    }
    public RegexTool(String pattern, boolean throwUnMatched) {
    	patt=Pattern.compile(pattern);
    	this.throwUnmatched=throwUnMatched;
    }
    public RegexTool throwUnMatched() {
    	throwUnmatched=true;
    	return this;
    }
    /**
     * parsed de value volgens de pattern.
     * @param value
     * @return matched de value met het patroon ?
     * @throws IllegalArgumentException als value niet matched met pattern en throwUnmatched=true
     */
    public boolean evaluate(String value)
    {
        matcher=patt.matcher(value);
        boolean match=matcher.matches();
        if(!match && throwUnmatched)
        	throw new IllegalArgumentException("snap ik niet: "+value);
        return match;
    }
    
    public int intGroup(int index) {
        return Integer.parseInt(matcher.group(index));
    }
    public long longGroup(int index) {
    	return Long.parseLong(matcher.group(index));
    }
    public String group(int index) {
        return matcher.group(index);
    }
    public List<String> groupAll(String value, int index) {
    	List<String> all=new ArrayList<>();
    	Matcher matcher=patt.matcher(value);
    	while(matcher.find()) {
    		all.add(matcher.group());
    	}
    	return all;
    }
    public static final String NUMBER_GROUP_PATTERN="([0-9]+)";
    public static final String NUMBER_NEG_GROUP_PATTERN="(-?[0-9]+)";
    
    /**
     * Doorloopt value en vindt alle
     * @param value
     * @return
     */
    public List<Integer> groupAllInt(String value) {
    	List<Integer> all=new ArrayList<>();
    	Matcher matcher=patt.matcher(value);
    	while(matcher.find()) {
    		all.add(Integer.parseInt(matcher.group()));
    	}
    	return all;
    }
    /**
     * Doorloopt value en vindt alle
     * @param value
     * @return
     */
    public List<Long> groupAllLong(String value) {
    	List<Long> all=new ArrayList<>();
    	Matcher matcher=patt.matcher(value);
    	while(matcher.find()) {
    		all.add(Long.parseLong(matcher.group()));
    	}
    	return all;
    }
}
