package aoc2021.day10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.main.AbstractMainMaster;

/**
 * zou perfect werken met Stack maar werkt sneller met een array en een last character pointer
 * @author walter
 *
 */
public class Main10 extends AbstractMainMaster<Long> {
	
	List<char[]> lines;
	
	@Override
	public void beforeEach() {
		lines=parseInput(String::toCharArray);
	}

	/**
	 * antwoord: 323613
	 */
	@Override
	public Long star1() {
		long score=0;
		char prev;
		line_loop:
		for(char[] line:lines) {
			char[] charStack=new char[line.length];
			int pointer=-1;
			char_loop:
			for(char c:line) {
				if (c=='{' || c=='(' || c== '[' || c=='<')
					//push
					charStack[++pointer]=c;
				else {
					// pop
					prev=charStack[pointer--];
					if (prev!='{' && c=='}')
						score+=1197;
					else if (prev!='(' && c==')')
						score+=3;
					else if (prev!='<' && c=='>')
						score+=25137;
					else if (prev!='[' && c==']')
						score+=57;
					else
						continue char_loop;
					continue line_loop;
				}
			}
		}
		return score;
	}

	/**
	 * antwoord: 3103006161
	 */
	@Override
	public Long star2() {
		List<Long> subscores=new ArrayList<>();
		line_loop:
		for(char[] line:lines) {
			char[] charStack=new char[line.length];
			int pointer=-1;
			char prev;
			for(char c:line) {
				if (c=='{' || c=='(' || c== '[' || c=='<')
					//push
					charStack[++pointer]=c;
				else {
					prev=charStack[pointer--];
					//pop
					if (prev!='{' && c=='}'||prev!='(' && c==')'||prev!='<' && c=='>'||prev!='[' && c==']')
						continue line_loop;
				}
			}
			long subscore=0;
			while(pointer>=0) {
				//pop
				char c=charStack[pointer--];
				subscore=subscore*5;
				if(c=='(')
					subscore+=1;
				else if(c=='[')
					subscore+=2;
				else if(c=='{')
					subscore+=3;
				else if(c=='<')
					subscore+=4;
				else
					logln("dees kan ni");
			}
			subscores.add(subscore);
		}
		Collections.sort(subscores);
		return subscores.get(subscores.size()/2);
	}

	public static void main(String[] args) {
		new Main10()
		//.testMode()
		.start();
	}
}
