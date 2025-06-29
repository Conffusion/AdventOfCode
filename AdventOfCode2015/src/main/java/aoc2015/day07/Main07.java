package aoc2015.day07;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2015.common.MainMaster;

public class Main07 extends MainMaster<Integer> {

	enum InstructionType {
		RSHIFT("^([a-z0-9]+) RSHIFT ([0-9]+) -> ([a-z]+)$",Main07::parseRSHIFT, Main07::calcRSHIFT),
		LSHIFT("^([a-z0-9]+) LSHIFT ([0-9]+) -> ([a-z]+)$",Main07::parseLSHIFT, Main07::applyLSHIFT),
		NOT("^NOT ([a-z0-9]+) -> ([a-z]+)$",Main07::parseNOT, Main07::applyNOT),
		AND("^([a-z0-9]+) AND ([a-z]+) -> ([a-z]+)$",Main07::parseAND,Main07::applyAND),
		OR("^([a-z0-9]+) OR ([a-z0-9]+) -> ([a-z]+)$",Main07::parseOR,Main07::applyOR),
		ASSIGN("^([a-z0-9]+) -> ([a-z]+)$",Main07::parseAssign,Main07::applyAssign);
		
		Pattern pattern;
		Function<Matcher,Instruction> parser;
		Function<Instruction,Integer>calcFunc;
		
		InstructionType(String regex, Function<Matcher,Instruction> parser,Function<Instruction,Integer> calcFunc) {
			this.pattern=Pattern.compile(regex);
			this.parser=parser;
			this.calcFunc=calcFunc;
		}
	}
	
	/**
	 * 
	 */
	static class Instruction {
		InstructionType instrType;
		String in1,in2,out;
		int shift;
		boolean calculated=false;
		int calcValue;
		public Instruction(InstructionType instrType, String in1, String in2, String out, int shift) {
			super();
			this.instrType = instrType;
			this.in1 = in1;
			this.in2 = in2;
			this.out = out;
			this.shift = shift;
		}
		public int getValue() {
			if(!calculated)
				calcValue=instrType.calcFunc.apply(this);
			calculated=true;
			return calcValue;
		}
		@Override
		public String toString() {
			return "Instruction [instrType=" + instrType + ", in1=" + in1 + ", in2=" + in2 + ", out=" + out + ", shift="
					+ shift + ", calculated=" + calculated + ", calcValue=" + calcValue + "]";
		}
		
		
	}
	static Map<String,Instruction> wires=new HashMap<>();
	String answerWire="a";
	Main07 withAnswerWire(String wireid) {
		this.answerWire=wireid;
		return this;
	}
	static Instruction parseLSHIFT(Matcher match) {
		return new Instruction(InstructionType.LSHIFT,match.group(1),null,match.group(3),Integer.parseInt(match.group(2)));}
	static Instruction parseRSHIFT(Matcher match) {
		return new Instruction(InstructionType.RSHIFT,match.group(1),null,match.group(3),Integer.parseInt(match.group(2)));
	}
	static Instruction parseNOT(Matcher match) {
		return new Instruction(InstructionType.NOT,match.group(1),null,match.group(2),0);
	}
	static Instruction parseAND(Matcher match) {
		return new Instruction(InstructionType.AND,match.group(1),match.group(2),match.group(3),0); 
	}
	static Instruction parseOR(Matcher match) {
		return new Instruction(InstructionType.OR,match.group(1),match.group(2),match.group(3),0);
	}
	static Instruction parseAssign(Matcher match) {
		return new Instruction(InstructionType.ASSIGN,match.group(1),null,match.group(2),0);
	}
	static Integer calcRSHIFT(Instruction instr) {
		//return getWireValue(instr.in1)/(2*instr.shift);
		return getWireValue(instr.in1) >>> instr.shift;
	}
	static Integer applyLSHIFT(Instruction instr) {
		//return getWireValue(instr.in1)*(2*instr.shift);
		return getWireValue(instr.in1) << instr.shift;
	}
	static Integer applyNOT(Instruction instr) {
		return 65535-getWireValue(instr.in1);
	}
	static Integer applyAND(Instruction instr) {
		return getWireValue(instr.in1)&getWireValue(instr.in2);
	}
	static Integer applyOR(Instruction instr) {
		return getWireValue(instr.in1)|getWireValue(instr.in2);
	}
	static Integer applyAssign(Instruction instr) {
		return getWireValue(instr.in1);
	}
	static int getWireValue(String wire) {
		try {
			return Integer.parseInt(wire);
		} catch (NumberFormatException ne) {
			return wires.get(wire).getValue();
		}		
	}
	
	@Override
	public Integer star1() {
		parseInput(this::parseInput).stream().forEach(i->wires.put(i.out,i));
		return getWireValue(answerWire);
	}

	Instruction parseInput(String line) {
		for(InstructionType instr:InstructionType.values()) {
			Matcher m=instr.pattern.matcher(line);
			if(m.matches()) {
				Instruction in=instr.parser.apply(m);
				System.out.println(in);
				return in;
			}
		}
		throw new RuntimeException("not instruction matches "+line);
	}
	@Override
	public Integer star2() {
		parseInput(this::parseInput).stream().forEach(i->wires.put(i.out,i));
		wires.get("b").in1="46065";
		return getWireValue(answerWire);
	}

	public static void main(String[] args) {
		new Main07()
		//.withAnswerWire("f")
		//.withTestMode()
		.start();

	}

}
