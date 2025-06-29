package aoc2021.day18;

public class SFNumber {
    private int part1=-1, part2=-1;
    private SFNumber sfnum1=null,sfnum2=null;

    public SFNumber part1(int p1) {
        part1=p1;
        return this;
    }
    public SFNumber part2(int p2) {
        part2=p2;
        return this;
    }
    public SFNumber sfnum1(SFNumber s1) {
        sfnum1=s1;
        return this;
    }
    public SFNumber sfnum2(SFNumber s2) {
        sfnum2=s2;
        return this;
    }
    
    public int getPart1() {
		return part1;
	}
	public int getPart2() {
		return part2;
	}
	public SFNumber getSfnum1() {
		return sfnum1;
	}
	public SFNumber getSfnum2() {
		return sfnum2;
	}
	public String toString() {
        StringBuffer buf=new StringBuffer("[");
        if(sfnum1==null)
            buf.append(part1);
        else buf.append(sfnum1.toString());
        buf.append(",");
        if(sfnum2==null)
            buf.append(part2);
        else buf.append(sfnum2.toString());
        buf.append("]");    
        return buf.toString();
    }
    
    public SFNumber add (SFNumber other) {
        return new SFNumber().sfnum1(this).sfnum2(other);
    }

    public long magnitude() {
        return 3L*(sfnum1==null?part1:sfnum1.magnitude())
            +2*(sfnum2==null?part2:sfnum2.magnitude());
    }
}
