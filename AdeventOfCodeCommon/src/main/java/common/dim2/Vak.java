package common.dim2;

import java.util.Objects;

import common.graph.Point;

/**
 * Vak bevat zijn positie binnen het terrein
 * Extend deze class om extra data over het vak te kunnen bijhouden.
 */
public class Vak {
	/**
	 * Positie van dit vak in het terrein
	 */
	public Point point;

	
	public Vak(Point point) {
		this.point = point;
	}

	@Override
	public int hashCode() {
		return Objects.hash(point);
	}
	
	/**
	 * Twee vakken zijn gelijk als ze dezelfde point hebben
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vak other = (Vak) obj;
		return Objects.equals(point, other.point);
	}
	
}
