package aoc2022.day13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.main.AbstractMainMaster;

public class Main13 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main13()
            //.testMode()
            //.nolog()
           .start();
    }
    List<PacketPair> pairs=new ArrayList<>();
    
    @Override
	public void beforeAll() {
    	List<String> input=loadInputByLine();
    	int linenum=0;
    	Packet p1=null,p2=null;
    	for(String line:input) {
    		linenum++;
    		if(linenum%3==1) {
    			p1=new Packet();
    			parsePacket(line.toCharArray(),p1,1);
    			logln("lees paar "+(linenum/3)+" deel 1"+p1);
    		} else if (linenum%3==2) {
    			p2=new Packet();
    			parsePacket(line.toCharArray(),p2,1);
    			logln("lees paar "+(linenum/3)+" deel 2"+p2);
    		} else {
    			pairs.add(new PacketPair(p1,p2));
    			logln("paar klaar "+(linenum/3));
    		}
    	}
    	// op het einde is geen lege lijn dus moet het laatste paar nog toegevoegd worden
		pairs.add(new PacketPair(p1,p2));
		logln("paar klaar "+(linenum/3));
    	logln("loaded "+pairs.size()+" pairs");
    }

    private int parsePacket(char[] content, Packet p, int start) {
		int curr=-1;
		while(true) {
	    	if(content[start]=='[') {
	    		Packet psub=new Packet();
	    		p.addPacket(psub);
	    		start=parsePacket(content,psub,start+1);
	    	} else if (content[start]>='0' && content[start]<='9') {
    			curr=(curr==-1?0:curr*10)+(content[start]-'0');
    			start++;
	    	} else if(content[start]==',') {
    			if(curr!=-1)
    				p.addPacket(new Packet(curr));
    			curr=-1;
    			start++;
	    	} else if (content[start]==']') {
    			if(curr!=-1)
    				p.addPacket(new Packet(curr));
    			break;
    		} else {
    			throw new IllegalArgumentException("niet verwacht character op positie "+start+" in "+new String(content));
    		}
    	}
		return start+1;
    }
    
	@Override
    public void beforeEach() {
    }

    public Long star1() {
    	long score=0;
    	int counter=0;
    	for(PacketPair pair:pairs) {
    		counter++;
    		if(compare(pair.left,pair.right)<0) {
    			logln("In orde: "+counter);
    			score+=counter;
    		}
    	}
    	return score;
    }

    private int compare(Packet p1, Packet p2) {
    	if(p1.value!=-1 && p2.value!=-1)
    		return Integer.compare(p1.value, p2.value);
    	List<Packet>pl1,pl2;
    	if(p1.value!=-1) {
    		pl1=new ArrayList<>();
    		pl1.add(new Packet(p1.value));
    	} else {
    		pl1=p1.subs;
    	}
    	if(p2.value!=-1) {
    		pl2=new ArrayList<>();
    		pl2.add(new Packet(p2.value));
    	} else {
    		pl2=p2.subs;
    	}
    	return compare(pl1,pl2);
    }
    
    private int compare(List<Packet> pl1, List<Packet> pl2) {
    	int i=0;
    	int cmp=0;
    	while(i<Math.min(pl1.size(), pl2.size())) {
    		if((cmp=compare(pl1.get(i),pl2.get(i)))!=0)
    			return cmp;
    		i++;
    	}
    	return Integer.compare(pl1.size(), pl2.size());
    }
    
    public Long star2() {
    	logln("Pairs:"+pairs.size());
    	List<Packet> packets=new ArrayList<>();
    	pairs.forEach(p->{packets.add(p.left); packets.add(p.right);});
    	logln("Packets: "+packets.size());
    	Packet p2=new Packet().addPacket(new Packet(2)).setSpecial();
    	Packet p6=new Packet().addPacket(new Packet(6)).setSpecial();
    	packets.add(p2);
    	packets.add(p6);
    	Collections.sort(packets,this::compare);
    	long prod=1;
    	for(int i=0;i<packets.size();i++) {
    		Packet p=packets.get(i);
    		log(""+(i+1)+" packet "+p);
    		if(p.special) {
    			prod*=i+1;
    			logln(" product:"+prod);
    		} else
    			logln("");
    	}
        return prod;

    }
    
    /**
     * een packet is een getal of een lijst van Packets
     * @author walter
     *
     */
    class Packet {
    	int value=-1;
    	List<Packet> subs=new ArrayList<>();
    	boolean special=false;
    	
    	public Packet() {}
    	
    	public Packet(int value) {
    		this.value=value;
    	}

    	public Packet setSpecial() {
    		special=true;
    		return this;
    	}
    	
    	public Packet addPacket(Packet psub) {
    		if(value!=-1)
    			log("FOUT: packet heeft al een value "+value);
    		subs.add(psub);
    		return this;
    	}
    	
    	public String toString() {
    		if(value!=-1)
    			return ""+value;
    		StringBuilder sb=new StringBuilder("[");
    		for(int ip=0;ip<subs.size();ip++) {
    			if(ip>0)
    				sb.append(",");
    			sb.append(subs.get(ip).toString());
    		}
    		//subs.stream().map(Packet::toString).collect(Collectors.joining("|"));
    		sb.append("]");
    		return sb.toString();
    	}
    }
    
    class PacketPair {
    	Packet left,right;
    	
		public PacketPair() {
		}

		public PacketPair(Packet left, Packet right) {
			this.left = left;
			this.right = right;
		}

		public void setLeft(Packet left) {
			this.left = left;
		}


		public Packet getLeft() {
			return left;
		}
		
		public void setRight(Packet right) {
			this.right = right;
		}


		public Packet getRight() {
			return right;
		}

		@Override
		public String toString() {
			return "[left=" + left + ", right=" + right + "]";
		}
		
    }
    
}