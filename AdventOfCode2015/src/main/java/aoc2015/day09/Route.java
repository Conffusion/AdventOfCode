package aoc2015.day09;

import java.util.ArrayList;
import java.util.List;

public class Route {
	List<String> visited=new ArrayList<>();
	int distance=0;
	Route(String from) {
		visited.add(from);
	}
	void addNode(String name, int distance) {
		visited.add(name);
		this.distance+=distance;
	}
	boolean hasVisited(String name) {
		return visited.contains(name);
	}
}
