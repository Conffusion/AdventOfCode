package aoc2020.day20;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2020.common.MainMaster;
import aoc2020.day20.graph.Config;
import aoc2020.day20.graph.MainFrame;

public class Main20 extends MainMaster {

	static Pattern titlePattern = Pattern.compile("Tile (\\d+):");
	static Pattern tileLinePattern = Pattern.compile("[\\.#]+");
	List<Tile> tiles;

	public void loadData(String filename) {
		List<String> data = loadInput(20, filename);
		tiles = new ArrayList<>();
		int tmpId = 0;
		boolean[][] tempTile = null;
		int line = 0;
		int dim = 10;
		for (String s : data) {
			Matcher m = titlePattern.matcher(s);
			if (m.matches()) {
				tmpId = Integer.parseInt(m.group(1));
				tempTile = new boolean[dim][dim];
				line = 0;
			} else if (tileLinePattern.matcher(s).matches()) {
				boolean[] tileline = new boolean[dim];
				for (int t = 0; t < dim; t++)
					tileline[t] = s.charAt(t) == '#';
				tempTile[line++] = tileline;
				if (line == dim) {
					tiles.add(new Tile(tmpId, tempTile));
				}
			} else if (s.length() != 0) {
				System.err.println("unexpected line:'" + s + "'");
			}
		}
		if (log)
			logln("Aantal stukjes:" + tiles.size());
	}

	public Tile match(Tile t, int s) {
		for (Tile x : tiles) {
			if (x.id != t.id && x.match(s, t))
				return x;
		}
		return null;
	}

	public Tile[] corners() {
		Tile[] result = new Tile[4];
		int currindex = 0;
		for (Tile t : tiles) {
			if (match(t, t.t) == null && match(t, t.l) == null)
				result[currindex++] = t;
			if (match(t, t.t) == null && match(t, t.r) == null)
				result[currindex++] = t;
			if (match(t, t.b) == null && match(t, t.l) == null)
				result[currindex++] = t;
			if (match(t, t.b) == null && match(t, t.r) == null)
				result[currindex++] = t;
		}
		if (result[0] == null || result[1] == null || result[2] == null || result[3] == null)
			System.err.println(
					"Niet alle 4 hoeken gevonden:" + result[0] + "," + result[1] + "," + result[2] + "," + result[3]);
		return result;
	}

	public void copyTile(Tile tile, char[][] bord, int row, int kol) {
		for (int r = 0; r < 8; r++)
			for (int k = 0; k < 8; k++)
				bord[row + r][kol + k] = tile.data[r + 1][k + 1] ? '#' : '.';
	}

	public String[] removeBorders(Tile[][] puzzle) {
		int dim = puzzle.length;
		int tiledim = puzzle[0][0].dim;
		char[][] carray = new char[dim * (tiledim - 2)][dim * (tiledim - 2)];
		for (int row = 0; row < dim; row++) {
			for (int kol = 0; kol < dim; kol++) {
				copyTile(puzzle[row][kol], carray, kol * (tiledim - 2), row * (tiledim - 2));
			}
		}
		String[] result = new String[dim * (tiledim - 2)];
		for (int r = 0; r < dim * (tiledim - 2); r++)
			result[r] = new String(carray[r]);
		return result;
	}

	public String[] rotateLeft(String[] content) {
		char[][] buf = new char[content.length][content.length];
		int dim = content.length;
		for (int r = 0; r < dim; r++)
			for (int k = 0; k < dim; k++)
				buf[dim - k - 1][r] = content[r].charAt(k);
		String[] result = new String[dim];
		for (int r = 0; r < dim; r++)
			result[r] = new String(buf[r]);
		return result;
	}

	public String[] flip(String[] content) {
		int dim = content.length;
		String[] result = new String[dim];
		for (int r = 0; r < dim; r++)
			result[r] = new StringBuffer(content[r]).reverse().toString();
		return result;
	}

	Pattern line1 = Pattern.compile(".{18}#.");
	Pattern line2 = Pattern.compile("#.{4}##.{4}##.{4}###");
	Pattern line3 = Pattern.compile(".#..#..#..#..#..#...");

	public int searchSeaMonsters(String[] content) {
		int count = 0;
		for (int r = 2; r < content.length; r++) {
			Matcher m1 = line3.matcher(content[r]);
			int index = 0;
			while (m1.find()) {
				index += m1.start();
				if (line2.matcher(content[r - 1].substring(index, index + 20)).find()
						&& line1.matcher(content[r - 2].substring(index, index + 20)).find()) {
					count++;
					System.out.println("Row " + r + " Start position :" + (index));
				}
				m1 = line3.matcher(content[r].substring(++index));
			}

		}
		return count;
	}

	public int searchSeaMonstersPart(String[] content) {
		int count = 0;
		for (int r = 0; r < content.length; r++) {
			Matcher m1 = line1.matcher(content[r]);
			int index = 0;
			while (m1.find()) {
				index += m1.start();
				count++;
				System.out.println("Row " + r + " Start position :" + (index));
				m1 = line1.matcher(content[r].substring(++index));
			}
		}
		return count;
	}

