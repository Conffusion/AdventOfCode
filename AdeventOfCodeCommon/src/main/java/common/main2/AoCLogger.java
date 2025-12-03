package common.main2;

import java.util.function.Supplier;

public class AoCLogger {
	protected boolean log = true;
	
	public void nolog(boolean flag) {
		log=!flag;
	}
	public boolean log(String msg) {
		if (log)
			System.out.print(msg);
		return true;
	}

	public <X> X logln(X msg) {
		if (log)
			System.out.println(msg);
		return msg;
	}
	public void logln(Supplier<?> msg) {
		if (log)
			System.out.println(msg.get());
	}

	public int[] logln(int[] line,int width) {
		String formatPattern="%1$"+width+"s";
		for(int i=0;i<line.length;i++)
			logln(" "+String.format(formatPattern, line[i]));
		return line;
	}
	
	public void info(String msg) {
		System.out.println(msg);
	}

}
