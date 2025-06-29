package aoc2023.day20;

/**
 * Bij elke call van process toggled isOn van false en true of omgekeerd.
 */
public class FlipFlop extends AbstractModule{
	boolean isOn;

	public FlipFlop(String id) {
		setId(id);
	}
	
	@Override
	public Pulse process(Event event) {
		if(event.pulse==Pulse.LOW) {
			isOn=!isOn;
			return isOn?Pulse.HIGH:Pulse.LOW;
		}
		return null;
	}	
}
