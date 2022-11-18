import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ImageGetAd implements ImageGet {

    public static BufferedImage toBufferedImage(byte[] bytes)
        throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;
    }
	final int _size;
	final int[] _lenX;
	final int[] _lenY;
	final FileBS _fileBS;
	public ImageGetAd(FileBS fileBS) throws IOException {
		_fileBS = fileBS;
		_size = fileBS.size();
		_lenX = new int[_size];
		_lenY = new int[_size];
		BufferedImage bimg;
		FileB fileB = null;
		for (int ith=0; ith<_size; ith++) {
			fileB = _fileBS.get(ith);
			if (fileB == null) {
				_lenX[ith] = 0;
				_lenY[ith] = 0;
				continue;
			}
			bimg = toBufferedImage(fileB._bytes);
			_lenX[ith] = bimg.getWidth();
			_lenY[ith] = bimg.getHeight();
		}
	}
	public int size() { return _size; }
	public int lenX(int index) { return _lenX[index]; }
	public int lenY(int index) { return _lenY[index]; }
	public FileB Bytes(int index) { return _fileBS.get(index); }
}