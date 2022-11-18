
/*
javac -encoding utf8  TestTouchDelayer02.java
*/

import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;
import com.cloud.manga.henryTing.main.Setting;

import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

import com.cloud.manga.henryTing.tool.FileM; 
import com.cloud.manga.henryTing.thread.TouchS; 
import com.cloud.manga.henryTing.thread.TouchUnit; 
import com.cloud.manga.henryTing.thread.TouchDelayer; 
import com.cloud.manga.henryTing.unit.ActionM; 
import com.cloud.manga.henryTing.unit.SDEBase; 
import com.cloud.manga.henryTing.unit.SDEError; 

public class TestTouchDelayer02 
{
	public static int level = 0;
	
	
	public static class DualActor implements TouchS.Actor<String>
	{
		public void summitAction(java.util.List<String> strings) {
			for (String key: strings) {
				println(String.format("send %s", key));
			}
		}
		public void reportException(SDEBase e) {
			println(e.toString());
		}
	}
	
	public static Setting initailizeSetting() {
		final String path = "IniSetting_ver01.json";
		String content = null;
		try {
			content = FileM.readString(path);
		} catch (IOException e) {
			println(e.toString()); return null;
		}
		JSONObject json = null;
		SettingAd settingAd = new SettingAd(".", ".");
		try {
			json = new JSONObject(content);
			settingAd.parseJson(json);
		} catch (JSONException e) {
			println(e.toString()); return null;
		}
		return settingAd;
	}
	
	public static void println(String content) {
		System.out.println(content);
	}
	
	public static void main(String[] arg) {
		Setting setting = initailizeSetting();
		if (setting == null) {
			System.out.println("fail to initailizeSetting");
			return;
		}
		com.cloud.manga.henryTing.thread.Thread.initialize(setting);
		
		
		
		TouchUnit<String> touchUnit;
		TouchDelayer<String> touchDelayer = TouchDelayer.create();
		touchDelayer._touchActor = new DualActor();
		Scanner scanner = new Scanner(System.in);
		String str = scanner.next();
		String strOld = str;
		while(!str.equals("e")) {
			touchUnit = new TouchUnit<String>(str, level);
			touchDelayer.sendATouch(touchUnit);
			str = scanner.next();
			if (!str.equals(strOld)) {
				strOld = str;
				level++;
			}
		}
		touchUnit = new TouchUnit<String>(str, 0);
		touchUnit.setActiveOff();
		touchDelayer.sendATouch(touchUnit);
		touchDelayer.shutdown();
		scanner.close();
	}
}

