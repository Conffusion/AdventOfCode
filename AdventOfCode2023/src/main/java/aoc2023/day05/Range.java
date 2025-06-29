package aoc2023.day05;

public class Range {
	public long start;
	public long length;
	public long end;
	public long destination;
	
	public Range(long start, long length,long destination) {
		this.start=start;
		this.length=length;
		this.end=start+length;
		this.destination=destination;
	}
	
	public boolean isInRange(Long value) {
		return start<=value && value <end;
	}
}
