// package com.cloud.manga.henryTing.consts;
/*
javac -encoding utf8  ./TestScreen01.java
*/




import java.util.Iterator;
import java.io.File;
import java.io.IOException;

import com.cloud.manga.henryTing.data.FileB;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.infor.FrameInfor;
import com.cloud.manga.henryTing.infor.LabelInfor;
import com.cloud.manga.henryTing.infor.ImageInfor;
import com.cloud.manga.henryTing.infor.CmdM;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.GUIInterface;
import com.cloud.manga.henryTing.test.*;

import com.cloud.manga.henryTing.thread.TouchUnit;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;

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

public class TestScreen01 extends JFrame
	implements KeyListener, GetFName, GUIInterface
{
	public static Setting initailizeSetting() {
		final String path = "IniSetting_ver01.json";
		String content = null;
		try {
			content = FileM.readString(path);
		} catch (IOException e) {
			println(e.toString()); return null;
		}
		JSONObject json = null;
		SettingAd settingAd = new SettingAd(".", ".");
		try {
			json = new JSONObject(content);
			settingAd.parseJson(json);
		} catch (JSONException e) {
			println(e.toString()); return null;
		}
		return settingAd;
	}
	public static void println(String content) {
		System.out.println(content);
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("args.length != 1");
			for (int ith=0; ith<args.length; ith++) {
				System.out.println(String.format("%02d: %s", ith, args[ith]));
			}
			return;
		}
		Setting setting = initailizeSetting();
		if (setting == null) {
			System.out.println("fail to initailizeSetting");
			return;
		}
		com.cloud.manga.henryTing.thread.Thread.initialize(setting);
		new TestScreen01(args[0]).initialize();
	}
	
	
	// GetFName
	public String getFName() {
		return _frameName;
	}
	
	private final String _frameName;
	TestScreen01 (String frameName) {
		_frameName = frameName;
	}
	
	private Key2CmdM _key2CmdM = null;
	private JPanel _jPanel = null;
	private JLabel[] _labelTexts = null;
	private JLabel[] _labelImages = null;
	private int _labelSize = 0;
	private int _imageSize = 0;
	private volatile ImageGet _imageGet = null;
	
	public void initialize() {
        this.addKeyListener(this);
		this.requestFocusInWindow();
		_key2CmdM = new Key2CmdM(this, this);
		_jPanel = new JPanel();
		// labels
		_labelTexts = new JLabel[9];
		for (int ith=0; ith<9; ith++) {
			_labelTexts[ith] = null;
		}
		_labelImages = new JLabel[2];
		for (int ith=0; ith<2; ith++) {
			_labelImages[ith] = null;
		}
		setContentPane(_jPanel);
		_jPanel.setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setPreferredSize(new Dimension(400, 300));
		addWindowListener(new WindowAdapter() { 
			@Override 
			public void windowClosing( WindowEvent e ) {
				_key2CmdM._closeTheProgram();
			}
		});
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
	
	public void refreshScreen() {
		_jPanel.removeAll();
		for (int ith=0; ith<_labelSize; ith++) {
			_jPanel.add(_labelTexts[ith]);
		}
		for (int ith=0; ith<_imageSize; ith++) {
			_jPanel.add(_labelImages[ith]);
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
		refreshScreen();
	}
	
	// GeoGet
	public int screenX() {
		return getBounds().width;
	}
	public int screenY() {
		return getBounds().height;
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
			_key2CmdM._closeTheProgram();
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
				// System.out.print("level = ");
				// System.out.println(_key2CmdM.getLevel());
				_key2CmdM._touchDelayer.sendATouch(new TouchUnit<CmdM>(cmdM, _key2CmdM.getLevel()));
				return;
			}
		} catch (SDEBase e1) {
			println(e1.toString()); return;
		}
		_key2CmdM.sendASkip();
		// System.out.print("level = ");
		// System.out.println(_key2CmdM.getLevel());

		_key2CmdM.runActionM(cmdM.getActionM(1));
    }
}