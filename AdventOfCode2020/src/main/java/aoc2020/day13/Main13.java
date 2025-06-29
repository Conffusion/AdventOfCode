package aoc2020.day13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import aoc2020.common.MainMaster;

public class Main13 extends MainMaster {

	int fromtime;
	List<Integer>busses;
	public void loadData1() {
		List<String>data=loadInput(13, "input.txt");
		fromtime=Integer.parseInt(data.get(0));
		busses=Arrays.asList(data.get(1).split(",")).stream().filter(s->!s.equals("x"))
		.mapToInt(s->Integer.parseInt(s)).boxed()
		.collect(Collectors.toList());
		System.out.println("vanaf "+fromtime+",bussen:"+busses);
	}
	
	public void star1() {
		int bestbus=0;
		int wait=fromtime;
		loadData1();
		for(int b:busses) {
			int firstbus=fromtime/b*b+b-fromtime;
			if (firstbus<wait)
			{
				wait=firstbus;
				bestbus=b;
			}
		}
		System.out.println("best bus:"+bestbus+", arrives at "+wait);
		System.out.println("***STAR1: "+wait*bestbus);
	}

	// ------------------------------------------
	// STAR 2
	class Bus {
		long nr;
		long pos;
		public Bus(long nr, long pos) {
			super();
			this.nr = nr;
			this.pos = pos;
		}		
	}
	public static long lcm(long number1, long number2) {
	    if (number1 == 0 || number2 == 0) {
	        return 0;
	    }
	    long absNumber1 = Math.abs(number1);
	    long absNumber2 = Math.abs(number2);
	    long absHigherNumber = Math.max(absNumber1, absNumber2);
	    long absLowerNumber = Math.min(absNumber1, absNumber2);
	    long lcm = absHigherNumber;
	    while (lcm % absLowerNumber != 0) {
	        lcm += absHigherNumber;
	    }
	    return lcm;
	}
	List<Bus> buslist=new ArrayList<>();
	long inc=0;
	long start=0;
	public void loadData2(String filename) {
		List<String>data=loadInput(13, filename);
		long pos=0;
		for(String b:Arrays.asList(data.get(1).split(","))) {
			if(!b.equals("x"))
			{
				long busnr=Long.parseLong(b);
				buslist.add(new Bus(busnr,pos));
//				if(start==0)
//					start=busnr-pos;
//				else
//					start=lcm((int)start,(int)(busnr-pos));
				inc+=busnr-pos;
			}pos++;
		}
		if(log)logln("inc="+inc);
	}

	class RangeTest implements Runnable {

		long start, end;
		
		public RangeTest(long start, long end) {
			super();
			this.start = start;
			this.end = end;
		}

		@Override
		public void run() {
			long loopcounter=0;
			if(log)logln("beginnen bij "+start);
			loop:
			while(start<end)
			{
				loopcounter++;
				if(loopcounter%100000==0)
					System.out.println("loop:"+loopcounter+", start:"+start);
				start+=buslist.get(0).nr;
				if(log)logln("check start:"+start);
				for(Bus b:buslist) {
					if((start+b.pos)%b.nr!=0)
					{
						if(log)logln("bus "+b.nr+" op pos "+b.pos+" klopt niet: "+((start+b.pos)%b.nr)+"!=0");
						continue loop;
					}
				}
				System.out.println("After loops:"+loopcounter);
				System.out.println("***STAR2:"+start);
				break loop;
			}			
		}
		
	}
	
	public void star2() {
		long loopcounter=0;
		loadData2("input.txt");
		// beginnen met hoogste waarde
		Bus bus=buslist.stream().max(Comparator.comparing(b->b.nr)).get();
		long start=bus.nr-bus.pos;
		inc=bus.nr;
		start=100000000000000L;
		start=start-bus.pos-(start%bus.nr);
		if(log)logln("beginnen bij "+start);
		loop:
		while(true)
		{
			loopcounter++;
			if(loopcounter%100000000L==0)
				System.out.println("loop:"+loopcounter+", start:"+start);
			start+=inc;
			if(log)logln("check start:"+start);
			for(Bus b:buslist) {
				if((start+b.pos)%b.nr!=0)
				{
					if(log)logln("bus "+b.nr+" op pos "+b.pos+" klopt niet: "+((start+b.pos)%b.nr)+"!=0");
					continue loop;
				}
			}
			break loop;
		}
		System.out.println("After loops:"+loopcounter);
		System.out.println("***STAR2:"+start);
	}
	
	public void star2KGV() {
		loadData2("testset1.txt");
		long loopcounter=0;
		int busit=0;
		start=buslist.get(0).nr;
		inc=start;
		System.out.println("bus "+buslist.get(0).nr+", start="+start+",inc="+inc);
		for(busit=1;busit<buslist.size();busit++)
		{
			Bus bus=buslist.get(busit);
			//inc=17;
			start+=bus.nr-bus.pos;
			inc*=bus.nr;
			System.out.println("bus "+bus.nr+", start="+start+",inc="+inc+", loopcounter="+loopcounter);
			while(true)
			{
				loopcounter++;
				if(loopcounter%100000000L==0)
					System.out.println("loop:"+loopcounter+", start:"+start);
				start+=inc;
				if(log)logln("check start:"+start);
				if((start+bus.pos)%bus.nr==0)
				{
					break;
				}
				if(log)logln("bus "+bus.nr+" op pos "+bus.pos+" klopt niet: "+((start+bus.pos)%bus.nr)+"!=0");
			}
		}
		System.out.println("oplossing:"+start+" in "+loopcounter+" iterations");
		
	}
	
	public void star2Threads () {
		loadData2("input.txt");
		ExecutorService pool = Executors.newFixedThreadPool(10);
		for(int i=1;i<=10;i++) {
			pool.execute(new RangeTest(100000000000000L*i,100000000000000L*(i+1)));
		}
		
	}
	
	public static void main(String[] args) {
		Main13 m=new Main13();
		m.log=false;
//		System.out.println("lcm(55,25)="+lcm(55,25));
//		m.star1();
//		m.star2KGV();
		m.star2();
//		m.timer(()->m.star2Threads());
	}

}
