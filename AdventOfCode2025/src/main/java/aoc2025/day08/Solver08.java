package aoc2025.day08;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

import common.dim3.Punt3D;
import common.io.Loader;
import common.main2.AoCLogger;
import common.main2.AoCRunner;
import common.main2.AoCSolver;

/**
 * input: punten in een 3D ruimte
 * 
 */
public class Solver08 extends AoCSolver<Long> {
	static AoCLogger logger=new AoCLogger();
	static boolean testMode=false;
	Loader loader=Loader.forMain(this).withTestMode(testMode).build();
	
	public static void main (String[] args) {
		//logger.nolog(true);
		AoCRunner<Long> runner=new AoCRunner<>(new Solver08());
		logger.nolog(false);
		if(testMode)
			runner.testMode();
		runner.start();
	}
	List<Junction> punten;
	
	@Override
    public void beforeEach() {
    	punten=loader.streamInput().map(s->s.split(","))
    			.map(a->new Junction(new Punt3D(Integer.parseInt(a[0]),Integer.parseInt(a[1]),Integer.parseInt(a[2]))))
    			.toList();
    }

	Comparator<Afstand> customComparator = Comparator.comparing(a -> a.lengte);
	
    // antwoord : 121770
    public Long star1() {
    	TreeSet<Afstand> afstanden = berekenAfstanden();
    	// overloop de afstanden van kort naar lang
    	int counter=0;
    	Map<Integer,Circuit> circuits=new HashMap<>();
   		for (Iterator<Afstand> iter=afstanden.iterator();iter.hasNext();) {
   			Afstand curr=iter.next();
   			if(curr.junction1.circuit==null&&curr.junction2.circuit==null) {
   				Circuit newCirc=new Circuit(++circuitSeq);
   				circuits.put(circuitSeq, newCirc);
   				newCirc.addJunction(curr.junction1);
   				newCirc.addJunction(curr.junction2);
   			} else if(curr.junction1.circuit!=null&&curr.junction2.circuit==null) {
   				circuits.get(curr.junction1.circuit.circuitID).addJunction(curr.junction2);
   			} else if (curr.junction1.circuit==null&&curr.junction2.circuit!=null) {
   				circuits.get(curr.junction2.circuit.circuitID).addJunction(curr.junction1);
   			} else if(curr.junction1.circuit.equals(curr.junction2.circuit)){
   				// beide junctions behoren al tot hetzelfde circuit
   				//continue;
   			} else {
   				// beide junctions behoren tot een verschillend circuit
   				// we verhuizen alle junctions van circuit2 naar circuit1
   				Circuit circ1=curr.junction1.circuit;
   				Circuit oldCirc=curr.junction2.circuit;
   				curr.junction2.circuit.junctions.stream()
   						.forEach(j->circ1.addJunction(j));
   				circuits.remove(oldCirc.circuitID);
   			}
   			counter++;
   			if(counter==1000)
   				break;
   		}
   		System.out.println("circuits:"+circuits.size());
        List<Circuit> largestCircuits=new ArrayList<>(circuits.values());
        Comparator<Circuit> largeToSmall=Comparator.comparing(a -> a.junctions.size());
        largeToSmall=largeToSmall.reversed();
        largestCircuits.sort(largeToSmall);
        return 1L*largestCircuits.get(0).junctions.size()
        		*largestCircuits.get(1).junctions.size()
        		*largestCircuits.get(2).junctions.size();
    }

