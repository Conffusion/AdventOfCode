package aoc2023.day22;

public class Model implements Cloneable{
	int maxX, maxY,maxZ;
	SteunBalk[][][] ruimte;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Model m=new Model();
		m.maxX=maxX;
		m.maxY=maxY;
		m.maxZ=maxZ;
		for(int x=0;x<maxX;x++)
			for(int y=0;y<maxY;y++)
				for(int z=0;z<maxZ;z++)
					m.ruimte[x][y][z]=ruimte[x][y][z];
		return m;
	}
	
}
