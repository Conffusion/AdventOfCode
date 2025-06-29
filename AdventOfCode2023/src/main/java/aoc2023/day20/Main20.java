package aoc2023.day20;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import common.main.AbstractMainMaster;
import common.regex.RegexMatch;
import common.regex.RegexMatchBuilder;

public class Main20 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main20()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start1();
    }
    RegexMatchBuilder linePattern=new RegexMatchBuilder("([%&a-z]+) -> ([a-z, ]+)",true);

    HashMap<String,Module> modules=null;
    
    @Override
    public void beforeEach() {
    	modules=new HashMap<>();
    	modules.put("output", new Output());
    	// first iteration of input to register all modules by name
    	streamInput(linePattern::evaluate).forEach(re->parseModules(re));
    	// second iteration to add output
    	streamInput(linePattern::evaluate).forEach(re->parseLine(re));
    	toPlantumlGraph();
    }
    
    private void parseModules(RegexMatch match) {
    	String from=match.group(1);
    	if(from.equals("broadcaster")) {
    		modules.put("broadcaster", new BroadCaster());
    	} else if(from.startsWith("%"))
    	{
    		Module m=new FlipFlop(from.substring(1));
    		modules.put(m.getId(), m);
    	} else if (from.startsWith("&")) {
    		Module m=new Conjunction(from.substring(1));
    		modules.put(m.getId(), m);
    	}
    }
    
    private void parseLine(RegexMatch match) {
    	String from=match.group(1).replace("%", "").replace("&", "");
    	String[] to=match.group(2).split(",");
    	Module m=modules.get(from);
    	if(m==null)
    		throw new IllegalArgumentException("geen module gevonden voor "+from);
    	for(String t:to) {
    		Module dest=modules.get(t.trim());
    		if(dest==null) {
    			dest=new Output(t.trim());
    			modules.put(t.trim(), dest);
    			System.err.println("Geen module gevonden voor "+t.trim());
    		} 
    		m.registerOutput(dest);
    		if(dest instanceof Conjunction) {
   				((Conjunction)dest).registerInputModule(m);
    		}
    	}
    }
    // antwoord : 
    public Long star1() {
    	EventController controller=new EventController();
    	for(int pushes=0;pushes<1000;pushes++)
    		controller.broadCast(modules.get("broadcaster"), Pulse.LOW);
    	System.out.println(
        "high:"+controller.getHighPulseCounter()+",low:"+controller.getLowPulseCounter()+"="+(controller.getHighPulseCounter()*controller.getLowPulseCounter()));
    	return controller.getHighPulseCounter()*controller.getLowPulseCounter();
    }

    // antwoord : 
    public Long star2() {
    	EventController controller=new EventController();
    	long buttonCounter=0;
    	while(true) {
    		buttonCounter++;
    		controller.broadCast(modules.get("broadcaster"), Pulse.LOW);
    		if(((Output)modules.get("rx")).isLowPulseReceived())
    			break;
    		if(buttonCounter%1000000==0)
    			System.out.println("buttonCounter:"+buttonCounter);
    	}
    	return buttonCounter;
    }
    
    public void toPlantumlGraph() {
    	File file=new File("target/day20-diagram.plantuml");
    	try (FileWriter fw=new FileWriter(file);
    			PrintWriter out=new PrintWriter(fw);) {
    		out.println("@startuml");
	    	for(Module m:modules.values()) {
	    		String cat=null;
	    		if(m instanceof FlipFlop)
	    			cat="F-F";
	    		else if (m instanceof Conjunction) {
	    			cat="Con";
	    		}
	    		out.print("rectangle "+m.getId());
	    		if(cat!=null) 
	    			out.print(" <<"+cat+">>");
	    		out.println();
	    	}
	    	out.println();
	    	for(Module m:modules.values()) {
	    		if (m.getOutputs()!=null) {
	    			for(Module mout:m.getOutputs()) {
	    				out.println(m.getId()+" --> "+mout.getId());
	    			}
	    		}
	    	}
    		out.println("@enduml");
    	} catch (IOException iox) {
    		System.err.println("Kan niet schrijven naar "+file.getAbsolutePath());
    		return;
    	}
    	logln("Diagram geschreven naar "+file.getAbsolutePath());
    }
}