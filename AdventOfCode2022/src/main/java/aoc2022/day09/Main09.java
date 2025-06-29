package aoc2022.day09;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import common.graph.Point;
import common.main.AbstractMainMaster;

public class Main09 extends AbstractMainMaster<Integer> {
	
    public static void main(String[] args) {
        Main09 main=new Main09();
        main
            //.testMode()
            .nolog()
           .start();
    }

    boolean[][] space;
    List<Command> commands;
    
    @Override
	public void beforeAll() {
    	commands=streamInput(s->new Command(s)).collect(Collectors.toList());
    }

	@Override
    public void beforeEach() {
	    // UpDown: [-8,396] LeftRight[-181,30]
	    space=new boolean[225][410];
    }

	// 6256
    public Integer star1() {
    	dimensies();
    	return commands.stream().mapToInt(this::moveRope).sum();
    }

    Point H=new Point(190,10);
    Point T=new Point(190,10);

    private int moveRope(Command c) {
    	int newPlaces=0;
        // start s op (190,10)    
    	for(int i=0;i<c.dist;i++) {
    		move(H,c.dir);
    		move(H,T);
			if(space[T.x][T.y]==false) {
				space[T.x][T.y]=true;
				newPlaces++;
			}
    	}
    	return newPlaces;
    }

	Point[] snake=new Point[10];

	public Integer star2() {
    	int newPlaces=0;
    	// init snake
    	for (int i=0;i<10;i++)snake[i]=new Point(190,10);
    	logln(printSnake());
    	for(Command comm:commands) {
    		for(int c=0;c<comm.dist;c++) {
    			move(snake[0],comm.dir);
    			for(int s=1;s<snake.length;s++) {
    				move(snake[s-1],snake[s]);
    	 		}
				if(space[snake[9].x][snake[9].y]==false) {
					space[snake[9].x][snake[9].y]=true;
					newPlaces++;
				}
	    		logln(comm+"("+c+"): "+printSnake());
    		}
    	}
        return newPlaces;
    }

    public void move(Point head,Direction dir) {
		switch (dir) {
		case R:
			head.x++;
			break;
		case L:
			head.x--;
			break;
		case U:
			head.y--;
			break;
		case D:
			head.y++;
			break;
		}	
    }
    
    /**
     * move tail in direction of head when necessary
     * @param head
     * @param tail
     */
    public void move (Point head,Point tail) {
		if(tail.x<head.x-1) {
			tail.x++;
			tail.y=head.y<tail.y?tail.y-1:head.y>tail.y?tail.y+1:tail.y;
		}
		if(tail.x>head.x+1) {
			tail.x--;
			tail.y=head.y<tail.y?tail.y-1:(head.y>tail.y?tail.y+1:tail.y);
		}
		if(tail.y>head.y+1) {
			tail.y--;
			tail.x=head.x<tail.x?tail.x-1:(head.x>tail.x?tail.x+1:tail.x);
		}
		if(tail.y<head.y-1) {
			tail.y++;
			tail.x=head.x<tail.x?tail.x-1:(head.x>tail.x?tail.x+1:tail.x);
		}
    }
    
    /**
     * Om de nodige dimensies van de 2D space te kennen
     */
    public void dimensies() {
    	int maxL=0, maxR=0, maxU=0, maxD=0;
    	int currX=0, currY=0;
    	for(Command c:commands) {
    		switch (c.dir) {
    		case R:
    			currX+=c.dist;
    			maxR=Math.max(maxR, currX);
    			break;
    		case L:
    			currX-=c.dist;
    			maxL=Math.min(maxL, currX);
    			break;
    		case U:
    			currY-=c.dist;
    			maxU=Math.min(maxU, currY);
    			break;
    		case D:
    			currY+=c.dist;
    			maxD=Math.max(maxD, currY);
    			break;
    		}
    	}
    	System.out.println("U:"+maxU+", D:"+maxD+", L:"+maxL+", R"+maxR);
    }
    
    public void test1() {
    	// resest slang
    	for (int i=0;i<10;i++)snake[i]=new Point(190,10);
    	commands.clear();
    	commands.add(new Command("R 5"));
    	commands.add(new Command("U 8"));
    	System.out.println(printSnake());
    	
    }
    private String printSnake() {
    	StringBuffer sb=new StringBuffer("[");
    	for(int i=0;i<snake.length;i++)
    		sb.append((i==0?"[":",[")+snake[i].x+"-"+snake[i].y+"]");
    	sb.append("]");
    	return sb.toString();
    }
    enum Direction { U,D,L,R}
    
    Pattern instrPattern=Pattern.compile("(.) (\\d+)");
    
    public class Command {
    	Direction dir;
    	int dist;
    	
    	public Command(String instr) {
    		Matcher m=instrPattern.matcher(instr);
    		if(m.matches()) {
    			dir=Direction.valueOf(m.group(1));
    			dist=Integer.parseInt(m.group(2));
    		}
    	}

		@Override
		public String toString() {
			return dir + " " + dist;
		}
    	
    }
}