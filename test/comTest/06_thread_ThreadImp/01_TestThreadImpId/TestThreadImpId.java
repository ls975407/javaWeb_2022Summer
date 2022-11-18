/*
javac -encoding utf8  TestThreadImpId.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;


import com.cloud.manga.henryTing.data.KAddress;
import com.cloud.manga.henryTing.data.KAddressT;


import com.cloud.manga.henryTing.consts.IdTokenS;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.tool.ThreadLocal;
import com.cloud.manga.henryTing.thread.ThreadImp;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

public class TestThreadImpId {	
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
		
		java.util.Locale.setDefault(java.util.Locale.TAIWAN);
		
		Setting setting = initailizeSetting();
		if (setting == null) {
			System.out.println("fail to initailizeSetting");
			return;
		}
		com.cloud.manga.henryTing.consts.Consts.initialize(setting);
		com.cloud.manga.henryTing.data.Data.initialize(setting);
		com.cloud.manga.henryTing.thread.Thread.initialize(setting);


		String finename = "input";
		String content;
		String[] localPaths;
		KAddress KAddress_t = null;
		KAddressT KAddressT_t = null;

		String localPath = "input09.json";
		try {
			content = FileM.readString(localPath);
		} catch(IOException e) {
			println(String.format("fail to read localPath = %s", localPath)); return;
		}
		try {
			KAddress_t = KAddress.parseJson(new JSONObject(content));
		} catch (JSONException e) {
			println(String.format("fail to read KAddress_t %s", e.toString())); return;
		}
		// KAddress_t.print();
		KAddressT_t = IdTokenS.kAddressToT(KAddress_t);
		KAddressT_t.print();
		ThreadImp.ptr().iniIdFolders(KAddressT_t.getKIdTS());
		
		ThreadImp.ptr().shutdown();
		ThreadLocal.shutdown();
	}
}