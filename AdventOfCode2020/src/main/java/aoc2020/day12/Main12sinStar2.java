package aoc2020.day12;

import java.util.List;
import java.util.stream.Collectors;

import aoc2020.common.MainMaster;

/**
 * Star 2 oplossing met radialen
 * @author walter
 *
 */
public class Main12sinStar2 extends MainMaster {

	List<Instruction> insdata;

	private static class Status {
		Position ship=new Position(), wp=new Position();
		Status() {
			moveWP(10,-1);
		}
		Status(Status status) {
			this.ship=new Position(status.ship);
			this.wp=new Position(status.wp);
		}
		void moveWP(double dx, double dy)
		{
			wp.x+=dx;
			wp.y+=dy;
		}
		// aantal kwartieren te draaien +=links, -=rechts
		// 1 = 90° links
		void rotateWP(int quarter)
		{
			quarter=quarter%4;
			while(quarter<0)quarter+=4;
			if(quarter==0)
				return;
			int tmp=wp.x;
			if(Math.abs(quarter)==1)
			{
				wp.x=wp.y;
				wp.y=-tmp;
			}
			if(quarter==2)
			{
				wp.x=-wp.x;
				wp.y=-wp.y;
			}
			if (quarter==3) {
				wp.x=-wp.y;
				wp.y=tmp;
			}
		}
		@Override
		public String toString() {
			return "[ship=" + ship + ", wp=" + wp + "]";
		}
	}
	
	private static class Position {
		int x = 0, y = 0;
		
		Position() {
		}

		Position(Position p) {
			x = p.x;
			y = p.y;
		}
				
		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}

	private static class Instruction {
		char direction;
		double distance;

		public Instruction(char direction, double distance) {
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

	public Status move(Status pos, Instruction ins) {
		Status newpos = new Status(pos);
		switch (ins.direction) {
		case 'L':
			newpos.rotateWP((int)ins.distance/90);
			break;
		case 'R':
			newpos.rotateWP(-(int)ins.distance/90);
			break;
		case 'F':
			newpos.ship.x+=ins.distance*pos.wp.x;
			newpos.ship.y-=ins.distance*pos.wp.y;
			break;
		case 'N':
			newpos.moveWP(0, -ins.distance);
			break;
		case 'S':
			newpos.moveWP(0, ins.distance);
			break;
		case 'E':
			newpos.moveWP(ins.distance,0);
			break;
		case 'W':
			newpos.moveWP(-ins.distance,0);
			break;
		default:
			System.err.println("unknown instruction " + ins.direction);
		}
		return newpos;
	}

	// correct answer= manhattan distance:58606
	public void star2() {
		Status currstatus = new Status();
		if(log)logln("start ship:"+currstatus);
		for(Instruction ins:insdata) {
			currstatus=move(currstatus,ins);
			if(log)logln("after "+ins+" is pos:"+currstatus);
		}
		System.out.println("manhattan distance:"+(Math.abs(currstatus.ship.x)+Math.abs(currstatus.ship.y)));
	}

	public static void main(String[] args) {
		Main12sinStar2 m = new Main12sinStar2();
		m.loadData("input.txt");
		m.log=false;
		m.timer(()->m.star2());
	}
}