	// oplossing: Gevonden hoeken:Tile-1301,Tile-1373,Tile-3593,Tile-1289
	// product: 8272903687921
	public void star1() {
		loadData("input.txt");
		Tile[] opl = corners();
		System.out.println("Gevonden hoeken:" + opl[0] + "," + opl[1] + "," + opl[2] + "," + opl[3]);
		System.out.println("***STAR1:" + (opl[0].id * opl[1].id * opl[2].id * opl[3].id));
	}
	public void star2() {
		logic2("input.txt",1301);
	}
	public void test2() {
		logic2("testinput.txt",1591);
	}
	
	public void logic2(String filename,int startTile) {
		loadData(filename);

		for (Tile t1 : tiles)
			for (Tile t2 : tiles)
				if (!t1.equals(t2))
					t1.fillNeighbours(t2);
		// linkerbovenhoek
		Tile topleft = findById(startTile); // input.txt:1301, testinput: 1951
		// linkerboven juist roteren zodat links en top null zijn
		for (int i = 0; i < 3; i++) {
			if (topleft.neighbours[i].tiles.isEmpty() && topleft.neighbours[i + 1].tiles.isEmpty()) {
				for (int k = 1; k <= i + 1; k++)
					topleft = topleft.rotate();
				break;
			}
		}
		int puzzledim = (int) Math.sqrt(tiles.size());
		Tile[][] puzzle = new Tile[puzzledim][puzzledim];
		puzzle[0][0] = topleft;
		Tile prev = null;
		for (int row = 0; row < puzzledim; row++) {
			if (log)
				logln("puzzel rij " + row);
			if (row == 0)
				prev = puzzle[0][0];
			else {
				prev = puzzle[row - 1][0];
				for (Tile t : prev.neighbours[2].tiles) {
					if ((puzzle[row][0] = t.rightAndBottom(null, prev)) != null)
						break;
				}
				if (puzzle[row][0] == null)
					throw new RuntimeException("Geen match gevonden");
			}
			if (log)
				logln("puzzel kolom 0: " + puzzle[row][0].toString(true));
			for (int kol = 1; kol < puzzledim; kol++) {
				for (Tile t : puzzle[row][kol - 1].neighbours[1].tiles) {
					puzzle[row][kol] = t.rightAndBottom(puzzle[row][kol - 1], row > 0 ? puzzle[row - 1][kol] : null);
					if (puzzle[row][kol] != null)
						break;
				}
				if (puzzle[row][kol] == null)
					throw new RuntimeException("Geen match gevonden");
				if (log)
					logln("puzzel kolom " + kol + ": " + puzzle[row][kol].toString(true));
			}
		}
		// nu is puzzle volledig gelegd
		if (log)
			logln(toString(puzzle));
		Config.includeBorders = false;
		String[] content = removeBorders(puzzle);
		content = flip(content);
		content = rotateLeft(content);
		for (int i = 0; i < content.length; i++)
			System.out.println(content[i]);
		int monstercount = 0;
		MainFrame.show(puzzle);
		search: {
			for (int i = 0; i <= 3; i++) {
				monstercount = searchSeaMonsters(content);
				System.out.println("monster count:" + monstercount);
				if (monstercount > 0)
					break search;
				content = rotateLeft(content);
			}
			content = flip(content);
			System.out.println("flipped");
			for (int i = 0; i <= 3; i++) {
				monstercount = searchSeaMonsters(content);
				System.out.println("monster count:" + monstercount);
				if (monstercount > 0)
					break search;
				content = rotateLeft(content);
			}
		}
		System.out.println("gevonden monsters:" + monstercount);
	}

	public String toString(Tile[][] puzzel) {
		StringBuffer buf = new StringBuffer();
		for (int row = 0; row < puzzel.length; row++) {
			for (int tilerow = 0; tilerow < 10; tilerow++) {
				for (int kol = 0; kol < puzzel.length; kol++) {
					for (int tilekol = 0; tilekol < 10; tilekol++) {
						buf.append(puzzel[row][kol].data[tilerow][tilekol] ? "#" : ".");
					}
					buf.append(" ");
				}
				buf.append("\n");
			}
			buf.append("\n");
		}
		return buf.toString();
	}

	public Tile findById(long id) {
		for (Tile t : tiles)
			if (t.id == id)
				return t;
		return null;
	}

	public void unitTest() {
		loadData("testinput.txt");
		Tile tile1951 = findById(1951);
		tile1951=tile1951.rightAndBottom(null, null);
		Tile leftTile = match(tile1951, tile1951.l);
		System.out.println("Found tile left of 1951:" + leftTile);
		Tile topTile = match(tile1951, tile1951.t);
		System.out.println("Found tile top of 1951:" + topTile);

	}

	public void test1() {
		loadData("testinput.txt");
		Tile[] opl = corners();
		System.out.println("Gevonden hoeken:" + opl[0] + "," + opl[1] + "," + opl[2] + "," + opl[3]);
		System.out.println("***TESTSTAR1:" + (opl[0].id * opl[1].id * opl[2].id * opl[3].id));
	}

	public static void main(String[] args) {
		Main20 m = new Main20();
		m.star2();
	}

}
