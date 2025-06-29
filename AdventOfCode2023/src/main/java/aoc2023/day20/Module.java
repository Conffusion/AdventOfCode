package aoc2023.day20;

import java.util.List;

public interface Module {
	/**
	 * Geeft unieke identifier (vooral handig voor logging)
	 * @return id
	 */
	String getId();
	/**
	 * Registreert een output voor deze module
	 * @param module
	 */
	void registerOutput(Module module);
	List<Module> getOutputs();
	
	Pulse process(Event event);
}
