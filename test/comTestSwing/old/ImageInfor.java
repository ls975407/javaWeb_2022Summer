public class ImageInfor {
	public final boolean _isRotate;
	public final int _lenX; 
	public final int _lenY; 
	public ImageInfor (
		double lenX, double lenY,
		boolean isRotate
	) {
		_lenX = (int) lenX;
		_lenY = (int) lenY;
		_isRotate = isRotate;
	}
} 