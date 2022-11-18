package com.cloud.manga.henryTing.test;
/*
javac -encoding utf8  com/cloud/manga/henryTing/test/JImageM.java
*/

import com.cloud.manga.henryTing.data.FileB;
import com.cloud.manga.henryTing.infor.ImageInfor;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;


public class JImageM extends JLabel {
	final Boolean _isRotate; 
	public JImageM(ImageInfor infor, FileB fileB) {
		// System.out.println("JImageM start");
		if (fileB == null) {
			_isRotate = null;
			return;
		}
		_isRotate = infor._isRotate;
		ImageIcon image = new ImageIcon(fileB._bytes);
        Image tmp = image.getImage();
		tmp = tmp.getScaledInstance(infor._lenX, infor._lenY, java.awt.Image.SCALE_SMOOTH); // scale it the
		setIcon(new ImageIcon(tmp));
		// if (_isRotate) {
			// setBounds(infor._posiY, infor._posiX, infor._lenY, infor._lenX);
		// } else {
		setBounds(infor._posiX, infor._posiY, infor._lenX, infor._lenY);

		
		// System.out.println(infor._posiX);
		// System.out.println(infor._posiY);
		// System.out.println(infor._lenX);
		// System.out.println(infor._lenY);
		
		// setBounds(-65, -15, 900, 900);
		// setBounds(0, 0, 450, 450);
	}
	// @Override
	// public void paintComponent(Graphics g) {
		// if (_isRotate) {
			// Graphics2D gx = (Graphics2D) g;
			// gx.rotate(Math.toRadians(90), getX() + getWidth()/2, getY() + getHeight()/2);
		// }
		// super.paintComponent(g);
	// }
}