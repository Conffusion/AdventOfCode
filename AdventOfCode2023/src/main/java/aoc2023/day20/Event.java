package aoc2023.day20;

public class Event {
	Module origin;
	Module destination;
	Pulse pulse;
	
	public Event(Module origin, Module destination, Pulse pulse) {
		if(destination==null)
			throw new IllegalArgumentException("destination mag niet null zijn");
		this.origin=origin;
		this.destination = destination;
		this.pulse = pulse;
	}

	@Override
	public String toString() {
		return origin.getId()+" -"+pulse.name().toLowerCase()+"-> "+destination.getId();
	}	
}
