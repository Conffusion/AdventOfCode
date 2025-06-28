package common.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import common.Executor;
import common.IMainMaster;
import common.dim2.Terrein;
import common.graph.Point;

public abstract class MainMaster<T> implements IMainMaster<T> {
	protected boolean log = true;
	protected boolean testMode=false;
	protected boolean fileoutputMode=false;
	protected String resourceName;
	protected FileWriter outputFileWriter;
	protected File outputFile;
	protected int onStar=1;
//	private PrintStream logStream=System.out;
	public Consumer<String[]> writeToFileImpl=(s)->{};
	
	/**
	 * Om star1 en star2 uit te voeren
	 * @param testMode
	 */
	public MainMaster<T> start() {
		if(testMode) {
			logln("--- TEST ---");
	        //Print the jvm heap size.
			logln("Heap Size = " + Runtime.getRuntime().totalMemory());

			beforeAllTest();
		}
		beforeAll();
		onStar=1;
		beforeEach();
		doStar1();
		logln("--------------------------");
		onStar=2;
		beforeEach();
		doStar2();
		return this;
	}

	/**
	 * Om enkel star1 uit te voeren
	 */
	public MainMaster<T> start1() {
		onStar=1;
		if(testMode) {
			logln("--- TEST ---");
			beforeAllTest();
		}
		beforeAll();
		beforeEach();
		doStar1();
		return this;
	}
	/**
	 * Om enkel star1 uit te voeren
	 */
	public MainMaster<T> start2() {
		onStar=2;
		if(testMode) {
			logln("--- TEST ---");
			beforeAllTest();
		}
		beforeAll();
		beforeEach();
		doStar2();
		return this;
	}

	/**
	 * Voert star2 loopcount keer uit. Opgelet beforeAll en beforeEach worden slechts 1 keer aangeroepen
	 * @param loopcount
	 * @return
	 */
	public MainMaster<T> start2(int loopcount) {
		onStar=2;
		if(testMode) {
			logln("--- TEST ---");
			beforeAllTest();
		}
		beforeAll();
		beforeEach();
		long timeSum=0L;
		for(int i=1;i<=loopcount;i++)
			timeSum+=doStar2();
		System.out.println("\n**** Average time after "+loopcount+" times Star 2: "+(timeSum/loopcount)/1000000.0 + "ms");
		return this;
	}

	public MainMaster<T> testMode() {
		return testMode(true);
	}
	
	@Override
	public MainMaster<T> testMode(boolean flag) {
		testMode=flag;
		return this;
	}

	@Override
	public MainMaster<T> nolog(boolean flag) {
		log=!flag;
		return this;
	}

	public MainMaster<T> nolog() {
		return nolog(true);
	}
	/**
	 * Roep dit aan bij initialisatie zodat de calls naar {@link #writeToFile(String)} een output file maken.
	 * @return
	 */
	public MainMaster<T> withFileOutput() {
		this.fileoutputMode=true;
		this.writeToFileImpl=(s)->enabledWriteToFile(s);
		return this;
	}
	public MainMaster<T> setResourceName(String resourceName){
		this.resourceName=resourceName;
		return this;
	}

	/**
	 * Naam van de resource die ingeladen moet worden relatief t.o.v.
	 * huidig package
	 * @return
	 */
	public String getResourceName() {
		if(resourceName==null)
			return (testMode?"test":"")+"input.txt";
		return resourceName;
	}

	public void loadInput() {}

	public void doStar1() {
		try {
			timer(1, this::star1);
		} finally {
			if(outputFileWriter!=null) {
				System.out.println("Data geschreven naar: "+outputFile.getAbsolutePath());
				IOUtils.closeQuietly(outputFileWriter);
				outputFileWriter=null;
			}
		}
	}
	
	public long doStar2() {
		try {
			return timer(2, this::star2);
		} finally {
			if(outputFileWriter!=null) {
				System.out.println("Data geschreven naar: "+outputFile.getAbsolutePath());
				IOUtils.closeQuietly(outputFileWriter);
				outputFileWriter=null;
			}
		}
	}

	public abstract void beforeAllTest();
	public abstract void beforeAll();
	public abstract void beforeEach();
	public abstract T star1();
	public abstract T star2();
	
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

