package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/Setting.java
*/
public interface Setting {
	// FrameM
	String getDFKeyName();
	String[] getLegalImageExtension();
	String getPathLFImage();
	// SlideCmds
	int getMinSlideAmount();
	// LabelBase
	String getFontTypes(int index);
	int getFontSizes(int index);
	String getErrorFontType();
	int getErrorFontSize();
	// LabelM
	int getErrorLabelIndex();
	// FrameInfor
	double getFontScaleValue();
	double getGeoScaleValue();
	int getLabelMarge();
	int getScreenX();
	int getScreenY();
} 