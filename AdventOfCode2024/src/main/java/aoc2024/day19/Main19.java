package aoc2024.day19;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import common.QueueHelper;
import common.main.AbstractMainMaster;
import common.regex.RegexMatchBuilder;

/**
 * Regex pattern matching
 */
public class Main19 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main19()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }
    String pattern;
    List<String> lines;
    QueueHelper<Phrase> phraseQueue=new QueueHelper<>();
    List<SubArrayMatcher> matchers;
    
    @Override
    public void beforeEach() {
        lines=loadInputByLine();
    }

    // antwoord : 338
    public Long star1() {
        pattern="("+lines.get(0).replaceAll(", ", "|")+")+";
        logln("pattern:"+pattern);
        lines.remove(0);
    	RegexMatchBuilder matchBuilder=new RegexMatchBuilder(pattern);
        return lines.stream().map(matchBuilder).filter(rm->rm.matches()).count();
    }
    long star2Counter=0L;
    
    /**
     * Alle mogelijke combinaties vinden die matchen
     */
    // antwoord : 
    public Long star2() {
        matchers=Arrays.stream(lines.get(0).split(", ")).map(SubArrayMatcher::new).toList();
        logln("matchers found: "+matchers.size());
        lines.remove(1);
        lines.remove(0);
        lines.stream().map(Phrase::new).forEach(phraseQueue::add);
        phraseQueue.consume(this::phraseHandler);
        return star2Counter;
    }

    private void phraseHandler(Phrase phrase) {
    	for(SubArrayMatcher matcher:matchers) {
    		if(matcher.test(phrase)) {
    			Phrase newp=phrase.clone();
    			newp.startPos+=matcher.len;
    			if(newp.startPos>=newp.line.length()) {
    				star2Counter++;
    			}
    			phraseQueue.add(newp);
    		}
    	}
    }
    
    class SubArrayMatcher implements Predicate<Phrase>{
    	String pattern;
    	int len;
		public SubArrayMatcher(String pattern) {
			this.pattern = pattern;
			this.len=pattern.length();
		}

		/**
		 * Controleert of the pattern overeenkomt met de phrase line vanaf phrase startPos
		 */
		@Override
		public boolean test(Phrase phrase) {
			return phrase.line.regionMatches(phrase.startPos, pattern, 0, len);
		}

		@Override
		public String toString() {
			return "SubArrayMatcher [" + new String(pattern) + "]";
		}
    }
    
    class Phrase {
    	String line;
    	int startPos;
    	
		public Phrase(String line) {
			this.line = line;
			this.startPos = 0;
		}
		public Phrase(String line, int startPos) {
			super();
			this.line = line;
			this.startPos = startPos;
		}

		public Phrase clone() {
			return new Phrase(line,startPos);
		}
		
		@Override
		public String toString() {
			return "Phrase [line=" + line + ", startPos=" + startPos + "]";
		}
    }
}