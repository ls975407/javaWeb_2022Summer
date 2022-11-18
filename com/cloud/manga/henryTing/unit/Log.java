package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/Log.java
*/
public class Log {
	public final static String TAG_TRACE = "TRACE";
	public final static String TAG_RESOURCE = "RESOURCE";
	public final static String TAG_ERROR = "ERROR";
	public final static String TAG_WARNING = "WARNING";
	public final static String TAG_DEBUG = "DEBUG";
	public final static String TAG_PROCESS = "PROCESS";
	public final static String PRINT_FORMAT = "%s: %s\t%s\n";
	public final static String PRINT_FORMAT_1 = "%s: %s\t%s %s\n";
	public final static String PRINT_FORMAT_2 = "%s: %s\t%s %s\n\t%s\n";
	
	private static int _setValue = 0;
	public int STATE = _setValue;
	public int STATE_TRACE = _setValue;
	public int STATE_RESOURCE = _setValue;
	public int STATE_ERROR = _setValue;
	public int STATE_WARNING = _setValue;
	public int STATE_DEBUG = _setValue;
	public int STATE_PROCESS = _setValue;
	
	public final String _title;
	public Log(String title) {
		_title = title;
	}
	public void t(String content) { trace(content); }
	public void r(String content) { resource(content); }
	public void e(String content) { error(content); }
	public void w(String content) { warning(content); }
	public void d(String content) { debug(content); }
	public void p(String content) { process(content); }
	
	public void t(String title, String content) { trace(title, content); }
	public void r(String title, String content) { resource(title, content); }
	public void e(String title, String content) { error(title, content); }
	public void w(String title, String content) { warning(title, content); }
	public void d(String title, String content) { debug(title, content); }
	public void p(String title, String content) { process(title, content); }
	
	public void t(String title, String subTitle, String content) { trace(title, subTitle, content); }
	public void r(String title, String subTitle, String content) { resource(title, subTitle, content); }
	public void e(String title, String subTitle, String content) { error(title, subTitle, content); }
	public void w(String title, String subTitle, String content) { warning(title, subTitle, content); }
	public void d(String title, String subTitle, String content) { debug(title, subTitle, content); }
	public void p(String title, String subTitle, String content) { process(title, subTitle, content); }
	
	
	public String toString() {
		return _title;
	}
	
	public void print(String content) {
		if(STATE == 0) { System.out.print(content); }
	}
	public void trace(String content) {
		if (STATE_TRACE == 0) { print(String.format(PRINT_FORMAT, TAG_TRACE, toString(), content)); }
	}
	public void resource(String content) {
		if (STATE_RESOURCE == 0) { print(String.format(PRINT_FORMAT, TAG_RESOURCE, toString(), content)); }
	}
	public void error(String content) {
		if (STATE_ERROR == 0) { print(String.format(PRINT_FORMAT, TAG_ERROR, toString(), content)); }
	}
	public void warning(String content) {
		if (STATE_WARNING == 0) { print(String.format(PRINT_FORMAT, TAG_WARNING, toString(), content)); }
	}
	public void debug(String content) {
		if (STATE_DEBUG == 0) { print(String.format(PRINT_FORMAT, TAG_DEBUG, toString(), content)); }
	}
	public void process(String content) {
		if (STATE_PROCESS == 0) { print(String.format(PRINT_FORMAT, TAG_PROCESS, toString(), content)); }
	}
	// ----------------------------------------------------------
	public void trace(String title, String content) {
		if (STATE_TRACE == 0) { print(String.format(PRINT_FORMAT_1, TAG_TRACE, toString(), title, content)); }
	}
	public void resource(String title, String content) {
		if (STATE_RESOURCE == 0) { print(String.format(PRINT_FORMAT_1, TAG_RESOURCE, toString(), title, content)); }
	}
	public void error(String title, String content) {
		if (STATE_ERROR == 0) { print(String.format(PRINT_FORMAT_1, TAG_ERROR, toString(), title, content)); }
	}
	public void warning(String title, String content) {
		if (STATE_WARNING == 0) { print(String.format(PRINT_FORMAT_1, TAG_WARNING, toString(), title, content)); }
	}
	public void debug(String title, String content) {
		if (STATE_DEBUG == 0) { print(String.format(PRINT_FORMAT_1, TAG_DEBUG, toString(), title, content)); }
	}
	public void process(String title, String content) {
		if (STATE_PROCESS == 0) { print(String.format(PRINT_FORMAT_1, TAG_PROCESS, toString(), title, content)); }
	}
	// ----------------------------------------------------------------
	public void trace(String title, String subTitle, String content) {
		if (STATE_TRACE == 0) { print(String.format(PRINT_FORMAT_2, TAG_TRACE, toString(), title, subTitle, content)); }
	}
	public void resource(String title, String subTitle, String content) {
		if (STATE_RESOURCE == 0) { print(String.format(PRINT_FORMAT_2, TAG_RESOURCE, toString(), title, subTitle, content)); }
	}
	public void error(String title, String subTitle, String content) {
		if (STATE_ERROR == 0) { print(String.format(PRINT_FORMAT_2, TAG_ERROR, toString(), title, subTitle, content)); }
	}
	public void warning(String title, String subTitle, String content) {
		if (STATE_WARNING == 0) { print(String.format(PRINT_FORMAT_2, TAG_WARNING, toString(), title, subTitle, content)); }
	}
	public void debug(String title, String subTitle, String content) {
		if (STATE_DEBUG == 0) { print(String.format(PRINT_FORMAT_2, TAG_DEBUG, toString(), title, subTitle, content)); }
	}
	public void process(String title, String subTitle, String content) {
		if (STATE_PROCESS == 0) { print(String.format(PRINT_FORMAT_2, TAG_PROCESS, toString(), title, subTitle, content)); }
	}
	// -------------------------------------------------------------
	public void t_lock() { STATE_TRACE++; }
	public void t_release() { STATE_TRACE--; }
	public void r_lock() { STATE_RESOURCE++; }
	public void r_release() { STATE_RESOURCE++; }
	public void e_lock() { STATE_ERROR--; }
	public void e_release() { STATE_ERROR++; }
	public void w_lock() { STATE_WARNING--; }
	public void w_release() { STATE_WARNING++; }
	public void d_lock() { STATE_DEBUG--; }
	public void d_release() { STATE_DEBUG++; }
	public void p_lock() { STATE_PROCESS--; }
	public void p_release() { STATE_PROCESS++; }
	public void lock() { STATE++; }
	public void release() { STATE++; }
}