	public long timer(int star,Executor<T> run) {
		long start = System.nanoTime();
		try {
			System.out.println("\n*** Star "+onStar+": "+run.run());
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.nanoTime();
		System.out.println("Time:" + (end - start)/1000000.0 + "ms");
		return end-start;
	}

	/**
	 * Leest de input lijn per lijn in. Gebruikt {@link #getResourceName()}
	 * @return List met element voor elke lijn
	 */
	public List<String> loadInputByLine() {
		return loadInputByLine(getResourceName());
	}
	
	/**
	 * Open file met resourceName en leest lijn per lijn.
	 * De file moet in de huidige package staan.
	 * @param resource naam van het bestand om in te lezen
	 * @return List met element voor elke lijn
	 */
	public List<String> loadInputByLine(String resourceName) {
		String packName=this.getClass().getPackageName().replace(".", "/");
		File inFile=new File("./src/main/java/"+packName+"/" + resourceName);
		try {
			return  FileUtils.readLines(inFile, "UTF-8");
		} catch (Exception ex) {
			throw new Error("Probleem bij lezen van "+resourceName,ex);
		}
	}

	/**
	 * Leest de volledige input als 1 String. Gebruikt {@link #getResourceName()}
	 * @return volledige inhoud van bestand
	 */
	public String loadInputToString() {
		return loadInputToString(getResourceName());
	}

	/**
	 * Lees elke lijn als een Long. Gebruikt {@link #getResourceName()}
	 * @return
	 */
	public List<Long> loadInputLongByLine() {
		return loadInputLongByLine(getResourceName());
	}
	/**
	 * Lees elke lijn als een Long. 
	 * @return
	 */
	public List<Long> loadInputLongByLine(String resourceName) {
		return streamInput(resourceName,s->Long.parseLong(s)).collect(Collectors.toList());
	}

	/**
	 * leest de volledige input in 2dim array [dimy][dimx]
	 * @param dimx horizontale grootte van de array = lengte van een lijn in het bestand
	 * @param dimy verticale grootte van de array = aantal lijnen van het bestand
	 * @return
	 */
	public char[][] loadInputInto2DArray(int dimx, int dimy) {
		char[][] schema = new char[dimy][dimx];
		// fill schema
		int linecount = 0;
		for (String line : loadInputByLine()) {
			char[] lineAsChars = line.toCharArray();
			for (int i = 0; i < lineAsChars.length; i++)
				schema[linecount][i] = lineAsChars[i];
			linecount++;
		}
		return schema;
	}

	/**
	 * leest de volledige input in 2dim array [dimx][dimy]
	 * @param dimx horizontale grootte van de array
	 * @param dimy verticale grootte van de array
	 * @return 2-dim array waarbij eerste index overeenkomt met de x waarde in het bestand
	 */
	public char[][] loadInputInto2DArrayXY(int dimx, int dimy) {
		char[][] schema = new char[dimx][dimy];
		// fill schema
		int linecount = 0;
		for (String line : loadInputByLine()) {
			char[] lineAsChars = line.toCharArray();
			for (int i = 0; i < lineAsChars.length; i++)
				schema[i][linecount] = lineAsChars[i];
			linecount++;
		}
		return schema;
	}

	/**
	 * leest de volledige input in 2dim array [dimx][dimy]
	 * Elke positie wordt omgezet naar een cijfer 0-9. Er worden geen andere tekens verwacht
	 * @param dimx horizontale grootte van de array
	 * @param dimy verticale grootte van de array
	 * @return
	 */
	public int[][] loadInputInto2DIntArrayXY(int dimx, int dimy) {
		int[][] schema = new int[dimx][dimy];
		// fill schema
		int linecount = 0;
		for (String line : loadInputByLine()) {
			char[] lineAsChars = line.toCharArray();
			for (int i = 0; i < lineAsChars.length; i++)
				schema[i][linecount] = lineAsChars[i]-'0';
			linecount++;
		}
		return schema;
	}
	/**
	 * leest de volledige input in 2dim array [dimy][dimx]
	 * Elke positie wordt omgezet naar een cijfer 0-9. Er worden geen andere tekens verwacht
	 * @param dimx horizontale grootte van de array
	 * @param dimy verticale grootte van de array
	 * @return
	 */
	public int[][] loadInputInto2DIntArrayYX(int dimx, int dimy) {
		int[][] schema = new int[dimy][dimx];
		// fill schema
		int linecount = 0;
		for (String line : loadInputByLine()) {
			char[] lineAsChars = line.toCharArray();
			for (int i = 0; i < lineAsChars.length; i++)
				schema[linecount][i] = lineAsChars[i]-'0';
			linecount++;
		}
		return schema;
	}

	/**
	 * Leest de volledige input in 2dim array.
	 * Het input character wordt via de converter omgezet naar T en in de array geplaatst
	 * @param area 2Dim array [x][y]
	 * @param converter functie om input character om te zetten naar object.
	 */
	public <X> void loadInputInto2DTArray(Terrein<X> terrein,InputCharConverter<X> converter) {
		int lineCounter=0;
		for (String line : loadInputByLine()) {
			char[] lineAsChars = line.toCharArray();
			for (int i = 0; i < lineAsChars.length; i++) {
				terrein.setField(new Point(i,lineCounter), converter.convert(lineAsChars[i],i,lineCounter));
			}
			lineCounter++;
		}		
	}
	public interface InputCharConverter<X> {
		X convert(Character c, int x, int y);
	}
	
	/**
	 * Lees elke lijn en converteer deze naar object van type X dmv converter
	 * @param <X>
	 * @param converter
	 * @return
	 */
	public <X> List<X> parseInput(Function<String,X> converter) {
		return streamInput(getResourceName(),converter).collect(Collectors.toList());
	}
	
	/**
	 * Lees elke lijn en converteer deze naar object van type X dmv 2 converters
	 * @param <X> type van resultaat element na converteren
	 * @param first eerste converter die lijn uit bestand als input krijgt
	 * @param second tweede converter die output van first als input krijgt
	 * @return
	 */
	public <X,A> List<X> parseInput(Function<String,A> first,Function<A,X> second) {
		return loadInputByLine(getResourceName()).stream().
				map(first).map(second)
				.collect(Collectors.toList());		
	}
	
	/**
	 * Lees elke lijn en converteer deze naar object van type X dmv converter
	 * @param <X>
	 * @param resource : naam van in te lezen resource
	 * @param converter
	 * @return
	 */
	public <X> List<X> parseInput(String resource,Function<String,X> converter) {
		return streamInput(resource,converter).collect(Collectors.toList());
	}
	
	public <X> Stream<X> streamInput(String resource,Function<String,X> converter) {
		return loadInputByLine(resource)
		.stream().map(converter);
	}
	/**
	 * Geeft Stream waarin eerste mapping als is gebeurd door converter
	 * @param <X>
	 * @param converter
	 * @return
	 */
	public <X> Stream<X> streamInput(Function<String,X> converter) {
		return streamInput().map(converter);
	}
	/**
	 * Geeft stream van de input met een lijn als element
	 * @param resource : naam van in te lezen resource
	 * @return
	 */
	public Stream<String> streamInput(String resource) {
		return loadInputByLine(resource).stream();
	}
	/**
	 * Geeft stream van de input met een lijn als element
	 * @return
	 */
	public Stream<String> streamInput() {
		return streamInput(getResourceName());
	}

	/**
	 * Lees het gehele bestand in 1 string
	 * @param day
	 * @param resource relatief ten opzichte van huidig package
	 * @return
	 */
	public String loadInputToString(String resource) {
		String packName=this.getClass().getPackageName().replace(".", "/");
		File inFile=new File("./src/main/java/"+packName+"/" + resource);
		try {
			String response=FileUtils.readFileToString(inFile, "UTF-8");
			if(response.endsWith("\n"))
				response=response.substring(0, response.length()-1);
			return response;
		} catch (Exception ex) {
			throw new Error("Probleem bij lezen van "+inFile.getAbsolutePath(),ex);
		}
	}
	/**
	 * Converteert een lijn met separator-separated getallen naar een array
	 * vb: 1;2;3 -> [1,2,3]
	 * @param line 
	 * @param separator
	 * @return
	 */
	public int[] parseNumbersLine(String line, String separator) {
		String[] values=line.trim().split(separator);
		int[] response=new int[values.length];
		for(int i=0;i<values.length;i++)
			try {
				response[i]=Integer.parseInt(values[i]);
			} catch(NumberFormatException nfe) {
				logln("Could not split '"+line+"' at index "+i);
				throw nfe;
			}
		return response;
	}
	/**
	 * Utility om een array leesbaar te printen
	 * @param in
	 * @return
	 */
	public String logArray(String[] in)
	{
		return String.join(",", in);
	}
	public String logArray(int[] in)
	{
		StringBuffer buf=new StringBuffer("[");
		for(int i=0;i<in.length;i++)
			buf.append((i>0?",":"")+in[i]);
		buf.append("]");
		return buf.toString();
	}
	public <E> String logArray(E[] in)
	{
		StringBuffer buf=new StringBuffer("[");
		for(int i=0;i<in.length;i++)
			buf.append((i>0?",":"")+in[i]);
		buf.append("]");
		return buf.toString();
	}
	public void logln(String text,Object... params) {
		if(log)
			logln(String.format(text, params));
	}

    private static File root=new File("src/main/java");
    
	/**
	 * Geeft de folder terug waarin de huidige Main99.java staat.
	 * Handig om een output file naar de package te laten schrijven
	 * @return
	 */
	public File currentFolder() {
		return new File(root, this.getClass().getPackageName().replace(".", "/"));
	}

	public void writeToFile(String... content) {
		writeToFileImpl.accept(content);
	}
	
	/**
	 * Schrijft de content naar de output file. Voegt geen automatische CRLF toe.
	 * Werkt alleen als {@link #withFileOutput()} is aangeroepen
	 * @param content
	 */
	private void enabledWriteToFile(String... content) {
		if(outputFileWriter==null) try {
			outputFileWriter=new FileWriter(outputFile=new File(currentFolder(),"output"+onStar+".csv"));
		} catch(IOException iox) {
			System.err.println("Probleem bij openen van output file");
			throw new RuntimeException(iox);
		}
		try {
			String line=String.join(";", content);
			outputFileWriter.write(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
