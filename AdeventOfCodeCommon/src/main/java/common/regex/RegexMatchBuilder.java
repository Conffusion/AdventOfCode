package common.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper voor het processen van input met een regex.
 * Kan gebruikt worden als Function<String,RegexMatch>:
 * <pre>
 *   streamInput(new RegexMatchBuilder("([0-9]+)"))
 *       .map(rm->rm.intGroup(1))
 *       .toList();
 * </pre>
 */
public class RegexMatchBuilder implements Function<String,RegexMatch>{
    Pattern patt;
    Matcher matcher;
    boolean throwUnmatched=false;

    public RegexMatchBuilder(String pattern) {
        this(pattern,false);
    }
    public RegexMatchBuilder(String pattern, boolean throwUnMatched) {
    	patt=Pattern.compile(pattern);
    	this.throwUnmatched=throwUnMatched;
    }
    public RegexMatchBuilder throwUnMatched() {
    	throwUnmatched=true;
    	return this;
    }

    public RegexMatch evaluate(String value)
    {
    	Matcher matcher=patt.matcher(value);
        boolean match=matcher.matches();
        if(!match && throwUnmatched)
        	throw new IllegalArgumentException("snap ik niet: "+value);
        
        return new RegexMatch(matcher);
    }
    
    /**
     * Doorloopt value en vindt alle groepen
     * @param value te evalueren input text
     * @return
     */
    public List<String> groupAll(String value) {
    	List<String> all=new ArrayList<>();
    	Matcher matcher=patt.matcher(value);
    	while(matcher.find()) {
    		all.add(matcher.group());
    	}
    	return all;
    }

    /**
     * Doorloopt value en vindt alle groepen en converteert iedere groep naar een Integer
     * @param value te evalueren input text
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
     * Doorloopt value en vindt alle groepen en converteert iedere groep naar een Long
     * @param value te evalueren input text
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
    
	@Override
	public RegexMatch apply(String in) {
		return evaluate(in);
	}
}
