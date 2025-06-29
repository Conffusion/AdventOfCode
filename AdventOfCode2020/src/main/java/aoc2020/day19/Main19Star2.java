package aoc2020.day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2020.common.MainMaster;

public class Main19Star2 extends MainMaster {
	Map<Long, String> rules = new HashMap<>();
	List<String> patterns = new ArrayList<>();
	Map<String, String> cache = new HashMap<>();

	public void loadData(String filename) {
		Pattern cmdPatt = Pattern.compile("(\\d+):(.+)");
		List<String> data = loadInput(19, filename);
		for (String s : data) {
			if (s.length() > 0) {
				Matcher m = cmdPatt.matcher(s);
				if (m.matches()) {
					rules.put(Long.parseLong(m.group(1)), m.group(2).trim());
				} else {
					patterns.add(s);
				}
			}
		}
	}

	static Pattern OrPattern = Pattern.compile("(.*)\\|(.*)");
	static Pattern CharPattern = Pattern.compile("\"([ab]+)\"");

	public String convert(String pattern) {
		Matcher mChar = CharPattern.matcher(pattern);
		if (mChar.matches())
			return mChar.group(1);
		Matcher mOr = OrPattern.matcher(pattern);
		if (mOr.matches()) {
			return "(" + convert(mOr.group(1)) + "|" + convert(mOr.group(2)) + ")";
		}
		// hier enkel nog rij van nummers mogelijk
		String[] nrs = pattern.trim().split(" ");

		StringBuffer buf = new StringBuffer();
		if (nrs.length > 1)
			buf.append("(");
		for (String nr : nrs) {
			if (nr.equals("8"))
				buf.append("(" + convert(rules.get(42L)) + ")+");
			else if (nr.equals("11"))
				buf.append("((" + convert(rules.get(42L)) + ")+("
						+convert(rules.get(31L))+")+)");
			else {
				if (cache.containsKey(nr)) {
					buf.append(cache.get(nr));
//					if (log)
//						logln("from cache " + nr);
				} else {
					String val = convert(rules.get(Long.parseLong(nr)));
					if (log)
						logln("new Cache:" + nr + "=" + val);
					cache.put(nr, val);
					buf.append(val);
				}
			}
		}
		if (nrs.length > 1)
			buf.append(")");
		return buf.toString();
	}

	public void star2() {
		loadData("input.txt");

		Pattern patt = Pattern.compile(convert(rules.get(0l)));
		System.out.println("pattern="+patt);
		int counter = 0;
		for (String s : patterns) {
			counter += patt.matcher(s).matches() ? 1 : 0;
		}
		System.out.println("***STAR2:" + counter);
	}

	public void test2() {
		loadData("testinput2.txt");
		Pattern patt = Pattern.compile(convert(rules.get(0l)));
		System.out.println("pattern="+patt);
		int counter = 0;
		StringBuffer out=new StringBuffer();
		for (String s : patterns) {
			boolean res=patt.matcher(s).matches();
			counter += res ? 1 : 0;
			out.append(res?"+":"-");
		}
		System.out.println("***TEST2:" + counter);
		System.out.println("detail:"+out.toString());
		System.out.println("expect:-++++++++++-+-+");
	}

