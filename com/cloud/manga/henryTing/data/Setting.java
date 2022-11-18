package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/Setting.java
*/
public interface Setting {
	// PathGet
	String getPathLPicture();
	String getPathCPicture();
	String getPathLRecord();
	String getPathCRecord();
	String getPathLIdJson();
	String getPathCIdJson();
	// InforCh
	Integer mark2Order(String mark);
	String markToName(String mark);
	// DateT
	long getDiffMaxDay();
}