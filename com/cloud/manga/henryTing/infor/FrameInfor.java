package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/FrameInfor.java
*/

import com.cloud.manga.henryTing.unit.FrameEnum;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.data.FileB;

import java.util.ArrayList;
import java.util.List;

public class FrameInfor {
	public static Setting _setting;
	private final List<LabelInfor> _labelInfor;
	public final ImageInfor _imageInfor;
	public final FrameEnum _frameType;
	private final FileBS _fileBS;
	
	public static boolean boolCount(boolean... eles) {
		int count = 0;
		for(boolean ele: eles) {
			if (ele) { count++; }
		}
		return count%2!=0;
	}
	public Iterable<FileB> getBytes() { return _fileBS; }
	public FrameInfor(FrameM frameM, GeoGet geoGet, LabelGet labelGet, FileBS fileBS) {
		_fileBS = fileBS;
		_frameType = frameM.getFrameType();

		final double screenX = geoGet.screenX();
		final double screenY = geoGet.screenY();
		final boolean screenP = screenX < screenY? true: false;
		final boolean isFillScale = _frameType != FrameEnum.dual? true: false;

		double t_userX=_setting.getScreenX(), t_userY=_setting.getScreenY();
		double userXt, userYt;
		if (boolCount(screenP, t_userX < t_userY)) {
			userXt = Math.min(t_userY, screenX); userYt = Math.min(t_userX, screenY);
		} else {
			userXt = Math.min(t_userX, screenX); userYt = Math.min(t_userY, screenY);
		}
		final double userX = userXt;
		final double userY = userYt;
		_imageInfor = new ImageInfor(screenP, isFillScale, userX, userY);
		
		_labelInfor = new ArrayList<>();
		double fontScaleValue = _setting.getFontScaleValue()*0.41;
		double geoScaleValue = _setting.getGeoScaleValue()*0.41;
		double marge = _setting.getLabelMarge()*geoScaleValue;
		LabelM.Infor labelI;
		for(LabelM labelM: frameM.getLabelMs()) {
			_labelInfor.add(new LabelInfor(labelM.getILabel(labelGet), fontScaleValue, false));
		}
	}

	public static double[] positionLabel(int screenX, int screenY, int  lenX, int lenY, int posiIndex) {
		double marge = _setting.getLabelMarge()*_setting.getGeoScaleValue()*0.41;
		final boolean screenP = screenX < screenY;
		if (screenP) {
			return position(screenX, screenY, lenX, lenY, marge, posiIndex);
		} else {
			double[] posi_t = position(screenY, screenX, lenX, lenY, marge, posiIndex);
			return new double[] {screenX-posi_t[1], posi_t[0]};
		}
	}
	public static double[] positionImage(int screenX, int screenY, int lenX, int lenY, int ith, FrameEnum frameType) {
		final boolean screenP = screenX < screenY;
		if (screenP) {
			return imagePosiP(screenX, screenY, lenX, lenY, frameType, ith);
		} else {
			return imagePosiL(screenX, screenY, lenX, lenY, frameType, ith);
		}
	}
	public static double[] scaleValueImage(ImageInfor imageInfor, double imageX, double imageY) {
		final boolean imageP = imageX < imageY;
		if (imageInfor._screenP) {
			if (imageInfor._isFillScale) {
				if (imageP) {
					return imageScale(imageInfor._userX, imageInfor._userY, imageX, imageY, false);
				} else {
					return imageScale(imageInfor._userX, imageInfor._userY, imageY, imageX, true);
				}
			} else {
				if (imageP) {
					return imageScale(imageInfor._userX, imageInfor._userY/2, imageY, imageX, true);
				} else {
					return imageScale(imageInfor._userX, imageInfor._userY/2, imageX, imageY, false);
				}	
			}
		} else {
			if (imageInfor._isFillScale) {
				if (imageP) {
					return imageScale(imageInfor._userX, imageInfor._userY, imageY, imageX, true);
				} else {
					return imageScale(imageInfor._userX, imageInfor._userY, imageX, imageY, false);
				}
			} else {
				if (imageP) {
					return imageScale(imageInfor._userX/2, imageInfor._userY, imageX, imageY, false);
				} else {
					return imageScale(imageInfor._userX/2, imageInfor._userY, imageY, imageX, true);
				}		
			}
		}
	}
	
	public static boolean isRotateImage(ImageInfor imageInfor, double imageX, double imageY) {
		final boolean imageP = imageX < imageY;
		if (imageInfor._screenP) {
			if (imageInfor._isFillScale) {
				if (imageP) {
					return false;
				} else {
					return true;
				}
			} else {
				if (imageP) {
					return true;
				} else {
					return false;
				}	
			}
		} else {
			if (imageInfor._isFillScale) {
				if (imageP) {
					return true;
				} else {
					return false;
				}
			} else {
				if (imageP) {
					return false;
				} else {
					return true;
				}		
			}
		}
	}


