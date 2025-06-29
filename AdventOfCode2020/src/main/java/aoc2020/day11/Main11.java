package aoc2020.day11;

import java.util.Arrays;
import java.util.List;

import aoc2020.common.MainMaster;

public class Main11 extends MainMaster {

	int[][] hall;
	int rows, cols;

	public void loadData(String filename) throws Exception {
		List<String> all = loadInput(11, filename);
		if (log) {
			logln(all.get(0));
			logln("lines:" + all.size() + ", cols:" + all.get(0).length());
		}
		hall = new int[all.size()][all.get(0).length()];
		int i = 0;
		for (String line : all) {
			hall[i++] = line.chars().map(c -> {
				if (c == '.')
					return 9;
				else if (c == '#')
					return 1;
				return 0;
			}).toArray();
		}
		rows = hall.length;
		cols = hall[0].length;
		if (log)
			logln("loaded " + rows + "," + cols);
	}

	public int[][] clone(int[][] in) {
		int[][] out = new int[rows][cols];
		int i = 0;
		for (int[] l : in) {
			System.arraycopy(l, 0, out[i++], 0, cols);
		}
		return out;
	}

	public int countSurroundingSeats(int row, int col, int value) {
		int counter = 0;
		counter += (row > 0 && col > 0 && hall[row - 1][col - 1] == value) ? 1 : 0;
		counter += (row > 0 && hall[row - 1][col] == value) ? 1 : 0;
		counter += (row > 0 && col < cols - 1 && hall[row - 1][col + 1] == value) ? 1 : 0;
		counter += (col > 0 && hall[row][col - 1] == value) ? 1 : 0;
		counter += (col < cols - 1 && hall[row][col + 1] == value) ? 1 : 0;
		counter += (row < rows - 1 && col > 0 && hall[row + 1][col - 1] == value ? 1 : 0);
		counter += (row < rows - 1 && hall[row + 1][col] == value ? 1 : 0);
		counter += (row < rows - 1 && col < cols - 1 && hall[row + 1][col + 1] == value ? 1 : 0);
		return counter;
	}

	public int checkViewLine(int row, int col, int rowD, int colD) {
		int r = row + rowD, c = col + colD;
		try {
			for (; hall[r][c] == 9; r += rowD, c += colD)
				;
		} catch (IndexOutOfBoundsException ex) {
			return 9;
		}
		return hall[r][c];
	}

	public int countSurroundingLines(int row, int col, int value) {
		return (row > 0 ? //
				(checkViewLine(row, col, -1, -1) == value ? 1 : 0)//
						+ (checkViewLine(row, col, -1, 0) == value ? 1 : 0) //
						+ (checkViewLine(row, col, -1, 1) == value ? 1 : 0)
				: 0) //
				+ (checkViewLine(row, col, 0, -1) == value ? 1 : 0) //
				+ (checkViewLine(row, col, 0, 1) == value ? 1 : 0) //
				+ (row < rows - 1 ? //
					(checkViewLine(row, col, 1, -1) == value ? 1 : 0) //
							+ (checkViewLine(row, col, 1, 0) == value ? 1 : 0) //
							+ (checkViewLine(row, col, 1, 1) == value ? 1 : 0)
					: 0);
	}

	public int countOccupied(int[][] room) {
		int count = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++)
				count += room[r][c] == 1 ? 1 : 0;
			if (log)
				logln(Arrays.toString(room[r]));
		}
		return count;
	}

	public void testIt1() throws Exception {
		loadData("testit1.txt");
		if (log)
			logln("" + countSurroundingLines(0, 8, 1));
	}

	public void star1() {
		int[][] temp;
		boolean diff = true;
		int it = 0;
		while (diff) {
			diff = false;
			it++;
			temp = clone(hall);
			for (int r = 0; r < rows; r++)
				for (int c = 0; c < cols; c++) {
					if (hall[r][c] == 0 && countSurroundingSeats(r, c, 1) == 0)
						temp[r][c] = 1;
					if (hall[r][c] == 1 && countSurroundingSeats(r, c, 1) >= 4)
						temp[r][c] = 0;
					diff = diff || temp[r][c] != hall[r][c];
				}
			hall = temp;
			if (log)
				logln("iteration " + it + ", occupied:" + countOccupied(hall));
		}
		System.out.println("*** STAR1:iteration " + it + ", occupied:" + countOccupied(hall));
	}

	public void star2() {
		int[][] temp;
		boolean diff = true;
		int it = 0;
		while (diff) {
			diff = false;
			it++;
			temp = clone(hall);
			for (int r = 0; r < rows; r++)
				for (int c = 0; c < cols; c++) {
					if (hall[r][c] == 0 && countSurroundingLines(r, c, 1) == 0)
						temp[r][c] = 1;
					if (hall[r][c] == 1 && countSurroundingLines(r, c, 1) >= 5)
						temp[r][c] = 0;
					diff = diff || temp[r][c] != hall[r][c];
				}
			hall = temp;
			if (log)
				logln("iteration " + it + ", occupied:" + countOccupied(hall));
		}
		System.out.println("***STAR2: iteration " + it + ", occupied:" + countOccupied(hall));
	}

	public static void main(String args[]) throws Exception {
		Main11 m = new Main11();
//		m.testIt1();
		m.loadData("input.txt");
		m.star2();
//		m.star1();
//		m.loadData("testit1.txt");
		long sumtime = 0;
		long iterations = 100;
		for (int i = 0; i < iterations; i++) {
			m.loadData("input.txt");
			sumtime += m.timer(() -> m.star2());
		}
		System.out.println("avg time:" + (sumtime / iterations));
	}
}