	public void utest1() {
		String regex = "((((((((a((b((((a((bb)|(ba)))|(b((ab)|((a|b)a))))a)|(((a((bb)|(aa)))|(b(aa)))b)))|(a((((((bb)|(aa))b)|((aa)a))b)|(((((ab)|(bb))a)|(((ab)|((a|b)a))b))a)))))|(b((((a((a((ba)|(ab)))|(b((ab)|((a|b)a)))))|(b((b((ba)|(ab)))|(a((bb)|(aa))))))b)|(((b((a(aa))|(b((bb)|(aa)))))|(a((b(aa))|(a((bb)|(ba))))))a))))b)|(((((((b(((aa)a)|(((aa)|(b(a|b)))b)))|(a(b((aa)|(b(a|b))))))a)|(((b((((aa)|(b(a|b)))b)|(((ab)|(bb))a)))|(a(((aa)a)|(((aa)|(b(a|b)))b))))b))a)|(((b((((b((aa)|(b(a|b))))|(a((ba)|(ab))))b)|(((a((ba)|(aa)))|(b((bb)|(aa))))a)))|(a((((b((bb)|(aa)))|(a((aa)|(b(a|b)))))a)|(((((ab)|(aa))a)|((aa)b))b))))b))a))a)|(((b((((b((((((b(a|b))|(ab))a)|(((bb)|(ba))b))a)|(((b((ba)|(ab)))|(a(ba)))b)))|(a((((((ab)|(bb))b)|(((bb)|(ba))a))a)|(((((ba)|(ab))a)|(((ab)|((a|b)a))b))b))))a)|(((((b((((aa)|(b(a|b)))a)|(((ba)|(ab))b)))|(a(((a|b)(a|b))(a|b))))a)|(((((((b(a|b))|(ab))b)|((ab)a))b)|(((aa)b)a))b))b)))|(a(((((((((a|b)(a|b))(a|b))b)|((((ab)b)|((aa)a))a))b)|(((a(b((ba)|(ab))))|(b(a((ba)|(aa)))))a))a)|(((a((a((((b(a|b))|(ab))b)|(((ba)|(aa))a)))|(b(((ab)b)|((ab)a)))))|(b((b(a((ba)|(aa))))|(a((a((a(a|b))|(bb)))|(b((aa)|(b(a|b)))))))))b))))b)))+((((((((a((b((((a((bb)|(ba)))|(b((ab)|((a|b)a))))a)|(((a((bb)|(aa)))|(b(aa)))b)))|(a((((((bb)|(aa))b)|((aa)a))b)|(((((ab)|(bb))a)|(((ab)|((a|b)a))b))a)))))|(b((((a((a((ba)|(ab)))|(b((ab)|((a|b)a)))))|(b((b((ba)|(ab)))|(a((bb)|(aa))))))b)|(((b((a(aa))|(b((bb)|(aa)))))|(a((b(aa))|(a((bb)|(ba))))))a))))b)|(((((((b(((aa)a)|(((aa)|(b(a|b)))b)))|(a(b((aa)|(b(a|b))))))a)|(((b((((aa)|(b(a|b)))b)|(((ab)|(bb))a)))|(a(((aa)a)|(((aa)|(b(a|b)))b))))b))a)|(((b((((b((aa)|(b(a|b))))|(a((ba)|(ab))))b)|(((a((ba)|(aa)))|(b((bb)|(aa))))a)))|(a((((b((bb)|(aa)))|(a((aa)|(b(a|b)))))a)|(((((ab)|(aa))a)|((aa)b))b))))b))a))a)|(((b((((b((((((b(a|b))|(ab))a)|(((bb)|(ba))b))a)|(((b((ba)|(ab)))|(a(ba)))b)))|(a((((((ab)|(bb))b)|(((bb)|(ba))a))a)|(((((ba)|(ab))a)|(((ab)|((a|b)a))b))b))))a)|(((((b((((aa)|(b(a|b)))a)|(((ba)|(ab))b)))|(a(((a|b)(a|b))(a|b))))a)|(((((((b(a|b))|(ab))b)|((ab)a))b)|(((aa)b)a))b))b)))|(a(((((((((a|b)(a|b))(a|b))b)|((((ab)b)|((aa)a))a))b)|(((a(b((ba)|(ab))))|(b(a((ba)|(aa)))))a))a)|(((a((a((((b(a|b))|(ab))b)|(((ba)|(aa))a)))|(b(((ab)b)|((ab)a)))))|(b((b(a((ba)|(aa))))|(a((a((a(a|b))|(bb)))|(b((aa)|(b(a|b)))))))))b))))b)))+(((b((((a((((a((((ab)|(aa))a)|(((bb)|(aa))b)))|(b((b((aa)|(b(a|b))))|(a(ba)))))b)|(((((((ab)|(bb))a)|(((bb)|(aa))b))b)|(((b((aa)|(b(a|b))))|(a(ab)))a))a)))|(b((((((b(aa))|(a((ab)|(aa))))a)|(((b(aa))|(a((bb)|(ba))))b))a)|(((((((ab)|(bb))a)|(((ab)|((a|b)a))b))a)|(((((bb)|(aa))b)|(((ba)|(ab))a))b))b))))a)|(((b((a(((((a(a|b))|(bb))(a|b))a)|(((((ba)|(ab))a)|(((ab)|((a|b)a))b))b)))|(b((((b(bb))|(a(ab)))a)|(((((ab)|((a|b)a))b)|((bb)a))b)))))|(a((a((a((b((bb)|(aa)))|(a((aa)|(b(a|b))))))|(b((a(aa))|(b(bb))))))|(b(((((ab)a)|(((bb)|(ba))b))a)|(((b(ab))|(a(ab)))b))))))b)))|(a((a((a((((a((b((ab)|(aa)))|(a((bb)|(aa)))))|(b((((ab)|((a|b)a))a)|((ba)b))))b)|(((b((((ab)|(aa))a)|(((ab)|(bb))b)))|(a(((aa)b)|((aa)a))))a)))|(b((((((b((a|b)(a|b)))|(a((ab)|(aa))))b)|(((a((ab)|(aa)))|(b(ba)))a))b)|(((b((((aa)|(b(a|b)))a)|(((ba)|(ab))b)))|(a((((a(a|b))|(bb))b)|(((ba)|(aa))a))))a)))))|(b((((((((((aa)|(b(a|b)))b)|(((ab)|(bb))a))b)|(((((bb)|(aa))b)|((aa)a))a))b)|(((a((a((bb)|(ba)))|(b((a(a|b))|(bb)))))|(b(((ab)a)|(((a(a|b))|(bb))b))))a))a)|(((a((a((a(ba))|(b((ab)|(aa)))))|(b((b(aa))|(a((bb)|(ba)))))))|(b((a((b((bb)|(aa)))|(a((aa)|(b(a|b))))))|(b((((ab)|((a|b)a))a)|(((a|b)(a|b))b))))))b)))))))+))";
		System.out.println("aaabbaaaaaabbbaaaaababbbbabbbabbbaaaababbbaaababbbbbabbba:" + "aaabbaaaaaabbbaaaaababbbbabbbabbbaaaababbbaaababbbbbabbba".matches(regex));
		System.out.println("aaabbaaaaaabbbaaaaababbbbabbbabbbaaaababbbaaababbbbbabbb:" + "aaabbaaaaaabbbaaaaababbbbabbbabbbaaaababbbaaababbbbbabbb".matches(regex));
		System.out.println("baaabbaaaaaabbbaaaaababbbbabbbabbbaaaababbbaaababbbbbabbb:" + "baaabbaaaaaabbbaaaaababbbbabbbabbbaaaababbbaaababbbbbabbb".matches(regex));
	}

	public static void main(String[] args) {
		Main19Star2 m = new Main19Star2();
		//m.log = false;
		//m.timer(() -> m.star1());
		//m.test2();
		m.star2();
		m.utest1();
		
	}

}
