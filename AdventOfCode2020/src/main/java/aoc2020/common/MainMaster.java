package aoc2020.common;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class MainMaster {
	protected boolean log = true;

	public boolean log(String msg) {
		if (log)
			System.out.print(msg);
		return true;
	}

	public boolean logln(Object msg) {
		if (log)
			System.out.println(msg);
		return true;
	}
	public void info(String msg) {
		System.out.println(msg);
	}

	public long timer(Executor run) {
		long start = System.currentTimeMillis();
		try {
			run.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("Time:" + (end - start) + "ms");
		return end-start;
	}

	public List<String> loadInput(int day,String filename) {
		try {
			return  FileUtils.readLines(new File("./src/main/java/aoc2020/day"+(day<10?"0":"")+day+"/" + filename), "UTF-8");
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}
}
