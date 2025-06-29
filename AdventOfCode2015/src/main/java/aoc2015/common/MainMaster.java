package aoc2015.common;

import java.io.File;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public abstract class MainMaster<T> {
	protected boolean log = true;
	protected boolean testMode=false;
	
	public void start() {
		beforeAll();
		doStar1();
		doStar2();
	}
	public void doStar1() {
		if(testMode)
			logln("--- TEST ---");
		timer(1, this::star1);
	}
	public void doStar2() {
		timer(2, this::star2);
	}
	
	public void beforeAll() {}
	public abstract T star1();
	public abstract T star2();
	
	public MainMaster<T> withTestMode() {
		this.testMode=true;
		return this;
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
	
	public void info(String msg) {
		System.out.println(msg);
	}

	public long timer(int star,Executor<T> run) {
		long start = System.nanoTime();
		try {
			System.out.println("\n*** Star "+star+": "+run.run());
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.nanoTime();
		System.out.println("Time:" + (end - start) + "ns");
		return end-start;
	}

	public List<String> loadInputByLine() {
		return loadInputByLine((testMode?"test":"")+"input.txt");
	}
	
	public List<String> loadInputByLine(String resource) {
		String packName=this.getClass().getPackageName().replace(".", "/");
		File inFile=new File("./src/main/java/"+packName+"/" + resource);
		try {
			return  FileUtils.readLines(inFile, "UTF-8");
		} catch (Exception ex) {
			throw new Error("Probleem bij lezen van "+resource,ex);
		}
	}
	
	public String loadInputToString() {
		return loadInputToString((testMode?"test":"")+"input.txt");
	}
	
	public List<Long> loadInputLongByLine(String resource) {
		return streamInput(resource,s->Long.parseLong(s)).collect(Collectors.toList());
	}

	public <X> List<X> parseInput(Function<String,X> converter) {
		return streamInput((testMode?"test":"")+"input.txt",converter).collect(Collectors.toList());
	}
	public <X,A> List<X> parseInput(Function<String,A> first,Function<A,X> second) {
		return loadInputByLine((testMode?"test":"")+"input.txt").stream().
				map(first).map(second)
				.collect(Collectors.toList());		
	}
	public <X> List<X> parseInput(String resource,Function<String,X> converter) {
		return streamInput(resource,converter).collect(Collectors.toList());
	}
	
	public <X> Stream<X> streamInput(String resource,Function<String,X> converter) {
		return loadInputByLine(resource)
		.stream().map(converter);
	}
	public <X> Stream<X> streamInput(Function<String,X> converter) {
		return loadInputByLine((testMode?"test":"")+"input.txt")
		.stream().map(converter);
	}
	/**
	 * Lees het gehele bestand in 1 string
	 * @param day
	 * @param resource
	 * @return
	 */
	public String loadInputToString(String resource) {
		String packName=this.getClass().getPackageName().replace(".", "/");
		File inFile=new File("./src/main/java/"+packName+"/" + resource);
		try {
			return FileUtils.readFileToString(inFile, "UTF-8");
		} catch (Exception ex) {
			throw new Error("Probleem bij lezen van "+inFile.getAbsolutePath(),ex);
		}
	}
}
