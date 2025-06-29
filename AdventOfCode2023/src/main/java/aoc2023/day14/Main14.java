package aoc2023.day14;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import common.graph.Direction;
import common.graph.Point;
import common.main.AbstractMainMaster;

/**
 * 2D veld met rolkeien op een platform dat kan bewegen zoals een knikker doolhof.
 * Alle rolkeien rollen dan naar 1 kant tot ze niet meer verder kunnen door: rand van bord, of andere kei of vast rotsblok 
 */
public class Main14 extends AbstractMainMaster<Long> {

	public static void main(String[] args) {
		new Main14()
			//.testMode()
				// .withFileOutput()
				// .nolog()
				.start();
	}

	char[][] platform;
	Dimension dim;

	@Override
	public void beforeEach() {
		dim = testMode ? new Dimension(10, 10) : new Dimension(100, 100);
		platform = loadInputInto2DArrayXY(dim.width, dim.height);
	}

	// valt het punt binnen ons platform
	public boolean inDim(Point point) {
		return point.x >= 0 && point.x < dim.width && point.y >= 0 && point.y < dim.height;
	}

	// antwoord : 109424
	public Long star1() {
		kantelPlatform(Direction.UP);
		return totalLoad();
	}

	public long totalLoad() {
		long total = 0;
		for (int x = 0; x < dim.width; x++)
			for (int y = 0; y < dim.height; y++) {
				if (platform[x][y] == 'O')
					total += dim.height - y;
			}
		return total;
	}

	public void kantelPlatform(Direction naar) {
		int xVan = naar == Direction.RIGHT ? dim.width - 1 : 0;
		int xdelta=xVan==0?1:-1;
		int yVan = naar == Direction.DOWN ? dim.height - 1 : 0;
		int ydelta=yVan==0?1:-1;
		int x=xVan;
		for(int i=0;i<dim.width;i++) {
			x=xVan+i*xdelta;
			for (int j=0;j<dim.height;j++) {
				int y=yVan+j*ydelta;
				if (platform[x][y] != 'O')
					continue;
				Point p = new Point(x, y);
				while (true) {
					Point dest = naar.move(p);
					if (inDim(dest) && platform[dest.x][dest.y] == '.') {
						platform[dest.x][dest.y] = 'O';
						platform[p.x][p.y] = '.';
						p = dest;
					} else
						break;
				}
			}
		}
	}

	/**
	 * Er moet eigenlijk 1000000000 keer geloopt worden maar we zien dat vanaf 109e er steeds eenzelfde set 
	 * waarden terugkomt. Om de 9 loops begint de rij weer opnieuw.
	 * de oplossing van 1000000000e = de oplossing van 109+ ((1000000000 - 109) mod 9)
	 * 
	 */
	// antwoord : 102509
	public Long star2() {
		List<Long> hits=new ArrayList<>();

		for (long teller = 1; teller <= 200; teller++) {
			kantelPlatform(Direction.UP);
			kantelPlatform(Direction.LEFT);
			kantelPlatform(Direction.DOWN);
			kantelPlatform(Direction.RIGHT);
			long tl=totalLoad();
			if(hits.contains(tl))
				logln(""+teller+" met waarde "+tl+" ***");
			else
				logln(""+teller+" met waarde "+tl);
			hits.add(tl);
		}
		return totalLoad();
	}
}