package com.cloud.manga.henryTing.tool;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/tool/FileM.java
*/


import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import java.io.IOException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.ArrayList;

public class FileM {
	public static final byte[] read(String localPath) throws IOException{
		java.io.InputStream is = null;
		try {
			is = new java.io.FileInputStream(localPath);
			long fileSize = new java.io.File(localPath).length();
			byte[] allBytes = new byte[(int) fileSize];
			is.read(allBytes);
			return allBytes;
		} catch (IOException e){
			throw e;
		} finally {
			if (is != null) is.close();
		}
	}
	public static final String readString(String localPath) throws IOException{
		return bytes2String(read(localPath));
	}
	public static final JSONObject readJsonObject(String localPath) throws JSONException {
		try {
			return new JSONObject(bytes2String(read(localPath)));
		} catch(IOException e) {
			throw new JSONException("readJsonObject");
		}
	}
	public static final JSONArray readJsonArray(String localPath) throws JSONException {
		try {
			return new JSONArray(bytes2String(read(localPath)));
		} catch(IOException e) {
			throw new JSONException("readJsonArray");
		}
	}
	public static final byte[] read(final java.net.URL cloudPath) throws IOException{
		java.io.InputStream is = null;
		java.io.BufferedInputStream bis = null;
		java.io.ByteArrayOutputStream bytes = null;
		try {
			java.net.URLConnection conn = cloudPath.openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new java.io.BufferedInputStream(is);
			bytes = new java.io.ByteArrayOutputStream();
			int bytesRead;
			byte[] dataBufferArray = new byte[1024];
			while (true) {
				bytesRead = bis.read(dataBufferArray, 0, 1024);
				if (bytesRead > 0) {
					bytes.write(dataBufferArray, 0, bytesRead);
				} else if (bytesRead < 0) {
					break;
				}
			}
		} catch (IOException e){
			throw e;
		} finally {
			if (is != null) is.close();
			if (bis != null) bis.close();
		}
		return bytes.toByteArray();
	}
	public static final String bytes2String(byte[] allBytesh) {
		return new String(allBytesh, java.nio.charset.StandardCharsets.UTF_8);
	}
	public static final byte[] string2Bytes(String content) {
		return content.getBytes(java.nio.charset.StandardCharsets.UTF_8);
	}
	public static final void write(final String localPath, String content) throws IOException{
		write(localPath, string2Bytes(content));
	}
	public static final void write(final String localPath, final byte[] allBytes) throws IOException{
		mkdirs(dirname(localPath));	
		java.io.OutputStream outputStream = null;
		try {
			outputStream = new java.io.FileOutputStream(localPath);
			outputStream.write(allBytes);
		} catch (IOException e){ 
			throw e;
		} finally {
			if (outputStream != null) outputStream.close();
		}
	}
	
//	private static Path str2Path(String str) {
//		return FileSystems.getDefault().getPath(str);
//	}
	public static String basename(String str) {
//		return str2Path(str).getFileName().toString();
		int index = str.lastIndexOf("/");
		if (index < 0 || index == str.length()) {
			index = str.lastIndexOf("\\");
			if (index < 0 || index == str.length()) {
				return null;
			}
		}
		return str.substring(index+1);
	}
	public static String dirname(String str) throws IOException {
		int index = str.lastIndexOf("/");
		if (index < 0) {
			index = str.lastIndexOf("\\");
			if (index < 0) {
				throw new IOException("index < 0");
			}
		}
		return dirnameByLoop(str.substring(0, index));
//		Path dir = str2Path(str).toAbsolutePath().getParent();
//		if (dir == null) {
//			throw new IOException("dirname fail");
//		}
//		return dir.toAbsolutePath().toString();
	}
	private static String dirnameByLoop(String str) {
		int index = str.lastIndexOf("/");
		if (index < 0 || index != str.length()-1) {
			index = str.lastIndexOf("\\");
			if (index < 0 || index != str.length()-1) {
				return str;
			}
		}
		return dirnameByLoop(str.substring(0, index));
	}
	public static void mkdirs(String str) throws IOException {
		if(!new java.io.File(str).mkdirs()) {
			if (isExist(str)) { return; }
			throw new IOException("mkdir fail 03 " + str);
		}
	}
	public static void mkdirForFolderByLoop(String str) throws IOException {
		if(isExist(str)) {
			// System.out.println("Folder isExist " + str);
			return;
		}
		mkdirForFolderByLoop(str, false);
	}
	public static void mkdirForFileByLoop(String str) throws IOException {
		String dirName = dirname(str);
		if (isExist(dirName)) {
			// System.out.println("File folder isExist " + str);
			return;
		} else {
			mkdirForFolderByLoop(dirName, false);
		}
	}
	private static void mkdirForFolderByLoop(String str, boolean state) throws IOException {
		if (state) {
			return;
		}
		String dirName = dirname(str);
		if (isExist(dirName)) {
			// System.out.println("mkdir " + str);
			if(!new java.io.File(str).mkdir()) {
				throw new IOException("mkdir fail 01 " + str);
			}
			return;
		} else {
			mkdirForFolderByLoop(dirName, false);
			// System.out.println("mkdir " + str);
			if(!new java.io.File(str).mkdir()) {
				throw new IOException("mkdir fail 02 " + str);
			}
			return;
		}
	}
	// public static String[] listDir(String path) throws IOException {
		// String[] dirs = str2Path(path).toFile().list();
		// if (dirs == null) {
			// throw new IOException("listDir fail");
		// }
		// return dirs;
	// }
	public static String[] listDirectory(String path) throws IOException {
//		File[] files = new File(str2Path(path).toFile().listFiles();
		File[] files = new File(path).listFiles();
		if (files == null) {
			throw new IOException("listFile fail");
		}
		List<String> buf = new ArrayList<>();
		for (File file: files) {
			if (file.isDirectory()) {
				buf.add(file.getName());
			}
		}
		return buf.toArray(new String[0]);
	}
	public static String[] listFile(String path) throws IOException {
//		File[] files = str2Path(path).toFile().listFiles();
		File[] files = new File(path).listFiles();
		if (files == null) {
			throw new IOException("listFile fail");
		}
		List<String> buf = new ArrayList<>();
		for (File file: files) {
			if (file.isFile()) {
				buf.add(file.getName());
			}
		}
		return buf.toArray(new String[0]);
	}
	public static String[] listFile(String path, String[] legalE) throws IOException {
		// System.out.println("listFile");
		// System.out.println(path);
		String[] names = listFile(path);
		List<String> buf = new ArrayList<>();
		for (String name: names) {
			// System.out.println(name);
			for (String ex: legalE) {
				if (name.endsWith(ex)) {
					buf.add(name);
				}
			}
		}
		return buf.toArray(new String[0]);
	}
	public static boolean isExist(String path) {
		return new File(path).exists();
	}
	public static boolean isFile(String path) {
		return !isFolder(path);
	}
	public static boolean isFolder(String path) {
		return new File(path).isDirectory();
	}
	
	public static void downloadFile(String fileName, java.net.URL url) throws IOException {
		byte[] bytes = read(url);
		write(
			fileName, 
			bytes
		);
	}
}