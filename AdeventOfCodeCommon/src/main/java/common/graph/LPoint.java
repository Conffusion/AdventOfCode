package common.graph;

/**
 * Point met long waarden
 */
public class LPoint {
    public long x, y;

    public LPoint(long x, long y) {
        this.x=x;
        this.y=y;
    }
    
    public LPoint(LPoint toclone) {
        this.x=toclone.x;
        this.y=toclone.y;
    }

    public String toString() {
        return "("+x+","+y+")";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (x ^ (x >>> 32));
		result = prime * result + (int) (y ^ (y >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LPoint other = (LPoint) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
