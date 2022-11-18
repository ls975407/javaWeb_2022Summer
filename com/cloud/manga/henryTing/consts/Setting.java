package com.cloud.manga.henryTing.consts;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/consts/Setting.java
*/
public interface Setting {
	// IdTokenS
	Long getNode();
	String getPathTokenS();
	// CloudM
	String getCloudToken();
	// CloudM
	// String getCFolderEndsWith();
	// String getCFileEndsWith();
	// ThreadWork
	String[] getLegalImageExtension();
} 