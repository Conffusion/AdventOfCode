package aoc2023.day20;

public class Output extends AbstractModule {

	boolean lowPulseReceived=false;
	
	public Output() {
		setId("output");
	}
	public Output(String id) {
		setId(id);
	}

	@Override
	public Pulse process(Event event) {
//		System.out.println("output received:"+event);
		if(event.pulse==Pulse.LOW)
			lowPulseReceived=true;
		return null;
	}
	public boolean isLowPulseReceived() {
		return lowPulseReceived;
	}
}
