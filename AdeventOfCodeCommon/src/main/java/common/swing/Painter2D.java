package common.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Component om een 2D array te printen
 * @author walter
 *
 */
public class Painter2D extends JPanel {
	private static final long serialVersionUID = -1967778673799355922L;
	
	int dimX, dimY;
	// als dimensies klein zijn kan de berekende size buiten proportie geraken
	// maxSize bepaalt de maximale size een cel kan hebben.
	int maxSizeX=20,maxSizeY=20;
	// plaats tussen cellen
	int paddingX=1,paddingY=1;
	
	CelPainter painter;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		Dimension dim=this.getPreferredSize();
		dimX=painter.getArrayDimension().width;
		dimY=painter.getArrayDimension().height;
		int cellWidth=Math.min(maxSizeX, (dim.width-paddingX*(dimX-1))/dimX);
		int cellHeight=Math.min(maxSizeY, (dim.height-paddingY*(dimY-1))/dimY);
		setSize(dimX*(cellWidth+paddingX), dimY*(cellHeight+paddingY));
		int xcell0,ycell0;
		for (int rij=0;rij<dimY;rij++) {
			for (int kol=0;kol<dimX;kol++) {
				xcell0=(cellWidth+paddingX)*kol;
				if(painter.getCoordBase()==CoordinationBase.LeftTop) {
					ycell0=(cellHeight+paddingY)*rij;
				} else {
					ycell0=(cellHeight+paddingY)*(dimY-1-rij);
				}
				painter.paint(kol,rij,xcell0,ycell0,cellWidth,cellHeight,g2);
			}
		}
	}

	public void setPainter(CelPainter painter) {
		this.painter = painter;
	}
}
