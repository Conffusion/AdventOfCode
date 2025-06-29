package aoc2023.day22;

import java.util.HashSet;
import java.util.Set;

/**
 * Een SteunBalk weet wie op hem rust en waarop hij rust.
 */
public class SteunBalk extends Balk {

	/**
	 * lijst van SteanBalken waarop deze steunbalk rust;
	 */
	private Set<SteunBalk> steuntOp=new HashSet<>();
	/**
	 * lijst van steunbalken die rusten op deze steunbalk
	 */
	private Set<SteunBalk> ondersteunt=new HashSet<>();
	/**
	 * Lijst van alle balken die zouden vallen wanneer deze balk wordt weggenomen
	 */
	private Set<SteunBalk> ondersteuntCumul=null;
	/**
	 * Een balk is dragend wanneer andere balken zouden vallen als we deze wegnemen.
	 */
	private boolean dragend=false;
	private int draagtTeller=0;
	
	public SteunBalk(String id, int x0, int y0, int z0, int x1, int y1, int z1) {
		super(id, x0, y0, z0, x1, y1, z1);
	}
	public void steuntOp(SteunBalk balk) {
		steuntOp.add(balk);
	}
	public void ondersteunt(SteunBalk balk) {
		ondersteunt.add(balk);
	}
	public Set<SteunBalk> ondersteunt(){
		return ondersteunt;
	}
	public Set<SteunBalk> steuntOp() {
		return steuntOp;
	}
	public boolean isDragend() {
		return dragend;
	}
	public void setDragend(boolean dragend) {
		this.dragend = dragend;
	}
	public int getDraagtTeller() {
		return draagtTeller;
	}
	public void setDraagtTeller(int draagtTeller) {
		this.draagtTeller = draagtTeller;
	}
	/**
	 * Opgelet: null wanneer deze nog niet is berekend
	 * @return
	 */
	public Set<SteunBalk> getOndersteuntCumul() {
		return ondersteuntCumul;
	}
	public void setOndersteuntCumul(Set<SteunBalk> ondersteuntCumul) {
		this.ondersteuntCumul = ondersteuntCumul;
	}
}
