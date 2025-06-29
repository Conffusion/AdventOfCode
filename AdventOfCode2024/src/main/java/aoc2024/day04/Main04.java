package aoc2024.day04;

import common.dim2.DimensionUtils;
import common.main.AbstractMainMaster;

/**
 * zoek in een 2D tabel naar alle voorkomens van 'XMAS' in elke mogelijke richting
 */
public class Main04 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main04()
          //.testMode()
           .start();
    }
    char[][] input;
    int dimx=140, dimy=140;
    
    @Override
    public void beforeEach() {
    	if(testMode)
    	{ 
    		dimx=10; 
    		dimy=10;
    	}
    	input=loadInputInto2DArray(dimx,dimy);
    }

    // antwoord : 2521
    public Long star1() {
    	long counter=0;
    	char[][] tabel1=input;
    	for(int r=0;r<4;r++) {
    		counter+=countXMASLeft2Right(tabel1);
    		logln("na "+r +" iteraties: "+counter);
    		counter+=countXMASDiagonaal(tabel1);
    		logln("na "+r +" iteraties: "+counter);
    		tabel1=rotate90(tabel1);
    	}
        return counter;
    }
    
    private long countXMASLeft2Right(char[][] tabel) {
    	return DimensionUtils.walkAndSum(dimx-3, dimy,p->{
    		return (tabel[p.y][p.x]=='X' &&tabel[p.y][p.x+1]=='M'&&tabel[p.y][p.x+2]=='A'&&tabel[p.y][p.x+3]=='S')
    				?1L:0L;});
    }

    private long countXMASDiagonaal(char[][] tabel) {
    	return DimensionUtils.walkAndSum(dimx-3, dimy-3,p->{
    		return (tabel[p.y][p.x]=='X' &&tabel[p.y+1][p.x+1]=='M'&&tabel[p.y+2][p.x+2]=='A'&&tabel[p.y+3][p.x+3]=='S')
    			?1L:0L;});
    }
    
    /**
     * Roteert de input table 90° wijzerszin en zet resultaat in out
     * @param in
     * @param out
     */
    private char[][] rotate90(char[][]in) {
    	int ldimx=in[0].length;
    	int ldimy=in.length;
    	char[][] out=new char[ldimy][ldimx];
    	DimensionUtils.walk(dimx, dimy, p->{
    		out[p.x][dimx-1-p.y]=in[p.y][p.x];
    	});
    	return out;
    }
    
    // antwoord : 1912
    public Long star2() {
    	long counter=0;
    	char[][] tabel1=input;
    	for(int r=0;r<4;r++) {
    		counter+=countMASDiagonaal(tabel1);
    		logln("na "+r +" iteraties: "+counter);
    		tabel1=rotate90(tabel1);
    	}
        return counter;
    }

    /**
     * Zoek naar X-MAS :
     * <pre>
     *  M.S
     *  .A.
     *  M.S
     * </pre>
     * @param tabel
     * @return
     */
    private long countMASDiagonaal(char[][] tabel) {
    	return DimensionUtils.walkAndSum(dimx-2, dimy-2,p->{
    		return (tabel[p.y][p.x]=='M' &&tabel[p.y+1][p.x+1]=='A'&&tabel[p.y+2][p.x+2]=='S'
					&&tabel[p.y+2][p.x]=='M'&&tabel[p.y][p.x+2]=='S')
    			?1L:0L;});
    }
}