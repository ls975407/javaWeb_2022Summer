
/*
javac -encoding utf8  TestThreadLocal.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.tool.ThreadLocal;
import com.cloud.manga.henryTing.unit.SDEBase;

import java.util.Scanner;

public class TestThreadLocal {
	public static void print(String content) {
		System.out.print(content);
	}
	public static void println(String content) {
		System.out.println(content);
	}
	public static String list2str(String[] eles) {
		StringBuilder _buider = new StringBuilder();
		for (String ele: eles) {
			_buider.append(ele + " ");
		}
		return _buider.toString();
	}
	public static void main(String[] arg) {
		SDEBase e1;
		try {
			final String path = "com/cloud/manga/henryTing/tool/FileM.java";
			final String pathDir = "com/cloud/manga/henryTing/tool";
			print(FileM.bytes2String(ThreadLocal.read("TestThreadLocal.java").get()));
			e1 = ThreadLocal.write("buf.txt", "Hello FileM.java");
			if (e1 != null) { throw e1; }
			println("path = " + path);
			println("dirs = " + list2str(ThreadLocal.listDirectory(pathDir).get()));
			println("file = " + list2str(ThreadLocal.listFile(pathDir).get()));
			println("path isExist " + Boolean.valueOf(ThreadLocal.isExist(path).get()).toString());
			println("dir isExist " + Boolean.valueOf(ThreadLocal.isExist(pathDir).get()).toString());
			println("not Exist " + Boolean.valueOf(ThreadLocal.isExist(pathDir + "123").get()).toString());
		} catch (SDEBase e) {
			print(e.toString());
		}
		ThreadLocal.shutdown();
	}
}