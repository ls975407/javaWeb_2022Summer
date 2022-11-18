package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/ResultEn.java
*/


public class ResultEn<T> {
	public final T _result;
	public final SDEBase _exception;
	public ResultEn(T result) {
		_result = result; _exception = null;
		if (result == null) {
			throw SDEError.cUncatchLCmd(2);
		}
	}
	public ResultEn(SDEBase exception) {
		_result = null; _exception = exception;
		if (exception == null) {
			throw SDEError.cUncatchLCmd(3);
		}
	}
	public boolean isNotSuccess() {
		return !isSuccess();
	}
	public boolean isSuccess() {
		if (_exception == null) { return true; }
		if (_exception._type == SDEEnum.Handle && _exception._h_type == SDEHEnum.none ) {
			return true;
		}
		return false;
	}
	public T get() throws SDEBase {
		if (_result == null) {
			throw _exception;
		}
		return _result;
	}
}