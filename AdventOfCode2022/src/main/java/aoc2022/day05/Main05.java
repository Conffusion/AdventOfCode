package aoc2022.day05;

import java.util.Stack;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

public class Main05 extends AbstractMainMaster<String> {
    public static void main(String[] args) {
        new Main05()
            //.testMode()
            //.nolog()
           .start();
    }

    String[] inqueues={"ZTFRWJG","GWM","JNHG","JRCNW","WFSBGQVM","SRTDVWC","HBNCDZGV","SJNMGC","GPNWCJDL"};
    @SuppressWarnings("unchecked")
	Stack<Character>[] queues=new Stack[inqueues.length];

    @Override
    public void beforeEach() {
        for(int i=0;i<inqueues.length;i++) {
            Stack<Character> queue=new Stack<>();
            for(char crat:inqueues[i].toCharArray()) {
                queue.push(crat);
            }
            queues[i]=queue;
        }
    }

    public String star1() {
        streamInput(z->new Instruction(z))
            .forEach(this::applyInstruction1);
        StringBuffer answer=new StringBuffer();
        for(Stack<Character> s:queues) {
            answer.append(s.peek());
        }
        return answer.toString();
    }

    public void applyInstruction1(Instruction instr) {
        for(int c=0;c<instr.cratCount;c++)
            queues[instr.toStack].push(queues[instr.fromStack].pop());
    }

    public String star2() {
        streamInput(z->new Instruction(z))
            .forEach(this::applyInstruction2);
        StringBuffer answer=new StringBuffer();
        for(Stack<Character> s:queues) {
            answer.append(s.peek());
        }
        return answer.toString();
    }

    public void applyInstruction2(Instruction instr) {
        Stack<Character> tmpStack=new Stack<>();
        for(int c=0;c<instr.cratCount;c++)
            tmpStack.push(queues[instr.fromStack].pop());
        for(int c=0;c<instr.cratCount;c++)
            queues[instr.toStack].push(tmpStack.pop());
    }

    static RegexTool instructionParser=new RegexTool("move (\\d+) from (\\d) to (\\d)");

    static class Instruction {
        int cratCount;
        int fromStack;
        int toStack;
        public Instruction(String zin) {
            if (instructionParser.evaluate(zin)) {
                cratCount=instructionParser.intGroup(1);
                fromStack=instructionParser.intGroup(2)-1;
                toStack=instructionParser.intGroup(3)-1;
            } else {
                System.err.println("Geen match "+zin);
            }
        }
    }
}