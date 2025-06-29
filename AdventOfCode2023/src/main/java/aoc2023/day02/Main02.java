package aoc2023.day02;

import java.util.List;

import common.main.AbstractMainMaster;
import common.regex.RegexMatch;
import common.regex.RegexMatchBuilder;
import common.regex.RegexTool;

public class Main02 extends AbstractMainMaster<Long>{
	
    public static void main(String[] args) {
		new Main02()
        //.testMode()
        .start();
	}
    
	RegexTool linePattern=new RegexTool("Game ([0-9]+): (.*)",true);
	RegexTool grabPattern=new RegexTool("[ ]?([0-9]+) (red|green|blue)",true);

	// antwoord: 2207
    @Override
    public Long star1() {
        long som=0;
        List<String> lines=loadInputByLine();
        gameIt:
        for(String line:lines) {
        	linePattern.evaluate(line);
    		int game=linePattern.intGroup(1);
    		String[] cubesets=linePattern.group(2).trim().split(";");
    		for(String cubeset:cubesets) {
    			String[] grabs=cubeset.split(",");
    			for(String grab: grabs) {
    				grabPattern.evaluate(grab);
					String color=grabPattern.group(2);
					int dies=grabPattern.intGroup(1);
					switch(color) {
					case "red": 
						if (dies>12)continue gameIt;break;
					case "blue":
						if(dies>14)continue gameIt;break;
					case "green":
						if(dies>13)continue gameIt;break;
					default:
						System.err.println("onbekende kleur "+color);
					}
    			}
    		}
       		som+=game;        		
        }
    	return som;
    }

    enum Color {red,blue,green}
        
    // antwoord: 62241
    @Override
    public Long star2() {
        return streamInput(new RegexMatchBuilder("Game ([0-9]+): (.*)",true))
        		.map(this::process)
        		.reduce(0L,Long::sum);
    }
    
    private long process(RegexMatch linePattern) {
		int[] dieCount=new int[3];
		String[] cubesets=linePattern.group(2).trim().split(";");
		for(String cubeset:cubesets) {
			String[] grabs=cubeset.split(",");
			for(String grab: grabs) {
				grabPattern.evaluate(grab);
				Color color=Color.valueOf(grabPattern.group(2));
				int dies=grabPattern.intGroup(1);
				dieCount[color.ordinal()]=Math.max(dieCount[color.ordinal()], dies);
			}
		}
   		return dieCount[0]*dieCount[1]*dieCount[2];
    }
}
