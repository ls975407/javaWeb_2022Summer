package com.cloud.manga.henryTing.test;
/*
javac -encoding utf8  com/cloud/manga/henryTing/test/JLabelM.java
*/

import com.cloud.manga.henryTing.infor.LabelInfor;
import com.cloud.manga.henryTing.infor.FrameInfor;
import com.cloud.manga.henryTing.infor.GeoGet;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

public class JLabelM extends JLabel {
	// final boolean _isRotate; 
	public JLabelM(LabelInfor infor, GeoGet geoGet) {
		super(infor._content);
		Font font = new Font(infor.getFontType(), Font.PLAIN, infor.getFontSize());
		setFont(font);
		FontMetrics metrics = new FontMetrics(font){};
		final Rectangle2D bounds = metrics.getStringBounds(infor._content, null);
		double[] posi = FrameInfor.positionLabel(geoGet.screenX(), geoGet.screenY(), (int) bounds.getWidth(), (int) bounds.getHeight(), infor._posiIndex);
		setBounds((int) posi[0], (int) posi[1], (int) bounds.getWidth(), (int) bounds.getHeight());
	}
}