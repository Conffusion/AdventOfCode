package aoc2015.day06;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2015.common.MainMaster;

public class Main06 extends MainMaster<Long>{

	public static void main(String[] args) {
		new Main06().start();
	}

	int[][] board=new int[1000][1000];
	
	@Override
	public Long star1() {
		parseInput(this::parseInstruction)
			.stream().forEach(this::applyInstruction1);
		return countOn();
	}

	private long countOn() {
		int total=0;
		for(int x=0;x<1000;x++)
			for(int y=0;y<1000;y++)
				total+=board[x][y];
		return total;
	}
	
	private void applyInstruction1(Instruction instr) {
		for(int x=instr.x0;x<=instr.x1;x++)
			for(int y=instr.y0;y<=instr.y1;y++)
				switch(instr.action)
				{
				case turnOn:
					board[x][y]=1;
					break;
				case turnOff:
					board[x][y]=0;
					break;
				case toggle:
					board[x][y]=board[x][y]==1?0:1;
					break;
				}
	}
	
	@Override
	public Long star2() {
		board=new int[1000][1000];
		parseInput(this::parseInstruction)
		.stream().forEach(this::applyInstruction2);
	return countOn();
	}
	
	private void applyInstruction2(Instruction instr) {
		for(int x=instr.x0;x<=instr.x1;x++)
			for(int y=instr.y0;y<=instr.y1;y++)
				switch(instr.action)
				{
				case turnOn:
					board[x][y]+=1;
					break;
				case turnOff:
					board[x][y]=Math.max(board[x][y]-1, 0);
					break;
				case toggle:
					board[x][y]+=2;
					break;
				}
	}

	private static Pattern pattern=Pattern.compile("^(.*) ([0-9]*),([0-9]*) through ([0-9]*),([0-9]*)$");

	private Instruction parseInstruction(String line) {
		Instruction instr=new Instruction();
		Matcher match=pattern.matcher(line);
		if(match.matches())
		{
			instr.action=Action.of(match.group(1));
			instr.x0=Integer.parseInt(match.group(2));
			instr.y0=Integer.parseInt(match.group(3));
			instr.x1=Integer.parseInt(match.group(4));
			instr.y1=Integer.parseInt(match.group(5));
		}
		return instr;
	}
	enum Action {
		turnOn("turn on"),
		turnOff("turn off"),
		toggle("toggle");
		private String inputValue;
		private Action(String input)
		{
			this.inputValue=input;
		}
		public static Action of (String value) {
			for(Action a :Action.values())
				if (a.inputValue.equals(value))
					return a;
			return null;
		}
	}
	
	public class Instruction {
		Action action;
		int x0,x1,y0,y1;
		@Override
		public String toString() {
			return "{action=" + action + ", x0=" + x0 + ", x1=" + x1 + ", y0=" + y0 + ", y1=" + y1 + "}";
		}
		
	}

}
