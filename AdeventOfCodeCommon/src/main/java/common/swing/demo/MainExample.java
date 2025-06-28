package common.swing.demo;

import java.awt.Dimension;
import java.awt.Graphics2D;

import common.swing.CelPainter;
import common.swing.CoordinationBase;
import common.swing.MainMasterGUI;

public class MainExample extends MainMasterGUI<Long> implements CelPainter{

	public static void main(String[] args) {
    	new MainExample().launch();
	}

	@Override
	public Long star1() {
		logln("Star 1 STARTED");
		refreshGUI();
		try {
			Thread.sleep(1000L);
			refreshGUI();
		} catch (InterruptedException e) {
			logln(e.toString());
		}
		logln("Star 1 Finished");
		return null;
	}

	@Override
	public Long star2() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void paint(int row, int col, int x0, int y0, int width, int height, Graphics2D graph) {
		graph.drawRect(x0+2, y0+2, width-4, height-4);		
	}

	@Override
	public Dimension getArrayDimension() {
		return new Dimension(20,10);
	}

	@Override
	public CoordinationBase getCoordBase() {
		return CoordinationBase.LeftTop;
	}
}
