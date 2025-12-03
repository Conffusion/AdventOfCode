package common.graph;

/**
 * Rechthoek in een 2D ruimte.
 * Voor de methodes die de plaats van de rechthoek bepalen t.o.v. een punt, is (0,0) links bovenaan.
 */
public class Rectangle {
    // pTop: upper left corner of the rectangle
    // pBottom: lower right corner of the rectangle
    private Point pTop, pBottom;

    public Rectangle(Point pTop,Point pBottom) {
        this.pTop=pTop;
        this.pBottom=pBottom;
    }
    
    public Point getpTop() {
        return pTop;
    }

    public Point getpBottom() {
        return pBottom;
    }

    public boolean contains(Point p) {
        return pTop.x<=p.x && p.x<=pBottom.x
            && pTop.y>=p.y && p.y >=pBottom.y;
    }
    /**
     * Geeft true terug wanneer de rechthoek boven het gegeven punt is
     * @param p
     * @return
     */
    public boolean isAbove(Point p) {
        return pBottom.y>p.y;
    }

    public boolean isBelow(Point p) {
        return pBottom.y<p.y;
    }
    public boolean isLeftOf(Point p) {
        return pBottom.x<p.x;
    }

    public boolean isRightOf(Point p) {
        return pBottom.x>p.x;
    }
}
