import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Main {
	private static window Wnd;

	private static BufferedImage img;

	public static void main(String[] args) {
		Wnd = new window(640, 480);
	}

	public static void setImage(URL url) {
		try {
			ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setImage(File file) {
		try {
			ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveImage(File file, String format) {
		try {
			ImageIO.write(img, format, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage getImg() {
		return img;
	}

	public static void setImg(BufferedImage img) {
		Main.img = img;
	}

}
