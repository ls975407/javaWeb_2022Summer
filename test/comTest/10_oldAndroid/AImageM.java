package com.student.tool.MangaServer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;

import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.data.FileB;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AImageM  {
	private static final Matrix _matrix_rotate = iniMatrix();
	private static Matrix iniMatrix() {
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		return matrix;
	}
	private static Bitmap readAndScaleAndRotate(byte[] bytes, int outWidth, int outHeight, boolean isRotate) throws SDEBase {

	}
	public AImageM(ImageView imageView, ImageInfor imageInfor, int ithAbs) throws SDEBase {

	}
}