package aoc2021.day12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.main.AbstractMainMaster;

/**
 * "alle mogelijke paden door een graph vinden tussen start en end"
 * Alternatief: gebruik van integer voor cave id ipv naam
 * @author walter
 *
 */
public class Main12Alt extends AbstractMainMaster<Long> {

	// alle caves
	Map<String,Cave> network=new HashMap<>();

	long solutionCount;
	// voor star2 mag 1 kleine grot 2 keer gedaan worden, voor star1 nooit
	boolean allowDoubleSmallCave=false;
	int idSeq;
	Cave startCave;
	
	class Cave {
		int id;
		boolean isBig=false;
		boolean isStart=false;
		boolean isEnd=false;
		List<Cave> connections=new ArrayList<>();
		public Cave(String name) {
			id=++idSeq;
			isBig=name.equals(name.toUpperCase());
			isStart=name.equals("start");
			isEnd=name.equals("end");
		}
	}

	// bevat de info van 1 passage
	class Pass {
		List<Integer> path;
		boolean doubleSmallCave=false;
		
		public Pass() {
			path=new ArrayList<>();
		}

		protected Pass copy() {
			Pass newpass=new Pass();
			newpass.path.addAll(path);
			newpass.doubleSmallCave=doubleSmallCave;
			return newpass;
		}
	}
	
	private void newConnection(String from,String to)
	{
		Cave cave=network.computeIfAbsent(from, Cave::new);
		Cave caveTo=network.computeIfAbsent(to, Cave::new);
		cave.connections.add(caveTo);
		network.put(from,cave);
		if(cave.isStart)
			startCave=cave;
		if(caveTo.isStart)
			startCave=caveTo;
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
		idSeq=0;
		solutionCount=0;
	}
	
	private void visit(Cave cave, Pass pass) {
		Pass newPass=null;
		if(cave.isEnd)
		{
			// we zijn er
			solutionCount++;
		} else {
			if(!cave.isBig && pass.path.contains(cave.id)) {
				if(allowDoubleSmallCave&&!pass.doubleSmallCave)
				{
					newPass=pass.copy();
					newPass.doubleSmallCave=true;
				} else {
					return;
				}
			} 
			if(newPass==null)
				newPass=pass.copy();
			newPass.path.add(cave.id);
			for(Cave neighbour:cave.connections)
			{
				if(!neighbour.isStart)
					visit (neighbour,newPass);
			}
		}
	}
	
	/**
	 * antwoord: 3761
	 */
	@Override
	public Long star1() {
		allowDoubleSmallCave=false;
		visit(startCave,new Pass());
		return solutionCount;
	}


	/**
	 * antwoord: 99138
	 */
	@Override
	public Long star2() {
		allowDoubleSmallCave=true;
		visit(startCave,new Pass());
		return solutionCount;
	}

	public static void main(String[] args) {
		new Main12Alt()
		.nolog()
		.start();
	}

}
