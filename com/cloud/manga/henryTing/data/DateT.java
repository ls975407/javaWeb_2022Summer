package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/DateT.java
*/
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.concurrent.TimeUnit;
import com.cloud.manga.henryTing.unit.Log;

public class DateT extends KeyString {
	public static Setting _setting;
	public final static Log _log = new Log("DateT");
	public final static String TIME_MARK = "yyyy-MM-dd";
	public final static String KEY = "DateT";
	private final Date _dateT;
	public DateT(String keyName, Date date_) { super(keyName); _dateT = date_; }
	public DateT(DateT dateT) { 
		super(dateT._keyName);
		_dateT = dateT._dateT;
	}
	public DateT() { 
		super(new SimpleDateFormat(TIME_MARK).format(new Date()));
		_dateT = new Date();
	}
	public static DateT parseJson(JSONObject json) throws JSONException {
		return create(json.getString(KEY));
	}
	public void addJson(JSONObject json) throws JSONException {
		json.put(KEY, _keyName);
	}
	public static DateT create(String mark) throws JSONException {
		// Date date_ = new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-01", 0);
		Date date_;
		try {
			date_ = new SimpleDateFormat(TIME_MARK).parse(mark);
		} catch(java.text.ParseException e) {
			throw new JSONException("");
		}
		if (date_ == null) {
			throw new JSONException("");
		}
		return new DateT(mark, date_);
	}
	public Date getDate() { return (Date) _dateT.clone(); }
	public boolean isOnDay() {
		long diffInMillies = Math.abs(new Date().getTime() - _dateT.getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		
		// System.out.println("DateT isOnDay() " + Boolean.valueOf(diff < _setting.getDiffMaxDay()).toString());
		boolean state = diff < _setting.getDiffMaxDay();
		if (state) {
			_log.t("OnDay");
		} else {
			_log.t("OffDay");
		}
		return state;
	}
}