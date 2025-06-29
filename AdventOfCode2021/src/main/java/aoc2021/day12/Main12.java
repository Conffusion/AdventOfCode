package aoc2021.day12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.main.AbstractMainMaster;

/**
 * "alle mogelijke paden door een graph vinden tussen start en end"
 * @author walter
 *
 */
public class Main12 extends AbstractMainMaster<Long> {

	// alle caves
	Map<String,Cave> network=new HashMap<>();

//	List<Pass> solutions;
	long solutionCount;
	// voor star2 mag 1 kleine grot 2 keer gedaan worden, voor star1 nooit
	boolean allowDoubleSmallCave=false;
	
	class Cave {
		String name;
		boolean isBig=false;
		List<Cave> connections=new ArrayList<>();
		public Cave(String name) {
			this.name = name;
			isBig=name.equals(name.toUpperCase());
		}
		@Override
		public String toString() {
			StringBuffer buf=new StringBuffer("{ Cave "+name+",[");
			for (Cave c:connections)
				buf.append(c.name+" ");
			buf.append("]}");
			return buf.toString();
		}
	}

	// bevat de info van 1 passage
	class Pass {
		List<String> path;
		String doubleSmallCave;
		
		public Pass() {
			path=new ArrayList<>();
		}

		protected Pass copy() {
			Pass newpass=new Pass();
			newpass.path.addAll(path);
			newpass.doubleSmallCave=doubleSmallCave;
			return newpass;
		}

		@Override
		public String toString() {
			return String.join(",", path);
		}
	}
	
	private void newConnection(String from,String to)
	{
		Cave cave=network.computeIfAbsent(from, Cave::new);
		Cave caveTo=network.computeIfAbsent(to, Cave::new);
		cave.connections.add(caveTo);
		network.put(from,cave);
	}
	
	@Override
	public void beforeAll() {
		for(String line:loadInputByLine()) {
			String[] split=line.split("-");
			newConnection(split[0],split[1]);
			newConnection(split[1],split[0]);
		}
	}

	@Override
	public void beforeEach() {
//		solutions=new ArrayList<>();
		solutionCount=0;
	}
	
	private void visit(Cave cave, Pass pass) {
		Pass newPass=null;
		if(cave.name.equals("end"))
		{
			// we zijn er
//			solutions.add(pass);
			solutionCount++;
			logln("Solution:",pass);
			return;
		}
		if(!cave.isBig && pass.path.contains(cave.name)) {
			if((!cave.name.equals("start"))&&allowDoubleSmallCave)
			{
				if(pass.doubleSmallCave==null)
				{
					newPass=pass.copy();
					newPass.doubleSmallCave=cave.name;
				} else {
					logln("skip other double smallCave {}, {}",pass,cave);
					return;
				}
			} else {
				logln("reentered:{},{}",pass,cave);
				return;
			}
		}
		if(newPass==null)
			newPass=pass.copy();
		newPass.path.add(cave.name);
		for(Cave neighbour:cave.connections)
		{
			visit (neighbour,newPass);
		}
	}
	
	
	@Override
	public String getResourceName() {
		return "input.txt";
	}

	/**
	 * antwoord: 3761
	 */
	@Override
	public Long star1() {
		allowDoubleSmallCave=false;
		Cave cave=network.get("start");
		Pass pass=new Pass();
		visit(cave,pass);
		// return (long)solutions.size();
		return solutionCount;
	}


	/**
	 * antwoord: 99138
	 */
	@Override
	public Long star2() {
		allowDoubleSmallCave=true;
		Cave cave=network.get("start");
		Pass pass=new Pass();
		visit(cave,pass);
		// return (long)solutions.size();
		return solutionCount;
	}

	public static void main(String[] args) {
		new Main12()
		.testMode()
		.nolog()
		.start();
	}

}
