package com.student.tool.MangaServer.connect;

public interface Main_Base_interface {
	void show_image_on_screen(int index, boolean is_next, boolean from_thread);
	void _close_program();
	void send_command_fail();
	void onBackPressed();
}
