package common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class Templater {
	private List<String> content;
	private Map<String,String> params=new HashMap<>();
	
	private Templater(String templateFile) throws IOException {
		content=IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream(templateFile),"UTF-8");
	}
	
	public static Templater forTemplate(String templateFile) throws IOException {
		return new Templater(templateFile);
	}
	
	public Templater addParam(String key,String value) {
		params.put(key,value);
		return this;
	}
	
	public void writeTo(Writer output) throws IOException {
		try (PrintWriter buf=new PrintWriter(output);) {
			for(String line:content) {
				line=replaceParams(line);
				buf.println(line);
			}
		}
	}
	private String replaceParams(String line) {
		for(Map.Entry<String, String> param:params.entrySet()) {
			line=line.replaceAll("\\$\\{"+param.getKey()+"\\}", param.getValue());
		}
		return line;
	}
}
