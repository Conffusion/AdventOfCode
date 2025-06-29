package aoc2023.day20;

import java.util.HashMap;
import java.util.Map;

/**
 * Als in memory alle Pulsen HIGH zijn, wordt LOW doorgestuurd, anders HIGH.
 */
public class Conjunction extends AbstractModule {
	private Map<Module,Pulse> memory=new HashMap<>();
	
	public Conjunction(String id) {
		setId(id);
	}
	
	public void registerInputModule(Module module) {
		memory.put(module,Pulse.LOW);		
	}
	
	@Override
	public Pulse process(Event event) {
		memory.put(event.origin, event.pulse);
		return memory.values().contains(Pulse.LOW)?Pulse.HIGH:Pulse.LOW;
		//return memory.keySet().stream().anyMatch(ff->!ff.isOn)?Pulse.HIGH:Pulse.LOW;
	}
}