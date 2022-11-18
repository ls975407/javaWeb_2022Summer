package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/Infor.java
*/

public class Infor {
	public static void initialize(Setting setting) {
		FrameM._setting = setting;
		SlideCmds._setting = setting;
		LabelBase._setting = setting;
		LabelM._setting = setting;
		FrameInfor._setting = setting;
	}
}