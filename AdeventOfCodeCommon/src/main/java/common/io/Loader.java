package common.io;

import java.io.File;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import common.dim2.Terrein;
import common.graph.Point;


public class Loader {
	private Class<?> mainClass;
	private String resourceName;
	private boolean testMode;
	
	public static class Builder {
		private Loader loader;
		private Builder(Object main) {
			loader=new Loader(main);
		}
		/**
		 * Optioneel. Default is input.txt en testinput.txt 
		 * @param resourceName
		 * @return
		 */
		public Builder withResource(String resourceName) {
			loader.resourceName=resourceName;
			return this;
		}
		public Builder withTestMode(boolean testMode) {
			loader.testMode = testMode;
			return this;
		}		
		public Loader build() {
			return loader;
		}
	}
	public static Builder forMain(Object main) {
		return new Builder(main);
	}
	
	/**
	 * Constructor
	 * @param main used to get the input resource in the same package as this object
	 */
	private Loader(Object main) {
		mainClass=main.getClass();
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
		String packName=mainClass.getPackageName().replace(".", "/");
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
		String packName=mainClass.getPackageName().replace(".", "/");
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
				System.err.println("Could not split '"+line+"' at index "+i);
				throw nfe;
			}
		return response;
	}
	/**
	 * Converteert een lijn met cijfers naar een array van integers, een element per cijfer
	 * vb: 123 -> [1,2,3]
	 */
	public int[] parseDigitsLine(String line) {
		int[] digits=new int[line.length()];
		for(int i=0;i<line.length();i++)
			digits[i]=Integer.parseInt(line, i, i+1, 10);
		return digits;
	}
}
