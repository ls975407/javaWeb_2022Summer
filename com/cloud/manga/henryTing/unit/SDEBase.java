package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/SDEBase.java
*/

public class SDEBase extends RuntimeException {
	public final SDEEnum _type;
	public final SDEHEnum _h_type;
	protected SDEBase(SDEEnum type, String signal) {
		super(signal);
		_type = type;
		_h_type = null;
	}
	protected SDEBase(SDEEnum type, SDEHEnum h_type, String signal) {
		super(signal);
		_type = type;
		_h_type = h_type;
	}	
	protected static String join_2(String left, String mark, String right) {
		return String.format("%s %s = %s", left, mark, right);
	}
}



	
