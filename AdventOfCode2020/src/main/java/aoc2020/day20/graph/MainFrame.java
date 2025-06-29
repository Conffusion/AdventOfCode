package aoc2020.day20.graph;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import aoc2020.day20.Tile;

public class MainFrame {

	public static void createAndShowGui(Tile[][] tapijt) {

		JFrame frame = new JFrame("DrawChit");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(new TapijtPanel(tapijt));
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		

//		int timerDelay = 1000;
//		new Timer(timerDelay, new ActionListener() {
//			private int drawCount = 0;
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (drawCount >= myDrawables.size()) {
//					drawCount = 0;
//					drawChit.clearAll();
//				} else {
//					drawChit.addMyDrawable(myDrawables.get(drawCount));
//					drawCount++;
//				}
//			}
//		}).start();
	}

	public static void show(Tile[][] tapijt) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui(tapijt);
			}
		});
	}

}
