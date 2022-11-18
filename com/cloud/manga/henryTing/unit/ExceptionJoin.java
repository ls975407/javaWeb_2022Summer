package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/ExceptionJoin.java
*/


public class ExceptionJoin {
	public final SDEEnum _type;
	public ExceptionJoin() {
		_type = SDEEnum.Error;
	}
	public ExceptionJoin(SDEEnum type) {
		_type = type;
	}
	private boolean isZero = true;
	private StringBuilder _buider = new StringBuilder();
	public boolean append(SDEBase e) {
		if (e._type != _type) { return false; }
		isZero = false;
		_buider.append(e.toString());
		_buider.append("\n");
		return true;
	}
	public synchronized SDEBase append(SDEBase e, boolean isClear) {
		if (isClear) {
			SDEBase e_t = get();
			if (e_t != null) {
				_buider = new StringBuilder();
			}
			return e_t;
		} else {
			append(e); return null;
		}
	}
	public SDEBase get() {
		if (isZero) { return null; }
		return new SDEBase(_type, _buider.toString());
	}
	public boolean isNotSuccess() { return !isZero; }
	public void throwException() throws SDEBase {
		if (!isZero) {
			throw new SDEBase(_type, _buider.toString());
		}
	}
}