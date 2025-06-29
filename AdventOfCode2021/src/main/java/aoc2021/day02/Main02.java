package aoc2021.day02;

import java.util.List;

import common.main.AbstractMainMaster;

public class Main02 extends AbstractMainMaster<Long> {

	public static void main(String[] args) {
		new Main02().start();
	}

	List<Command> input;
	
	long horPos=0;
	long verPos=0;
	long aim=0;
	
	@Override
	public void beforeAll() {
		input=parseInput(s->s.split(" "),s->new Command(CommandType.valueOf(s[0]),Long.parseLong(s[1])));
	}

	@Override
	public Long star1() {
		input.stream().forEachOrdered(this::parseCommand1);
		return horPos * verPos;
	}

	enum CommandType {
		forward,
		down,
		up;
	}
	public class Command {
		CommandType commType;
		long distance;
		public Command(CommandType commType, long distance) {
			this.commType = commType;
			this.distance = distance;
		}
	}
	
	public void parseCommand1(Command comm) {
		switch (comm.commType) {
		case forward:
			horPos+=comm.distance;
			break;
		case down:
			verPos+=comm.distance;
			break;
		case up:
			verPos-=comm.distance;
		}
	}
	
	@Override
	public Long star2() {
		horPos=0;
		verPos=0;
		aim=0;
		input.stream().forEachOrdered(this::parseCommand2);
		return horPos * verPos;
	}
	
	public void parseCommand2(Command comm) {
		switch (comm.commType) {
		case forward:
			horPos+=comm.distance;
			verPos+=aim*comm.distance;
			break;
		case down:
			aim+=comm.distance;
			break;
		case up:
			aim-=comm.distance;
		}
	}
}
