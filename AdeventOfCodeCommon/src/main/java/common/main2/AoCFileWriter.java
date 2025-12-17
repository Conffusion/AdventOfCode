package common.main2;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AoCFileWriter implements Closeable{
	private File outputFile;
	private FileWriter outputFileWriter;
	private AoCContext context;
	private String fileName;
	
	public AoCFileWriter(AoCContext context) {
		this.context=context;
	}

	public AoCFileWriter(AoCContext context, String fileName) {
		this.context=context;
		this.fileName=fileName;
	}

	public void writeToFile(String... content) {
		try {
			String line=String.join(";", content);
			getOutputFileWriter().write(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileWriter getOutputFileWriter() {
		if(outputFileWriter==null) try {
			context.setOutputFile(getOutputFile());
			outputFileWriter=new FileWriter(outputFile);
		} catch(IOException iox) {
			System.err.println("Probleem bij openen van output file");
			throw new RuntimeException(iox);
		}
		return outputFileWriter;
	}

	public File getOutputFile() {
		if(fileName==null)
			fileName="output"+context.getOnStar()+".csv";
		outputFile=new File(context.getCurrentFolder(),fileName);
		return outputFile;
	}

	@Override
	public void close() throws IOException {
		outputFileWriter.close();		
	}
}
