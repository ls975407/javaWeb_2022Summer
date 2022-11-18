package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/InforCh.java
*/

import com.cloud.manga.henryTing.unit.Log;

public class InforCh {
	public final static Log _log = new Log("InforCh");
	private static String defaultMark = "z";
	public final KCh _kch;
	public final int _chapter, _order;
	public final String _markName, _name, _mark;
	public static Setting _setting;
	public InforCh(KCh kch) {
		_kch = kch;
		String name = kch._keyName;
		int id_, ch_;
		Integer order_;
		String mark_, name_ = "";
		String[] tokens = name.split("_", 3);
		if (tokens.length < 2) {
			_chapter=999; _order=999;
			_markName=""; _name=kch._keyName; _mark=defaultMark;
			_log.w("illegal format", "tokens.length < 2");
			return;
		}
		mark_ = tokens[1].substring(0, 1);
		order_ = _setting.mark2Order(mark_);
		if (order_ == null) {
			order_ = 999; mark_ = "z";
		} else {
			tokens[1] = tokens[1].substring(1, tokens[1].length());
		}
		try {
			id_ = Integer.parseInt(tokens[0]);
			ch_ = Integer.parseInt(tokens[1]);
		} catch (NumberFormatException e) {
			_chapter=999; _order=999;
			_markName=""; _name=kch._keyName; _mark=defaultMark;
			_log.w("illegal format", "NumberFormatException");
			return;
		}
		
		if (tokens.length >= 3) {
			int start = name.length() - tokens[2].length();
			name_ = name.substring(start, name.length()); 
		}
		_chapter=ch_; _order=order_;
		_markName = _setting.markToName(mark_);
		_name=name_; _mark=mark_;
	}
}