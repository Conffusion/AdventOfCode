package aoc2023.day20;

import java.util.ArrayList;
import java.util.List;

public class EventController {
	List<Event> events=new ArrayList<>();
	long[] pulseCounters=new long[2];
	
	public void broadCast(Module broadCaster,Pulse pulse) {
		events.add(new Event(null,broadCaster,pulse));
		processEvents();
	}
	
	private void processEvents() {
		while(!events.isEmpty()) {
			List<Event> newEvents=new ArrayList<>();
			for(Event event:events) {
				pulseCounters[event.pulse.ordinal()]++;
				Pulse outPulse=event.destination.process(event);
				if(outPulse!=null) {
					if(event.destination.getOutputs()==null) {
						System.err.println("No destination output:"+event);
					} else {
						for(Module mod:event.destination.getOutputs()) {
							if(mod==null)
								System.err.println("output module null "+event);
							else
								newEvents.add(new Event(event.destination,mod,outPulse));
						}
					}
				}
			}
			events=newEvents;
		}
	}
	public long getLowPulseCounter() {
		return pulseCounters[Pulse.LOW.ordinal()];
	}
	public long getHighPulseCounter() {
		return pulseCounters[Pulse.HIGH.ordinal()];
	}
}
