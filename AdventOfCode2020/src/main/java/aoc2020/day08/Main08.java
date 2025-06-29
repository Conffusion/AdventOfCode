package aoc2020.day08;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import aoc2020.common.MainMaster;
import aoc2020.day08.Cmd.Statement;
/**
 * Assembler :)
 */
public class Main08 extends MainMaster {
	List<String> data;
	List<Cmd> cmds;
	int index = 0, acc = 0;
	boolean infinite = false;

	public Main08() {
		data=loadInput(8, "input.txt");
		cmds = data.stream().map(s -> new Cmd(s.substring(0, 3), Integer.parseInt(s.substring(4))))
				.collect(Collectors.toList());
	}
	
	public int checkFlow() {
		infinite = false;
		index = 0;
		acc = 0;
		Set<Object> passed = new HashSet<>();
		while (!passed.contains(index)) {
			Cmd cmd = cmds.get(index);
			passed.add(index);
			index += cmd.stat == Statement.jmp ? cmd.nr : 1;
			acc += cmd.stat == Statement.acc ? cmd.nr : 0;
			if (index == cmds.size())
				return acc;
		}
		infinite = true;
		return acc;
	}

	public void star1() {
		info("***STAR 1: acc:" + checkFlow());
	}

	public void star2() {
		for (Cmd c : cmds) {
			if (c.stat == Statement.acc)
				continue;
			Statement oldstat = c.stat;
			c.stat = c.stat == Statement.jmp ? Statement.nop : Statement.jmp;
			checkFlow();
			if (!infinite) {
				info("***STAR 2: acc:" + acc);
				return;
			}
			c.stat = oldstat;
		}
	}

	public static void main(String[] args) throws Exception {
		Main08 m=new Main08();
		m.timer(()-> m.star1());
		m.timer(()->m.star2());
	}
}
