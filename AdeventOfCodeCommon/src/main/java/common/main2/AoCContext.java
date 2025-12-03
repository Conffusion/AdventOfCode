package common.main2;

import java.io.File;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;

public class AoCContext {

	private boolean testMode;
	private String resourceName;
	private int onStar=1;
	private File outputFile;
	private File currentFolder;
	private AoCFileWriter outputFileWriter;
	public Consumer<String[]> writeToFileImpl=(s)->{};
	
	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
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

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public int getOnStar() {
		return onStar;
	}

	public void setOnStar(int onStar) {
		this.onStar = onStar;
	}

	public File getCurrentFolder() {
		return currentFolder;
	}

	public void setCurrentFolder(File currentFolder) {
		this.currentFolder = currentFolder;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public File setOutputFile(File outputFile) {
		this.outputFile = outputFile;
		return outputFile;
	}
	public void enableWriteToFile() {
		if(outputFileWriter==null) {
			outputFileWriter=new AoCFileWriter(this);
			writeToFileImpl=outputFileWriter::writeToFile;
		}
	}
	public void writeToFile(String... content) {
		writeToFileImpl.accept(content);
	}
	void terminateOutputFileWriter() {
		if(outputFileWriter!=null) {
			System.out.println("Data geschreven naar: "+getOutputFile().getAbsolutePath());
			IOUtils.closeQuietly(outputFileWriter);
			outputFileWriter=null;
		}
	}
	AoCFileWriter getOutputFileWriter() {
		return outputFileWriter;
	}
}
