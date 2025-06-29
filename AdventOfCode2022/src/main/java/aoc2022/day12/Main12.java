package aoc2022.day12;

import java.util.List;

import common.dim2.DimensionUtils;
import common.graph.Point;
import common.main.AbstractMainMaster;

public class Main12 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main12()
            //.testMode()
            //.nolog()
            .start();
    }

    // input map (a==0 .. z=25)
    int[][] hoogteMap;
    // per punt het minimum aantal stappen om er te geraken
    int[][] stepMap;
    int breedte,hoogte;
    Point S,E;
    
    @Override
	public void beforeAll() {
    	List<String> input=loadInputByLine();
    	hoogteMap=new int[input.get(0).length()][input.size()];
    	breedte=hoogteMap.length;
    	hoogte=hoogteMap[0].length;
    	for (int y=0;y<input.size();y++) {
    		char[] line=input.get(y).toCharArray();
    		for(int x=0;x<input.get(0).length();x++) {
    			if(line[x]=='S') {
    				S=new Point(x,y);
    				line[x]='a';
    			} else if (line[x]=='E') {
    				E=new Point(x,y);
    				line[x]='z';
    			}
    			hoogteMap[x][y]=line[x]-'a';
    		}
    	}
	}

	// 472
    public Long star1() {
    	stepMap=new int[hoogteMap.length][hoogteMap[0].length];
    	DimensionUtils.fill2Dim(stepMap, Integer.MAX_VALUE);
    	stepMap[S.x][S.y]=0;
    	evaluate(S.x,S.y);
        return Long.valueOf(stepMap[E.x][E.y]);
    }

    // 465
    public Long star2() {
    	stepMap=new int[hoogteMap.length][hoogteMap[0].length];
    	DimensionUtils.fill2Dim(stepMap, Integer.MAX_VALUE);
    	stepMap[S.x][S.y]=0;
    	// alle 'a' punten op 0 zetten 
    	for (int x=0;x<breedte;x++)
    		for(int y=0;y<hoogte;y++)
    			if(hoogteMap[x][y]==0)
        	    	stepMap[x][y]=0;
    	int acounter=0;
    	for (int x=0;x<breedte;x++)
    		for(int y=0;y<hoogte;y++)
    			if(hoogteMap[x][y]==0) {
    				++acounter;
    				evaluate(x,y);
    			}
    	logln("aantal startpunten:"+acounter);
        return Long.valueOf(stepMap[E.x][E.y]);
    }

    /**
     * veronderstelling: p is ok en heeft al een stepwaarde.
     * @param p
     */
    private void evaluate(int x, int y) {
    	int curval=stepMap[x][y];
    	// check links
    	if(x>0&&canDo(x-1,y,curval,hoogteMap[x][y]))
    	{
    		stepMap[x-1][y]=curval+1;
        	evaluate(x-1,y);    			
    	};
    	// check rechts
    	if(x+1<breedte&&canDo(x+1,y,curval,hoogteMap[x][y]))
    	{
    		stepMap[x+1][y]=curval+1;
       		evaluate(x+1,y);    			
   		};
    	// check omhoog
    	if(y>0&&canDo(x,y-1,curval,hoogteMap[x][y]))
    	{
    		stepMap[x][y-1]=curval+1;
       		evaluate(x,y-1);    			
    	};
    	// check omlaag
    	if(y+1<hoogte&&canDo(x,y+1,curval,hoogteMap[x][y]))
		{
    		stepMap[x][y+1]=curval+1;
    		evaluate(x,y+1);    			
		};
    }

    private boolean canDo(int x,int y,int curval,int curHoogte) {
    	return (stepMap[x][y]>curval+1&&hoogteMap[x][y]<=curHoogte+1);
    }
}