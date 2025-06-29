package aoc2023.day15;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

import common.dim2.DimensionUtils;
import common.main.AbstractMainMaster;
import common.regex.RegexMatch;
import common.regex.RegexMatchBuilder;

public class Main15 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main15()
            //.testMode()
            //.withFileOutput()
            .nolog()
           .start();
    }
    String[] inputsteps;
    
    @Override
    public void beforeEach() {
    	inputsteps=loadInputToString().split(",");
    }


    // antwoord : 512283
    public Long star1() {
    	Integer som=Arrays.stream(inputsteps).map(this::toHASH).reduce(0, Integer::sum);
    	return som.longValue();
    }

    Box[] boxes=new Box[256];
    
    // antwoord : 
    public Long star2() {
    	DimensionUtils.fill2Dim(boxes, i->new Box());
    	Arrays.stream(inputsteps).forEach(this::fillBoxes);
    	long power=0;
    	for (int i=0;i<boxes.length;i++) {
    		int l=0;
    		for(Lens lens: boxes[i].lenses) {
    			l++;
    			power+=(i+1)*l*lens.focal;
    		}
    	}
    	return power;
    }
    
    private int toHASH(String text) {
    	int hash=0;
    	for(char c:text.toCharArray()) {
    		hash=((hash+c)*17)%256;
    	}
    	return hash;
    }
    RegexMatchBuilder instructionBuilder=new RegexMatchBuilder("([a-z]+)([=-])([0-9]*)");
    
    public void fillBoxes(String instruction) {
    	RegexMatch match=instructionBuilder.evaluate(instruction);
    	int focal=0;
    	int hash=toHASH(match.group(1));
    	char operation=match.group(2).charAt(0);
    	if(operation=='=') {
    		focal=match.intGroup(3);
    		boxes[hash].addLens(new Lens(match.group(1),focal));
    	}
    	if(operation=='-')
    		boxes[hash].removeLens(new Lens(match.group(1),0));
    	logln(instruction+": hash="+hash+", operation="+operation+", focal="+focal);
    }
    
    static class Box {
    	LinkedList<Lens> lenses=new LinkedList<>();
    	public void addLens(Lens lens) {
    		int index=lenses.indexOf(lens);
    		if(index>-1)
    			lenses.get(index).focal=lens.focal;
    		else
    			lenses.addLast(lens);
    	}
    	public void removeLens(Lens lens) {
    		lenses.remove(lens);    		
    	}
    }
    
    static class Lens {
    	String label;
    	int focal;
    	
		public Lens(String label, int focal) {
			this.label = label;
			this.focal = focal;
		}
		@Override
		public int hashCode() {
			return Objects.hash(label);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Lens other = (Lens) obj;
			return Objects.equals(label, other.label);
		}
		@Override
		public String toString() {
			return "[" + label + " " + focal + "]";
		}
		
    }
}