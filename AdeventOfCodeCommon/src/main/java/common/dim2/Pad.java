package common.dim2;

import java.util.ArrayList;
import java.util.List;

/**
 * Een pad bevat een afgelegde weg. Het kent alle vakken waar het pad passeert.
 * huidigePos is de huidige plaats van het pad.
 * @param <V>
 */
public class Pad<V extends Vak>{

	private List<V> weg;
	V huidigePos;
	
	private Pad() {
		// used by clone
	}
	
	public Pad(V start) {
		weg=new ArrayList<>();
		weg.add(start);
		huidigePos=start;
	}

	public boolean alGeweest(V vak) {
		return weg.contains(vak);
	}

	public V getHuidigePos() {
		return huidigePos;
	}

	public void setHuidigePos(V huidigePos) {
		weg.add(huidigePos);
		this.huidigePos = huidigePos;
	}

	public Pad<V> clone() {
		Pad<V> newPad=new Pad<>();
		newPad.weg=new ArrayList<V>();
		for(V v:weg)
			newPad.setHuidigePos(v);
		newPad.huidigePos=huidigePos;
		return newPad;
	}

	@Override
	public String toString() {
		return "Pad [" + huidigePos + ", weg=" + weg + "]";
	}
	
}
