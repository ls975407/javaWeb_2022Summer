// package com.cloud.manga.henryTing.consts;
/*
javac -encoding utf8  ./TestScreenMain.java
*/




import java.util.Iterator;
import java.io.File;
import java.io.IOException;

import com.cloud.manga.henryTing.data.FileB;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.FrameEnum;
import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.infor.FrameInfor;
import com.cloud.manga.henryTing.infor.LabelInfor;
import com.cloud.manga.henryTing.infor.ImageInfor;
import com.cloud.manga.henryTing.infor.CmdM;
import com.cloud.manga.henryTing.infor.GeoGet;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.GUIInterface;
import com.cloud.manga.henryTing.test.*;

import com.cloud.manga.henryTing.thread.TouchUnit;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JFileChooser;

import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

public class TestScreenMain extends JFrame
	implements KeyListener, GUIInterface, GeoGet // , GetFName
{
	public static void main(String[] args) {
		// if (args.length != 1) {
			// System.out.println("args.length != 1");
			// for (int ith=0; ith<args.length; ith++) {
				// System.out.println(String.format("%02d: %s", ith, args[ith]));
			// }
			// return;
		// }
		// new TestScreenMain(args[0]).initialize();
		new TestScreenMain().initialize();
	}
	
	
	// GetFName
	// public String getFName() {
		// return _frameName;
	// }
	// private final String _frameName;
	// TestScreenMain (String frameName) {
		// _frameName = frameName;
	// }
	
	private Key2CmdM02 _key2CmdM = null;
	private JPanel _jPanel = null;
	private JLabel[] _labelTexts = null;
	private JImageM[] _labelImages = null;
	private int _labelSize = 0;
	private int _imageSize = 0;
	private volatile ImageGet _imageGet = null;
	
	public void initialize() {
        this.addKeyListener(this);
		this.requestFocusInWindow();
		// _key2CmdM = new Key2CmdM02(this, this, this);
		_jPanel = new JPanel();
		// labels
		_labelTexts = new JLabel[9];
		for (int ith=0; ith<9; ith++) {
			_labelTexts[ith] = null;
		}
		_labelImages = new JImageM[2];
		for (int ith=0; ith<2; ith++) {
			_labelImages[ith] = null;
		}
		setContentPane(_jPanel);
		_jPanel.setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setPreferredSize(new Dimension(400, 300));
		setBounds(0,0,600,900);
		addWindowListener(new WindowAdapter() { 
			@Override 
			public void windowClosing( WindowEvent e ) {
				// _key2CmdM._closeTheProgram();
				_key2CmdM.closeTheProgram();
			}
		});
		setVisible(true);
		_key2CmdM = null;
		while(_key2CmdM == null) {
			Scanner sc = new Scanner(System.in);
			try {
				_key2CmdM = new Key2CmdM02(this, this);
			} catch (SDEBase e) {
				System.out.println(e.toString());
				sc.next();
			}
		}
	}
	
	// GUIInterface
	public String openAFile(String openPath, String[] appList) throws SDEBase {
		JFileChooser jfc = new JFileChooser(openPath);
		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			return selectedFile.getAbsolutePath();
		}
		return null;
	}
	
	public ImageGet decodeImages(FileBS fileBS) throws SDEBase {
		try {
			_imageGet = new ImageGetAd(fileBS);
		} catch (IOException e) {
			throw new SDEError("decodeImages ImageGetAd fail");
		}		
		return _imageGet;
	}
	public void closeTheProgram() {
		setVisible(false);
		dispose(); 
		System.out.println("close");
		System.exit(0);
	}
	
	public void refreshScreen(FrameEnum frameType) {
		_jPanel.removeAll();

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JImageM label_0 = _labelImages[0], label_1 = _labelImages[1];
		switch(frameType) {
			// 0 1 2
			// 3 4 5
			// 6 7 8
			case single: {
				// c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 0;
				// label_0 = new JImageM(infors[0], fileBS.get(0), 4);
				label_0.setAlignment(4);
				panel.add(label_0, c);
				panel.setBounds(0, 0, screenX(), screenY());
			}
			break;
			case dual: {
				// c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 0;
				// label_0 = new JImageM(infors[0], fileBS.get(0), 4);
				label_0.setAlignment(4);
				panel.add(label_0, c);
				c.gridx = 0;
				c.gridy = 1;
				// label_1 = new JImageM(infors[1], fileBS.get(1), 4);
				label_1.setAlignment(4);
				panel.add(label_1, c);
				panel.setBounds(0, 0, screenX(), screenY());
			}
			break;
			case updown: {
				// c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 0;
				// label_0 = new JImageM(infors[0], fileBS.get(0), 7);
				label_0.setAlignment(7);
				panel.add(label_0, c);
				c.gridx = 0;
				c.gridy = 1;
				// label_1 = new JImageM(infors[1], fileBS.get(1), 1);
				label_1.setAlignment(1);
				panel.add(label_1, c);
				panel.setBounds(0, -(int) (screenY()/2), screenX(), screenY()*2);
			}
			break;
			case leftright: {
				// c.fill = GridBagConstraints.VERTICAL;
				c.gridx = 0;
				c.gridy = 0;
				// label_0 = new JImageM(infors[0], fileBS.get(0), 3);
				label_0.setAlignment(3);
				panel.add(label_0, c);
				c.gridx = 1;
				c.gridy = 0;
				// label_1 = new JImageM(infors[1], fileBS.get(1), 5);
				label_1.setAlignment(5);
				panel.add(label_1, c);
				panel.setBounds(-(int) (screenX()/2)+10, 0, screenX()*2, screenY());
			}
			break;
			default:
			System.out.println(frameType);
			break;
		}
		// for (int ith=0; ith<_imageSize; ith++) {
			// _jPanel.add(_labelImages[ith]);
		// }
		_jPanel.add(panel);
		for (int ith=0; ith<_labelSize; ith++) {
			_jPanel.add(_labelTexts[ith]);
		}
		_jPanel.revalidate();
		_jPanel.repaint();
		setVisible(true);
	}
	
	public void updateScreen(FrameInfor frameInfor) throws SDEBase {
		int ith=0;
		for (LabelInfor labelInfor: frameInfor.getLabels()) {
			_labelTexts[ith++] = new JLabelM(labelInfor);	
		}
		_labelSize = ith;
		ith=0;
		Iterator<FileB> iter = frameInfor.getBytes().iterator();
		for (ImageInfor imageInfor: frameInfor.getImages()) {
			if (!iter.hasNext()) {
				System.out.println("error: FileB !iter.hasNext()"); break;
			}
			_labelImages[ith++] = new JImageM(imageInfor, iter.next());	
		}
		_imageSize = ith;
		refreshScreen(frameInfor._frameType);
	}
	
	
	// GeoGet
	public int screenX() {
		return _jPanel.getBounds().width;
	}
	public int screenY() {
		return _jPanel.getBounds().height;
	}

	// KeyListener
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode());
		switch (e.getKeyCode()) {
			case KeyEvent.VK_F5:
			System.out.println("f5 refresh");
			if (_imageGet != null) {
				_key2CmdM._updateScreen(_imageGet);
			}
			return;
			case KeyEvent.VK_ESCAPE:
			System.out.println("esc exist");
			// _key2CmdM._closeTheProgram();
			_key2CmdM.closeTheProgram();
			return;
			default:
			break;
		}
		CmdM cmdM = null;
		try {
			cmdM = _key2CmdM.parseClickCmdMs(e);
			if (cmdM == null) {
				cmdM = _key2CmdM.parseCountCmdMs(e);
				if (cmdM == null) {
					System.out.println(String.format("unknown %c %03d", e.getKeyChar(), e.getKeyCode()));
					return;
				}
				// _key2CmdM._touchDelayer.sendATouch(new TouchUnit<CmdM>(cmdM, _key2CmdM.getLevel()));
				_key2CmdM.sendATouch(new TouchUnit<CmdM>(cmdM, _key2CmdM.getLevel()));
				return;
			}
		} catch (SDEBase e1) {
			System.out.println(e1.toString()); return;
		}
		_key2CmdM.sendASkip();

		_key2CmdM.runActionM(cmdM.getActionM(1));
    }
}