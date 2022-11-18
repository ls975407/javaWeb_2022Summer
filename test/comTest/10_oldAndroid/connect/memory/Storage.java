package com.student.tool.MangaServer.connect.memory;
/*  
javac -encoding utf8 ./lib/connect/memory/Storage.java
*/

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.net.URL;

import lib.memory.Mode;
import lib.memory.Path_Link;

abstract class Scale_Operator
{
	protected double width, height, scale_w_h;

	protected final static Matrix _matrix = getMatrix();

	protected static Matrix getMatrix()
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		return matrix;
	}

	static int get_scale_value(double max_length, BitmapFactory.Options _option_infor) {
		int value_1 = (int) Math.min(_option_infor.outWidth / max_length, _option_infor.outHeight / max_length);
		int value_max = Math.max(1, value_1);
		int value_choose = 1;
		while(value_choose <= value_max) {
			value_choose *= 2;
		}
		return value_choose / 2;
	}

	Bitmap load(Path_Link link)
	{
		if(link == null) {
			// System.out.println("Storage: programming error: link should not be null");
			return null;
		}

		URL cloud_path = link.getCloudPath();
		String url = link.getLocalPath();
		if(cloud_path == null) {
//			BitmapFactory.Options _option_infor = new BitmapFactory.Options();
//			_option_infor.inJustDecodeBounds = true;
//			BitmapFactory.decodeFile(url, _option_infor);

			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inSampleSize = 1; // get_scale_value(Math.max(width, height), _option_infor);
			option.inJustDecodeBounds = false;

			return BitmapFactory.decodeFile(url, option);
		} else {
			Bitmap bitmap;
			bitmap = _loading_image_by_thread(url, cloud_path);
			if(bitmap == null) {
				return null;
			}
			// FileOutputStream iStream;
			// File fImage = new File(url);
			// try {
				// // fImage.createNewFile();
				// iStream = new FileOutputStream(fImage);
				// bitmap.compress(Bitmap.CompressFormat.JPEG, 70, iStream);
				// iStream.close();
			// }
        	// catch(IOException e) {
				// // e.printStackTrace();
				// return bitmap;
			// }
			link.setIsDownload();
			return bitmap;
		}
	}
	
	boolean check_static(Bitmap bmp, Bitmap_Infor infor)
	{
		if (bmp == null) return false;
		if(infor.getIsRotate()) {
			return !(Math.min(Math.abs(bmp.getHeight() - width), Math.abs(bmp.getWidth() - height)) > 1);
		} else {
			return !(Math.min(Math.abs(bmp.getHeight() - height), Math.abs(bmp.getWidth() - width)) > 1);
		}
	}
	abstract boolean check(Bitmap bmp, boolean is_dual_page, Bitmap_Infor infor);

	// some scale need to rotate
	abstract Bitmap scale(Bitmap bmp, boolean is_dual_page, Bitmap_Infor infor);

}

class Scale_Single extends Scale_Operator
{
	Scale_Single(Size size)
	{
		width = size.single_width; height = size.single_height; scale_w_h = size.single_width / size.single_height;
	}

	boolean check(Bitmap bmp, boolean is_dual_page, Bitmap_Infor infor)
	{
		return check_static(bmp, infor);
	}

	Bitmap scale(Bitmap bmp, boolean is_dual_page, Bitmap_Infor infor)
	{
		if(!is_dual_page) {
			if(bmp.getWidth() > bmp.getHeight())
			{
				Bitmap bmp_rotate = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), _matrix, true);
				infor.setIsRotate(true);
				bmp = bmp_rotate;
			} else {
				infor.setIsRotate(false);
			}
			return scale_static(bmp);
		}
		return scale_static(bmp, true);
	}
}

class Scale_Dual extends Scale_Operator
{
	Scale_Dual(Size size)
	{
		width = size.dual_width; height = size.dual_height; scale_w_h = size.dual_width / size.dual_height;
	}

	boolean check(Bitmap bmp, boolean is_dual_page, Bitmap_Infor infor)
	{
		return check_static(bmp, infor);
	}

	Bitmap scale(Bitmap bmp, boolean is_dual_page, Bitmap_Infor infor)
	{
		bmp = scale_static(bmp);
		infor.setIsRotate(true);
		return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), _matrix, true);
	}
}

class Scale_Third extends Scale_Operator
{
	Scale_Third(Size size)
	{
		width = size.single_width; height = size.single_height; scale_w_h = size.single_width / size.single_height;
	}

	boolean check(Bitmap bmp, boolean is_dual_page, Bitmap_Infor infor)
	{
		return check_static(bmp, infor);
	}

	Bitmap scale(Bitmap bmp, boolean is_dual_page, Bitmap_Infor infor)
	{
		infor.setIsRotate(true);
		return scale_static(Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), _matrix, true));
	}
}

public class Storage extends lib.memory.Storage
{
	// private static String ACTIVITY_TAG = "Manga storage";

