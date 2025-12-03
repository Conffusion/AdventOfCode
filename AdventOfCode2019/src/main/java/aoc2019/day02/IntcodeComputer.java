package aoc2019.day02;

public class IntcodeComputer {
    
    public int[] run(int[] input) {
        int opIndex=0;
        loop:
        while(true) {
            int op=input[opIndex];
            switch(op) {
                case 1: // som
                  input[input[opIndex+3]]=input[input[opIndex+1]]+input[input[opIndex+2]];
                  opIndex+=4;
                  break;
                case 2: // multiply
                  input[input[opIndex+3]]=input[input[opIndex+1]]*input[input[opIndex+2]];
                  opIndex+=4;
                  break;
                case 99:
                    break loop;
                default:
                   System.err.println("onverwachte operator "+op);
                   break loop;
            }
        }
        return input;
    }
}
