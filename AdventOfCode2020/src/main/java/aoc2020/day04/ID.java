package aoc2020.day04;

import java.util.regex.Matcher;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ID {

	@NotNull
	@Min(value=1920)
	@Max(value=2002)
	public Integer byr;
	
	@NotNull
	@Min(value=2010)
	@Max(value=2020)
	public Integer iyr;
	@NotNull
	@Min(value=2020)
	@Max(value=2030)
	public Integer eyr;
	@NotNull
	@Pattern(regexp="\\d{2,3}(in|cm)")
	public String hgt;
	@NotNull
	@Pattern(regexp="#[0-9a-f]{6}")
	public String hcl;
	@NotNull
	@Pattern(regexp="amb|blu|brn|gry|grn|hzl|oth")
	public String ecl;
	@NotNull
	@Pattern(regexp="\\d{9}")
	public String pid;
	public String cid;

	public ID() {}
	
	public ID(Integer byr, Integer iyr,
			Integer eyr,String hgt,
			String hcl,
			String ecl,
			String pid, String cid) {
		super();
		this.byr = byr;
		this.iyr = iyr;
		this.eyr = eyr;
		this.hgt = hgt;
		this.hcl = hcl;
		this.ecl = ecl;
		this.pid = pid;
		this.cid = cid;
	}

	private static java.util.regex.Pattern hgtPattern=java.util.regex.Pattern.compile("(\\d+)(in|cm)");
	public boolean extraValidation()
	{
		if(hgt==null)
			return false;
		Matcher m=hgtPattern.matcher(hgt);
		if(m.matches())
		{
			int h=Integer.parseInt(m.group(1));
			String unit=m.group(2);
			return unit.equals("in")&&(59<=h&&h<=76)||
					unit.equals("cm")&&(150<=h&&h<=193);
		}
		return false;
	}
	@Override
	public String toString() {
		return "ID [byr=" + byr + ", iyr=" + iyr + ", eyr=" + eyr + ", hgt=" + hgt + ", hcl=" + hcl + ", ecl=" + ecl
				+ ", pid=" + pid + ", cid=" + cid + "]";
	}
	
}
