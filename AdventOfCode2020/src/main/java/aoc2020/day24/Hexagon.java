package aoc2020.day24;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Een hexagon is 2D tegelstructuur van zeshoeken waarbij elke tegel een boolean waarde heeft.
 * Het is een oneindig tapijt.
 * @author walter
 *
 */
public class Hexagon {
	private Map<Point,Boolean> tapijt=new HashMap<>();
	public static Boolean WHITE=Boolean.FALSE;
	private Point current;
	private Point start;
	int minx,maxx,miny,maxy;
	
	public boolean log;
	
	public Hexagon() {
		tapijt.getOrDefault(start=new Point(0,0), WHITE);
	}
	
	public void gotoStart()
	{
		current=start;
	}
	
	public boolean getValue(Point p)
	{
		return tapijt.getOrDefault(p, WHITE);
	}
	
	public int count(Point p,Boolean value) {
		int count=0;
		
		return count;				
	}
	
	/**
	 * Geeft de buur terug tov p in de richting van direction
	 */
	public Point goTo(Point p,String direction) {
		int newx=p.x,newy=p.y;
		if(direction.contains("w"))newx--;
		if ("e".equals(direction))newx++;
		else if("w".equals(direction));
		else if("se,ne,nw,sw".contains(direction)) newx+=Math.abs(p.y)%2;
		
		if("se,sw".contains(direction))newy++;
		if("nw,ne".contains(direction))newy--;
		return new Point(newx,newy);
	}

	public void move(String direction) {
		Point newpoint=goTo(current,direction);
		log(current+" moving "+direction+" to "+newpoint);
		current=newpoint;
		getOrAdd(current);
	}
	
	private Boolean getOrAdd(Point p)
	{
		if (minx>p.x) minx=p.x;
		if (maxx<p.x) maxx=p.x;
		if (miny>p.y) miny=p.y;
		if (maxy<p.y) maxy=p.y;
		return tapijt.computeIfAbsent(p, (tp)->WHITE);
	}
	
	public int countTrueNeighbours(Point p)
	{
		int result=0;
		result+= (getValue(goTo(p,"nw"))?1:0); 
		result+= (getValue(goTo(p,"w"))?1:0);
		result+= (getValue(goTo(p,"sw"))?1:0);
		if (result<3 && getValue(goTo(p,"se"))) result++;
		if (result<3 && getValue(goTo(p,"e"))) result++;
		if (result<3 && getValue(goTo(p,"ne"))) result++;
		return result;
	}
	
	/**
	 * Als de tile ontbreekt wordt hij toegevoegd en geflipt.
	 * @param p
	 */
	public void flipTile(Point p)
	{
		tapijt.put(p, !getOrAdd(p).booleanValue());		
	}
	
	public void flipCurrent() {
		log("Flipping "+current+" to "+!tapijt.get(current).booleanValue());
		tapijt.put(current, !tapijt.get(current).booleanValue());		
	}
	
	private void log(String text) {
		if(log)System.out.println(text);
	}
	public long count() {
		return tapijt.values().stream().filter(v->v.booleanValue()).count();
	}
	public Hexagon clone() {
		Hexagon copy=new Hexagon();
		copy.current=new Point(current.x,current.y);
		copy.minx=minx;
		copy.maxx=maxx;
		copy.miny=miny;
		copy.maxy=maxy;
		for (Map.Entry<Point, Boolean> entry:tapijt.entrySet())
		{
			copy.tapijt.put(entry.getKey(),entry.getValue());
		}
		return copy;
	}
	public void show() {
		System.out.println("Vloer(x=["+minx+","+maxx+"] y=["+miny+","+maxy+"]---------");
		for(int y=miny;y<=maxy;y++) {
			if(Math.abs(y)%2==1) System.out.print(" ");
			for (int x=minx;x<=maxx;x++) {
				System.out.print(getOrAdd(new Point(x,y))?"T ":"F ");
			}
			System.out.println();
		}
		System.out.println("--------------------------");
	}
}
