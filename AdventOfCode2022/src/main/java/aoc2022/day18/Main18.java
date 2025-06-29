package aoc2022.day18;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

public class Main18 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main18()
            //.testMode()
            //.nolog()
           .start();
    }
    RegexTool inputPhrase=new RegexTool("(\\d+),(\\d+),(\\d+)",true);
   
    int xMax=0,yMax=0,zMax=0;
    boolean[][][] space;
	long aantBlokken=0;
    
    @Override
	public void beforeAll() {
    	streamInput().map(this::parseLine).map(this::calculateMax).forEach(p->aantBlokken++);
    	log("Dimensies: ("+xMax+","+yMax+","+zMax+"); aantal blokken "+aantBlokken);
        // space is 2 groter dan de nodige ruimte zodat er steeds een buitenrand is zonder blokken
    	space=new boolean[xMax+2][yMax+2][zMax+2];
    	streamInput().map(this::parseLine).forEach(p->space[p.x][p.y][p.z]=true);
    }

    private XYZPoint parseLine(String line) {
    	if(inputPhrase.evaluate(line)) {
    		return new XYZPoint(inputPhrase.intGroup(1),inputPhrase.intGroup(2),inputPhrase.intGroup(3));
    	} else {
    		log("geskipte lijn:"+ line);
    	}
    	return null;
    }
    
    private XYZPoint calculateMax(XYZPoint p) {
    	xMax=Math.max(xMax, p.x);
    	yMax=Math.max(yMax, p.y);
    	zMax=Math.max(zMax, p.z);
    	return p;
    }
    
    class XYZPoint {
    	int x,y,z;

		public XYZPoint(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
    }

	@Override
	public Long star1() {
		long buitenvlakken=0;
		int blokcounter=0;
		for(int x=1;x<=xMax;x++) {
			for(int y=1;y<=yMax;y++) {
				for (int z=1;z<=zMax;z++) {
					if (space[x][y][z]) {
						// er is een blok op deze positie
						buitenvlakken+=!space[x-1][y][z]?1:0;
						buitenvlakken+=!space[x+1][y][z]?1:0;
						buitenvlakken+=!space[x][y-1][z]?1:0;
						buitenvlakken+=!space[x][y+1][z]?1:0;
						buitenvlakken+=!space[x][y][z-1]?1:0;
						buitenvlakken+=!space[x][y][z+1]?1:0;
						blokcounter++;
						if(x==xMax||y==yMax||z==zMax||x==1||y==1||z==1)
							logln("randblok "+x+","+y+","+z);
					}
				}
			}
		}
		logln("processed "+blokcounter);
		
		return buitenvlakken;
	}
    
}
