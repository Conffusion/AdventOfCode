package aoc2023.day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import common.QueueHelper;
import common.main.AbstractMainMaster;
import common.regex.RegexMatch;
import common.regex.RegexMatchBuilder;
import common.regex.RegexTool;

public class Main19 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main19()
            .testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }

    RegexMatchBuilder workflowPattern=new RegexMatchBuilder("([a-z]+)\\{(.+)\\}",true);
    RegexMatchBuilder partPattern=new RegexMatchBuilder("\\{x=([0-9]+),m=([0-9]+),a=([0-9]+),s=([0-9]+)\\}",false);
	static RegexTool cmdPattern=new RegexTool("([xmas])([<>=])([0-9]+):([a-zRA]+)");
    Map<String,Workflow> workflows;
    List<Part> parts; 
    static Map<String,BiFunction<Long,Long,Boolean>> evaluators=new HashMap<>();
        
    @Override
    public void beforeEach() {
    	workflows=new HashMap<>();
    	List<String> lines=loadInputByLine();
    	String line=null;
    	Iterator<String> lineIt=lines.iterator();
    	// load workflows
    	while(!StringUtils.isBlank(line=lineIt.next())) {
    		RegexMatch match=workflowPattern.evaluate(line);
    		workflows.put(match.group(1), new Workflow(match.group(1),match.group(2)));
    	}
    	parts=new ArrayList<>();
    	// load parts
    	while(lineIt.hasNext()) {
    		line=lineIt.next();
    		RegexMatch match= partPattern.evaluate(line);
    		parts.add(new Part(match.longGroup(1),match.longGroup(2),match.longGroup(3),match.longGroup(4)));
    	}
    	evaluators.put("<", (l1,l2)->l1<l2);
    	evaluators.put(">", (l1,l2)->l1>l2);
    	evaluators.put("=", (l1,l2)->l1==l2);
    }

    // antwoord : 386787
    public Long star1() {
    	return parts.stream().map(this::applyWF).reduce(0L,Long::sum);
    }

    private Long applyWF(Part part) {
    	Workflow currWF=workflows.get("in");
    	while(true) {
    		String result=currWF.apply(part);
    		if(result.equals("A"))
    			return part.rating;
    		if(result.equals("R"))
    			return 0L;
    		currWF=workflows.get(result);
    	}
    }

    static class Message {
    	Part2 part;
    	String wfstart;
    	int cmdIndex;
		public Message(Part2 part, String wfstart, int cmdIndex) {
			this.part = part;
			this.wfstart = wfstart;
			this.cmdIndex = cmdIndex;
		}
		@Override
		public String toString() {
			return "Message [part=" + part + ", wfstart=" + wfstart + ", cmdIndex=" + cmdIndex + "]";
		} 
		
    }
    QueueHelper<Message> partsQueue=new QueueHelper<>();
    List<Part2> completed=new ArrayList<>();
    
    // antwoord : 
    public Long star2() {
    	partsQueue.add(new Message(new Part2(),"in",0));
    	partsQueue.consume(this::applyWF2);
    	long som=0L;
    	for(Part2 p2:completed) {
    		long psom=p2.getRating2();
    		System.out.println(p2.toString()+" : "+psom);
    		som+=psom;
    	}
    	return som;
    }
    
	private void applyWF2(Message message) {
		Part2 part=message.part;
		if(message.wfstart.equals("A"))
			completed.add(part);
		else if (message.wfstart.equals("R")) {
			// negeren
			System.err.println("Rejected:"+part);			
		}else {
			Workflow wf=workflows.get(message.wfstart);
			for (int i=message.cmdIndex;i<wf.commands.size();i++) {
				WFCommand currWFC=wf.commands.get(i);
				if(currWFC.comparator==null) {
					if(currWFC.next.equals("A"))
						completed.add(part);
					else if (!currWFC.next.equals("R")) {
						partsQueue.add(new Message(part,currWFC.next,0));
					}
				} else {
					part=process2(part,currWFC,wf);
				}
			}
		}
	}
	
	/**
	 * vergelijkt part met command. post zelf op queue als er een nieuwe tak moet gestart worden
	 * @param part huidig part
	 * @param cmd te evalueren commando. Moet een comparator hebben
	 * @return part om mee verder te gaan in huidige workflow
	 */
	private Part2 process2(Part2 part, WFCommand cmd, Workflow wf) {
		Long partValue1=part.cats.get(cmd.catname).getLeft();
		Long partValue2=part.cats.get(cmd.catname).getRight();
		switch (cmd.comparator) {
		case ">":
			if(partValue2>cmd.value) {
				Part2 newpart=part.clone();
				newpart.cats.get(cmd.catname).setLeft(Math.max(partValue1, cmd.value+1));
				partsQueue.add(new Message(newpart,cmd.next,0));
			}
			if (partValue1<=cmd.value) {
				part.cats.get(cmd.catname).setRight(Math.min(partValue2, cmd.value));
			}
			break;
		case "<":
			if (partValue1<cmd.value) {
				Part2 newpart=part.clone();
				newpart.cats.get(cmd.catname).setRight(Math.min(partValue2, cmd.value-1));
				partsQueue.add(new Message(newpart,cmd.next,0));
			}
			if(partValue2>=cmd.value) {
				part.cats.get(cmd.catname).setLeft(Math.max(partValue1, cmd.value));
			}
			break;
		case "=":
			if (partValue1<=cmd.value && partValue2>=cmd.value) {
				Part2 newpart=part.clone();
				newpart.cats.get(cmd.catname).setLeft(cmd.value);
				newpart.cats.get(cmd.catname).setRight(cmd.value);
				partsQueue.add(new Message(newpart,cmd.next,0));
			}
			if (partValue1<cmd.value) {
				Part2 newpart=part.clone();
				newpart.cats.get(cmd.catname).setRight(Math.min(partValue2, cmd.value-1));
				partsQueue.add(new Message(newpart,wf.name,cmd.index+1));
			}
			if(partValue2>cmd.value) {
				part.cats.get(cmd.catname).setLeft(Math.max(partValue1, cmd.value));
			}
			break;
		}
		return part;
	}
	
    static class Workflow {
    	String name;
    	List<WFCommand> commands=new ArrayList<>();
    	
    	Workflow(String name,String commandLine) {
    		this.name=name;
    		String[] commandSegments=commandLine.split(",");
    		for(int i=0;i<commandSegments.length;i++ ) {
    			String command=commandSegments[i];
    			if(cmdPattern.evaluate(command)) {
    				commands.add(new WFCommand(cmdPattern.group(1),cmdPattern.group(2),cmdPattern.longGroup(3),cmdPattern.group(4),i));
    			} else 
   					commands.add(new WFCommand(null,null,null,command,i));
    		}    	
    	}
    	public String apply(Part part) {
    		for(WFCommand wfc:commands) {
    			if(wfc.catname!=null) { 
    				if(evaluators.get(wfc.comparator).apply(part.get(wfc.catname), wfc.value))
    					return wfc.next;
    			} else
    				return wfc.next;
    		}
    		System.err.println("Failed to apply part "+part+" on WF "+name);
    		return null;
    	}
    }
    
    static class WFCommand {
    	String catname;
    	String comparator;
    	Long value;
    	String next;
    	int index; // positie van dit command in de workflow (base 0)
		public WFCommand(String catname, String comparator, Long value, String next,int index) {
			this.catname = catname;
			this.comparator = comparator;
			this.value = value;
			this.next = next;
			this.index=index;
		}
		@Override
		public String toString() {
			return "WFCommand [" + catname + comparator + value + ":"+ next + ", index=" + index + "]";
		}
    }
    
    static class Part {
    	Map<String,Long> cats=new HashMap<>();
    	long rating;
    	Part(Long x, Long m, Long a, Long s) {
    		cats.put("x",x);
    		cats.put("m", m);
    		cats.put("a", a);
    		cats.put("s", s);
    		rating=x+m+a+s;
    	}
    	public Long get(String name) {
    		return cats.get(name);
    	}
    }
    static class Part2 {
    	Map<String,MutablePair<Long,Long>> cats=new HashMap<>();
    	long rating;
    	Part2() {
    		cats.put("x", MutablePair.of(1L,4000L));
    		cats.put("m", MutablePair.of(1L,4000L));
    		cats.put("a", MutablePair.of(1L,4000L));
    		cats.put("s", MutablePair.of(1L,4000L));
    	}
    	public MutablePair<Long,Long> get(String name) {
    		return cats.get(name);
    	}
    	
    	public Long getRating() {
    		long multiplier=1L;
    		multiplier*=(cats.get("x").getRight()-cats.get("x").getLeft()+1);
    		multiplier*=(cats.get("m").getRight()-cats.get("m").getLeft()+1);
    		multiplier*=(cats.get("a").getRight()-cats.get("a").getLeft()+1);
    		multiplier*=(cats.get("s").getRight()-cats.get("s").getLeft()+1);
    		long result=0;
    		result+=rating(cats.get("x"),multiplier);
    		result+=rating(cats.get("m"),multiplier);
    		result+=rating(cats.get("a"),multiplier);
    		result+=rating(cats.get("s"),multiplier);
    		return result;
    	}
    	private long rating(Pair<Long,Long> values,long multiplier) {
    		return (values.getRight()+values.getLeft())*multiplier/2;
    	}
    	private long getRating2() {
    		long result=0;
    		result+=(cats.get("x").getRight()+cats.get("x").getLeft())*(cats.get("x").getRight()-cats.get("x").getLeft()+1)/2
    					*(cats.get("m").getRight()-cats.get("m").getLeft()+1)
    					*(cats.get("a").getRight()-cats.get("a").getLeft()+1)
    					*(cats.get("s").getRight()-cats.get("s").getLeft()+1);
    		result+=(cats.get("m").getRight()+cats.get("m").getLeft())*(cats.get("m").getRight()-cats.get("m").getLeft()+1)/2
    					*(cats.get("x").getRight()-cats.get("x").getLeft()+1)
    					*(cats.get("a").getRight()-cats.get("a").getLeft()+1)
    					*(cats.get("s").getRight()-cats.get("s").getLeft()+1);
    		result+=(cats.get("a").getRight()+cats.get("a").getLeft())*(cats.get("a").getRight()-cats.get("a").getLeft()+1)/2
    					*(cats.get("x").getRight()-cats.get("x").getLeft()+1)
    					*(cats.get("m").getRight()-cats.get("m").getLeft()+1)
    					*(cats.get("s").getRight()-cats.get("s").getLeft()+1);
    		result+=(cats.get("s").getRight()+cats.get("s").getLeft())*(cats.get("s").getRight()-cats.get("s").getLeft()+1)/2
					*(cats.get("x").getRight()-cats.get("x").getLeft()+1)
					*(cats.get("m").getRight()-cats.get("m").getLeft()+1)
					*(cats.get("a").getRight()-cats.get("a").getLeft()+1);
    		return result;
    	}
    	
    	public Part2 clone() {
    		Part2 cloned=new Part2();
    		cloned.get("x").setLeft(this.get("x").getLeft());
    		cloned.get("m").setLeft(this.get("m").getLeft());
    		cloned.get("a").setLeft(this.get("a").getLeft());
    		cloned.get("s").setLeft(this.get("s").getLeft());
    		cloned.get("x").setRight(this.get("x").getRight());
    		cloned.get("m").setRight(this.get("m").getRight());
    		cloned.get("a").setRight(this.get("a").getRight());
    		cloned.get("s").setRight(this.get("s").getRight());
    		return cloned;
    	}
    	public String toString() {
    		StringBuffer buf=new StringBuffer("{");
    		buf.append("x:"+cats.get("x"));
    		buf.append(",m:"+cats.get("m"));
    		buf.append(",a:"+cats.get("a"));
    		buf.append(",s:"+cats.get("s"));
    		buf.append("}");
    		return buf.toString();
    	}
    }
}