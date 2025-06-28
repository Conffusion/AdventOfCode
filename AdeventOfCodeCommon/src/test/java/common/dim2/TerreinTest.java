package common.dim2;

import common.graph.Direction;
import common.graph.Point;

public class TerreinTest {

	static class Elem {
		Point point;
		int order;
	}
	public static void main(String[] args) throws Exception {
		Terrein<Elem> ter=new Terrein<Elem>(5,4,Elem.class,"point");
		for(int x=0;x<5;x++)
			for(int y=0;y<4;y++)
				ter.setField(new Point(x,y), new Elem());
		ter.walker().withStart(0, 0)
				.withFirstDirection(Direction.RIGHT)
				.withSecondDirection(Direction.DOWN)
				.toStream()
				.filter(elem->elem.point.x==elem.point.y)
				.forEach(elem->elem.order=1);
		System.out.println(ter);
	}

}
