package common.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import common.main.AbstractMainMaster;

public abstract class MainMasterGUI<T> extends AbstractMainMaster<T> implements ActionListener {
	JFrame frame;
	JCheckBox chkTestMode=new JCheckBox("Test mode");
	JCheckBox chkNoLog=new JCheckBox("No log");
	JSplitPane mainSection;
	JScrollPane paintSection;
	Painter2D mainCanvas;
	JTextArea logArea;
	CelPainter celPainter=null;
	// time to wait in every refreshGUI call (in ms)
	long frameSleeptime=0;
	boolean noGUI=false;
	
	public MainMasterGUI() {
	}
	
	//  +-----------+------------+--------------+-----------+
	//  | [Start 1] | [ Start 2] |  O testMode  |  O nolog  |
	//  +-----------+------------+--------------+-----------+
	//  |                           |                       |
	//  |    (paintSection)         |   (logsection)        |
	//  |                           |                       |
	//  +---------------------------------------------------+
	protected void buildAndShow() {
		JPanel contentPane = new JPanel();
		GridBagLayout gblayout=new GridBagLayout();
		contentPane.setLayout(gblayout);
		JButton btnStart1=new JButton("Start 1");
		btnStart1.addActionListener(this);
		btnStart1.setActionCommand("start1");
		contentPane.add(btnStart1,gbSingleBuilder(0,0));
		JButton btnStart2=new JButton("Start 2");
		btnStart2.addActionListener(this);
		btnStart2.setActionCommand("start2");
		contentPane.add(btnStart2,gbSingleBuilder(1,0));
		chkTestMode.setActionCommand("testMode");
		chkTestMode.addActionListener(this);
		contentPane.add(chkTestMode,gbSingleBuilder(2,0));
		chkNoLog.setActionCommand("nolog");
		chkNoLog.addActionListener(this);
		contentPane.add(chkNoLog,gbSingleBuilder(3,0));
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		Dimension framedim=new Dimension(toolkit.getScreenSize().width*4/5,toolkit.getScreenSize().height*4/5);
		// body section
		paintSection=new JScrollPane();
		mainSection=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,paintSection,new JScrollPane(getLogPanel()));
		mainSection.setPreferredSize(new Dimension(framedim.width/2,framedim.height));
		GridBagConstraints gb=gbSingleBuilder(0, 2);
		gb.gridwidth=4;
		gb.fill=GridBagConstraints.BOTH;
		contentPane.add(mainSection,gb);
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);		
	}
	
	public MainMasterGUI<T> disableGUI() {
		this.noGUI=true;
		return this;
	}
	public MainMasterGUI<T> testMode() {
		return testMode(true);
	}
	
	@Override
	public MainMasterGUI<T> testMode(boolean flag) {
		testMode=flag;
		return this;
	}
	
	private JTextArea getLogPanel() {
		if(logArea==null) {
			logArea=new JTextArea(10,80);
			logArea.setPreferredSize(new Dimension(800,400));
			System.setOut(new PrintStream(new CustomOutputStream(logArea)));
			System.setErr(new PrintStream(new CustomOutputStream(logArea)));
		}
		return logArea;
	}
		
	private GridBagConstraints gbSingleBuilder(int gridx, int gridy) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=gridx;
		c.gridy=gridy;
		c.gridwidth=1;
		c.gridheight=1;
		return c;
	}
	
	public void launch() {
		if(noGUI) {
			start();
		} else {
			SwingUtilities.invokeLater(()->{
				frame=new JFrame("AdventOfCode-"+this.getClass().getPackageName()+"."+this.getClass().getSimpleName());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationByPlatform(true);
			});
			if(celPainter==null && this instanceof CelPainter) {
				setCelPainter((CelPainter)this);
				log("CelPainer instance set to this");
			}
			SwingUtilities.invokeLater(this::buildAndShow);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if("start1".equals(event.getActionCommand())) {
			start1();
		}
		if("start2".equals(event.getActionCommand())) {
			start2();
		}
		if("nolog".equals(event.getActionCommand())) {
			testMode(((JCheckBox) event.getSource()).isSelected());
		}
		if("testMode".equals(event.getActionCommand())) {
			testMode(((JCheckBox) event.getSource()).isSelected());
		}
	}

	public void setCelPainter(CelPainter cp) {
		this.celPainter=cp;
	}
	
	public void setFrameSleeptime(long frameSleeptime) {
		this.frameSleeptime = frameSleeptime;
	}

	protected void refreshGUI() {
		if(noGUI)
			return;
		if(celPainter==null)
			throw new IllegalCallerException("You can't call refreshGUI without setting a celPainter implementation.");
		if(mainCanvas==null) {
			mainCanvas=new Painter2D();
			mainCanvas.setPainter(celPainter);
			mainCanvas.setPreferredSize(new Dimension(800,600));
			paintSection.getViewport().removeAll();
			paintSection.getViewport().add(mainCanvas);			
			frame.pack();
		}
		SwingUtilities.invokeLater(mainCanvas::repaint);
		if(frameSleeptime>0)
			try {
				Thread.sleep(frameSleeptime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}
