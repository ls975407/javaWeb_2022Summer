package com.cloud.manga.henryTing.test;
/*
javac -encoding utf8  com/cloud/manga/henryTing/test/NameBuilder.java
*/


import com.cloud.manga.henryTing.unit.HolderEnum;
import com.cloud.manga.henryTing.unit.FrameEnum;
import com.cloud.manga.henryTing.unit.CmdEnum;

import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;

import java.util.Map;
import java.util.HashMap;

public class NameBuilder {
	final HolderEnum _holderEnum;
	final Integer _fileCode;
	final FrameEnum _frameEnum;
	final static Map<String, String> _mapFrame = mapFrameIni();
	final static Map<String, String> mapFrameIni() {
		Map<String, String> mapFrame = new HashMap<>();
		mapFrame.put("single", "Single");
		mapFrame.put("dual", "Dual");
		mapFrame.put("leftright", "LeftRight");
		mapFrame.put("updown", "UpDown");
		return mapFrame;
	}
	public NameBuilder(String name) {
		if (name.startsWith("Setting")) {
			_holderEnum = HolderEnum.Ini;
			name = name.substring(7, name.length());
			try {
				_fileCode = Integer.parseInt(
					name.substring(0, 2)
				);
			} catch (java.lang.IllegalArgumentException e) {
				System.out.println(name.substring(0, 2)); throw e;
			}
			try {
				_frameEnum = Enum.valueOf(
					FrameEnum.class, 
					name.substring(2, name.length()).toLowerCase()
				);
			} catch (java.lang.IllegalArgumentException e) {
				System.out.println(name.substring(2, name.length()).toLowerCase()); throw e;
			}
			return;
		}
		try {
			_holderEnum =  Enum.valueOf(
				HolderEnum.class, 
				name.substring(0, 2)
			);
		} catch (java.lang.IllegalArgumentException e) {
			System.out.println(name.substring(0, 2)); throw e;
		}

		_fileCode = null;
		try {
			_frameEnum = Enum.valueOf(
				FrameEnum.class, 
				name.substring(2, name.length()).toLowerCase()
			);
		} catch (java.lang.IllegalArgumentException e) {
			System.out.println(name.substring(2, name.length()).toLowerCase()); throw e;
		}
		// System.out.println(holderStr(_holderEnum));
		// System.out.println(fileCodeStr(_fileCode));
		// System.out.println(frameStr(_frameEnum));
	}
	static String holderStr(HolderEnum holderEnum) {
		if (holderEnum == HolderEnum.Ini) {
			return "Setting";
		}
		return holderEnum.toString();
	}
	static String fileCodeStr(Integer fileCode) {
		if (fileCode != null) {
			return String.format("%02d", fileCode);
		}
		return "";
	}
	static String frameStr(FrameEnum frameEnum) {
		return _mapFrame.get(frameEnum.toString());
	}
	String toString(HolderEnum holderEnum) {
		return String.format(
			"%s%s%s", 
			holderStr(holderEnum), 
			fileCodeStr(_fileCode), 
			frameStr(_frameEnum)
		);
	}
	String toString(Integer fileCode) {
		return String.format(
			"%s%s%s", 
			holderStr(_holderEnum), 
			fileCodeStr(fileCode), 
			frameStr(_frameEnum)
		);
	}
	String toString(FrameEnum frameEnum) {
		return String.format(
			"%s%s%s", 
			holderStr(_holderEnum), 
			fileCodeStr(_fileCode), 
			frameStr(frameEnum)
		);
	}
	
	
	CmdEnum getCmd(KeyEnum keyEnum) throws SDEBase {
		switch (keyEnum) {
			case NextChapter: 
			case PrevChapter: 
			case NextTwoPage: 
			case PrevTwoPage: 
			case NextMark: 
			case PrevMark: 
			case GoFirst: 
			case GoLast: 
			case TxtCurrntFolder: 
			case TxtDefaultFolder: 
			case SetIni: 
			case SetPathUser: 
			case SetCloudStorage: 
			case SetLocalStorage: 
			case Show: 
			case None: 
			return Enum.valueOf( CmdEnum.class, keyEnum.toString() );
			default: break;
		}
		switch(_frameEnum) {
			case single:
			switch(keyEnum) {
				case PrevHPage1: case PrevHPage2:
				if (_holderEnum == HolderEnum.Pg) {
					return CmdEnum.MinusTo;
				}
				return CmdEnum.PrevPage;
				case NextHPage1: case NextHPage2:
				if (_holderEnum == HolderEnum.Pg) {
					return CmdEnum.To;
				}
				return CmdEnum.NextPage;
				case NextPage:
				return CmdEnum.NextPage;
				case PrevPage:
				return CmdEnum.PrevPage;
				default:
				return CmdEnum.To;
			}
			case dual:
			switch(keyEnum) {
				case PrevHPage1: case PrevHPage2:
				return CmdEnum.PrevPage;
				case NextHPage1: case NextHPage2:
				return CmdEnum.NextPage;
				case NextPage: 
				return CmdEnum.NextTwoPage;
				case PrevPage:
				return CmdEnum.PrevTwoPage;
				default:
				return CmdEnum.To;
			}
			case leftright:
			case updown: default:
			switch(keyEnum) {
				case NextHPage1: case NextHPage2:
				return CmdEnum.AddTo;
				case NextPage:
				return CmdEnum.NextPage;
				case PrevPage:
				return CmdEnum.PrevPage;
				case PrevHPage1: case PrevHPage2:
				default:
				return CmdEnum.To;
			}
		}
	}
	String getArg(KeyEnum keyEnum, int count) throws SDEBase {
		switch(keyEnum) {
			case Show:
			switch(_holderEnum) {
				case Ini:
				if (_fileCode == null) {
					return toString(1); 
				}
				return toString((_fileCode)%2+1); 
				default:
				if (_frameEnum == FrameEnum.leftright || 
					_frameEnum == FrameEnum.updown) {
					return "Setting01Single";
				} else {
					return String.format(
						"Setting01%s",
						frameStr(_frameEnum)
					);	
				}
			}
			case Switch:
			switch(_frameEnum) {
				case dual:
				return toString(FrameEnum.single);
				default:
				return toString(FrameEnum.dual);
			}
			case NextHPage1: case PrevHPage1:
			switch(_frameEnum) {
				case single:
				if (_holderEnum == HolderEnum.Pg) {
					return toString(FrameEnum.leftright);
				}
				return "";
				case dual:
				return "";
				case leftright:
				case updown: default:
				if (_holderEnum == HolderEnum.Pg) {
					return toString(FrameEnum.single);
				}
				return "";
			}
			case NextHPage2: case PrevHPage2:
			switch(_frameEnum) {
				case single:
				if (_holderEnum == HolderEnum.Pg) {
					return toString(FrameEnum.updown);
				}
				return "";
				case dual:
				return "";
				case leftright:
				case updown: default:
				if (_holderEnum == HolderEnum.Pg) {
					return toString(FrameEnum.single);
				}
				return "";
			}
			case Back:
			switch(_holderEnum) {
				case Pg:
				if(_frameEnum == FrameEnum.leftright || _frameEnum == FrameEnum.updown) {
					if (count == 1) {
						return "ChSingle";
					} 
					return "IdSingle";
				} else {
					if (count == 1) { return toString(HolderEnum.Ch); } 
					return toString(HolderEnum.Id);
				}
				case Ch:
				return toString(HolderEnum.Id);
				case Id:
				default:
				throw new SDEError("Ini not support Back");
			}
			case Enter:
			switch(_holderEnum) {
				case Id:
				if (count == 1) { return toString(HolderEnum.Ch); } 
				return toString(HolderEnum.Pg);
				case Ch:
				return toString(HolderEnum.Pg);
				case Pg:
				default:
				throw new SDEError("Ini not support CmdEnter");
			}
			default:
			return "";
		}
	}
}