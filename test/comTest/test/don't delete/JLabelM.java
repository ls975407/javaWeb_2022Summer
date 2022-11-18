package com.cloud.manga.henryTing.test;
/*
javac -encoding utf8  com/cloud/manga/henryTing/test/JLabelM.java
*/

import com.cloud.manga.henryTing.infor.LabelInfor;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

public class JLabelM extends JLabel {
	final boolean _isRotate; 
	public JLabelM(LabelInfor infor) {
		super(infor._content);
		
		// System.out.println("JLabelM");
		// System.out.println(infor._content);
		
		Font font = new Font(infor.getFontType(), Font.PLAIN, infor.getFontSize());
		setFont(font);
		FontMetrics metrics = new FontMetrics(font){};
		final Rectangle2D bounds = metrics.getStringBounds(infor._content, null);
		_isRotate = infor.needRotate();
		// if (_isRotate) {
			// setBounds(infor._posiY, infor._posiX, (int) bounds.getWidth(), (int) bounds.getHeight());
			// // setBounds(300, 450, (int) bounds.getWidth(), (int) bounds.getHeight());
		// } else {
			setBounds(infor._posiX, infor._posiY, (int) bounds.getWidth(), (int) bounds.getHeight());
			// setBounds(300, 450, (int) bounds.getWidth(), (int) bounds.getHeight());
		// }
		// System.out.println(infor._posiX);
		// System.out.println(infor._posiY);
		// System.out.println(bounds.getWidth());
		// System.out.println(bounds.getHeight());
	}
	// @Override
	// public void paintComponent(Graphics g) {
		// // if (_isRotate) {
			// Graphics2D gx = (Graphics2D) g;
			// gx.rotate(Math.toRadians(10), getX() + getWidth()/2, getY() + getHeight()/2);
		// // }
		
		// // if (_isRotate) {
			// // setBounds(infor._posiY, infor._posiX, (int) bounds.getWidth(), (int) bounds.getHeight());
			// // setBounds(300, 450, (int) bounds.getWidth(), (int) bounds.getHeight());
		// // } else {
			// // setBounds(posi_y, posi_x, (int) bounds.getHeight(), (int) bounds.getWidth());
			// // setBounds(300, 450, (int) bounds.getWidth(), (int) bounds.getHeight());
		// // }
		// super.paintComponent(g);
		// // setBounds(posi_y, posi_x, (int) bounds.getHeight(), (int) bounds.getWidth());
	// }
}