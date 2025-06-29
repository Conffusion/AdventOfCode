package aoc2023.day22;

import java.util.ArrayList;
import java.util.List;

/**
 * Een balk heeft een start punt, een lengte, en een richting tov het startpunt.
 * Richting is uitgedrukt als een delta in de juiste richting (0, +1 of -1 voor X,Y of Z)
 * In geval van een balk uit 1 blokje zijn deltaX, deltaY, deltaZ allemaal 0. 
 */
public class Balk {
	public String id;
	/**
	 * start van de balk. Altijd laagste waarden voor x,y,z
	 */
	public int x,y,z; 
	/**
	 * lengte van de balk
	 */
	public int length;
	public int deltaX, deltaY, deltaZ;
	public List<BalkPos> balkPoss=new ArrayList<>();
	
	/**
	 * Lijst van BalkPos die het grondvlak bepalen. Voor een verticale balk is dit het onderste blok,
	 * Anders is het gelijk aan alle posities van de Balk.
	 */
	public List<BalkPos> grondVlak=new ArrayList<>();
	public List<BalkPos> dakVlak=new ArrayList<>();
	
	public Balk(String id, int x0,int y0, int z0,int x1,int y1, int z1) {
		this.id=id;
		this.x=Math.min(x0,x1);
		this.y=Math.min(y0, y1);
		this.z=Math.min(z0, z1);
		if(x0!=x1) {
			length=Math.abs(x1-x0)+1;
			deltaX=1;
		}
		if(y0!=y1) {
			if(length>0)
				throw new IllegalArgumentException("Zowel x als y zijn verschillend.");
			length=Math.abs(y1-y0)+1;
			deltaY=1;
		} 
		if(z0!=z1) {
			if(length>0)
				throw new IllegalArgumentException("Zowel z als (x of y) zijn verschillend.");
			length=Math.abs(z1-z0)+1;
			deltaZ=1;
		}
		if (length==0)
			length=1;
		for(int l=0;l<length;l++)
			balkPoss.add(new BalkPos(this,l));
		if(deltaZ==0) {
			grondVlak=balkPoss;
			dakVlak=balkPoss;
		} else {
			grondVlak=List.of(balkPoss.get(0));
			dakVlak=List.of(balkPoss.get(length-1));
		}
	}
	
	public List<BalkPos> getPosities(){
		return balkPoss;
	}

	@Override
	public String toString() {
		return "Balk-" + id;
	}
	
}
