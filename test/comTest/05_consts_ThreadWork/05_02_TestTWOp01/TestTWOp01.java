/*
javac -encoding utf8  TestTWOp01.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;


import com.cloud.manga.henryTing.data.Token;
import com.cloud.manga.henryTing.data.KIdT;
import com.cloud.manga.henryTing.data.KId;
import com.cloud.manga.henryTing.data.PathGet;
import com.cloud.manga.henryTing.data.KeyStringBase;
import com.cloud.manga.henryTing.data.KAddress;
import com.cloud.manga.henryTing.data.KAddressT;
import com.cloud.manga.henryTing.data.KCh2D;


import com.cloud.manga.henryTing.consts.ThreadWork;
import com.cloud.manga.henryTing.consts.IdTokenS;
import com.cloud.manga.henryTing.tool.FileM;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;
import java.util.List;
import java.util.Arrays;

public class TestTWOp01 {	
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
		com.cloud.manga.henryTing.consts.Consts.initialize(setting);
		com.cloud.manga.henryTing.data.Data.initialize(setting);

		KId kid = new KId("2021_東立航海王");
		String path = PathGet.getLocal().Image(kid);
		KeyStringBase[] keys = new KeyStringBase[] {
			new KeyStringBase("0001_402_東立電子書_002"), 
			new KeyStringBase("0001_402_東立電子書_005"), 
			new KeyStringBase("0001_402_東立電子書_007")
		};
		List<Boolean> result = ThreadWork.isExistFolder(path, Arrays.asList(keys));
		for (int ith=0; ith<keys.length; ith++) {
			println(String.format("key %s: %s", keys[ith].toString(), result.get(ith).toString()));
		}
		Boolean filterFalse = Boolean.valueOf(true);
		println(String.format("set filterFalse %s", filterFalse.toString()));
		for (KeyStringBase key: ThreadWork.filterEle(Arrays.asList(keys), result, filterFalse)) {
			println(String.format("key %s", key.toString()));
		}
	}
}