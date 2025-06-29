package aoc2021.day04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.main.AbstractMainMaster;

public class Main04 extends AbstractMainMaster<Integer> {
	static int DIMENSION=5; 
	int[] called;
	List<BingoBoard> boards;
	
	class BoardCell {
		int value;
		boolean marked=false;
		public BoardCell(int value) {
			this.value = value;
		}
		@Override
		public String toString() {
			return String.format("%2d", value) +(marked?"*":" ")+ " ";
		}		
	}
	
	class BingoBoard {
		int id=0;
		BoardCell[][] board=new BoardCell[DIMENSION][DIMENSION];
		Map<Integer,BoardCell> figures=new HashMap<>();
		
		public BingoBoard(int id) {
			this.id = id;
		}
		public void setValue(int row, int col, int value) {
			board[row][col]=new BoardCell(value);
			figures.put(value,board[row][col]);
		}
		public void newCall(int value) {
			if(figures.containsKey(value))
				figures.get(value).marked=true;
		}
		public boolean checkBoard() {
			first_loop:
			for(int i=0;i<DIMENSION;i++) {
				boolean rowcomplete=true;
				boolean colcomplete=true;
				for(int j=0;j<DIMENSION;j++)
				{
					rowcomplete=rowcomplete && board[i][j].marked;
					colcomplete=colcomplete && board[j][i].marked;
					if(!rowcomplete && !colcomplete)
						continue first_loop;
				}
				return true;
			}
			return false;
		}
		public int calculateSum() {
			int unmarkedsum=0;
			for(int i=0;i<DIMENSION;i++)
				for (int j=0;j<DIMENSION;j++)
					unmarkedsum+=board[i][j].marked?0:board[i][j].value;
			logln("board "+id+" unmarked sum:"+unmarkedsum);
			return unmarkedsum;
		}
		@Override
		public String toString() {
			StringBuffer buf=new StringBuffer();
			for(int i=0;i<DIMENSION;i++) {
				for (int j=0;j<DIMENSION;j++) {
					buf.append(board[i][j]);
				}
				buf.append("\n");
			}
			buf.append("\n");
			return buf.toString();
		}
		
	}
	
	@Override
	public void beforeEach() {
		boards=new ArrayList<>();
		List<String> all=loadInputByLine();
		called=parseNumbersLine(all.get(0),",");
		int loadindex=2; // skip first 2 lines
		int boardcount=0;
		while(true) {
			BingoBoard bb=new BingoBoard(++boardcount);
			for(int r=0;r<DIMENSION;r++) {
				int[] row=parseNumbersLine(all.get(loadindex++),"\\s+");
				for(int c=0;c<DIMENSION;c++)
					bb.setValue(r, c, row[c]);
			}
			boards.add(bb);
			loadindex++;
			if(all.size()<=loadindex)
				break;
		}
	}

	/**
	 * van het bord dat als eerste een rij vol heeft geeft de som van de overgebleven getallen vermenigvuldigd met laatst
	 * afgeroepen getal
	 * antwoord: 39984
	 */
	@Override
	public Integer star1() {
		for(int n=0;n<called.length;n++) {
			for(BingoBoard b:boards)
			{
				b.newCall(called[n]);
				if(b.checkBoard()) {
					logln("board "+b.id+" won after "+called[n]);
					return b.calculateSum()*called[n];
				}
			}
		}
		return null;
	}

	/**
	 * van het bord dat als laatste een rij vol heeft geeft de som van de overgebleven getallen vermenigvuldigd met laatst
	 * afgeroepen getal
	 * antwoord 8468
	 */
	@Override
	public Integer star2() {
		Set<Integer> completeBoards=new HashSet<>();
		for(int n=0;n<called.length;n++) {
			for(BingoBoard b:boards)
			{
				b.newCall(called[n]);
			}
			BingoBoard lastWinning=null;
			for(BingoBoard b:boards)
				if(!completeBoards.contains(b.id) && b.checkBoard())
				{
					completeBoards.add(b.id);
					lastWinning=b;
				}
			if(log) {
				logln("after called "+called[n]);
				for(BingoBoard b:boards)
				{
					logln(b);
				}
				logln("------------------");
			}
			if(completeBoards.size()==boards.size()) {
				logln("last won after "+called[n]);
				return lastWinning.calculateSum()*called[n];
			}
				
		}
		return null;
	}

	public static void main(String[] args) {
		new Main04()
			.nolog()
			//.testMode()
			.start();
	}

}
