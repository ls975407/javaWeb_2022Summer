/*
javac -encoding utf8  TestTWSave.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;


import com.cloud.manga.henryTing.data.*;


import com.cloud.manga.henryTing.consts.ThreadWork;
import com.cloud.manga.henryTing.consts.IdTokenS;
import com.cloud.manga.henryTing.tool.FileM;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

public class TestTWSave {	
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
		PathGet pathGet = PathGet.getLocal();
		KCh[] kchs = new KCh[] {
			new KCh(kid, "0001_402_東立電子書_002"), 
			new KCh(kid, "0001_402_東立電子書_005")
		};
		KPgS kpgs = new KPgS(new KPg[]{
			new KPg(kchs[0], new TokenU("0001_402_011.jpg")),
			new KPg(kchs[0], new TokenU("0001_402_012.jpg"))
		});
		ThreadWork.saveHistory(pathGet, kchs[0]);
		ThreadWork.saveMarkHistory(pathGet, kchs[0]);
		ThreadWork.saveMarkCover(pathGet, kchs[0]);
		ThreadWork.saveHistory(pathGet, kpgs);
		ThreadWork.saveCover(pathGet, kchs[0]);
		ThreadWork.saveCover(pathGet, kpgs);
	}
}