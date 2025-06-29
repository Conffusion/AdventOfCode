package aoc2020.day08;

public class Cmd {

	enum Statement {nop,acc,jmp};
	Statement stat;
	int nr;
	public Cmd(String stat, int nr) {
		this.stat = Statement.valueOf(stat);
		this.nr = nr;
	}
	@Override
	public String toString() {
		return "Cmd [stat=" + stat + ", nr=" + nr + "]";
	}
	
}
