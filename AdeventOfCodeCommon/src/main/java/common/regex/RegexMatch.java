package common.regex;

import java.util.regex.Matcher;

public class RegexMatch {
	private Matcher matcher;
	
	RegexMatch(Matcher matcher) {
		this.matcher=matcher;
	}
	public boolean matches() {
		return matcher.matches();
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


}
