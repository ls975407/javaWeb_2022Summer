
/*
javac -encoding utf8  TestFrameIniExampleJson.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.SettingAd;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

public class TestFrameIniExampleJson implements com.cloud.manga.henryTing.infor.Setting {
	public static void println(String content) {
		System.out.println(content);
	}
	public static void main(String[] arg) {
		final String path = "frameIniExample.json";
		String content = null;
		try {
			content = FileM.readString(path);
		} catch (IOException e) {
			println(e.toString()); return;
		}
		JSONArray jsonA = null;
		try {
			jsonA = new JSONArray(content);
			SettingAd._iniTS2F(jsonA, new TestFrameIniExampleJson());
		} catch (JSONException e) {
			println(e.toString()); return;
		}
	}
	// FrameM
	public String getDFKeyName() { return "getDFKeyName"; }
	public String[] getLegalImageExtension() { return new String[0]; }
	public String getPathLFImage() { return "getPathLFImage"; }
	// LabelBase
	public String getFontTypes(int index) { return "getFontTypes"; }
	public int getFontSizes(int index) { return 10; }
	public String getErrorFontType() { return "getFontSizes"; }
	public int getErrorFontSize() { return 10; }
	// LabelM
	public int getErrorLabelIndex() { return 10; }
	// FrameInfor
	public double getFontScaleValue() { return 10; }
	public double getGeoScaleValue() { return 10; }
	public int getLabelMarge() { return 10; }
	public int getScreenX() { return 10; }
	public int getScreenY() { return 10; }
}