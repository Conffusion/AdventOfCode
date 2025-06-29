package aoc2020.day04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import aoc2020.common.MainMaster;

public class Main04 extends MainMaster {

	static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	static Validator validator = factory.getValidator();
	static Pattern patt=Pattern.compile("([^ :]+):([^ ]+)");
	static ObjectMapper mapper=new ObjectMapper();
	List<String> data;
	
	public Main04() {
		data=loadInput(4, "input.txt");
	}
	
	public int processOne(String srecord)
	{
		HashMap<String,Object> fields=new HashMap<>();
		Matcher m=patt.matcher(srecord);
		while(m.find())
		{
			fields.put(m.group(1), m.group(2));
		}
		ID id=mapper.convertValue(fields, ID.class);
		Set<ConstraintViolation<ID>> violations = validator.validate(id);
		if(violations.isEmpty()&&id.extraValidation()) {
			logln("valid: "+id);
			return 1;
		}
		else {
			logln("invalid: "+srecord);
			return 0;
		}		
	}
	
	public void star() {
		List<String> records=new ArrayList<>();
		StringBuilder buffer=new StringBuilder();
		for(String s:data)
			if(s.length()==0)
			{
				records.add(buffer.toString());
				buffer=new StringBuilder();
			} else
				buffer.append(s).append(" ");
		int valid=records.parallelStream().mapToInt(s->processOne(s)).sum();
		info(""+valid);
	}
	
	public static void main(String[] args) {
		Main04 m =new Main04();
		m.log=false;
		m.timer(()->m.star());
	}

}
