package aoc2022.day08;

import java.util.List;

import common.main.AbstractMainMaster;

public class Main08 extends AbstractMainMaster<Integer> {
    static int DIM_ROW=99, DIM_COL=99;

    int[][] forrest;
    boolean[][] visible;
    
    public static void main(String[] args) {
        new Main08()
            //.testMode()
            //.nolog()
           .start();
    }

    @Override
    public void beforeAllTest() {
        DIM_ROW=5;
        DIM_COL=5;
    }

    @Override
    public void beforeAll() {
        forrest=new int[DIM_ROW][DIM_COL];
        visible=new boolean[DIM_ROW][DIM_COL];
        List<String>input=loadInputByLine();
        for(int r=0;r<DIM_ROW;r++) {
            char[] line=input.get(r).toCharArray();
            for(int c=0;c<DIM_COL;c++) {
                forrest[r][c]=line[c]-'0';
            }
        }
    }

    public Integer star1() {
        int totcounter=0;
        for (int c=1;c<DIM_COL-1;c++) {
            totcounter+=walkIn1(0, c, 1, 0); // van boven naar beneden
            totcounter+=walkIn1(c, 0, 0, 1); // van links naar rechts
            totcounter+=walkIn1(c, DIM_COL-1, 0, -1); // van rechts naar links
            totcounter+=walkIn1(DIM_ROW-1, c, -1, 0); // van beneden naar boven
        }
        printForrest();
        return totcounter+4; // 4 hoeken
    }

    // alleen het aantal nieuwe zichtbare bomen wordt geteld
    private int walkIn1(int rstart, int cstart, int rinc, int cinc) {
        int prevHeight=-1;
        int counter=0;
        for (int i=0;i<DIM_COL-1;i++) {
            int trow=rstart+i*rinc;
            int tcol=cstart+i*cinc;
            if(forrest[trow][tcol]>prevHeight) {
                if(!visible[trow][tcol]) 
                    counter++;   
                visible[trow][tcol]=true;
                prevHeight=forrest[trow][tcol];
            }
        }
        return counter;
    }

    // 474606
    public Integer star2() {
        int bestscore=0;
        for(int r=1;r<DIM_ROW-1;r++) {
            for(int c=1;c<DIM_COL-1;c++) {
                int score=walkOut2(r, c, -1, 0) // boven
                *walkOut2(r, c, 0, 1) // rechts
                *walkOut2(r, c, 1, 0) // beneden
                *walkOut2(r, c, 0, -1) // links
                ;
                if(score>bestscore)
                    bestscore=score;
            }
        }
        return bestscore;
    }

    private int walkOut2(int rstart, int cstart, int rinc, int cinc) {
        int prevHeight=forrest[rstart][cstart];
        int treecount=0;
        for (int i=1;i<DIM_COL-1;i++) {
            int trow=rstart+i*rinc;
            int tcol=cstart+i*cinc;
            if (trow<0||tcol<0||trow>=DIM_ROW||tcol>=DIM_COL)
                break;
            treecount++;
            if(forrest[trow][tcol]>=prevHeight) 
                break;
        }
        return treecount;
    }
    public void printForrest() {
    	for (int r=0;r<DIM_ROW;r++) {
    		for (int c=0;c<DIM_COL;c++)
    			System.out.print(visible[r][c]?"T":" ");
    		System.out.println();
    	}
    }
}