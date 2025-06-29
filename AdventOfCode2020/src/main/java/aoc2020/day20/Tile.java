package aoc2020.day20;

import java.util.HashSet;
import java.util.Set;

public class Tile {

	public long id;
	// matrix [rijen][kolommen]
	public boolean[][] data;
	public int t,l,r,b;
	public int rt,rl,rr,rb;
	public int dim;
	//
	//      [0]
	// [3] this [1]
	//      [2]
	//
	static class TileList {
		Set<Tile> tiles=new HashSet<>();
		public String toString() {
			StringBuffer buf=new StringBuffer("[");
			tiles.forEach(t->buf.append(t.id+","));
			return buf.append("]").toString();
		}
	}
	TileList[] neighbours=new TileList[4];
	
	public Tile(long id,boolean[][] data)
	{
		this.id=id;
		dim=data.length;
		this.data=data;
		for (int x=0;x<dim;x++)
		{
			t=t*2+(data[0][x]?1:0);
			rt=rt*2+(data[0][dim-1-x]?1:0);
			l=l*2+(data[dim-1-x][0]?1:0);
			rl=rl*2+(data[x][0]?1:0);
			r=r*2+(data[x][dim-1]?1:0);
			rr=rr*2+(data[dim-1-x][dim-1]?1:0);
			b=b*2+(data[dim-1][dim-1-x]?1:0);
			rb=rb*2+(data[dim-1][x]?1:0);
		}
		for(int s=0;s<4;s++)
			neighbours[s]=new TileList();
	}
	
	public Tile clone() {
		Tile tmp=new Tile(id,data);
		tmp.dim=dim;
		tmp.t=t;tmp.rt=rt;
		tmp.l=l;tmp.rl=rl;
		tmp.r=r;tmp.rr=rr;
		tmp.b=b;tmp.rb=rb;
		for(int i=0;i<4;i++){
			tmp.neighbours[0]=neighbours[0];
		}
		return tmp;
	}
	
	/**
	 * Controleert of deze tile past op de gegeven zijde.
	 * Deze tile wordt in alle mogelijkheden gechecked (flipped, rotated)
	 * @param side of other tile to match
	 * @return
	 */
	public boolean match(int sideInt,Tile tile) {
		if(sideInt==t||sideInt==rt) {
			neighbours[0].tiles.add(tile);
			return true;
		}
		if(sideInt==r||sideInt==rr) {
			neighbours[1].tiles.add(tile);
			return true;
		}
		if(sideInt==l||sideInt==rl) {
			neighbours[3].tiles.add(tile);
			return true;
		}
		if(sideInt==b||sideInt==rb) {
			neighbours[2].tiles.add(tile);
			return true;
		}
		return false;
	}
	/**
	 * als de tile aan een zijde past wordt hij opgeslagen als buur
	 * @param tile
	 */
	public void fillNeighbours(Tile tile) {
		if(match(tile.t,tile)||match(tile.r,tile)||match(tile.b,tile)||match(tile.l,tile));
	}

	public int mirrored(boolean[] side) {
		int result=0;
		for(int i=side.length-1;i>0;i--)
			result=result*2+(side[i]?1:0);
		return result;
	}
	/**
	 * Geeft nieuwe tile terug links-rechts-gespiegeld
	 * @return
	 */
	private Tile flipped() {
		boolean[][] newdata=new boolean[dim][dim];
		for(int r=0;r<dim;r++) {
			for(int k=0;k<dim;k++) {
				newdata[r][dim-k-1]=data[r][k];
			}
		}
		Tile newtile=new Tile(id,newdata);
		newtile.dim=dim;
		newtile.t=rt;
		newtile.rt=t;
		newtile.b=rb;
		newtile.rb=b;
		newtile.l=rr;
		newtile.rl=r;
		newtile.r=rl;
		newtile.rr=l;
		newtile.neighbours[0]=neighbours[0];
		newtile.neighbours[1]=neighbours[3];
		newtile.neighbours[2]=neighbours[2];
		newtile.neighbours[3]=neighbours[1];
//		System.out.print(" flipped");
		return newtile;
	}
	/**
	 * draait 90° links
	 * @return
	 */
	public Tile rotate() {
		boolean[][] newdata=new boolean[dim][dim];
		for(int r=0;r<dim;r++) {
			for(int k=0;k<dim;k++) {
				newdata[dim-k-1][r]=data[r][k];
			}
		}
		Tile newtile=new Tile(id,newdata);
		newtile.t=r;
		newtile.rt=rr;
		newtile.r=b;
		newtile.rr=rb;
		newtile.b=l;
		newtile.rb=rl;
		newtile.l=t;
		newtile.rl=rt;
		newtile.neighbours[0]=neighbours[1];
		newtile.neighbours[1]=neighbours[2];
		newtile.neighbours[2]=neighbours[3];
		newtile.neighbours[3]=neighbours[0];
//		System.out.print(" rotated");
		return newtile;
	}
	
	public Tile rightfit(Tile leftTile) {
		if(leftTile.r==rl)
			return this;
		if(leftTile.r==l)
			return flipped().rotate().rotate();
		if(leftTile.r==rr)
			return rotate().rotate();
		if(leftTile.r==r)
			return flipped();
		if(leftTile.r==t)
			return flipped().rotate();
		if(leftTile.r==rt)
			return rotate();
		if(leftTile.r==b)
			return flipped().rotate().rotate().rotate();
		if(leftTile.r==rb)
			return rotate().rotate().rotate();
		throw new RuntimeException("Tile "+leftTile+" is geen buur van "+id);
	}
	
	public Tile bottomFit(Tile topTile) {
		if(topTile.b==rt) return this;
		if(topTile.b==t) return flipped();
		if(topTile.b==rr) return rotate();
		if(topTile.b==r) return rotate().flipped();
		if(topTile.b==b) return flipped().rotate().rotate();
		if(topTile.b==rb) return rotate().rotate();
		if(topTile.b==l) return flipped().rotate();
		if(topTile.b==rl) return rotate().rotate().rotate();
		throw new RuntimeException("Tile "+topTile+" is geen buur van "+id);		
	}
	/**
	 * Geeft de huidige tile terug juist georienteerd of null als
	 * de huidige tile niet past.
	 * @param leftTile linker tile (kan null zijn)
	 * @param topTile top tile (kan null zijn)
	 * @return
	 */
	public Tile rightAndBottom(Tile leftTile, Tile topTile) {
		Tile result=null;
		if(leftTile!=null)
		{
			result=rightfit(leftTile);
			if(topTile==null && result.neighbours[0].tiles.isEmpty()
			 ||result.neighbours[0].tiles.contains(topTile))
				return result;
		} else {
			if(topTile==null) {
				// beide null
				result=this;
				while(true) {
					if(neighbours[0].tiles.isEmpty()&&neighbours[3].tiles.isEmpty())
						break;
					result=result.rotate();
				}
				return result;
			}
			result=bottomFit(topTile);
			if(result.neighbours[3].tiles.isEmpty())
				return result;
		}
		return null;
	}
	
	public String toString() {
		return "Tile-"+id;
	}

	public String toString(boolean extras) {
		StringBuffer buf=new StringBuffer("Tile-"+id+" (");
		for (int i=0;i<4;i++)
			buf.append(" "+(neighbours[i]!=null?neighbours[i]:"null")+" ");
		buf.append(")");
		return buf.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
