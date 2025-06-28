package common.swing;

import java.awt.Dimension;
import java.awt.Graphics2D;

public interface CelPainter {

	/**
	 * Implementation must return the dimension of the array to paint.
	 * this is used to calculate the size and position of each cell on the Graphics.
	 * @return
	 */
	public Dimension getArrayDimension();
	/**
	 * Where is the (0,0) located ?
	 * @return
	 */
	public CoordinationBase getCoordBase();
	
	/**
	 * Wordt aangeroepen wanneer een vorm moet getekend worden
	 * (x0pos,y0pos) is de linker bovenhoek van de cell en (x0pos+width, y0pos+height) de rechter benedenhoek
	 * @param x x-index in de 2 dim array
	 * @param y y-index in de 2 dim array
	 * @param x0pos x-positie waar de cell moet getekend worden 
	 * @param xYpos y-positie waar de cell moet getekend worden
	 * @param width breedte van de cell in pixels
	 * @param height hoogte van de cell in pixels
	 * @param graph
	 */
	public void paint(int row, int col, int x0, int y0, int width, int height,Graphics2D graph);
}