	private final static int STORAGE_SIZE = lib.memory.Memory_storage.STORAGE_SIZE;
	private lib.memory.Path_Link[] _path = new lib.memory.Path_Link[STORAGE_SIZE];
	private boolean[] is_load = new boolean[STORAGE_SIZE];
	
	private Mode.Picture[] _mode = new Mode.Picture[STORAGE_SIZE];
	private Bitmap[] _bitmap = new Bitmap[STORAGE_SIZE];
	private Bitmap[] _bitmap_scale = new Bitmap[STORAGE_SIZE];
	private Bitmap_Infor[] _infor = new Bitmap_Infor[STORAGE_SIZE];
	
	private Scale_Operator scale_operator = null;
	
	private Size _size_infor;
	
	public Storage(lib.data.interfaces.Size size)
	{
		_size_infor = new Size(size);
		for(int i=0; i<STORAGE_SIZE; i++)
		{
			// ini the array buffer to null
			_mode[i] = Mode.Picture.raw;
			_bitmap[i] = null;
			_bitmap_scale[i] = null;
			_infor[i] = null;
			_path[i] = null;
			is_load[i] = false;
		}
	}
	
	void setScaleMode(lib.memory.Mode.Method mode)
	{
		//Log.d(Storage.ACTIVITY_TAG, "hi storage setScaleMode");
		if(mode == lib.memory.Mode.Method.single_page)
		{
			scale_operator = new Scale_Single(_size_infor);
		}
		else
		{
			scale_operator = new Scale_Dual(_size_infor);
		}
	}

	public void setMemoryMode_fragment_third(boolean is_start)
	{
		if(is_start) scale_operator = new Scale_Third(_size_infor);
		else scale_operator = new Scale_Single(_size_infor);
	}

	private boolean _is_dual_page = false;

	public void setIsDualPage(boolean is_dual_page)
	{
		_is_dual_page = is_dual_page;
	}
	
//	@Override
//	public void showMemory(int index){} // for begug
	
	@Override
	public void empty(int index)
	{
		//Log.d(Storage.ACTIVITY_TAG, "empty " + index);
		index = index % STORAGE_SIZE;
		_mode[index] = Mode.Picture.raw;
		_bitmap[index] = null;
		_bitmap_scale[index] = null;
		_infor[index] = null;
		_path[index] = null;
		is_load[index] = false;
	}
	
	@Override
	public void create(int index)
	{
		index = index % STORAGE_SIZE;
		if (_mode[index] == Mode.Picture.raw) {
			//Log.d(Storage.ACTIVITY_TAG, String.format("hi raw %03d %s", index, _path[index]));
			Bitmap t_bitmap;
			t_bitmap = scale_operator.load(_path[index]);
			if(t_bitmap == null) {
				System.out.println("fail to load the image");
				is_load[index] = true;
				return;
			}
			_bitmap[index] = t_bitmap;
			_infor[index] = new Bitmap_Infor();
			_mode[index] = Mode.Picture.container;
			return;
			
		} else if (_mode[index] == Mode.Picture.container) {
			//Log.d(Storage.ACTIVITY_TAG, String.format("hi storage container %03d", index));
			_bitmap_scale[index] = scale_operator.scale(_bitmap[index], _is_dual_page, _infor[index]);

			is_load[index] = true;
			return;
		}
		//Log.d(Storage.ACTIVITY_TAG, "hi storage finish");
		is_load[index] = true;
	}
	
	
	@Override
	public boolean check_is_size(int index)
	{
		// check if the array pointer is null or not
		// if(_bitmap[index] == null) return false;
		// scale_operator will check if null or not
		
		// check if the size of the image is not match the required ones
		index = index % STORAGE_SIZE;
		return scale_operator.check(_bitmap_scale[index], _is_dual_page, _infor[index]);
		
		// both the size and memory are correct
	}
	
	public Bitmap get_memory(int index)
	{
		// return the data type that cannot be used to show on the screen directed
		return _bitmap_scale[index%STORAGE_SIZE];
		//return _bitmap_scale[index%STORAGE_SIZE];
	}
	
	/////////////////////////////////////////////////////////////////
	
	// const methods from all platform do not remove
	
//	@Override
//	public boolean is_prepared(int index){return is_load[index%STORAGE_SIZE];}
	
	@Override
	public void createAll(int index)
	{
		index = index % STORAGE_SIZE;
		while(!is_load[index])
			create(index);
	}
	
	// public boolean is_contained(int index){return is_load[index%STORAGE_SIZE];}
	
	@Override
	public void set_resize_mode(int index)
	{
		//empty(index%STORAGE_SIZE); //redo since memory is too less
		_mode[index] = Mode.Picture.raw;
		is_load[index] = false;
	}
	
	@Override
	public lib.memory.Path_Link getPathLink(int index){ return _path[index%STORAGE_SIZE]; }
	@Override
	public void setPathLink(int index, lib.memory.Path_Link str){ _path[index%STORAGE_SIZE] = str; }
	
	public void setMemoryMode(lib.memory.Mode.Method mode)
	{
		this.setScaleMode(mode);
	}
}