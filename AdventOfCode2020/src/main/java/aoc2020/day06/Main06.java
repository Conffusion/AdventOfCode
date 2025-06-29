package aoc2020.day06;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc2020.common.MainMaster;
public class Main06 extends MainMaster {
	List<String> data;

	public Main06() {
		data=loadInput(6, "input.txt");
	}

	public void star1() throws Exception {
		Set<Character> currentSet=new HashSet<>();
		int total=0;
		for(String line:data)
		{
			if(line.length()==0)
			{
				total+=currentSet.size();
				currentSet.clear();
			}
			for(char c:line.toCharArray())
				currentSet.add(c);
		}
		total+=currentSet.size();
		info("star1:"+total);
	}
	
	@SuppressWarnings("unchecked")
	public void star2() throws Exception {
		HashSet<Character> abcSet=new HashSet<>();
		for (char c = 'a'; c <= 'z'; c++)
			abcSet.add(c);
		
		HashSet<Character> currentSet=(HashSet<Character>)abcSet.clone();
				
		int total=0;
		boolean processed=false;
		for(String line:data)
		{
			if(line.length()==0)
			{
				total+=currentSet.size();
				currentSet=(HashSet<Character>)abcSet.clone();
				processed=true;
			}else {
				Set<Character> lineset=new HashSet<>();
				for(char c:line.toCharArray())
					lineset.add(c);
				currentSet.retainAll(lineset);
				processed=false;
			}
		}
		// add last set
		if(!processed)
			total+=currentSet.size();
		info("star2:"+total);
	}
	
	public static void main(String[] args) throws Exception{
		Main06 m=new Main06();
		m.timer(()->m.star2());
	}
}