package aoc2023.day23;

import common.dim2.Vak;
import common.graph.Direction;
import common.graph.Point;

public class Vak23 extends Vak{
	
	public enum VakType {
		WEG, BOS, STEIL_O, STEIL_Z;
	}
	VakType type;
	/**
	 * Langst afgelegde weg om tot hier te komen 
	 */
	long afstand=0;

	
	public Vak23(VakType type,Point p) {
		super(p);
		this.type = type;
	}


	/**
	 * Is dit vak toegankelijk vanuit de gegeven richting
	 * @param richting
	 * @return
	 */
	public boolean kanPasseren(Direction richting, boolean steilteCheck) {
		return switch(type) {
			case BOS-> false;
			case WEG->true;
			case STEIL_O-> !steilteCheck ||richting!=Direction.LEFT;
			case STEIL_Z-> !steilteCheck ||richting!=Direction.UP;
		};
	}


	@Override
	public String toString() {
		return "Vak[" + type + "," + point +","+afstand+ "]";
	}	
}