	private static int _count = 0;
	public Iterable<LabelInfor> getLabels() { return _labelInfor; }
	public String toString() {
		StringBuilder _buider = new StringBuilder();
		_buider.append(String.format("FrameInfor Debug: %02d\n", ++_count));
		int count = 0;
		for (LabelInfor labelInfor: getLabels()) {
			_buider.append(String.format("labelInfor %d: ", count));
			if (labelInfor.needRotate()) {
				_buider.append("landscape\n");
			} else {
				_buider.append("portrait\n");
			}	
			_buider.append(String.format("%s %s %f \n", 
				labelInfor._isRotate?"true":"false", 
				labelInfor._needRotate?"true":"false", 
				labelInfor._scaleValue)
			);
		}
		_buider.append("imageInfor");
		_buider.append(String.format("user (%f, %f)\n\n", _imageInfor._userX, _imageInfor._userY));
			
		_buider.append(String.format("_screenP=%s\n_isFillScale=%s\n", 
			String.valueOf(_imageInfor._screenP), String.valueOf(_imageInfor._isFillScale)));
		// _buider.append("\n");
		return _buider.toString();
	}
	
	public static int pt2px(int sizePt) {
		return sizePt + (int) (sizePt/3);
	}
	public static int px2pt(int sizePx) {
		return (int) (sizePx*3/4);
	}
	
	private enum PosiEnum {
		small, equal, big
	} 
	private static PosiEnum _positionX(int posiIndex) {
		switch(posiIndex) {
			case 0: case 3: case 5: return PosiEnum.small;
			case 1: case 8: case 6: return PosiEnum.equal;
			case 2: case 4: case 7: default: return PosiEnum.big;
		}
	}
	private static PosiEnum _positionY(int posiIndex) {
		switch(posiIndex) {
			case 0: case 1: case 2: return PosiEnum.small;
			case 3: case 8: case 4: return PosiEnum.equal;
			case 5: case 6: case 7: default: return PosiEnum.big;
		}
	}
	public static double[] position(double screenX, double screenY, double itemX, double itemY, double gap, int posiIndex) {
		double[] reArray = new double[2];
		switch(_positionX(posiIndex)) {
			case small: reArray[0] = gap; break;
			case equal: reArray[0] = (screenX-itemX)/2; break;
			case big: default: reArray[0] = screenX-gap-itemX; break;
		}
		switch(_positionY(posiIndex)) {
			case small: reArray[1] = gap; break;
			case equal: reArray[1] = (screenY-itemY)/2; break;
			case big: default: reArray[1] = screenY-gap-itemY; break;
		}
		return reArray;
	}
	public static double[] imageScale(double ux, double uy, double ix, double iy, boolean isRotate) {
		double sx, sy;
		if ((int) ux == (int) ix || (int) uy == (int) iy ) {
			sx = ix; sy = iy;
		} else {
			double ixuy = ix*uy, iyux = iy*ux;
			if (ixuy > iyux) {
				sx = ux; sy = iyux/ix;
			} else {
				sx = ixuy/iy; sy = uy;
			}
		}
		if (isRotate) {
			return new double[]{sy, sx};
		} else {
			return new double[]{sx, sy};
		}
	}
	public static double[] imagePosiP(double sx, double sy, double ix, double iy, FrameEnum type, int index) {
		switch(type) {
			case single:
			return new double[]{(sx-ix)/2, (sy-iy)/2};
			case dual:
			if (index == 0) {
				return new double[]{(sx-ix)/2, sy/2};
			} else {
				return new double[]{(sx-ix)/2, sy/2-iy};
			}
			case leftright:
			if (index == 0) {
				return new double[]{sx/2, (sy-iy)/2};
			} else {
				return new double[]{sx/2-ix, (sy-iy)/2};
			}
			case updown:
			default:
			if (index == 0) {
				return new double[]{(sx-ix)/2, sy/2-iy};
			} else {
				return new double[]{(sx-ix)/2, sy/2};
			}
		}
	}
	public static double[] imagePosiL(double sx, double sy, double ix, double iy, FrameEnum type, int index) {
		switch(type) {
			case single:
			return new double[]{(sx-ix)/2, (sy-iy)/2};
			case dual:
			if (index == 0) {
				return new double[]{sx/2, (sy-iy)/2};
			} else {
				return new double[]{sx/2-ix, (sy-iy)/2};
			}
			case leftright:
			if (index == 0) {
				return new double[]{sx/2, (sy-iy)/2};
			} else {
				return new double[]{sx/2-ix, (sy-iy)/2};
			}
			case updown:
			default:
			if (index == 0) {
				return new double[]{(sx-ix)/2, sy/2};
			} else {
				return new double[]{(sx-ix)/2, sy/2-iy};
			}
		}
	}
} 