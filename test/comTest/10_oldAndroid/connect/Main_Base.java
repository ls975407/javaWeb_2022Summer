package com.student.tool.MangaServer.connect;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.student.tool.MangaServer.connect.memory.Storage;

import lib.path.data.Path;

public class Main_Base extends lib.data.Gui
{
	private int mode_int = 0;
	// 0 mean manactivity
	// 1 mean firstFragment
	// 2 mean secondFragment
	// 3 mean thirdFragment

	public void setModeInt(int int_t){mode_int = int_t;}
	public int getModeInt(){return mode_int;}

	public void FragmentBack()
	{
		if(mode_int == 2) _engine_dual.onBackPressed();
		else if(mode_int == 3)  _engine_third.onBackPressed();
	}

	private Main_Base_interface _engine = null;
	private Main_Base_interface _engine_single = null;
	private Main_Base_interface _engine_dual = null;
	private Main_Base_interface _engine_third = null;

	public void initialize(Main_Base_interface engine, lib.data.interfaces.Size size_const, Storage storage)
	{
		initialize(storage, size_const);
		_engine = engine;
		_engine_single = _engine;
		_engine_dual = _engine;
		_engine_third = _engine;

		String mark_cloud = "manga";
		String mark_local = "manga";
		String filename =
				getSDPath("") +
						"manga_resouce.txt";
		lib.infor.TxtReaderMark reader = lib.infor.TxtReaderMark.setFilename(filename);
		if(reader == null) {
			System.out.println("cannot not find file" + filename);
		} else {
			String str;
			str = reader.getInforByMark("cloud_folder");
			if(str == null) {
				System.out.println("cannot find 'cloud_folder' mark in the file " + filename);
			} else {
				mark_cloud = str;
			}
			str = reader.getInforByMark("local_folder");
			if(str == null) {
				System.out.println("cannot find 'local_folder' mark in the file " + filename);
			} else {
				mark_local = str;
			}
		}

		Path.setCloudPath(mark_cloud);
		String local_path = getSDPath(mark_local);
		Path.setLocalPath(local_path);

		Log.d("manga local path", String.format("%s",Path.getLocalPath()));
		Log.d("manga cloud path", String.format("%s",Path.getCloudPath()));
	}
	
	public static String getSDPath(String mark){
		boolean sdCardExist = Environment.getExternalStorageState()
								.equals(android.os.Environment.MEDIA_MOUNTED);//判斷sd卡是否存在
		if(sdCardExist) {
			java.io.File sdDir = Environment.getExternalStorageDirectory();//獲取跟目錄
			return (sdDir.toString() + "/" + mark);
		}
		return (Environment.getDataDirectory().getParent() + "/" + mark);
	}

	public void setSingleEngine(Main_Base_interface engine)
	{
		_engine_single = engine;
	}
	public void setDualEngine(Main_Base_interface engine)
	{
		_engine_dual = engine;
	}
	public void setThirdEngine(Main_Base_interface engine){ _engine_third = engine; }

	//private lib.memory.Mode.Method _mode = Mode.Method.single_page;
	public void show_image_on_screen(int index, lib.memory.Mode.Method mode, boolean is_next)
	{
		if(mode_int == 0) {
			_engine.show_image_on_screen(index, is_next, true);
		}
		else if(mode_int == 1) {
			_engine_single.show_image_on_screen(index, is_next, true);
		}
		else if(mode_int == 2) {
			_engine_dual.show_image_on_screen(index, is_next, true);
		}
		else if(mode_int == 3) {
			_engine_third.show_image_on_screen(index, is_next, true);
		}
	}
	public void send_command_fail()	{
		if(mode_int == 0) {
			_engine.send_command_fail();
		}
		else if(mode_int == 1) {
			_engine_single.send_command_fail();
		}
		else if(mode_int == 2) {
			_engine_dual.send_command_fail();
		}
		else if(mode_int == 3) {
			_engine_third.send_command_fail();
		}
	}

	@SuppressLint("DefaultLocale")
	public String getLabelString()
	{
		// lib.path.Path.Struct infor = getInfor();
		// return String.format("%03d / %03d", infor._chapter, infor._page);
		
		lib.path.data.Page page = super.getPageObj();
		return String.format("%03d / %03d", page.getChapter(), page.getPage());
	}

	private static boolean is_picture_dual(String url)
	{
		BitmapFactory.Options _option_infor = new BitmapFactory.Options();
		_option_infor.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(url, _option_infor);

		//Log.d("Manga Main_Base", "width = " + _option_infor.outWidth + " height = " + _option_infor.outHeight);
		return _option_infor.outWidth > _option_infor.outHeight;
	}

	public void _close_program()
	{
		super._close_program();
		_engine._close_program();
	}

	@Override
	public boolean is_picture_not_dual(lib.path.data.Path_old path_op) {
		java.io.File t_file;
		boolean _is_dual_page;

		t_file = new java.io.File(path_op.getPathToReadChapter() + "/single.mode");
		if(t_file.exists())
		{
			_is_dual_page = false;
		} else {
			t_file = new java.io.File(path_op.getPathToReadChapter() + "/dual.mode");
			if(t_file.exists()) {
				_is_dual_page = true;
			} else {
				lib.path.data.Path_old.Struct infor = path_op.getInfor();
				lib.path.data.Path_old[] path_array = new lib.path.data.Path_old[]{
						path_op.is_legal_path(new lib.path.data.Path_old.Struct(infor._chapter, 60)),
						path_op.is_legal_path(new lib.path.data.Path_old.Struct(infor._chapter, 80)),
						path_op.is_legal_path(new lib.path.data.Path_old.Struct(infor._chapter, 150))
				};

				if(path_array [0] == null || path_array[1] == null || path_array [2] != null)
				{
					_is_dual_page = false;
					//Log.d("Manga Main_Base", "_is_dual_page false 01");
				}else {
					//Log.d("Manga Main_Base", "_is_dual_page true");
					//Log.d("Manga Main_Base", "_is_dual_page false 02");
					_is_dual_page = is_picture_dual(path_array[0].getPathToReadPicture()) &&
							is_picture_dual(path_array[1].getPathToReadPicture());
				}
			}
		}
		return !_is_dual_page;
	}

	public void setPath(Storage storage, Path path)
	{
		//Log.d("Manga Main_Base", "super setPath start");

		storage.setIsDualPage(super.setIsDualPage(path));
		super.setPath(path);

		//Log.d("Manga Main_Base", "super setPath finish");
	}

	// @Override
	public void switch_page_show_mode_fragment_third(com.student.tool.MangaServer.connect.memory.Storage _storage, boolean is_start)
	{
		//Log.d("Manga Main_Base", "super fragment_third start");
		// it must be _method_mode == lib.memory.Mode.Method.single_page
		super.setMemoryMode(lib.memory.Mode.Method.single_page);
		_storage.setMemoryMode_fragment_third(is_start);
		//Log.d("Manga Main_Base", "super fragment_third end");
		super.show_image();
	}

	public boolean is_dual_page()
	{
		// return _is_dual_page;
		return super.isDualPage();
	}
	
	public void full_screen(){}
	public void _switch_page_show_mode(){}
}