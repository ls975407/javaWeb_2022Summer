// package com.cloud.manga.henryTing.consts;
/*
javac -encoding utf8  ./TestJScreen.java
*/




import java.util.Iterator;
import java.io.File;
import java.io.IOException;

import com.cloud.manga.henryTing.data.FileB;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.FrameEnum;
import com.cloud.manga.henryTing.unit.Log;
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
import com.cloud.manga.henryTing.unit.Point; 

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
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import java.awt.geom.Rectangle2D;

import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

public class TestJScreen extends JFrame
	implements KeyListener, GUIInterface, GeoGet, MouseListener // , GetFName
{
	public final static Log _log = new Log("TestJScreen");
	public static void main(String[] args) {
		// if (args.length != 1) {
			// System.out.println("args.length != 1");
			// for (int ith=0; ith<args.length; ith++) {
				// System.out.println(String.format("%02d: %s", ith, args[ith]));
			// }
			// return;
		// }
		// new TestJScreen(args[0]).initialize();
		if (args.length == 0) {
			new TestJScreen().initialize(null);
		} else {
			new TestJScreen().initialize(args[0]);
		}
		
	}
	
	
	// GetFName
	// public String getFName() {
		// return _frameName;
	// }
	// private final String _frameName;
	// TestJScreen (String frameName) {
		// _frameName = frameName;
	// }
	
	private Key2CmdAbcE _key2CmdAbcE = null;
	private JPanel _jPanel = null;
	private int _labelSize = 0;
	private int _imageSize = 0;
	private int _title_height = 0;
	
	public void initialize(String localPath) {
		
		// _key2CmdAbcE = new Key2CmdAbcE(this, this, this);
		_jPanel = new JPanel();
		setContentPane(_jPanel);
		_jPanel.setLayout(null);
		_jPanel.addMouseListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setLocationRelativeTo(null);
		setLocation(1000, 50);
		getContentPane().setPreferredSize(new Dimension(675, 900));
		pack();
		
		_title_height = getSize().height - 900;
		
		// setBounds(0,0,600,900);
		_jPanel.requestFocusInWindow();
		_jPanel.addKeyListener(this);
		addWindowListener(new WindowAdapter() { 
			@Override 
			public void windowClosing( WindowEvent e ) {
				// _key2CmdAbcE._closeTheProgram();
				_key2CmdAbcE.closeTheProgram();
			}
		});
		setVisible(true);
		// _key2CmdAbcE = null;
		// while(_key2CmdAbcE == null) {
			// Scanner sc = new Scanner(System.in);
			// try {
		if (localPath == null) {
			_key2CmdAbcE = new Key2CmdAbcE(this, this);
		} else {
			_key2CmdAbcE = new Key2CmdAbcE(this, this, localPath);
		}
				// );
			// } catch (SDEBase e) {
				// System.out.println(e.toString());
				// sc.next();
			// }
		// }
	}
	
	// GUIInterface
	public String openAFile(String openPath, String[] appList) throws SDEBase {
		JFileChooser jfc = new JFileChooser(openPath);
		int returnValue = jfc.showOpenDialog(this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			return selectedFile.getAbsolutePath();
		}
		return null;
	}
	
	public String saveAFile(String openPath, String[] appList) throws SDEBase {
		JFileChooser fileChooser = new JFileChooser();
		// fileChooser.setDialogTitle("Specify a file to save");   
		int userSelection = fileChooser.showSaveDialog(this);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			return fileToSave.getAbsolutePath();
		}
		return null;
	}
	
	public void promptInformation(String infor) throws SDEBase {
		_log.w(infor);
	}
	// public ImageGet decodeImages(FileBS fileBS) throws SDEBase {
		// try {
			// _imageGet = new ImageGetAd(fileBS);
		// } catch (IOException e) {
			// throw new SDEError("decodeImages ImageGetAd fail");
		// }		
		// return _imageGet;
	// }
	public void closeTheProgram() {
		setVisible(false);
		dispose(); 
		System.out.println("close");
		System.exit(0);
	}
	
	JLabel[] _labels = new JLabel[]{ null, null, null};
	public void updateScreenLabel(FrameInfor frameInfor) throws SDEBase {
		int ith = -1;
		for (LabelInfor infor: frameInfor.getLabels()) {
			ith++;
			// System.out.println("132-----" + infor._content);
			JLabel label;
			if (_labels[ith] == null) {
				label = new JLabel();
			} else {
				label = _labels[ith];
			}
			label.setText(infor._content);
			label.setOpaque(false);
			
			Font font = new Font(infor.getFontType(), Font.PLAIN, infor.getFontSize());
			label.setFont(font);
			FontMetrics metrics = new FontMetrics(font){};
			Rectangle2D bounds = metrics.getStringBounds(infor._content, null);
			double[] posi = FrameInfor.positionLabel(screenX(), screenY(), (int) bounds.getWidth(), (int) bounds.getHeight(), infor._posiIndex);
			label.setBounds((int) posi[0], (int) posi[1], (int) bounds.getWidth(), (int) bounds.getHeight());
			label.setVisible(true);
			if (_labels[ith] == null) {
				_jPanel.add(label);
				_labels[ith] = label;
			}
		}
		ith++;
		for (; ith<3; ith++) {
			if (_labels[ith] != null) {
				_labels[ith].setVisible(false);
			}
		}
		
		
		// _jPanel.revalidate();
		// _jPanel.repaint();
		setVisible(true);
	}
	JLabel[] _images = new JLabel[]{ null, null};
	public void updateScreen(FrameInfor frameInfor) throws SDEBase {
		updateScreenLabel(frameInfor);
		final ImageInfor infor = frameInfor._imageInfor;
		int ith=-1;
		
		final int screenX = screenX(), screenY = screenY();
		for (FileB fileB: frameInfor.getBytes()) {
			ith++;
			if (ith>=2) { break;}
			
			ImageIcon image = new ImageIcon(fileB._bytes);
			Image tmp = image.getImage();
			
			int imageX = image.getIconWidth();
			int imageY = image.getIconHeight();
			
			double[] scales = FrameInfor.scaleValueImage(infor, imageX, imageY);
			int[] scaleInt = new int[] { (int) scales[0], (int) scales[1]};
			
			if (scaleInt[0] != imageX && scaleInt[1] != imageY) {
				tmp = tmp.getScaledInstance(scaleInt[0], scaleInt[1], java.awt.Image.SCALE_SMOOTH);
			}
			JLabel label;
			if (_images[ith] == null) {
				label = new JLabel();
			} else {
				label = _images[ith];
			}
			
			double[] posi;
			if (FrameInfor.isRotateImage(infor, imageX, imageY)) {
				posi = FrameInfor.positionImage(
					screenX, screenY, 
					scaleInt[1], scaleInt[0], 
					ith, frameInfor._frameType
				);
				label.setIcon(new RotatedIcon(new ImageIcon(tmp), RotatedIcon.Rotate.DOWN));
				label.setBounds((int) posi[0], (int) posi[1], scaleInt[1], scaleInt[0]);
			} else {
				posi = FrameInfor.positionImage(
					screenX, screenY, 
					scaleInt[0], scaleInt[1], 
					ith, frameInfor._frameType
				);
				label.setIcon(new ImageIcon(tmp));
				label.setBounds((int) posi[0], (int) posi[1], scaleInt[0], scaleInt[1]);
			}
			label.setVisible(true);
			if (_images[ith] == null) {
				_jPanel.add(label);
				_images[ith] = label;
			}
		}
		ith++;
		for (; ith<2; ith++) {
			if (_images[ith] != null) {
				_images[ith].setVisible(false);
			}
		}
		
		_jPanel.revalidate();
		_jPanel.repaint();
		setVisible(true);
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
		_key2CmdAbcE.sendAKey(e);
    }
	private Point p_enter = new Point(0,0), p_exist = new Point(0,0);
    public void mousePressed(MouseEvent e) {
        java.awt.Point point = e.getPoint();
		p_enter = new Point(point.x, point.y);
		_key2CmdAbcE.sendAPoint(p_enter);
    }
    public void mouseReleased(MouseEvent e) {
        java.awt.Point point = e.getPoint();
		p_exist = new Point(point.x, point.y);
		_key2CmdAbcE.sendASlide(p_enter, p_exist);
	}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
}