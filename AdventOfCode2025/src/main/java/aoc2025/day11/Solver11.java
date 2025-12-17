package aoc2025.day11;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import common.graph.PlantUMLGenerator;
import common.io.Loader;
import common.main2.AoCLogger;
import common.main2.AoCRunner;
import common.main2.AoCSolver;

public class Solver11 extends AoCSolver<Long> {
	static AoCLogger logger=new AoCLogger();
	static boolean testMode=false;
	Loader loader=Loader.forMain(this).withTestMode(testMode).build();
	
	public static void main (String[] args) {
		AoCRunner<Long> runner=new AoCRunner<>(new Solver11());
		logger.nolog(false);
		if(testMode)
			runner.testMode();
		runner.start();
	}
	
	Map<String,Device> devices=new HashMap<>();
	
    @Override
    public void beforeEach() {
    	devices=loader.streamInput(this::parseDeviceLine).collect(Collectors.toMap(Device::getName,Function.identity()));
    	Device outDevice=new Device();
    	outDevice.name="out";
    	devices.put("out",outDevice);
    	// connectedFrom opvullen
    	for(Device dev:devices.values()) {
    		for(String devTo:dev.connectedTo)
    			devices.get(devTo).connectedFrom.add(dev.name);
    	}
    	genGraph();
    }
    
    private Device parseDeviceLine(String line) {
    	Device newDevice=new Device();
    	newDevice.name=line.split(":")[0];
    	Arrays.stream(line.split(":")[1].trim().split(" ")).forEach(conn->newDevice.connectedTo.add(conn));
    	return newDevice;
    }
    // hoeveel paden gaan van "you" naar "out"
    // antwoord : 494
    public Long star1() {
    	devices.get("you").countedPaths=1L;
        return countPaths(devices.get("out"));
    }
    /**
     * Geeft terug hoeveel mogelijke paden er zijn om tot dit device te komen
     * @param device
     * @return
     */
    private long countPaths(Device device) {
    	if(device.countedPaths==null)
    		device.countedPaths=device.connectedFrom.stream().map(devices::get).mapToLong(this::countPaths).sum();
		return device.countedPaths;
    }
    // hoeveel paden gaan van "svr" naar "out" en passeren "fft" en "dac
    // antwoord : 
    public Long star2() {
        // #(svr->fft) * #(fft->dac) * #(dac->out) + #(svr->dac) * #(dac->fft) * #(fft->out)
        return (countPaths("svr","fft")*countPaths("fft","dac")*countPaths("dac","out"))
        		+(countPaths("svr","dac")*countPaths("dac","fft")*countPaths("fft","out"));
    }
    private long countPaths(String from, String to) {
        resetDevices();
        devices.get(from).countedPaths=1L;
        return countPaths(devices.get(to));
    	
    }
    static class Device {
    	String name;
    	Long countedPaths=null;
    	List<String> connectedFrom=new ArrayList<>();
    	List<String> connectedTo=new ArrayList<>();
		public String getName() {
			return name;
		}
    }
    private void resetDevices() {
    	devices.values().stream().forEach(d->d.countedPaths=null);
    }
    private void genGraph() {
    	File graphFile=new File(context.getCurrentFolder(),testMode?"graph-test.puml":"graph.puml");
    	try (PlantUMLGenerator graphGen=new PlantUMLGenerator(graphFile)) {
    		for(Device d:devices.values())
    			graphGen.addNode(d.name);
    		for(Device d:devices.values()) {
    			for(String to:d.connectedTo) {
	    			graphGen.addConnection(d.name, to);
    			}
    		}
    	}catch(IOException iox) {
    		
    	};    	
    }
    
}
