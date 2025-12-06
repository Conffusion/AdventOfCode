package aoc2025;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDate;

import org.apache.commons.io.IOUtils;

public class DayBuilder2025 {
    static File root=new File("src/main/java");
    private int jaar;
    private String dagnr;
    private String packageName;
    private File packageFolder;

    public static void main(String[] args) throws Exception {
        int jaar=LocalDate.now().getYear();
        int dagnr=LocalDate.now().getDayOfMonth();
        jaar=2025;
        dagnr=6;
        new DayBuilder2025(jaar,dagnr)
        .writeMain()
        .writeInputFile()
        .writeTestInputFile();
    }

    public DayBuilder2025(int jaar, int dag) {
        this.jaar=jaar;
        dagnr=(dag<10?"0":"")+dag;
        packageName="aoc"+jaar+".day"+dagnr;
        packageFolder=new File(root,packageName.replace(".", "/"));
        packageFolder.mkdirs();
        System.out.println("Folder aangemaakt "+packageFolder.getAbsolutePath());
    }

    public DayBuilder2025 writeMain() throws IOException {
        System.out.println("package: "+packageName);
        String solverTemplate=IOUtils.toString(this.getClass().getResourceAsStream("template.txt"), "UTF-8");
        File mainClass=new File(packageFolder,"Solver"+dagnr+".java");
        if(mainClass.exists())
        {
            System.err.println(mainClass.getAbsolutePath()+" bestaat al.");
            return this;
        }
        try (Writer mainWriter=new FileWriter(mainClass);
        	PrintWriter out=new PrintWriter(mainWriter);) {
            out.println(solverTemplate.replaceAll("#year#",""+jaar).replaceAll("#dagnr#",dagnr));
        }
        return this;
    }

    public DayBuilder2025 writeInputFile() throws IOException {
        File inputFile=new File(packageFolder,"input.txt");
        inputFile.createNewFile();
        return this;
    }
    
    public DayBuilder2025 writeTestInputFile() throws IOException {
        File inputFile=new File(packageFolder,"testinput.txt");
        inputFile.createNewFile();
        return this;
    }
}
