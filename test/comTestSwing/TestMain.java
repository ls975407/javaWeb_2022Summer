// package com.cloud.manga.henryTing.consts;
/*
javac -encoding utf8  ./TestMain.java
*/




import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import com.cloud.manga.henryTing.data.FileB;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.infor.FrameInfor;
import com.cloud.manga.henryTing.infor.LabelInfor;
import com.cloud.manga.henryTing.infor.ImageInfor;
import com.cloud.manga.henryTing.unit.FrameEnum;
import com.cloud.manga.henryTing.infor.CmdM;
import com.cloud.manga.henryTing.infor.GeoGet;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.GUIInterface;
import com.cloud.manga.henryTing.test.*;

import com.cloud.manga.henryTing.thread.TouchUnit;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;
import com.cloud.manga.henryTing.tool.GeoMethod;

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
import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;

public class TestMain extends JFrame implements GeoGet
{
	
	public static ImageInfor[] getImageInfor(ImageGet imageGet, GeoGet geoGet, int setX, int setY, FrameEnum frameEnum) {
		final List<ImageInfor> imageInfor = new ArrayList<>();
	
		final int SX = geoGet.screenX();
		final int SY = geoGet.screenY();
		final boolean needRotate = SX >= SY;
		final int sx = needRotate? SY: SX;
		final int sy = needRotate? SX: SY;
		final int imageSize = imageGet.size();
		// getScreenX() must be smaller than getScreenY()
		double ux = Math.min(setX, sx);
		double uy = Math.min(setY, sy);
		
		// System.out.println(String.format("uxy = (%f %f)", ux, uy));
		assert imageSize >= 0 && imageSize <= 2: "FrameInfor error imageSize >= 0 && imageSize <= 2";
		double[] scales, posis; int countRotate;
		for (int ith=0 ; ith<imageSize; ith++) {
			
			countRotate = needRotate? 1: 0;
			int x_len = imageGet.lenX(ith);
			int y_len = imageGet.lenY(ith);
			
			// System.out.println(String.format("lenxy = (%f %f)", x_len, y_len));
			
			if (x_len > y_len) {
				int t_int = y_len; y_len = x_len; x_len = t_int;
				countRotate++;
			}
			scales = GeoMethod.imageScale(ux, uy, x_len, y_len, frameEnum);
			posis = GeoMethod.imagePosi(sx, sy, scales[0], scales[1], frameEnum, ith);
			if (frameEnum == FrameEnum.dual) { countRotate++; }
			if (countRotate%2 == 0) {
				imageInfor.add(new ImageInfor(scales[0], scales[1], posis[0], posis[1], false));
			} else {
				imageInfor.add(new ImageInfor(scales[1], scales[0], posis[0], posis[1], true ));
			}
		}
		return imageInfor.toArray(new ImageInfor[0]);
	}
	
	// java TestFrameInfor 600 900 600 900 dual
	public static void main(String[] arg) {
		if (arg.length != 5) {
			System.out.println("Error: arg.length() != 5");
			for (int ith=0; ith<arg.length; ith++) {
				System.out.println(String.format("%d: %s", ith, arg[ith]));
			}
			return;
		}
		int screenX = Integer.parseInt(arg[0]);
		int screenY = Integer.parseInt(arg[1]);
		int setX = Integer.parseInt(arg[2]);
		int setY = Integer.parseInt(arg[3]);
		FrameEnum frameEnum = Enum.valueOf(
			FrameEnum.class, 
			arg[4]
		);
		
		TestMain frame = new TestMain();
		frame.initialize(screenX, screenY);

		String[] localPaths = new String[]{"0052_201_010.jpg", "0052_201_011.jpg"};
		String2ImageGet op = null;
		try {
			op = new String2ImageGet(localPaths);
		} catch (IOException e) {
			System.out.println(e.toString());
			return;
		}
		ImageGet imageGet = op._imageGet;
		FileBS fileBS = op._fileBS;
		ImageInfor[] infors = getImageInfor(imageGet, frame, setX, setY, frameEnum);

		// frame.updateScreen(panel);
		
		// panel.revalidate();
		// panel.repaint();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel label_0 = null, label_1 = null;
		switch(frameEnum) {
			// 0 1 2
			// 3 4 5
			// 6 7 8
			case single: {
				// c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 0;
				label_0 = new JImageM(infors[0], fileBS.get(0), 4);
				panel.add(label_0, c);
				panel.setBounds(0, 0, frame.screenX(), frame.screenY());
			}
			break;
			case dual: {
				// c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 0;
				label_0 = new JImageM(infors[0], fileBS.get(0), 4);
				panel.add(label_0, c);
				c.gridx = 0;
				c.gridy = 1;
				label_1 = new JImageM(infors[1], fileBS.get(1), 4);
				panel.add(label_1, c);
				panel.setBounds(0, 0, frame.screenX(), frame.screenY());
			}
			break;
			case updown: {
				// c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 0;
				label_0 = new JImageM(infors[0], fileBS.get(0), 7);
				panel.add(label_0, c);
				c.gridx = 0;
				c.gridy = 1;
				label_1 = new JImageM(infors[1], fileBS.get(1), 1);
				panel.add(label_1, c);
				panel.setBounds(0, -(int) (frame.screenY()/2), frame.screenX(), frame.screenY()*2);
			}
			break;
			case leftright: {
				// c.fill = GridBagConstraints.VERTICAL;
				c.gridx = 0;
				c.gridy = 0;
				label_0 = new JImageM(infors[0], fileBS.get(0), 3);
				panel.add(label_0, c);
				c.gridx = 1;
				c.gridy = 0;
				label_1 = new JImageM(infors[1], fileBS.get(1), 5);
				panel.add(label_1, c);
				panel.setBounds(-(int) (frame.screenX()/2)+10, 0, frame.screenX()*2, frame.screenY());
			}
			break;
			default:
			System.out.println(frameEnum);
			break;
		} 
		frame.updateScreen(panel);
	}
	
	
	// GetFName
	// public String getFName() {
		// return _frameName;
	// }
	// private final String _frameName;
	// TestMain (String frameName) {
		// _frameName = frameName;
	// }
	
	private JPanel _jPanel = null;
	
	public void initialize(int screenX, int screenY) {
		this.requestFocusInWindow();
		// _key2CmdM = new Key2CmdM02(this, this, this);
		_jPanel = new JPanel();
		_jPanel.setLayout(null);
		setContentPane(_jPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setBounds(0,0,screenX,screenY);
		setVisible(true);
	}
	public void updateScreen(JPanel panel) throws SDEBase {
		_jPanel.removeAll();
		_jPanel.add(panel);
		// setContentPane(panel);
		// panel.revalidate();
		// panel.repaint();
		updateScreen();
	}
	public void updateScreen() {
		_jPanel.revalidate();
		_jPanel.repaint();
	}
	// GeoGet
	public int screenX() {
		return _jPanel.getBounds().width;
	}
	public int screenY() {
		return _jPanel.getBounds().height;
	}
}