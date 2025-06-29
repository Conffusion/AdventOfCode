package aoc2021.day17;

import org.apache.commons.lang3.tuple.Pair;

import common.graph.Point;
import common.graph.Rectangle;
import common.main.AbstractMainMaster;

/**
 * bereken de baan van een projectiel om te kijken of het binnen de target terecht zal komen
 * Tupple<Point,Point> wordt gebruikt om de huidige situatie van het projectiel te kennen:
 *   value1 = positie van projectiel
 *   value2 = snelheid van het projectiel (in x en y bepaald) 
 */
public class Main17 extends AbstractMainMaster<Integer> {

    public static void main(String[] args) {
        new Main17().start();
    }

    Rectangle target;
    int xHighest=0;
    Point best=null;
    int hitcount=0;

    @Override
    public void beforeAll() {
        target=new Rectangle(new Point(102,-90),new Point(157,-146));
        // star1 en star2 worden beide berekend bij een enkele uitvoering    
        starX();
    }

    // antwoord: 10585
    @Override
    public Integer star1() {
        return xHighest;
    }
    
    // antwoord: 5247
    @Override
    public Integer star2() {
        return hitcount;
    }

    private void starX() {
        for (int dx=0;dx<=target.getpBottom().x;dx++) {
            for(int dy=target.getpBottom().y;dy<1000;dy++) {
                evaluate(Pair.of(new Point(0,0),new Point(dx, dy)));
            }
         }
    }

    private void evaluate(Pair<Point,Point> p) {
        int high=p.getLeft().y;
        while(!target.isAbove(p.getLeft())&&!target.isLeftOf(p.getLeft())) {
            fire(p);
            if(p.getLeft().y>high) {
                high=p.getLeft().y;
                best=new Point(p.getRight().x,p.getRight().y);
            }                        
            if(target.contains(p.getLeft()))
            {
                // log("Velocity "+p.value2.x+","+p.value2.y+":");
                // logln("een oplossing:%s met hoogste punt op %d", p.value1,high);
                hitcount++;
                if(xHighest<high)
                    xHighest=high;
                return;
            }
        }
    }

    private void fire(Pair<Point, Point> curr) {
        curr.getLeft().x+=curr.getRight().x;
        curr.getLeft().y+=curr.getRight().y;
        curr.getRight().x=curr.getRight().x>0?curr.getRight().x-1:0;
        curr.getRight().y--;
    }
}
