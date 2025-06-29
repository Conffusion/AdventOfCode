package aoc2022.day17;

import java.awt.Dimension;
import java.awt.Graphics2D;

import common.LoopIterator;
import common.graph.Point;
import common.swing.CelPainter;
import common.swing.CoordinationBase;
import common.swing.MainMasterGUI;

/**
 * TETRIS !!!
 * @author walter
 *
 */
public class Main17 extends MainMasterGUI<Integer> implements CelPainter {
    public static void main(String[] args) {
    	Main17 m=new Main17();
    	m.setCelPainter(m);
    	m.testMode();
    	m.launch();
    }

    private int nrOfRocks=2022;
    private Jet[] jets;
    private boolean[][] tunnel;
    private int top=0; // hoogste punt
    private RockType[] rocks=new RockType[5];
    
    @Override
	public void beforeAll() {
        char[] jetPattern=loadInputToString().toCharArray();
		jets=new Jet[jetPattern.length];		
		for (int i=0;i<jetPattern.length;i++) {
			jets[i]=new Jet(jetPattern[i]=='<'?-1:1);
		}
		// rock 0 :  ----
		rocks[0]=new RockType("Balk",4,1);
		rocks[0].parts[0][0]=true;
		rocks[0].parts[1][0]=true;
		rocks[0].parts[2][0]=true;
		rocks[0].parts[3][0]=true;
		rocks[0].leftside=new int[] {-1};
		rocks[0].rightside=new int[] {4};
		rocks[0].bottom= new int[]{-1,-1,-1,-1};
		// rock 1 : +
		rocks[1]=new RockType("Plus",3,3);
		rocks[1].parts[0][1]=true;
		rocks[1].parts[1][0]=true;
		rocks[1].parts[1][1]=true;
		rocks[1].parts[1][2]=true;
		rocks[1].parts[2][1]=true;
		rocks[1].leftside=new int[] {0,-1,0};
		rocks[1].rightside=new int[] {2,3,2};
		rocks[1].bottom=new int[] {0,-1,0};
		// rock 2 : L (omgekeerd)
		rocks[2]=new RockType("Lvorm",3,3);
		rocks[2].parts[0][0]=true;
		rocks[2].parts[1][0]=true;
		rocks[2].parts[2][0]=true;
		rocks[2].parts[2][1]=true;
		rocks[2].parts[2][2]=true;
		rocks[2].leftside=new int[] {-1,1,1};
		rocks[2].rightside=new int[] {3,3,3};
		rocks[2].bottom=new int[] {-1,-1,-1};
		// rock 3 : |
		rocks[3]=new RockType("Zuil",1,4);
		rocks[3].parts[0][0]=true;
		rocks[3].parts[0][1]=true;
		rocks[3].parts[0][2]=true;
		rocks[3].parts[0][3]=true;
		rocks[3].leftside=new int[] {-1,-1,-1,-1};
		rocks[3].rightside=new int[] {1,1,1,1};
		rocks[3].bottom=new int[] {-1};
		// rock 4 : vierkant
		rocks[4]=new RockType("Vierkant",2,2);
		rocks[4].parts[0][0]=true;
		rocks[4].parts[0][1]=true;
		rocks[4].parts[1][0]=true;
		rocks[4].parts[1][1]=true;
		rocks[4].leftside=new int[] {-1,-1};
		rocks[4].rightside=new int[] {2,2};
		rocks[4].bottom=new int[] {-1,-1};
	}

	@Override
    public void beforeEach() {
		if(testMode)
			nrOfRocks=50;
	    tunnel=new boolean[7][(nrOfRocks+2)*4];
    	for (int x=0;x<7;x++)
    		tunnel[x][0]=true;
    }

    public Integer star1() {
    	LoopIterator<RockType> rockIt=new LoopIterator<>(rocks);
    	LoopIterator<Jet>jetIt=new LoopIterator<>(jets);
    	for(int i=0;i<nrOfRocks;i++) {
    		Rock r=new Rock(rockIt.next(),new Point(2,top+4));
    		while(true) {
	    		// jets
	    		jetIt.next().apply(r);
	    		if(!fall(r)) {
	    			// we zijn er, rock toevoegen aan tunnel
	    			fixRock(r);
	    			break;
	    		}
	    		refreshGUI();
    		}
    	}
        return top;
    }

    public Integer star2() {
        return null;

    }
    
    /**
     * laat de rock 1 zakken
     * @param rock
     * @return true: rock is nog kunnen vallen
     */
    public boolean fall(Rock rock) {
    	for (int xd=0;xd<rock.type.bottom.length;xd++) {
    		if(tunnel[rock.nulPunt.x+xd][rock.nulPunt.y+rock.type.bottom[xd]])
    			return false;
    	}
    	rock.nulPunt.y--;
    	return true;
    }
    
    public void fixRock(Rock rock) {
    	for (int x=0;x<rock.type.parts.length;x++)
    		for(int y=0;y<rock.type.parts[x].length;y++)
    			if(rock.type.parts[x][y])
    				tunnel[rock.nulPunt.x+x][rock.nulPunt.y+y]=true;
    	top=Math.max(top, rock.nulPunt.y+rock.type.parts[0].length-1);
    }
    
    class Rock {
    	Point nulPunt;
    	RockType type;
		public Rock(RockType type, Point nulPunt) {
			super();
			this.type = type;
			this.nulPunt = nulPunt;
		}
		public String toString() {
			return ""+type+nulPunt;
		}
    }
    
    class RockType {
    	String naam;
    	boolean[][] parts;
    	int[] leftside; // x-delta tov linker onderhoek=0,0
    	int[] rightside; // x-delta tov linker onderhoek=0,0
    	int[] bottom; // y-delta tov linker onderhoek=0,0
    	RockType(String naam, int width, int height) {
    		this.naam=naam;
    		parts=new boolean[width][height];
    	}
    	public String toString() {
    		return naam;
    	}
    }
    
    class Jet {
    	int direction; // 1 of -1
    	Jet(int direction) {
    		this.direction=direction;
    	}
    	
    	void apply(Rock rock) {
    		if(direction==-1) {
    			// go left
    			if(rock.nulPunt.x==0)
    				// we zijn al aan de linkerkant van de tunnel
    				return;
    			for(int y=0;y<rock.type.leftside.length;y++) {
    				if(tunnel[rock.nulPunt.x+rock.type.leftside[y]][rock.nulPunt.y+y])
    					// er is een rots aan de linkerzijde dus kunnen we niet naar links
    					return;
    			}
    			// we kunnen naar links
    			rock.nulPunt.x--;
    		}
    		if(direction==1) {
    			// go right
    			if(rock.nulPunt.x+rock.type.bottom.length-1>=6)
    				// we zijn al aan de rechterzijde
    				return;
    			for(int y=0;y<rock.type.rightside.length;y++) {
    				if(tunnel[rock.nulPunt.x+rock.type.rightside[y]][rock.nulPunt.y])
    					// er is een rots aan de rechterzijde dus kunnen we niet naar rechts
    					return;
    			}
    			// we kunnen naar links
    			rock.nulPunt.x++;    			
    		}
    	}
    }

    // CelPainter implementations:
    
	@Override
	public Dimension getArrayDimension() {
		return new Dimension(7,tunnel[0].length);
	}

	@Override
	public void paint(int col, int row, int x0, int y0, int width, int height, Graphics2D graph) {
		if(tunnel[col][row])
			graph.fillRect(x0, y0, width, height);
	}

	@Override
	public CoordinationBase getCoordBase() {
		return CoordinationBase.LeftBottom;
	}
}