package aoc2020.day20.graph;

import java.awt.Graphics;
import java.awt.Panel;

import aoc2020.day20.Tile;

/**
 * toont een tapijt van tegels
 * Een tegel heeft 8x8 vakjes of 10x10(incl border)
 * @author walter
 *
 */
public class TapijtPanel extends Panel {

	private static final long serialVersionUID = 126493363663637967L;

	Tile[][] tapijt;
	
	public TapijtPanel(Tile[][] tapijt) {
		this.tapijt=tapijt;
		setSize(Config.BLOK_PIXELS*(Config.includeBorders?10:8)*tapijt.length+tapijt.length, Config.BLOK_PIXELS*(Config.includeBorders?10:8)*tapijt[0].length+tapijt[0].length);
		setPreferredSize(getSize());
	}

	public void paintTile(Tile tile,Graphics g,int startx,int starty) {
		super.paint(g);
		int dimr=tile.data.length-(Config.includeBorders?0:1);
		int dimk=tile.data[0].length-(Config.includeBorders?0:1);
		int y=starty;
		for (int r=Config.includeBorders?0:1;r<dimr;r++,y+=Config.BLOK_PIXELS) {
			int x=startx;
			for (int k=Config.includeBorders?0:1;k<dimk;k++,x+=Config.BLOK_PIXELS) {
			 	g.setColor(tile.data[r][k]?Config.TRUE_COLOR:Config.FALSE_COLOR);
				g.fillRect(1+x, 1+y, Config.BLOK_PIXELS, Config.BLOK_PIXELS);
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		int dimTile=Config.BLOK_PIXELS*(Config.includeBorders?10:8);
		for (int row=0;row<tapijt.length;row++) {
			for(int kol=0;kol<tapijt[0].length;kol++) {
				paintTile(tapijt[row][kol],g,dimTile*kol+kol,dimTile*row+row);
			}
		}
	}
	
}
