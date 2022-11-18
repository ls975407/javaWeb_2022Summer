
/*
javac -encoding utf8  TestFileM.java
*/

import com.cloud.manga.henryTing.tool.FileM;

import java.util.Scanner;

public class TestFileM {
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
		try {
			final String path = "com/cloud/manga/henryTing/tool/FileM.java";
			// print(FileM.readString("TestFileM.java"));
			FileM.write("buf.txt", "Hello FileM.java");
			println("path = " + path);
			println("basename = " + FileM.basename(path));
			String pathDir = FileM.dirname(path);
			println("dirname = " + pathDir);
			println("dirs = " + list2str(FileM.listDirectory(pathDir)));
			println("file = " + list2str(FileM.listFile(pathDir)));
			println("path isExist " + Boolean.valueOf(FileM.isExist(path)).toString());
			println("dir isExist " + Boolean.valueOf(FileM.isExist(pathDir)).toString());
			println("not Exist " + Boolean.valueOf(FileM.isExist(pathDir + "123")).toString());
			println("path isFile " + Boolean.valueOf(FileM.isFile(path)).toString());
			println("dir isFile " + Boolean.valueOf(FileM.isFile(pathDir)).toString());
			println("path isFolder " + Boolean.valueOf(FileM.isFolder(path)).toString());
			println("dir isFolder " + Boolean.valueOf(FileM.isFolder(pathDir)).toString());
		} catch (java.io.IOException e) {
			print(e.toString());
		}
	}
}