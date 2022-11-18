package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/ImageInfor.java
*/
public class ImageInfor {
	public final boolean _screenP, _isFillScale;
	public final double _userX;
	public final double _userY;
	public ImageInfor(boolean screenP, boolean isFillScale, double userX, double userY) {
		_screenP = screenP; _isFillScale = isFillScale;
		_userX = userX; _userY = userY;
	}
} 