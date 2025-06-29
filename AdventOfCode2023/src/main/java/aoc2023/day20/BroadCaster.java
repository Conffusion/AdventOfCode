package aoc2023.day20;

public class BroadCaster extends AbstractModule {

	@Override
	public String getId() {
		return "broadcaster";
	}

	@Override
	public Pulse process(Event event) {
		return event.pulse;
	}
}
