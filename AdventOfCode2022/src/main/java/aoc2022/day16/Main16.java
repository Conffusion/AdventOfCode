package aoc2022.day16;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import common.main.AbstractMainMaster;
import common.regex.RegexTool;

/**
 * een graph van pijpleidingen en kranen
 * in welk volgorde moeten ze geopend worden om zoveel mogelijk druk vrij te krijgen in 30 minuten ?
 * vereenvoudiging: een kraan met debiet 0 en enkel verbonden met 2 andere kranen wordt vervangen door een pad met waarde 2
 * een plantUML schema wordt gegenereerd
 * @author walter
 *
 */
public class Main16 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main16()
            .testMode()
            //.nolog()
           .start();
    }

    Map<String,Valve> network;
    List<String> valveNames=new ArrayList<>();
    
    @Override
	public void beforeAll() {
    	network=streamInput(this::parseInput).collect(Collectors.toMap(v->v.name,v->v));
    	printPlantUML();
    }

    RegexTool linePattern=new RegexTool("Valve (..) has flow rate=(\\d+); tunnels? leads? to valves? (.+)"); 

    public Valve parseInput(String s) {
    	if(linePattern.evaluate(s)) {
    		Valve v=new Valve();
    		v.name=linePattern.group(1);
    		v.flowRate=linePattern.intGroup(2);
    		v.links= Arrays.stream(linePattern.group(3).split(", ")).map(s2->new Link(1,s2))
    		.collect(Collectors.toList());
    		valveNames.add(v.name);
    		return v;
    	} else 
    		throw new IllegalArgumentException("snap ik niets van:"+s);
    }
    
	@Override
    public void beforeEach() {
		
    }

    public Long star1() {
    	reduce();
    	printPlantUML();
        return null;
    }

    /**
     * een link X(n) -1- A(0) -1- Y(m) vervangen door X(n) -2- Y(m)
     */
    private void reduce() {
    	for(String vn:valveNames) {
    		Valve v=network.get(vn);
    		if(v.flowRate==0&&v.links.size()==2&&!v.name.equals("AA")) {
    			
    			Valve v1=network.get(v.links.get(0).val);
    			Valve v2=network.get(v.links.get(1).val);
    			// X -(+1)-> Y
    			Link l1=v1.findLink(v.name);
    			l1.cost+=v.links.get(1).cost;
    			l1.val=v2.name;
    			// Y -(+1)-> X
    			Link l2=v2.findLink(v.name);
    			l2.cost+=v.links.get(0).cost;
    			l2.val=v1.name;
    			network.remove(v.name);
    			logln("removed "+v.name+" ("+v.flowRate+") by "+l1+ " and "+l2);
    		}
    	}
    }
    
    public Long star2() {
        return null;

    }
    
    class Valve {
    	String name;
    	int flowRate;
    	List<Link> links=new ArrayList<>();
    	public Link findLink(final String vName) {
    		return links.stream().filter(l->l.val.equals(vName)).findFirst().orElse(null);
    	}
    }
    
    class Link {
    	int cost;
    	String val; // valve name
    	
    	public Link() {
		}

		public Link(int cost, String val) {
			this.cost = cost;
			this.val = val;
		}

		@Override
		public String toString() {
			return "Link [cost=" + cost + ", val=" + val + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((val == null) ? 0 : val.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Link other = (Link) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (val == null) {
				if (other.val != null)
					return false;
			} else if (!val.equals(other.val))
				return false;
			return true;
		}

		private Main16 getEnclosingInstance() {
			return Main16.this;
		}
    }
    
    public void printPlantUML() {
        final Set<String> printedValveLinks=new HashSet<>();
    	try(PrintWriter pw=new PrintWriter(new File("src/main/java/aoc2022/day16/input.puml"))) {
    		pw.append("@startuml\n");
    		network.values().forEach(v->pw.append(printValve(v)));
    		pw.append("\n");
    		
    		network.values().stream().map(v->{printedValveLinks.add(v.name); return v;}).forEach(v->v.links.stream().filter(l->!printedValveLinks.contains(l.val))
    			.forEach(l->pw.append("  "+v.name)
    				.append(" -[thickness="+l.cost+"]-- ")
    				.append(l.val+":"+l.cost+"\n")));
    		pw.append("@enduml");
    	} catch (IOException iox) {
    		iox.printStackTrace();
    	}
    }
    
    public String printValve(Valve v) {
    	StringBuffer buf=new StringBuffer("  object \"")
    			.append(v.name)
    			.append(" (")
    			.append(v.flowRate)
    			.append(")\" as ")
    			.append(v.name);
    	if(v.name.equals("AA"))
    		buf.append(" #yellow");
    	else if(v.flowRate==0)
    		buf.append(" #lightgrey");
    	else if (v.flowRate<10)
    		buf.append(" #lightgreen");
    	else buf.append(" #green");
    	buf.append("\n");
    	return buf.toString();
    }
}