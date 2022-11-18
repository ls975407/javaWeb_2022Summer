package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/Point.java
*/
public class Point {
	public final int _x, _y;
	public Point(int x, int y) {
		_x = x; _y = y;
	}
	public Point(Point point) {
		_x = point._x; _y = point._y;
	}
	public String toString() {
		return String.format("(%d, %d)", _x, _y);
	}
}