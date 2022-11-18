package com.cloud.manga.henryTing.test;
/*
javac -encoding utf8  com/cloud/manga/henryTing/test/JImageM.java
*/

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Dimension;

import com.cloud.manga.henryTing.data.FileB;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.infor.ImageInfor;

public class JImageM extends JLabel {
	
	private void _ini(ImageInfor infor, FileB fileB) {
		ImageIcon image = new ImageIcon(fileB._bytes);
        Image tmp = image.getImage();
		tmp = tmp.getScaledInstance(infor._lenX, infor._lenY, java.awt.Image.SCALE_SMOOTH); // scale it the
		
		if (infor._isRotate) {
			setIcon(new RotatedIcon(new ImageIcon(tmp)));
		} else {
			setIcon(new ImageIcon(tmp));
		}
	}
	public JImageM(ImageInfor infor, FileB fileB, int direct) {
		assert direct>=0 && direct<9: "direct not in range";
		_ini(infor, fileB);
		setAlignment(direct);
	}
	public JImageM(ImageInfor infor, FileB fileB) {
		_ini(infor, fileB);
	}
	public void setAlignment(int direct) {
		// 0 1 2
		// 3 4 5
		// 6 7 8
		
		// System.out.println(_isRotate?"true": "false");
		switch(direct) {
			case 0: case 1: case 2:
			setVerticalAlignment(TOP);
			break;
			case 3: case 4: case 5:
			setVerticalAlignment(CENTER);
			break;
			case 6: case 7: case 8:
			setVerticalAlignment(BOTTOM);
			break;
			default:
			System.out.print("direct not in range"); break;
		}
		switch(direct) {
			case 0: case 3: case 6:
			setHorizontalAlignment(LEFT);
			break;
			case 1: case 4: case 7:
			setHorizontalAlignment(CENTER);
			break;
			case 2: case 5: case 8:
			setHorizontalAlignment(RIGHT);
			break;
			default:
			System.out.print("direct not in range"); break;
		}
		// System.out.println(_isRotate?"true": "false");
	}	
	// @Override
	// public Dimension getPreferredSize() {
		// Dimension dim = super.getPreferredSize();
		// if (_isRotate) {
			// return new Dimension(dim.height, dim.width);
		// } else {
			// return dim;
		// }
	// }
	
	// @Override
	// public void paintComponent(Graphics g) {
		// if (_isRotate) {
			// Graphics2D gx = (Graphics2D) g;
			// gx.rotate(Math.toRadians(90), getX() + getWidth()/2, getY() + getHeight()/2);
			// // gx.rotate(Math.toRadians(90), getX(), getY() );
			// System.out.println(getWidth());
			// System.out.println(getHeight());
		// }
		
		// super.paintComponent(g);
	// }
}