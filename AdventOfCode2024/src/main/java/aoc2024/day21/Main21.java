package aoc2024.day21;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import common.main.AbstractMainMaster;
/**
 * robot aansturen die numeriek toetsenbord moet gebruiken.
 * Maar deze robot wordt ook aangestuurd door een pijltjes toetsenbord (<^v>A)
 * En deze 2e robot wordt ook aangestuurd door een pijltjes toetsenbord (<^v>A)
 * Deel 2 heeft 25 zo'n robots.
 * Oplossing: 1e robot berekent hoeveel stappen het nodig heeft om van een toets naar een andere te gaan.
 * 2e robot berekent zijn nodige stappen op basis van wat 1e robot teruggeeft
 * ...
 * Na de setup weet de laatste robot dus hoeveel handelingen er nodig zijn voor elke input beweging.  
 */
public class Main21 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {     
    	new Main21()
            //.testMode()
            .nolog()
           .start();
    }
    List<String> instructions;
    
    @Override
    public void beforeAll() {
    	instructions=loadInputByLine();
    }

    /**
     * 2 pijltjes toetsenborden + 1 numeriek toetsenbord
     */
    // antwoord : 164960
	public Long star1() {
        return run(2);
	}

    /**
     * 25 pijltjes toetsenborden + 1 numeriek toetsenbord
     * Werkt niet :(
     */
    // antwoord : 205620604017764
    public Long star2() {
        return run(25);
    }
    private Long run(int robotCnt) {
        Long result=0L;
        for(String line:instructions) {
        	char[] instr=line.split("=")[1].toCharArray();
        	Function<Character,Long> ir=_->1L;
        	for(int i=0;i<robotCnt;i++)ir=new DirPadRobot(ir,i+1);
            long subresult=0L;
            for(char c:instr)
            	subresult+=ir.apply(c);
        	int value=Integer.parseInt(line.split("=")[0].substring(0, 3));
        	logln(""+subresult+"*"+value);        	
        	result+=subresult*value;
        }
        return result;
    	
    }
	private static char[] ALL_KEYS=new char[] {'A','<','>','v','^'};
    class DirPadRobot implements Function<Character,Long> {
    	int id=0;
    	char currPos='A';
    	Function<Character,Long> commander;
    	Map<Character,Map<Character,Long>> memory=new HashMap<>();
    	
    	public DirPadRobot(Function<Character,Long> commander, int id) {
			this.commander = commander;
			this.id=id;
			buildMemory();
		}
    	private void buildMemory() {
    		for(char from:ALL_KEYS) {
    			for(char to: ALL_KEYS) {
    				long bestSize=Long.MAX_VALUE;
    				for(String option:AllDirectionPadInstructions.get(from).get(to)) {
    					long subsize=0;
    		    		for(char c:option.toCharArray()) 
    		    			 subsize+=commander.apply(c);
    		    		if(bestSize>subsize) 
    		    			bestSize=subsize;
    				}
		    		memory.computeIfAbsent(from,_->new HashMap<Character,Long>())
		    			.put(to, bestSize);
    			}
    		}
    	}
		@Override
		public Long apply(Character command) {
    		long size=memory.get(currPos).get(command);
			currPos=command;
    		return size;
		}
    }

    static Map<Character,Map<Character,String[]>>AllDirectionPadInstructions= new DirectionPadInstructionBuilder()
    	.add('A','A',new String[]{"A"})
    	.add('A','^',new String[]{"<A"})
	    .add('A','v',new String[]{"v<A","<vA"})
	    .add('A','>',new String[]{"vA"})
	    .add('A','<',new String[]{"v<<A"})
	    .add('v','v',new String[]{"A"})
	    .add('v','^',new String[]{"^A"})
	    .add('v','<',new String[]{"<A"})
	    .add('v','>',new String[]{">A"})
	    .add('v','A',new String[]{">^A","^>A"})
	    .add('<','<',new String[]{"A"})
	    .add('<','^',new String[]{">^A"}) //"^>A" mag niet
	    .add('<','>',new String[]{">>A"})
    	.add('<','v',new String[]{">A"})
    	.add('<','A',new String[]{">>^A"}) 
    	.add('>','>',new String[]{"A"})
    	.add('>','<',new String[]{"<<A"})
    	.add('>','^',new String[]{"<^A","^<A"})
    	.add('>','v',new String[]{"<A"})
    	.add('>','A',new String[]{"^A"})
    	.add('^','^',new String[]{"A"})
    	.add('^','>',new String[]{"v>A",">vA"})
    	.add('^','v',new String[]{"vA"})
    	.add('^','<',new String[]{"v<A"}) //<v mag niet
    	.add('^','A',new String[]{">A"})
    	.build();
    
    private static class DirectionPadInstructionBuilder {
    	Map<Character,Map<Character,String[]>> allDirectionPadInstructions=new HashMap<Character,Map<Character,String[]>>();
    	public DirectionPadInstructionBuilder add(Character from, Character to,String[] wayInstr) {
    		allDirectionPadInstructions.computeIfAbsent(from, _->new HashMap<Character,String[]>()).put(to, wayInstr);
    		return this;
    	}
    	public Map<Character,Map<Character,String[]>> build() {
    		return allDirectionPadInstructions;
    	}
    }
}