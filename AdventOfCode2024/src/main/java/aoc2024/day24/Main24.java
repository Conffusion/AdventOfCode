package aoc2024.day24;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.graph.PlantUMLGenerator;
import common.main.AbstractMainMaster;
import common.regex.RegexMatch;
import common.regex.RegexMatchBuilder;

/**
 * graph met XOR, OR en AND poorten.
 * Deel 1: bereken output
 * Deel 2: hoe moet graph aangepast worden zodat het een optel machine wordt (x+y->z) (niet begonnen)
 */
public class Main24 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main24()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }
    RegexMatchBuilder schakelingRE= new RegexMatchBuilder("([a-z0-9]+) (XOR|OR|AND) ([a-z0-9]+) -> ([a-z0-9]+)");
    RegexMatchBuilder inputRE= new RegexMatchBuilder("([a-z0-9]+): (0|1)");
    Map<String,Wire> wires;
    List<Gate> gates;

    @Override
    public void beforeEach() {
    	List<String> input=loadInputByLine();
    	wires=new HashMap<>();
    	gates=new ArrayList<>();
    	input.stream().map(schakelingRE::evaluate).filter(RegexMatch::matches).forEach(this::parseGraph);
    	input.stream().map(inputRE::evaluate).filter(RegexMatch::matches).forEach(rm->wires.get(rm.group(1)).value=rm.intGroup(2));
    	if(onStar==1)
    		genGraph();
    }
    
    private void parseGraph(RegexMatch rm) {
    	Gate gate=switch(rm.group(2)) {
    	case "XOR"-> new XorGate();
    	case "OR"-> new OrGate();
    	case "AND"-> new AndGate();
    	default->null;    	
    	};
    	gate.in1=wires.computeIfAbsent(rm.group(1), w->new Wire(w));
    	gate.in2=wires.computeIfAbsent(rm.group(3), w->new Wire(w));
    	gate.out=wires.computeIfAbsent(rm.group(4), w->new Wire(w));
    	gate.in1.to.add(gate);
    	gate.in2.to.add(gate);
    	gate.out.from=gate;
    	gates.add(gate);
    }
    
    private void genGraph() {
    	try (PlantUMLGenerator graphGen=new PlantUMLGenerator(new File(currentFolder(),"graph.puml"))) {
    		for(Gate g:gates)
    			graphGen.addNode(g.id, g.type());
    		for(Wire w:wires.values()) {
    			String from=w.from!=null?w.from.id:"in";
    			if(w.to.isEmpty())
    				graphGen.addConnection(from, "out",w.name);
    			else for(Gate tog:w.to) {
	    			String to=tog!=null?tog.id:"out";
	    			graphGen.addConnection(from, to,w.name);
    			}
    		}
    	}catch(IOException iox) {
    		
    	};    	
    }
    
    // antwoord : 66055249060558
    public Long star1() {
        Long result=0L;
        int i=0;
        StringBuffer buf=new StringBuffer();
        while(true) {
        	Wire zwire=wires.get("z"+(i<10?"0"+i:""+i));
        	if(zwire!=null) {
        		buf.append( ""+zwire.getValue());
        		result+=(long)(zwire.getValue()==1?Math.pow(2, i):0);
        	}
        	else
        		break;
        	i++;
        }
        System.out.println(buf);
        return result;
    }

    // antwoord : 
    public Long star2() {
        Long result=0L;

        return result;
    }
    
    class Wire {
    	String name;
    	int value=-1;
    	Gate from;
    	List<Gate> to=new ArrayList<>();
    	
    	public Wire(String name) {
			this.name = name;
		}

		public int getValue() {
    		if(value==-1)
    			value=from.getValue();
    		return value;
    	}

		@Override
		public String toString() {
			return "Wire [" + name + "=" + value + ", " + from + "->" + to + "]";
		}
		
    }
    
    abstract class Gate {
    	String id;
    	abstract String type();
    	Wire in1, in2;
    	Wire out;
    	abstract int getValue();
		@Override
		public String toString() {
			return ""+this.getClass().getName()+"[in1=" + (in1!=null?in1.name:"null") + ", in2=" + (in2!=null?in2.name:"null") + ", out=" + (out!=null?out.name:"null") + "]";
		}
    }
    class AndGate extends Gate {
    	static int GATE_SEQ=1;
    	public AndGate() {
    		id="AND_"+(GATE_SEQ++);
		}
		String type() {return "AND";}
		int getValue() {
    		return in1.getValue()==1&&in2.getValue()==1?1:0;
    	}
    }
    class OrGate extends Gate {
    	static int GATE_SEQ=1;
    	public OrGate() {
    		id="OR_"+(GATE_SEQ++);
		}
    	String type() {return "OR";}
    	int getValue() {
    		return in1.getValue()==1||in2.getValue()==1?1:0;
    	}
    }
    class XorGate extends Gate {
    	static int GATE_SEQ=1;
    	public XorGate() {
    		id="XOR_"+(GATE_SEQ++);
		}
    	String type() {return "XOR";}
    	int getValue() {
    		return in1.getValue()!=in2.getValue()?1:0;
    	}
    }
}