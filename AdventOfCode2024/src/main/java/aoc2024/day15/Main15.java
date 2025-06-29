package aoc2024.day15;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.dim2.CharTerrein;
import common.graph.Direction;
import common.graph.Point;
import common.main.AbstractMainMaster;

public class Main15 extends AbstractMainMaster<Long> {
	
    public static void main(String[] args) {
        new Main15()
           //.testMode()
            .nolog()
           .start();
    }
    
    @Override
	public String getResourceName() {
		// return "testinput2.txt"
    	//return "testlarger.txt";
		 return super.getResourceName();
	}

	// input data
    int dimx=50, dimy=50;
    CharTerrein terrein;
    char[] commands;

    private static Map<Character,Direction> dirMap=new HashMap<>();
    static {
    	dirMap.put('<', Direction.LEFT);
    	dirMap.put('>', Direction.RIGHT);
    	dirMap.put('^', Direction.UP);
    	dirMap.put('v', Direction.DOWN);
    }
    // robot
    Point robot;
    
    @Override
	public void beforeAll() {
		if(testMode) {
			dimx=8; dimy=8;
		}
		if(getResourceName().equals("testinput2.txt")) {
			dimx=7; dimy=7;
		}
		if(getResourceName().equals("testlarger.txt")) {
			dimx=10; dimy=10;
		}		
	}

	@Override
    public void beforeEach() {
		List<String>lines=loadInputByLine();
		if(onStar==2)
			dimx*=2;
		char[][] t=new char[dimy][dimx];
		
		for(int i=0;i<dimy;i++) {
			if(onStar==1)
				t[i]=lines.get(i).toCharArray();
			else {
				// verdubbel de x-input
				int x=0;
				for(char c:lines.get(i).toCharArray()) {
					switch (c) {
					case '#':t[i][x++]='#';t[i][x++]='#'; break;
					case 'O':t[i][x++]='[';t[i][x++]=']'; break;
					case '.':t[i][x++]='.';t[i][x++]='.'; break;
					case '@':t[i][x++]='@';t[i][x++]='.'; break;
					}
				}
			}
			for(int x=0;x<dimx;x++)
				if(t[i][x]=='@')
					robot=new Point(x,i);
		}
		terrein=new CharTerrein(t);
		int l=dimy+1;
		String c="";
		for(int i=l;i<lines.size();i++)
			c=c+lines.get(i);
		commands=c.toCharArray();
    }
	
    // antwoord : 
    public Long star1() {
        for(char c:commands) {
        	Point newloc=move1(robot,dirMap.get(c));
        	if(newloc!=null)
        		robot=newloc;
        }
        return terrein.scanAndSum((c,p)->c=='O'?100L*p.y+p.x:0L);
    }

    // antwoord : 1337648
    public Long star2() {
    	printMap();
        for(char c:commands) {
        	Point newloc=move2(robot,dirMap.get(c));
        	if(newloc!=null)
        		robot=newloc;
        }
        return terrein.scanAndSum((c,p)->c=='['?100L*p.y+p.x:0L);
    }
    
    /**
     * controleert of voorwerp op plaats p kan verplaatst worden in richting dir.
     * Als bestemming een muur is gebeurt er niets.
     * Kan meerdere dozen in één keer verplaatsen.
     * @param p startpositie van de beweging
     * @param dir richting
     */
    private Point move1(Point p,Direction dir) {
    	Point dest=dir.move(p);
    	char destChar=terrein.at(dest);
    	if(destChar=='#')
    		return null;
    	if(destChar=='O'&&move1(dest,dir)==null)
    		return null;
		terrein.setValue(dest, terrein.at(p));
		terrein.setValue(p, '.');
		return dest;
    }
    
    private Point move2(Point p,Direction dir) {
    	Point dest=dir.move(p);
    	char destChar=terrein.at(dest);
    	if(destChar=='#')
    		return null;
    	if(destChar=='[' || destChar==']') {
    		if(canMove2Block(dest,dir))
    			move2Block(dest,dir);
    		else
    			return null;
    	}
		terrein.setValue(dest, terrein.at(p));
		terrein.setValue(p, '.');
        if(log) printMap();

		return dest;
    }
    
    /**
     * Controleert of een [] blok kan verplaatst worden in richting dir
     * @param p linkse of rechtse deel van het blok
     * @param dir richting
     * @return is het gelukt om het blok te verplaatsen ?
     */
    private boolean canMove2Block(Point p,Direction dir) {
    	Point p1=terrein.at(p)=='['?p:new Point(p.x-1,p.y);
    	Point p2=terrein.at(p)==']'?p:new Point(p.x+1,p.y);
    	return switch(dir) {
    	case LEFT-> canMove2Part(p1,dir);
    	case RIGHT -> canMove2Part(p2,dir);
    	case UP -> canMove2Part(p1,dir)&&canMove2Part(p2,dir);
    	case DOWN->canMove2Part(p1,dir)&&canMove2Part(p2,dir);
    	};
    }
    
    private boolean canMove2Part(Point p, Direction dir) {
    	Point dest=dir.move(p);
    	switch (terrein.at(dest)) {
    	case '#': return false;
    	case '.': return true;
    	case '[': 
    	case ']': return canMove2Block(dest,dir);
    	}
    	return true;
    }
    
    /**
     * mag enkel aangeroepen worden wanneer we zeker zijn dat we kunnen moven.
     * Duwt ook eventuele andere blokken indien nodig.
     * @param p
     * @param dir
     */
    private void move2Block(Point p,Direction dir) {
    	Point p1=terrein.at(p)=='['?p:new Point(p.x-1,p.y);
    	Point p2=terrein.at(p)==']'?p:new Point(p.x+1,p.y);
    	Point p1dest=dir.move(p1);
    	Point p2dest=dir.move(p2);
    	switch(dir) {
    	case RIGHT: 
    		// er is een ander blok rechts van dit blok, dus dat eerst verplaatsen
    		if(terrein.at(p2dest)=='[') 
    			move2Block(p2dest,dir);
    		move1(p2,dir);
    		move1(p1,dir); break;
    	case LEFT:  
    		// er is een ander blok links van dit blok, dus dat eerst verplaatsen
    		if(terrein.at(p1dest)==']') 
    			move2Block(p1dest,dir);
    		move1(p1,dir);move1(p2,dir); break;
    	case UP:
    		if(terrein.at(p1dest)=='[' || terrein.at(p1dest)==']')
    			move2Block(p1dest,dir);
    		if(terrein.at(p2dest)=='[')
    			move2Block(p2dest,dir);
    		move1(p1,dir);move1(p2,dir); break;
    	case DOWN:  
    		if(terrein.at(p1dest)=='[' || terrein.at(p1dest)==']')
    			move2Block(p1dest,dir);
    		if(terrein.at(p2dest)=='[')
    			move2Block(p2dest,dir);
    		move1(p1,dir);move1(p2,dir); break;
    	}
    }
    private void printMap() {
    	terrein.scan((c,p)->
    	{
    		if(p.x==0)
    			System.out.println();
    		System.out.print(c);
    	});
    }

}