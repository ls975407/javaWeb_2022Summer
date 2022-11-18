/*
javac -encoding utf8  TestThreadImpSyn01.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;


import com.cloud.manga.henryTing.data.KAddress;
import com.cloud.manga.henryTing.data.TokenS;
import com.cloud.manga.henryTing.data.Token;
import com.cloud.manga.henryTing.data.KIdT;


import com.cloud.manga.henryTing.consts.CloudM;
import com.cloud.manga.henryTing.consts.IdTokenS;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.tool.ThreadLocal;
import com.cloud.manga.henryTing.thread.ThreadImp;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

public class TestThreadImpSyn01 {	
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

		// Long node = Long.valueOf("11008705516");
		// Token token = new Token(node, "0021_nana_0021_scaled");
		// TokenS tokenS = CloudM.ptr().getTokenSFolder(token);
		// ThreadImp.ptr().synKAddress(tokenS, "./testThreadImp", new KIdT(token));
		
		// Long node = Long.valueOf("11008705516");
		// Token token = new Token(node, "0021_nana_0021_scaled");
		// TokenS tokenS = ;
		ThreadImp.ptr().synKAddress(CloudM.ptr().getTokenSFolder(setting.getNode()), "./testThreadImp");
		
		ThreadImp.ptr().shutdown();
		ThreadLocal.shutdown();
	}
}