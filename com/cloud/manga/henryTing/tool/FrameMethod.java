package com.cloud.manga.henryTing.tool;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/tool/FrameMethod.java
*/
import com.cloud.manga.henryTing.unit.FrameEnum;
public class FrameMethod {
	public static boolean isPortait(FrameEnum frameType) {
		switch(frameType) {
			case single:
			case leftright:
			case updown:
			return true;
			case dual:
			default:
			return false;
		}
	}
	public static boolean isOnePage(FrameEnum frameType) {
		switch(frameType) {
			case single:
			return true;
			case leftright:
			case updown:
			case dual:
			default:
			return false;
		}
	}
}

