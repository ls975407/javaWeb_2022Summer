
/*
javac -encoding utf8  TestThreadLocal02.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.tool.ThreadLocal;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.ResultEn;
import com.cloud.manga.henryTing.unit.ExceptionJoin;

import java.util.Scanner;

public class TestThreadLocal02 {
	public static void print(String content) {
		System.out.print(content);
	}
	public static void println(String content) {
		System.out.println(content + "\n");
	}
	public static String list2str(String[] eles) {
		StringBuilder _buider = new StringBuilder();
		for (String ele: eles) {
			_buider.append(ele + " ");
		}
		return _buider.toString();
	}
	public static void main(String[] arg) {
		Scanner scanner = new Scanner(System.in);
		SDEBase e1;
		final String path = "com/cloud/manga/henryTing/tool/FileM.java";
		final String pathDir = "com/cloud/manga/henryTing/tool";
		ExceptionJoin joiner = new ExceptionJoin();
		ResultEn<Boolean> isExist;
		ResultEn<byte[]> bytes;
		ResultEn<String[]> names;
		try {
			
			e1 = ThreadLocal.write("buf00.txt", "Hello FileM.java");
			if (e1 == null) {
				println("write.isSuccess()"); 
			} else {
				println("write fail"); println(e1.toString());
			}
			scanner.nextInt();
			ThreadLocal.setLock();
			
			bytes = ThreadLocal.read("TestThreadLocal.java");
			if (bytes.isSuccess()) {
				println("bytes.isSuccess()"); println(FileM.bytes2String(bytes.get()));
			} else {
				println("bytes fail"); println(bytes._exception.toString());
				joiner.append(bytes._exception);
				joiner.throwException();
			}
			
			
			e1 = ThreadLocal.write("buf01.txt", "Hello FileM.java");
			if (e1 == null) {
				println("write.isSuccess()"); 
			} else {
				println("write fail"); println(e1.toString());
			}
			
			isExist = ThreadLocal.isExist(path);
			if (isExist.isSuccess()) {
				println("isExist.isSuccess()"); println(Boolean.valueOf(isExist.get()).toString());
			} else {
				println("isExist fail"); println(isExist._exception.toString());
				joiner.append(isExist._exception);
				joiner.throwException();
			}
			
			names = ThreadLocal.listDirectory(pathDir);
			if (names.isSuccess()) {
				println("names.isSuccess()"); println("dirs = " + list2str(names.get()));
			} else {
				println("names fail"); println(names._exception.toString());
				joiner.append(names._exception);
				joiner.throwException();
			}
			
			names = ThreadLocal.listFile(pathDir);
			if (names.isSuccess()) {
				println("names.isSuccess()"); println("dirs = " + list2str(names.get()));
			} else {
				println("names fail"); println(names._exception.toString());
				joiner.append(names._exception);
				joiner.throwException();
			}
			scanner.nextInt();
			ThreadLocal.releaseLock();
			e1 = ThreadLocal.write("buf02.txt", "Hello FileM.java");
			if (e1 == null) {
				println("write.isSuccess()"); 
			} else {
				println("write fail"); println(e1.toString());
			}
			
			isExist = ThreadLocal.isExist(path);
			if (isExist.isSuccess()) {
				println("isExist.isSuccess()"); println(Boolean.valueOf(isExist.get()).toString());
			} else {
				println("isExist fail"); println(isExist._exception.toString());
				joiner.append(isExist._exception);
				joiner.throwException();
			}
			
			names = ThreadLocal.listDirectory(pathDir);
			if (names.isSuccess()) {
				println("names.isSuccess()"); println("dirs = " + list2str(names.get()));
			} else {
				println("names fail"); println(names._exception.toString());
				joiner.append(names._exception);
				joiner.throwException();
			}
			
			names = ThreadLocal.listFile(pathDir);
			if (names.isSuccess()) {
				println("names.isSuccess()"); println("dirs = " + list2str(names.get()));
			} else {
				println("names fail"); println(names._exception.toString());
				joiner.append(names._exception);
				joiner.throwException();
			}
			
			
		} catch (SDEBase e) {
			print(e.toString());
		}
		ThreadLocal.shutdown();
	}
}