/*
javac JImageM.java
*/

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;


public class JImageM extends JLabel {
	final boolean _isRotate; 
	public JImageM(ImageInfor infor, FileB fileB) {
		_isRotate = infor._isRotate;
		ImageIcon image = new ImageIcon(fileB._bytes);
        Image tmp = image.getImage();
		tmp = tmp.getScaledInstance(infor._lenX, infor._lenY, java.awt.Image.SCALE_SMOOTH); // scale it the
		setIcon(new ImageIcon(tmp));
	}
	@Override
	public void paintComponent(Graphics g) {
		if (_isRotate) {
			Graphics2D gx = (Graphics2D) g;
			gx.rotate(Math.toRadians(90), getX() + getWidth()/2, getY() + getHeight()/2);
		}
		super.paintComponent(g);
	}
}