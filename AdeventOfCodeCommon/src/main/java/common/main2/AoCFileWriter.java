package common.main2;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AoCFileWriter implements Closeable{
	private FileWriter outputFileWriter;
	private AoCContext context;
	
	public AoCFileWriter(AoCContext context) {
		this.context=context;
	}

	public void writeToFile(String... content) {
		if(outputFileWriter==null) try {
			File outputFile=new File(context.getCurrentFolder(),"output"+context.getOnStar()+".csv");
			context.setOutputFile(outputFile);
			outputFileWriter=new FileWriter(outputFile);
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

	@Override
	public void close() throws IOException {
		outputFileWriter.close();		
	}
}
