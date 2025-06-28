package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;

public class DayBuilder {
    static File root=new File("src/main/java");
    private int jaar;
    private String dagnr;
    private String packageName;
    private File packageFolder;

    public static void main(String[] args) throws Exception {
        int jaar=LocalDate.now().getYear();
        int dagnr=LocalDate.now().getDayOfMonth();
        jaar=2023;
        dagnr=23;
        new DayBuilder(jaar,dagnr)
        .writeMain()
        .writeInputFile()
        .writeTestInputFile();
    }

    public DayBuilder(int jaar, int dag) {
        this.jaar=jaar;
        dagnr=(dag<10?"0":"")+dag;
        packageName="aoc"+jaar+".day"+dagnr;
        packageFolder=new File(root,packageName.replace(".", "/"));
        packageFolder.mkdirs();
        System.out.println("Folder aangemaakt "+packageFolder.getAbsolutePath());
    }

    public DayBuilder writeMain() throws IOException {
        System.out.println("package: "+packageName);
        File mainClass=new File(packageFolder,"Main"+dagnr+".java");
        if(mainClass.exists())
        {
            System.err.println(mainClass.getAbsolutePath()+" bestaat al.");
            return this;
        }
        try (Writer mainWriter=new FileWriter(mainClass);
            BufferedWriter buf=new BufferedWriter(mainWriter);) {
        	
            buf.write("package aoc"+jaar+".day"+dagnr+";");
            buf.newLine();buf.newLine();
            buf.write("import aoc"+jaar+".common.AbstractMainMaster;");
            buf.newLine();buf.newLine();
            buf.write("public class Main"+dagnr+" extends AbstractMainMaster<Long> {");
            buf.newLine();
            buf.write("    public static void main(String[] args) {"); buf.newLine();
            buf.write("        new Main"+dagnr+"()"); buf.newLine();
            buf.write("            .testMode()"); buf.newLine();
            buf.write("            //.withFileOutput()"); buf.newLine();
            buf.write("            //.nolog()");buf.newLine();
            buf.write("           .start();"); buf.newLine();
            buf.write("    }"); buf.newLine();
            buf.newLine();
            buf.write("    @Override"); buf.newLine();
            buf.write("    public void beforeEach() {"); buf.newLine();
            buf.write("    }"); buf.newLine();
            buf.newLine();
            buf.write("    // antwoord : "); buf.newLine();
            buf.write("    public Long star1() {"); buf.newLine();
            buf.write("        return null;"); buf.newLine();
            buf.write("    }"); buf.newLine();
            buf.newLine();
            buf.write("    // antwoord : "); buf.newLine();
            buf.write("    public Long star2() {"); buf.newLine();
            buf.write("        return null;"); buf.newLine();
            buf.newLine();
            buf.write("    }"); buf.newLine();
            buf.write("}");
        }
        return this;
    }

    public DayBuilder writeInputFile() throws IOException {
        File inputFile=new File(packageFolder,"input.txt");
        inputFile.createNewFile();
        return this;
    }
    
    public DayBuilder writeTestInputFile() throws IOException {
        File inputFile=new File(packageFolder,"testinput.txt");
        inputFile.createNewFile();
        return this;
    }
}
