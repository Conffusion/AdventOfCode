package aoc2022.day16;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

public class MainGPT extends AbstractMainMaster<Long> {
	
    public static void main(String[] args) {
        new MainGPT()
            //.testMode()
            //.nolog()
           .start();
    }

	@Override
	public Long star1() {
//		// start the DFS traversal at the source valve
		dfs(network.get("AA"), 0, 0);
//
//		// print the maximum flowrate
		System.out.println("Maximum flowrate: " + maxFlowrate);
		return maxFlowrate;
	}

	static class Valve {
		String name;
		int flowrate;
		List<Valve> neighbors;

		public Valve(int flowrate) {
			this.flowrate = flowrate;
			this.neighbors = new ArrayList<>();
		}

		public void addNeighbor(Valve neighbor) {
			this.neighbors.add(neighbor);
		}
	}

	static int timeLimit = 30; // time limit in minutes
	static long maxFlowrate = 0; // maximum flowrate found so far

	static void dfs(Valve v, int flowrate, int timeSpent) {
		// base case: time limit reached
		if (timeSpent > timeLimit) {
			return;
		}

		// update maximum flowrate
		maxFlowrate = Math.max(maxFlowrate, flowrate);

		// explore all the neighboring valves
		for (Valve neighbor : v.neighbors) {
			// compute the flowrate if the valve is opened
			int flowrateIfOpened = flowrate + neighbor.flowrate;
			// compute the flowrate if the valve is not opened
			int flowrateIfNotOpened = flowrate;
			// choose the path that leads to the maximum flowrate
			int flowrateToUse = Math.max(flowrateIfOpened, flowrateIfNotOpened);
			// continue the DFS traversal along the chosen path
			dfs(neighbor, flowrateToUse, timeSpent + 1);
		}
	}

    Map<String,Valve> network;

    @Override
	public void beforeAll() {
    	List<String> in=loadInputByLine();
    	network=in.stream().map(this::parseValves).collect(Collectors.toMap(v->v.name,v->v));
    	in.forEach(this::parseLinks);
    }
    
    RegexTool linePattern=new RegexTool("Valve (..) has flow rate=(\\d+); tunnels? leads? to valves? (.+)"); 

    public Valve parseValves(String s) {
    	if(linePattern.evaluate(s)) {
    		
    		Valve v=new Valve(linePattern.intGroup(2));
    		v.name=linePattern.group(1);
    		return v;
    	} else 
    		throw new IllegalArgumentException("snap ik niets van:"+s);
    }

    public void parseLinks(String s) {
    	if(linePattern.evaluate(s)) {
    		String name=linePattern.group(1);
    		Valve v=network.get(name);
	    	for (String tov:linePattern.group(3).split(", ")) {
	    		v.addNeighbor(network.get(tov));
	    	}
    	}
    }
    
//	public static void main(String[] args) {
//
//		// create the valves and add their connections
//		Valve v1 = new Valve(10);
//		Valve v2 = new Valve(20);
//		Valve v3 = new Valve(30);
//		Valve v4 = new Valve(40);
//		v1.addNeighbor(v2);
//		v2.addNeighbor(v3);
//		v3.addNeighbor(v4);
//
//		// start the DFS traversal at the source valve
//		dfs(v1, 0, 0);
//
//		// print the maximum flowrate
//		System.out.println("Maximum flowrate: " + maxFlowrate);
//	}
}
