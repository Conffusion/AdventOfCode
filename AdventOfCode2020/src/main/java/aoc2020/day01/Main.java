package aoc2020.day01;

import java.util.ArrayList;
import java.util.List;

import aoc2020.common.MainMaster;

public class Main extends MainMaster {

	public List<Long> importData() throws Exception {
		return loadInput(1,"input.txt")
		.stream().mapToLong(Long::parseLong).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	public void solve(List<Long> input) {
		for (int i=0;i<input.size()-2;i++)
			for(int j=i+1;j<input.size()-1;j++)
				if(input.get(i)+input.get(j)<2020)
				{
					for (int k=j+1;k<input.size();k++)
						if(input.get(i)+input.get(j)+input.get(k)==2020)
							info(""+input.get(i)+" * "+input.get(j)+" * "+input.get(k)+"="+(input.get(i)*input.get(j)*input.get(k)));
	
				}
	}
	public static void main(String[] args) throws Exception {
		Main m=new Main();
		m.timer(()-> m.solve(m.importData()));
	}

}
