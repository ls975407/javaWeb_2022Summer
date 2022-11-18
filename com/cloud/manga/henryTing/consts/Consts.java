package com.cloud.manga.henryTing.consts;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/consts/Consts.java
*/

public class Consts {
	public static void initialize(Setting setting) {
		IdTokenS._setting = setting;
		CloudM._setting = setting;
		ThreadWork._setting = setting;
	}
}