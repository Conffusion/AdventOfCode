package aoc2024.day17;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

public class Main17 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
    	new Main17()
    		//.testMode()
            //.nolog()
           .start();
    }
    RegexTool registerPattern=new RegexTool("Register ([ABC]): "+RegexTool.NUMBER_GROUP_PATTERN);
    RegexTool programPattern=new RegexTool(RegexTool.NUMBER_GROUP_PATTERN);
    
    Map<String,Long> registers;
    List<Long> program;
    List<Long> output;
    
    @Override
    public void beforeEach() {
    	registers=new HashMap<>();
    	List<String> lines=loadInputByLine();
    	for(String line:lines) {
    		if(registerPattern.evaluate(line)) {
    			registers.put(registerPattern.group(1), registerPattern.longGroup(2));
    		} else if(line.startsWith("Program:")) {
    			List<Integer> ilist=programPattern.groupAllInt(line);
    			program=ilist.stream().map(i->Integer.toUnsignedLong(i)).toList();
    		}    		
    	}
    }

    // antwoord : 
    public Long star1() {
    	output=new ArrayList<>();
        Long result=0L;
        int index=0;
        while(index<program.size()) {
        	int nexindex=(int) execute(program.get(index).intValue(),program.get(index+1));
        	if(nexindex>=0)
        		index=nexindex;
        	else
        		index+=2;
        	if(output.size()>0) {
        		output.forEach(v->log(v+","));
        		logln("");
        	}
        }
        System.out.println("antwoord:"+StringUtils.join(output,","));
//        output.forEach(v->System.out.print(v+","));
        return result;
    }

    // antwoord : 
    public Long star2() {
        Long result=0L;

        return result;
    }
    /**
     * voert operator uit
     * @param operator
     * @param operand
     * @return als >=0: jump naar dit statement
     */
    long execute(int operator,long operand) {
    	switch (operator) {
    	case 0: registers.put("A", Double.valueOf(Math.floor(registers.get("A")/(Math.pow(2, operand)))).longValue()); break;
    	case 1: registers.put("B", operand^registers.get("B")); break;
    	case 2: registers.put("B", comboOperand((int)operand)%8); break;
    	case 3: if(registers.get("A")!=0) return operand; break;
    	case 4: registers.put("B",registers.get("B")^registers.get("C")); break;
    	case 5: output.add(comboOperand((int)operand)%8); break;
    	case 6: registers.put("B", Math.round(registers.get("A")/(Math.pow(2, operand)))); break;
    	case 7: registers.put("C", Math.round(registers.get("A")/(Math.pow(2, operand)))); break;
    	}
    	return -1;
    }
    
    private long comboOperand(int operand) {
    	return switch (operand) {
    	case 0->operand;
    	case 1->operand;
    	case 2->operand;
    	case 3->operand;
    	case 4->registers.get("A");
    	case 5->registers.get("B");
    	case 6->registers.get("C");
    	case 7->-1;
    	default->-1;
    	};
    }
    
    public static void unitTest() {
    	Main17 main=new Main17();
    	main.beforeEach();
    	main.registers.put("C", 9L);
    	main.execute(2, 6);
    	System.out.println(main.registers.get("B"));
    }
}