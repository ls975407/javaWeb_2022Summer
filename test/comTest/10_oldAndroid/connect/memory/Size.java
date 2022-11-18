package com.student.tool.MangaServer.connect.memory;
/*  
javac -encoding utf8 ./lib/connect/memory/Size.java
*/
public class Size
{
	final public double single_width;
	final public double single_height;
	
	final public double dual_width;
	final public double dual_height;

	public Size(lib.data.interfaces.Size size_infor)
	{
		single_height = size_infor.getPictureHeight();
		single_width = size_infor.getPictureWidth();
			
		dual_width = single_height / 2.0;
		dual_height = single_width;
	}
}