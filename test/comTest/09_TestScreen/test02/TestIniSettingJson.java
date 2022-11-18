
/*
javac -encoding utf8  TestIniSettingJson.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.SettingAd;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

public class TestIniSettingJson {
	public static void println(String content) {
		System.out.println(content);
	}
	public static void main(String[] arg) {
		final String path = "MangaResouceIni.json";
		String content = null;
		try {
			content = FileM.readString(path);
		} catch (IOException e) {
			println(e.toString()); return;
		}
		JSONObject json = null;
		SettingAd settingAd = new SettingAd(".", ".");
		try {
			json = new JSONObject(content);
			settingAd.parseJson(json);
		} catch (JSONException e) {
			println(e.toString()); return;
		}
	}
}