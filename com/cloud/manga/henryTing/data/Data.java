package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/Data.java
*/

public class Data {
	public static void initialize(Setting setting) {
		PathGet._setting = setting;
		InforCh._setting = setting;
		DateT._setting = setting;
	}
}