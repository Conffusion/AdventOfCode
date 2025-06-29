package aoc2020.day20.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;

import aoc2020.day20.Tile;

public class TilePanel extends Panel {

	private static final long serialVersionUID = -1580690999255764976L;

	private Tile tile;

	public boolean includeBorders=true;
	
	public TilePanel(Tile tile,boolean includeBorders) {
		this.tile=tile;
		this.includeBorders=includeBorders;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int dimr=tile.data.length-(includeBorders?0:2);
		int dimk=tile.data[0].length-(includeBorders?0:2);
		int y=0;
		for (int r=includeBorders?0:1;r<dimr;r++,y+=Config.BLOK_PIXELS) {
			int x=0;
			for (int k=includeBorders?0:1;k<dimk;k++,x+=Config.BLOK_PIXELS) {
			 	g.setColor(tile.data[r][k]?Config.TRUE_COLOR:Config.FALSE_COLOR);
				g.fillRect(x, y, Config.BLOK_PIXELS, Config.BLOK_PIXELS);
			}
		}
		g.setColor(Color.RED);
		g.drawRect(0, 0, Config.BLOK_PIXELS*dimk, Config.BLOK_PIXELS*dimr);
	}
	
}