    // antwoord : 
    // foute: 2145283032 (te laag)
    public Long star2() {
    	long answer=0;
    	TreeSet<Afstand> afstanden = berekenAfstanden();
    	int counter=0;
    	// overloop de afstanden van kort naar lang
    	Map<Integer,Circuit> circuits=new HashMap<>();
    	afstLoop:
   		for (Iterator<Afstand> iter=afstanden.iterator();iter.hasNext();) {
   			Afstand curr=iter.next();
   			if(curr.junction1.circuit==null&&curr.junction2.circuit==null) {
   				Circuit newCirc=new Circuit(++circuitSeq);
   				circuits.put(circuitSeq, newCirc);
   				newCirc.addJunction(curr.junction1);
   				newCirc.addJunction(curr.junction2);
   			} else if(curr.junction1.circuit!=null&&curr.junction2.circuit==null) {
   				circuits.get(curr.junction1.circuit.circuitID).addJunction(curr.junction2);
   			} else if (curr.junction1.circuit==null&&curr.junction2.circuit!=null) {
   				circuits.get(curr.junction2.circuit.circuitID).addJunction(curr.junction1);
   			} else if(curr.junction1.circuit.equals(curr.junction2.circuit)){
   				// beide junctions behoren al tot hetzelfde circuit
   				//continue;
   			} else {
   				// beide junctions behoren tot een verschillend circuit
   				// we verhuizen alle junctions van circuit2 naar circuit1
   				Circuit circ1=curr.junction1.circuit;
   				Circuit oldCirc=curr.junction2.circuit;
   				curr.junction2.circuit.junctions.stream()
   						.forEach(j->circ1.addJunction(j));
   				circuits.remove(oldCirc.circuitID);
   				if(circuits.size()==1 && circ1.junctions.size()>=997) {
   					answer=curr.junction1.loc.x()*curr.junction2.loc.x();
   					System.out.println("Na "+counter+" circuits:"+circuits.size()+" junctions:"+circ1.junctions.size());
   					break afstLoop;
   				}
   			}
   			counter++;
   			if(counter%100==0)
   				System.out.println("Na "+counter+" circuits:"+circuits.size());
   		}
   		System.out.println("circuits:"+circuits.size());
        return answer;
    }
    private TreeSet<Afstand> berekenAfstanden() {
    	TreeSet<Afstand> afstanden = new TreeSet<>(customComparator);
    	for(int i=0;i<punten.size()-1;i++) {
    		Junction p1=punten.get(i);
    		for(int j=i+1;j<punten.size();j++) {
    			Junction p2=punten.get(j);
    			double dist=Math.sqrt(Math.pow(p1.loc.x()-p2.loc.x(), 2)+Math.pow(p1.loc.y()-p2.loc.y(), 2)+Math.pow(p1.loc.z()-p2.loc.z(), 2));
    			afstanden.add(new Afstand(p1,p2,dist));
    		}
    	}
    	return afstanden;
    }
    int circuitSeq=0;
    static class Junction {
    	Punt3D loc;
    	Circuit circuit;
		public Junction(Punt3D loc) {
			this.loc = loc;
		}
		@Override
		public String toString() {
			return "Junction [loc=" + loc + ", circuit=" +(circuit!=null?circuit.circuitID:0)+"]";
		}
		
    }
    static class Afstand {
    	Junction junction1,junction2;
    	double lengte;
		public Afstand(Junction junction1, Junction junction2, double lengte) {
			this.junction1 = junction1;
			this.junction2 = junction2;
			this.lengte=lengte;
		}
		@Override
		public String toString() {
			return "Afstand [junction1=" + junction1 + ", junction2=" + junction2 + ", lengte=" + lengte + "]";
		}
    }
    static class Circuit {
    	int circuitID;
    	List<Junction> junctions=new ArrayList<>();
		public Circuit(int circuitID) {
			this.circuitID = circuitID;
		}
		@Override
		public int hashCode() {
			return Objects.hash(circuitID);
		}
		public void addJunction(Junction newJunc) {
			newJunc.circuit=this;
			junctions.add(newJunc);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Circuit other = (Circuit) obj;
			return circuitID == other.circuitID;
		}
		@Override
		public String toString() {
			return "Circuit [circuitID=" + circuitID + ", junctions=" + junctions + "]";
		}
    }
}
