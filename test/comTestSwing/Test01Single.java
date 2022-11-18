// package com.cloud.manga.henryTing.consts;
/*
javac -encoding utf8  ./Test01Single.java
*/

import java.util.Iterator;
import java.io.File;
import java.io.IOException;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.Scanner;
import java.io.IOException;


import com.cloud.manga.henryTing.infor.ImageInfor;
import com.cloud.manga.henryTing.infor.GeoGet;
import com.cloud.manga.henryTing.data.FileB;
import com.cloud.manga.henryTing.data.FileBS;

public class Test01Single extends JPanel
{
	private final JImageM _image_0;
	public Test01Single(FileBS fileBS, ImageInfor infor_0) {
		_image_0 = new JImageM(infor_0, fileBS.get(0), 0);
	}
	public JImageM getImage() {
		return _image_0;
	}
	public Test01Single get(GeoGet getGet) {
		
		removeAll();
		// setLayout(null);
		setLayout(new BorderLayout(0, 0));
		
		int screenX = getGet.screenX();
		int screenY = getGet.screenY();
		final int SX = Math.min(screenX, screenY);
		final int SY = Math.max(screenX, screenY);
		_image_0.setBounds(0, 0, SX, SY);
		add(_image_0, BorderLayout.CENTER);
		// add(_image_0);
		setBounds(0, 0, SX, SY);
		return this;
	}
}