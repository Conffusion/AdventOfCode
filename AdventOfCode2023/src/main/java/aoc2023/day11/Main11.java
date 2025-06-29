package aoc2023.day11;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import common.dim2.DimensionUtils;
import common.graph.Point;
import common.main.AbstractMainMaster;

public class Main11 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main11()
            //.testMode()
            //.withFileOutput()
            //.nolog()
           .start();
    }
    Dimension testDim=new Dimension(10,10);
    Dimension realDim=new Dimension(140,140);
    List<Point> galaxies;
    Dimension dim;
    int[] xDistance, yDistance;
    char[][] image;
    
    @Override
    public void beforeEach() {
    	if(testMode)
    		dim=testDim;
    	else
    		dim=realDim;
    	galaxies=new ArrayList<>();
    	xDistance=new int[dim.width];
    	yDistance=new int[dim.height];
    	Arrays.fill(xDistance, 1);
    	Arrays.fill(yDistance, 1);
    	image=loadInputInto2DArrayXY(dim.width, dim.height);
    }

    // berekent de juiste positie van galaxy p rekening houdend met xDistance en yDistance
    private Point shiftGalaxy(Point p) {
    	int newX=0, newY=0;
    	for (int x=0;x<p.x;x++)
    		newX+=xDistance[x];
    	for (int y=0;y<p.y;y++)
    		newY+=yDistance[y];
    	return new Point(newX,newY);
    }
    
    // antwoord : 10077850
    public Long star1() {
    	// kolommen zonder # tellen als breedte 2
    	for(int x=0;x<dim.width;x++) {
    		if (!DimensionUtils.testArray(image[x], (c,i)->c=='#', Boolean.TRUE))
    			xDistance[x]=2;
    	}
    	// rijen zonder # tellen als breedte 2
    	for(int y=0;y<dim.height;y++) {
    		if (!DimensionUtils.testArrayRow(image,y, (c,i)->c=='#', Boolean.TRUE))
    			yDistance[y]=2;
    	}
    	// registreer alle galaxies
    	DimensionUtils.walk(image, (c,p)->{if(Objects.equals(c,'#'))galaxies.add(shiftGalaxy(p));});
    	long afstand=0L;
    	for (int iFrom=0;iFrom<galaxies.size()-1;iFrom++)
    		for (int iTo=iFrom+1;iTo<galaxies.size();iTo++)
    			afstand+=Math.abs(galaxies.get(iFrom).x-galaxies.get(iTo).x)+Math.abs(galaxies.get(iFrom).y-galaxies.get(iTo).y);
        return afstand;
    }

    // antwoord : 504715068438
    public Long star2() {
    	// kolommen zonder # tellen als breedte 1000000
    	for(int x=0;x<dim.width;x++) {
    		if (!DimensionUtils.testArray(image[x], (c,i)->c=='#', Boolean.TRUE))
    			xDistance[x]=1000000;
    	}
    	// rijen zonder # tellen als breedte 1000000
    	for(int y=0;y<dim.height;y++) {
    		if (!DimensionUtils.testArrayRow(image,y, (c,i)->c=='#', Boolean.TRUE))
    			yDistance[y]=1000000;
    	}
    	// registreer alle galaxies
    	DimensionUtils.walk(image, (c,p)->{if(Objects.equals(c,'#'))galaxies.add(shiftGalaxy(p));});
    	long afstand=0L;
    	for (int iFrom=0;iFrom<galaxies.size()-1;iFrom++)
    		for (int iTo=iFrom+1;iTo<galaxies.size();iTo++)
    			afstand+=Math.abs(galaxies.get(iFrom).x-galaxies.get(iTo).x)+Math.abs(galaxies.get(iFrom).y-galaxies.get(iTo).y);
        return afstand;
    }
}