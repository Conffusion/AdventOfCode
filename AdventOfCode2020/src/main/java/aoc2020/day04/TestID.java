package aoc2020.day04;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class TestID {
	static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	static Validator validator = factory.getValidator();

	public static void testOne(ID id)
	{
		Set<ConstraintViolation<ID>> violations = validator.validate(id);
		int viols=violations.size()+(id.extraValidation()?0:1);
		if(viols==0)
			System.out.println("valid: "+id);
		else {
			System.err.println("invalid: "+id+", violations:"+viols);
			System.err.println(violations);
		}
		
	}
	public static void main(String[] args) {
		List<ID> ids=new ArrayList<>();
		ids.add(new ID(2003,2021,2031,"194cm","#23abc","grh","0123456789",null));
		ids.add(new ID(1919,2009,2019,"149cm","123abc","oti","01234567",null));
		ids.add(new ID(1919,2009,2019,"149cm","123abc","oti",null,null));
		ids.add(new ID(null,2009,2019,"149cm","123abc","oti","01234567",null));
		ids.add(new ID(1919,null,2019,"149cm","123abc","oti","01234567",null));
		ids.add(new ID(1919,2009,null,"149cm","123abc","oti","01234567",null));
		ids.add(new ID(1919,2009,2019,null,"123abc","oti","01234567",null));
		ids.add(new ID(1919,2009,2019,"149cm",null,"oti","01234567",null));
		ids.add(new ID(1919,2009,2019,"149cm","123abc",null,"01234567",null));

		ids.forEach(id->testOne(id));
	}

}
