package common.graph;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utility class to generate PlantUML file to visualize a graph structure.
 */
public class PlantUMLGenerator implements Closeable{

	PrintWriter writer;
	File outFile;
	
	public PlantUMLGenerator(String packagePath,String filename) {
		this(new File("src/main/java/"+packagePath,filename));
	}
	/**
	 * Genereert PlantUML diagram source file naar outFile
	 * @param outFile
	 */
	public PlantUMLGenerator(File outFile) {
		try {
			this.outFile=outFile;
			FileWriter fw=new FileWriter(outFile);
			writer=new PrintWriter(fw);
			writer.println("@startuml");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public PlantUMLGenerator addNode(String name) {
		return addNode(name,null);
	}
	public PlantUMLGenerator addNode(String name, String category) {
		writer.print(" rectangle "+name);
		if(category!=null)
			writer.print("<<"+category+">>");
		writer.println();
		return this;
	}
	public PlantUMLGenerator addConnection(String fromNode, String toNode) {
		writer.println(" "+fromNode+" --> "+toNode);
		return this;
	}
	public PlantUMLGenerator addConnection(String fromNode, String toNode, String label) {
		writer.print(" "+fromNode+" --> "+toNode);
		if(label!=null)
			writer.print(": "+label);
		writer.println();
		return this;
	}
	
	@Override
	public void close() throws IOException {
		writer.println("@enduml");
		writer.close();
		System.out.println("Graph written to "+outFile.getAbsolutePath());
	}
}
