package aoc2024;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDate;

public class DayBuilder2024 {
    static File root=new File("src/main/java");
    private int jaar;
    private String dagnr;
    private String packageName;
    private File packageFolder;

    public static void main(String[] args) throws Exception {
        int jaar=LocalDate.now().getYear();
        int dagnr=LocalDate.now().getDayOfMonth();
        jaar=2024;
        dagnr=25;
        new DayBuilder2024(jaar,dagnr)
        .writeMain()
        .writeInputFile()
        .writeTestInputFile();
    }

    public DayBuilder2024(int jaar, int dag) {
        this.jaar=jaar;
        dagnr=(dag<10?"0":"")+dag;
        packageName="aoc"+jaar+".day"+dagnr;
        packageFolder=new File(root,packageName.replace(".", "/"));
        packageFolder.mkdirs();
        System.out.println("Folder aangemaakt "+packageFolder.getAbsolutePath());
    }

    public DayBuilder2024 writeMain() throws IOException {
        System.out.println("package: "+packageName);
        File mainClass=new File(packageFolder,"Main"+dagnr+".java");
        if(mainClass.exists())
        {
            System.err.println(mainClass.getAbsolutePath()+" bestaat al.");
            return this;
        }
        try (Writer mainWriter=new FileWriter(mainClass);
        	PrintWriter out=new PrintWriter(mainWriter);) {
        	out.println("package aoc"+jaar+".day"+dagnr+";");
            out.println("import common.main.AbstractMainMaster;");
            out.println();
            out.println("public class Main"+dagnr+" extends AbstractMainMaster<Long> {");
            out.println("    public static void main(String[] args) {");
            out.println("        new Main"+dagnr+"()");
            out.println("            .testMode()");
            out.println("            //.withFileOutput()");
            out.println("            //.nolog()");
            out.println("           .start();");
            out.println("    }");
            out.println();
            out.println("    @Override");
            out.println("    public void beforeEach() {");
            out.println("    }");
            out.println();
            out.println("    // antwoord : ");
            out.println("    public Long star1() {");
            out.println("        Long result=0L;");
            out.println();
            out.println("        return result;");
            out.println("    }");
            out.println();
            out.println("    // antwoord : ");
            out.println("    public Long star2() {");
            out.println("        Long result=0L;");
            out.println();
            out.println("        return result;");
            out.println();
            out.println("    }");
            out.print("}");
        }
        return this;
    }

    public DayBuilder2024 writeInputFile() throws IOException {
        File inputFile=new File(packageFolder,"input.txt");
        inputFile.createNewFile();
        return this;
    }
    
    public DayBuilder2024 writeTestInputFile() throws IOException {
        File inputFile=new File(packageFolder,"testinput.txt");
        inputFile.createNewFile();
        return this;
    }
}
