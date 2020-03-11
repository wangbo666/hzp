package com.qgyyzs.globalcosmetics.utils;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadTool {
	private final static String TAG = "UPLOADTOL:UploadTool";

	/**
	 * 上传文件
	 * 
	 * @return String UploadTool.java
	 */
	public static String upLoadFile(Context context, String urlPath,
									String filePath, String newName) throws Exception {
		// urlPath：上传地址
		// filePath：文件路径
		// newName：上传之后的文件名
		Log.e("----", "@upLoadFile");
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";

		URL url = new URL(urlPath);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		/* 允许Input，Output。不使用Cache */
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);// 不允许使用缓存
		/* 设置传送的method=POST */
		con.setRequestMethod("POST");
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ boundary);
		/* 设置DataOutputStream */
		DataOutputStream ds = new DataOutputStream(con.getOutputStream());
		ds.writeBytes(twoHyphens + boundary + end);
		ds.writeBytes("Content-Disposition: form-data; "
				+ "name=\"file1\";filename=\"" + newName + "\"" + end);
		ds.writeBytes(end);
		// 取得文件的FileInputStream
		FileInputStream fs = new FileInputStream(filePath);
		// 设置每次写入1024byte
		int byteSize = 1024 * 1024;
		byte[] buffer = new byte[byteSize];
		int length = -1;
		// 将文件写入到缓冲区
		while ((length = fs.read(buffer)) != -1) {
			ds.write(buffer, 0, length);
		}
		Log.e("----", "11111111111");
		ds.writeBytes(end);
		ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
		/* close stream */
		fs.close();
		ds.flush();
		/* 取得response的内容 */
		InputStream is = con.getInputStream();
		int ch;
		StringBuffer b = new StringBuffer();
		while ((ch = is.read()) != -1) {
			b.append((char) ch);
		}
		Log.e("成功", "----");
		/* 关闭DataOutputStream */
		ds.close();
		return b.toString();
	}

	/**
	 * 上传文件夹
	 * 
	 * @return String UploadTool.java
	 * @throws Exception
	 */
	public static String upLoadFolder() {
		Log.d(TAG, "@upLoadFolder");
		// TODO
		return null;
	}

}
