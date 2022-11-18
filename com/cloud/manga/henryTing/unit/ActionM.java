package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/ActionM.java
*/

public class ActionM {
	private static ActionM _actionNone = null;
	
	public final CmdEnum _cmdEnum;
	public final String _arg;
	public final int _clickTimes;
	private ActionM() {
		_cmdEnum = CmdEnum.None; _arg = ""; _clickTimes = 1;
	}
	public ActionM(CmdEnum cmdEnum) {
		_cmdEnum = cmdEnum; _arg = ""; _clickTimes = 1;
	}
	public ActionM(CmdEnum cmdEnum, String arg) {
		_cmdEnum = cmdEnum; _arg = arg; _clickTimes = 1;
	}
	public ActionM(CmdEnum cmdEnum, String arg, int clickTimes) {
		_cmdEnum = cmdEnum; _arg = arg; _clickTimes = clickTimes;
	}
	public ActionM(ActionM actionM, int clickTimes) {
		_cmdEnum = actionM._cmdEnum; _arg = actionM._arg; _clickTimes = clickTimes;
	}
	public static ActionM getNone() {
		if (_actionNone == null) {
			_actionNone = new ActionM();
		}
		return _actionNone;
	}
	public void print() {
		System.out.println("cmdEnum: " + _cmdEnum.toString());
		System.out.println("arg: " + _arg);
		System.out.println("clickTimes: " + Integer.valueOf(_clickTimes).toString());
	}
	public String toString() {
		return String.format("%s(%s)-%d", _cmdEnum.toString(), _arg, _clickTimes);
	}
}