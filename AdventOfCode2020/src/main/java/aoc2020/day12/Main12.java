package aoc2020.day12;

import java.util.List;
import java.util.stream.Collectors;

import aoc2020.common.MainMaster;

public class Main12 extends MainMaster {

	List<Instruction> insdata;

	private static class Position {
		int x = 0, y = 0;
		int facingNS = 0;
		int facingEW = 1;
		
		Position() {
		}

		Position(Position p) {
			x = p.x;
			y = p.y;
			facingNS = p.facingNS;
			facingEW = p.facingEW;
		}

		@Override
		public String toString() {
			return "Position [x=" + x + ", y=" + y + ", facingNS=" + facingNS + ", facingEW=" + facingEW +"]";
		}
		
	}

	private static class Instruction {
		char direction;
		int distance;

		public Instruction(char direction, int distance) {
			super();
			this.direction = direction;
			this.distance = distance;
		}

		@Override
		public String toString() {
			return direction + "-" + distance;
		}
		
	}

	public void loadData(String file) {
		insdata = loadInput(12, file).stream().map(l -> {
			return new Instruction(l.charAt(0), Integer.parseInt(l.substring(1)));
		}).collect(Collectors.toList());
	}

	public Position move(Position pos, Instruction ins) {
		Position newpos = new Position(pos);
		switch (ins.direction) {
		case 'L':
			for (int i = 0; i < ins.distance / 90; i++) {
				int newNS= newpos.facingNS == 0 ? (newpos.facingEW == 1 ? -1 : 1) : 0;
				int newEW= newpos.facingEW == 0 ? (newpos.facingNS == 1 ? 1 : -1) : 0;
				newpos.facingNS =newNS;
				newpos.facingEW =newEW;
			}
			break;
		case 'R':
			for (int i = 0; i < ins.distance / 90; i++) {
				int newNS=newpos.facingNS == 0 ? (newpos.facingEW == 1 ? 1 : -1) : 0;
				int newEW=newpos.facingEW == 0 ? (newpos.facingNS == 1 ? -1 : 1) : 0;
				newpos.facingNS = newNS;
				newpos.facingEW = newEW;
			}
			break;
		case 'F':
			newpos.x+=ins.distance*pos.facingEW;
			newpos.y+=ins.distance*pos.facingNS;
			break;
		case 'N':
			newpos.y -= ins.distance;
			break;
		case 'S':
			newpos.y += ins.distance;
			break;
		case 'E':
			newpos.x += ins.distance;
			break;
		case 'W':
			newpos.x -= ins.distance;
			break;
		default:
			System.err.println("unknown instruction " + ins.direction);
		}
		return newpos;
	}

	// correct answer= manhattan distance:1631
	public void star1() {
		Position currpos = new Position();
		if(log)logln("start pos:"+currpos);
		for(Instruction ins:insdata) {
			currpos=move(currpos,ins);
			if(log)logln("after "+ins+" is pos:"+currpos);
		}
		System.out.println("manhattan distance:"+(currpos.x+currpos.y));
	}

	public static void main(String[] args) {
		Main12 m = new Main12();
		m.loadData("input.txt");
		m.star1();

	}

}
