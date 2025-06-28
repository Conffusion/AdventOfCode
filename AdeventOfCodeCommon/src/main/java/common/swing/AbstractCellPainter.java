package common.swing;

/**
 * Extend from this class to use CelPainter so it remains compatible.
 * @author walter
 *
 */
public abstract class AbstractCellPainter implements CelPainter {

	@Override
	public CoordinationBase getCoordBase() {
		return CoordinationBase.LeftTop;
	}
}
