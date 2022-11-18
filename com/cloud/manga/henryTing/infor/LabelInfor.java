package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/LabelInfor.java
*/
public class LabelInfor extends LabelBase {
	public final String _content; 
	public final double _scaleValue;
	public final boolean _isRotate;
	LabelInfor (
		LabelM.Infor infor, double scaleValue, boolean isRotate
	) {
		super(infor);
		_content = infor._content;
		_scaleValue = scaleValue;
		_isRotate = isRotate;
	}
	public int getFontSize() {
		return super.getFontSize();
	}
	public int getFontSizeScaled() {
		return (int) (_scaleValue*getFontSize());
	}
	public boolean needRotate() {
		int count = 0;
		if (_needRotate) { count++; }
		if (_isRotate) { count++; }
		return count%2==0? false: true;
	}
} 