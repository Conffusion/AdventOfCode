package aoc2021.day03;

import java.util.ArrayList;
import java.util.List;

import common.main.AbstractMainMaster;

public class Main03 extends AbstractMainMaster<Long> {

	List<Long> lines=null;
	int linesize=12;
	
	@Override
	public void loadInput() {
		lines=parseInput(s->Long.parseLong(s, 2));
		if(testMode)
			linesize=5;
	}

	@Override
	public Long star1() {
		log("linesize:"+linesize);
		int[] tellers=new int[linesize];
		int linecounter=0;
		for(Long m:lines) {
			for(int i=0;i<linesize;i++)
				tellers[linesize-i-1]+=(m&(int)Math.pow(2, i))==Math.pow(2, i)?1:0;
			linecounter++;
		}
		long gamma=0;
		for (int i=0;i<linesize;i++) {
			gamma+=tellers[i]>linecounter/2?Math.pow(2, linesize-i-1):0;
		}
		long epsilon= (long)Math.pow(2, linesize)-1-gamma;
		return epsilon * gamma;
	}

	@Override
	public Long star2() {
		// oxygen
		final List<Long> oxygen=new ArrayList<>(lines);
		for (int i=0;i<linesize;i++) {
			List<Long> newremaining=splitter(oxygen,i,true,true);
			oxygen.clear();
			oxygen.addAll(newremaining);
			if(newremaining.size()==1)
				break;
		}
		// scrubber
		final List<Long> scrubber=new ArrayList<>(lines);
		for (int i=0;i<linesize;i++) {
			List<Long> newremaining=splitter(scrubber,i,false,false);
			scrubber.clear();
			scrubber.addAll(newremaining);
			if(newremaining.size()==1)
				break;
		}
		logln("oxygen:"+oxygen.get(0));
		logln("scrubber:"+scrubber.get(0));
		return oxygen.get(0)*scrubber.get(0);
	}
	
	/**
	 * 
	 */
	private List<Long> splitter(List<Long> list,int index,boolean returnMax,boolean onEqual1) {
		long macht=(long)Math.pow(2, linesize-1-index);
		List<Long> with0=new ArrayList<>();
		List<Long> with1=new ArrayList<>();
		list.stream()
				.forEach(l->{
					if ((l&macht)==macht) 
						with1.add(l);
					 else 
						with0.add(l);
					});
		if(with1.size()==with0.size())
			return onEqual1?with1:with0;
		if(with1.size()>with0.size())
			return returnMax?with1:with0;
		else
			return returnMax?with0:with1;
	}
	
	public static void main(String[] args) {
		new Main03()
		.testMode()
		.start();
	}

}
